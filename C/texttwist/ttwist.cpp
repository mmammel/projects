#include <stdlib.h>
#include <fstream.h>
#include <iostream.h>
#include <ctype.h>
#include <windows.h>

#define MAX_WORD_LEN 7

typedef HANDLE fileDesc;

bool validFile(fileDesc file);
fileDesc createFile(const char *fileName);
fileDesc openFile(const char *fileName);
int fileRead(fileDesc fd, void *buf, size_t s);
int fileGetCurrentPointer(fileDesc fd);
int fileSetCurrentPointer(fileDesc fd, unsigned int offset);
bool closeFile(fileDesc file);
bool writeFile(fileDesc file, const char *buffer, size_t len);

enum state { S_START=0, S_ALPHA=1, S_ZERO=2, S_ONE=3, S_ERROR=5 };

class treeReader {
private:
  ofstream fout;
  fileDesc descriptor;
  int markers[MAX_WORD_LEN], reader;
  state currentState;
  char currentVal;
  static const char fileName[];
public:
  treeReader(){
    fout.open("ttwist_output.txt", ios::out);
    descriptor = openFile(fileName);
    currentState = S_START;
    for(int i = 0; i < MAX_WORD_LEN; i++){
      markers[i] = -1;
    }
    reader = 0;
    fileSetCurrentPointer(descriptor, reader);
    currentVal = getCharacter(reader);
  }

  ~treeReader(){
    fout.close();
    closeFile(descriptor);
  }
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

void treeReader::processTree(){
  char value;
  while(value = getCharacter(reader)){
    if(isalpha(value)){
      foundAlpha();
    } else if(value == '0') {
      foundZero();
    } else if(value == '1') {
      foundOne();
    } else {
      cout << "Error: foreign character found\n";
      exit(1);
    }
    advanceReader();
  }
  cout << "Done!\n";
}

void treeReader::foundAlpha(){
  switch(currentState){
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

void treeReader::foundZero(){
  switch(currentState){
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

void treeReader::foundOne(){
  switch(currentState){
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

void treeReader::getWordSkipLast(){
  char foundWord[MAX_WORD_LEN + 1];
  int i;
  for(i = 0; i < MAX_WORD_LEN; i++){
    if(markers[i] == -1) break;
      foundWord[i] = getCharacter(markers[i]);
  }
  foundWord[i - 1] = '\0';
  fout << foundWord << endl;
}

void treeReader::getWordFull(){
  char foundWord[MAX_WORD_LEN + 1];
  int i;
  for(i = 0; i < MAX_WORD_LEN; i++){
    if(markers[i] == -1) break;
      foundWord[i] = getCharacter(markers[i]);
  }
  foundWord[i] = '\0';
  fout << foundWord << endl;
}

char treeReader::getCharacter(int pos){
  void *buf = malloc(32);
  char *returnVal, returnChar;
  fileSetCurrentPointer(descriptor, pos);
  if(!fileRead(descriptor, buf, 1)) {
    return 0; //EOF
  }
  fileSetCurrentPointer(descriptor, pos);  //don't increment
  returnVal = (char*)buf;
  returnChar = returnVal[0];
  free((char*)buf);
  return returnChar;
}

char treeReader::lookAtNext(int pos){
  char returnChar;
  returnChar = getCharacter(pos+1);  //check for EOF
  return returnChar;
}

void treeReader::setNextMarker(int pos){
  int i = 0;
  while((markers[i] != -1) && (i < MAX_WORD_LEN)) i++;
  markers[i] = pos;
}

void treeReader::deleteAndAdvanceMarkers(){
  for(int i = MAX_WORD_LEN - 1; i >= 0; i--){
    if(markers[i] != -1){
      if(!isalpha(lookAtNext(markers[i]))){
	markers[i] = -1;
      } else {
	markers[i]++;
	return;
      }
    }
  }
}

const char treeReader::fileName[] = "longtree.bin";

///////////////////////////////////////////
//                                       //
//  Main                                 //
//                                       //
///////////////////////////////////////////

int
main(){

  treeReader TR;
  TR.processTree();
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
