#include <string>
#include <stdlib.h>
#include <fstream>
#include <iostream>
#include <ctype.h>
#include <windows.h>

/**
 * Use the following for TextTwist 1:
#define MAX_WORD_LEN 7
#define RESULT_TABLE_SIZE 64
 */

#define MAX_WORD_LEN 15
#define RESULT_TABLE_SIZE 512

typedef HANDLE fileDesc;
typedef int (*compareFunc)(std::string, std::string);

bool validFile(fileDesc file);
fileDesc createFile(const char *fileName);
fileDesc openFile(const char *fileName);
int fileRead(fileDesc fd, void *buf, size_t s);
int fileGetCurrentPointer(fileDesc fd);
int fileSetCurrentPointer(fileDesc fd, unsigned int offset);
bool closeFile(fileDesc file);
bool writeFile(fileDesc file, const char *buffer, size_t len);
int alphaCompare(std::string a, std::string b);
int hashCompare(std::string a, std::string b);
int sortHash(std::string str, int idx);
void rotateStr( std::string &str, int idx);

enum state { S_START=0, S_ALPHA=1, S_ZERO=2, S_ONE=3, S_ERROR=5 };

///////////////
// nodeList  //
///////////////
struct nodeList
{
  char letter;
  bool isWord;
  nodeList *nextLetter;
  nodeList *nextLocal;
};

/////////////////
// searchTree  //
/////////////////
class searchTree
{
private:
  std::ofstream tout;
  nodeList *root;
  std::string *resultTable[RESULT_TABLE_SIZE];
  int wordCounts[MAX_WORD_LEN];
public:
  searchTree()
  {
    root = NULL;
    tout.open("debugTree.txt", std::ios::out);
    for(int i = 0; i < RESULT_TABLE_SIZE; i++) resultTable[i] = NULL;
    for(int j = 0; j < MAX_WORD_LEN; j++) wordCounts[j] = 0;
  }
  void addWordToTree(std::string newWord);
  int getAllSubWords(std::string findMe);
  nodeList *createBasicNode(char letter);
  nodeList *isLetterInList(nodeList *head, char let);
  nodeList *addLetterToList(nodeList *head, char let);
  void operateOnPerms(std::string s, int i);
  void findAllSubsOf(std::string str);
  void addToResultTable(std::string str);
  void resetResultTable();
  void printResultTable();
  void sortResultTable(compareFunc func);
  void printWordCounts();
};

/////////////////
// treeReader  //
/////////////////
class treeReader
{
private:
  std::ofstream fout;
  searchTree *myTree;
  fileDesc descriptor;
  int markers[MAX_WORD_LEN], reader;
  state currentState;
  char currentVal;
  static const char fileName[];
public:
  treeReader(char *treeFileName)
  {
    fout.open("ttwist_debug.txt", std::ios::out);
    myTree = new searchTree;
    descriptor = openFile(treeFileName);
    currentState = S_START;

    for(int i = 0; i < MAX_WORD_LEN; i++)
    {
      markers[i] = -1;
    }

    reader = 0;
    fileSetCurrentPointer(descriptor, reader);
    currentVal = getCharacter(reader);
  }

  ~treeReader(){ fout.close(); }
  void processTree();
  char getCharacter(int pos);
  char lookAtNext(int pos);
  void advanceReader(){ reader++; }
  void getWordSkipLast();
  void getWordFull();
  void deleteAndAdvanceMarkers();
  void setNextMarker(int pos);
  void setCurrentState(state newState){ currentState = newState; }
  void foundAlpha();
  void foundZero();
  void foundOne();
};

////////////////////////////////////
//                                //
// treeReader Functions           //
//                                //
////////////////////////////////////

void treeReader::processTree()
{
  char value;
  char inputStr[256];
  std::string *temp = new std::string;
  while(value = getCharacter(reader))
  {
    if(isalpha(value))
    {
      foundAlpha();
    }
    else if(value == '0')
    {
      foundZero();
    }
    else if(value == '1')
    {
      foundOne();
    }
    else
    {
      std::cout << "Error: foreign character found\n";
      exit(1);
    }

    advanceReader();
  }

  myTree->printWordCounts();

  while(1)
  {
    std::cout << "Enter a string: ";
    std::cin >> *temp;
    if(*temp == "kwit") break;
    myTree->findAllSubsOf(*temp);
  }
  std::cout << "Done!\n";
}

void treeReader::foundAlpha()
{
  switch(currentState)
  {
    case S_START:
      setNextMarker(reader);
      break;
    case S_ALPHA:
      break;
    case S_ZERO:
      setNextMarker(reader);
      break;
    case S_ONE:
      setNextMarker(reader);
      break;
  }

  setCurrentState(S_ALPHA);
}

void treeReader::foundZero()
{
  switch(currentState)
  {
    case S_START:
      break;
    case S_ALPHA:
      break;
    case S_ZERO:
      break;
    case S_ONE:
      break;
  }

  setCurrentState(S_ZERO);
}

void treeReader::foundOne()
{
  switch(currentState)
  {
    case S_START:
      break;
   case S_ALPHA:
      getWordSkipLast();
      break;
    case S_ZERO:
      getWordFull();
      deleteAndAdvanceMarkers();
      break;
    case S_ONE:
      getWordFull();
      deleteAndAdvanceMarkers();
      break;
  }

  setCurrentState(S_ONE);
}

void treeReader::getWordSkipLast()
{
  char *foundWord = (char*)malloc((MAX_WORD_LEN + 1)*sizeof(char));
  int i;
  for(i = 0; i < MAX_WORD_LEN; i++)
  {
    if(markers[i] == -1) break;
      foundWord[i] = getCharacter(markers[i]);
  }

  foundWord[i - 1] = '\0';
  std::string str(foundWord);
  myTree->addWordToTree(str);
  free(foundWord);
}

void treeReader::getWordFull()
{
  char *foundWord = (char*)malloc((MAX_WORD_LEN + 1)*sizeof(char));
  int i;
  for(i = 0; i < MAX_WORD_LEN; i++)
  {
    if(markers[i] == -1) break;
      foundWord[i] = getCharacter(markers[i]);
  }

  foundWord[i] = '\0';
  std::string str(foundWord);
  myTree->addWordToTree(str);
  free(foundWord);
}

char treeReader::getCharacter(int pos)
{
  void *buf = malloc(32);
  char *returnVal, returnChar;
  fileSetCurrentPointer(descriptor, pos);

  if(!fileRead(descriptor, buf, 1))
  {
    return 0; //EOF
  }

  fileSetCurrentPointer(descriptor, pos);  //don't increment
  returnVal = (char*)buf;
  returnChar = returnVal[0];
  free((char*)buf);
  return returnChar;
}

char treeReader::lookAtNext(int pos)
{
  char returnChar;
  returnChar = getCharacter(pos+1);  //check for EOF
  return returnChar;
}

void treeReader::setNextMarker(int pos)
{
  int i = 0;
  while((markers[i] != -1) && (i < MAX_WORD_LEN)) i++;
  markers[i] = pos;
}

void treeReader::deleteAndAdvanceMarkers()
{
  for(int i = MAX_WORD_LEN - 1; i >= 0; i--)
  {
    if(markers[i] != -1)
    {
      if(!isalpha(lookAtNext(markers[i])))
      {
        markers[i] = -1;
      }
      else
      {
        markers[i]++;
        return;
      }
    }
  }
}

const char treeReader::fileName[] = "longtree.bin";

///////////////////////////////////////////
//                                       //
//  searchTree Functions                 //
//                                       //
///////////////////////////////////////////

void searchTree::addWordToTree(std::string newWord)
{
  nodeList *nextLetterList = root;
  nodeList *follow = root;
  nodeList *looker = root;

  wordCounts[newWord.length() - 1] += 1;

  tout << newWord << std::endl;

  for(int i = 0; i < newWord.length(); i++)
  {
    if(root == NULL)
    {
      root = createBasicNode(newWord[i]);
      follow = root;
      nextLetterList = looker = root->nextLetter;
      continue;
    }

    looker = isLetterInList(nextLetterList, newWord[i]);

    if(looker == NULL)
    {  //not in list
      looker = addLetterToList(nextLetterList, newWord[i]);

      if(nextLetterList == NULL)
      {
        nextLetterList = looker; //first let in list
      }

      if(follow != nextLetterList)
      { //if not on first level of tree
        follow->nextLetter = nextLetterList;
      }
    }

    follow = looker;
    nextLetterList = looker = looker->nextLetter;
  }

  follow->isWord = true;
}

nodeList *searchTree::createBasicNode(char letter)
{
  nodeList *ptr = new nodeList;
  ptr->letter = letter;
  ptr->nextLocal = ptr->nextLetter = NULL;
  ptr->isWord = false;
  return ptr;
}

nodeList *searchTree::isLetterInList(nodeList *head, char let)
{
  nodeList *temp;
  for(temp = head; temp != NULL; temp = temp->nextLocal)
  {
    if(temp->letter == let)
    {
      return temp;
    }
  }

  return NULL;
}

//this assumes that let is not in list
nodeList *searchTree::addLetterToList(nodeList *head, char let)
{
  nodeList *temp, *follow = NULL;
  for(temp = head; temp != NULL; temp = temp->nextLocal)
  {
    follow = temp;
  }

  temp = createBasicNode(let);

  if(follow != NULL) follow->nextLocal = temp; //if not first letter
  return temp;
}

int searchTree::getAllSubWords(std::string findMe)
{
  int i = 0;
  std::string result("");
  nodeList *lastLet = root;
  nodeList *looker = root;
  char c = 0;

  for(int i = 0; i < findMe.length(); i++)
  {

    while((looker != NULL) && (looker->letter != findMe[i]))
    {
      looker = looker->nextLocal;
    }

    if(looker == NULL)
    {
      return 0;
    }
    else if(looker->letter == findMe[i])
    {
      result += looker->letter;
      if(looker->isWord)
      {
        addToResultTable(result);
      }
    }

    lastLet = looker;
    looker = looker->nextLetter;
  }
}

void searchTree::addToResultTable(std::string str)
{
  int i;
  for(i = 0; resultTable[i] != NULL; i++)
  {
    if(i >= RESULT_TABLE_SIZE)
    {
      std::cout << "Result table limit exceeded\n";
      return;
    }
    if(*resultTable[i] == str)
    {
      return;
    }
  }

  resultTable[i] = new std::string(str);
}

void searchTree::printResultTable()
{
  sortResultTable(&hashCompare);
  sortResultTable(&alphaCompare);
  for(int i = 0; i < RESULT_TABLE_SIZE; i++)
  {
    if(resultTable[i] == NULL) return;
    std::cout << *resultTable[i] << std::endl;
  }
}

void searchTree::resetResultTable()
{
  for(int i = 0; i < RESULT_TABLE_SIZE; i++)
  {
    if(resultTable[i] == NULL) return;
    delete resultTable[i];
    resultTable[i] = NULL;
  }
}

void searchTree::sortResultTable(compareFunc func)
{
  int i, j, m, n;
  std::string *temp;
  for(m = 0; resultTable[m] != NULL; m++);
  i = m-1;
  int lastExchangeIdx;

  while (i > 0)
  {
    lastExchangeIdx = 0;
    for(j = 0; j < i; j++)
    {
      if((*func)(*resultTable[j+1],*resultTable[j]) == 1)
      {
        temp = resultTable[j];
        resultTable[j] = resultTable[j+1];
        resultTable[j+1] = temp;
        lastExchangeIdx = j;
      }
    }

    i = lastExchangeIdx;
  }
}

//return a < b ? 1 : 0
int alphaCompare(std::string a, std::string b)
{
  if(a.length() != b.length()) return -1;

  for(int i = 0; i < a.length(); i++)
  {
    if(a[i] == b[i])
    {
      continue;
    }
    else if(a[i] < b[i])
    {
      return 1;
    }
    else
    {
      return 0;
    }
  }
}

//return a < b ? 1 : 0
int hashCompare(std::string a, std::string b)
{
  int aHash = sortHash(a, 0);
  int bHash = sortHash(b, 0);
  if(aHash == bHash)
  {
    return -1;
  }
  else if(aHash < bHash)
  {
    return 1;
  }
  else
  {
    return 0;
  }
}

int sortHash(std::string str, int idx)
{
  return ((int)str[idx] * (str.length() - 2));
}

void searchTree::findAllSubsOf(std::string str)
{
  operateOnPerms(str, 0);
  printResultTable();
  resetResultTable();
}

void searchTree::operateOnPerms(std::string s, int n)
{
  //std::cout << "[" << s << "][n=" << n << "]" << std::endl;
  int i;
  if( n == s.length()-1 )
  {
    //std::cout << "Get all subwords" << std::endl;
    getAllSubWords(s);
  }
  else
  {
    int k=s.length();

    for( i=n ; i<k ; i++ )
    {
      //std::cout << "[i=" << i << "][n=" << n << "][k=" << k << "]" << std::endl;

      int j;

      for( j=k+n-i ; j<k ; j++ ){
        //std::cout << "[j=" << j << "]" << std::endl;
        if( s[n] == s[j]){
           //std::cout << "Breaking!!" << std::endl;
           break;
        }
      }

      if( j==k ) {
         //std::cout << "Recursing!!" << std::endl;
         operateOnPerms( s,n+1);
      }

      //std::cout << "Rotating!!" << std::endl;
      rotateStr( s, n);
      //std::cout << "[" << s << "][n=" << n << "]" << std::endl;
    }
  }
}

void searchTree::printWordCounts()
{
  for(int i = 0; i < MAX_WORD_LEN; i++)
  {
    if(wordCounts[i])
    {
      std::cout << "Words with " << i+1 << " letters: " << wordCounts[i] << std::endl;
    }
  }
}

void rotateStr( std::string &str, int idx )
{
  char temp = 0;
  int i = 0;
  int len = str.length();

  if(idx < len )
  {
    temp = str[idx];

    for(int i = idx; i<len-1; i++) str[i] = str[i+1];
    str[len-1] = temp;
  }
}

///////////////////////////////////////////
//                                       //
//  Main                                 //
//                                       //
///////////////////////////////////////////

int
main(int argc, char **argv)
{
  treeReader *TR;
  if( argc == 1 )
  {
	TR = new treeReader( "longtree.bin" );
  }
  else
  {
	TR = new treeReader( argv[1] );
  }

  TR->processTree();
  return 0;
}

////////////////////////////////////////////
//                                        //
// File Processing Functions              //
//                                        //
////////////////////////////////////////////

bool
validFile(fileDesc file)
{
    return file != INVALID_HANDLE_VALUE;
}

fileDesc
createFile(const char *fileName)
{
    return CreateFile(fileName,
                      GENERIC_READ | GENERIC_WRITE,
                      FILE_SHARE_READ,
                      NULL,
                      CREATE_ALWAYS,
                      FILE_ATTRIBUTE_NORMAL,
                      NULL);
}

fileDesc
openFile(const char *fileName)
{
    return CreateFile(fileName,
                      GENERIC_READ,
                      FILE_SHARE_READ,
                      NULL,
                      OPEN_EXISTING,
                      0,
                      NULL);
}

int
fileRead(fileDesc fd, void *buf, size_t s)
{
    BOOL successful;
    DWORD br;

    successful = ReadFile((HANDLE)fd, buf, s, &br, 0);
    if(!successful)
        return -1;
    else
        return br;
}

int
fileGetCurrentPointer(fileDesc fd)
{
    return SetFilePointer((HANDLE)fd, 0, NULL, FILE_CURRENT);
}

int
fileSetCurrentPointer(fileDesc fd, unsigned int offset)
{
    return SetFilePointer((HANDLE)fd, offset, NULL, FILE_BEGIN);
}

bool
closeFile(fileDesc file)
{
    return CloseHandle(file);
}

bool
writeFile(fileDesc file, const char *buffer, size_t len)
{
    DWORD bytesWritten;

    return WriteFile(file, buffer, len, &bytesWritten, NULL);
}
