#include <fstream.h>
#include <iostream.h>
#include <ctype.h>
#include <string.h>
#include <stdlib.h>
#include "String.h"
#include <stdio.h>

//struct for word table
struct let_node{
   char letter;
  int *len[26];
 let_node *next_let[26];
};

//struct for open hash table
struct hash_node{
   unsigned hash_val;
     hash_node *next;
};

class Dictionary{
private:
    ifstream fin;           //input file stream
    let_node **table;       //The word table      
    hash_node **hash_table; //The hash table
    int max_lets;           //Maximum number of letters in a stored word
    unsigned int word_count;//keep a count that can be queried by the user
public:
    Dictionary(int num, char *infile);  //Default Constructor
   ~Dictionary();                       //Destructor
    void fill();                        //Read words from fin, fill word table
    void add(String s);                 //Add a word to the table on the fly
    void print();                       //not implemented
    int lookup(String s);               //Lookup a word taken from user input
    unsigned int get_count(){return word_count;}
    void wild_card(String s, int n);           //Handle the '*' wildcard character
    void permuter(String s, int n);      //permute a string
};

void print_help();       //print a help message
int getidx(char c);                     //take a lowercase char, return 0-26
char *file_strip(char *ifile, char *ofile);  //lose ".,':;?! from input file

/*
 *start command:
 *$> dic <max letters> <input filename>
 */
int
main(int argc, char *argv[]){
    if(argc != 3){
        cerr << argv[0] << ": Usage: " << argv[0] << " <max_lets> <input filename>"
             << endl;
        return 0;
    }
cout << "Welcome to Max's dictionary program" << endl;
cout << "Type \"halp\" for a help message, and \"kwit\" to quit" << endl;
char *filename = file_strip(argv[2], "jomama2.txt");
Dictionary D(atoi(argv[1]), filename);
D.fill();
String findme(1);
 while(1){
     cout << "Enter word to find: ";
     cin >> findme;
     if(findme == "kwit") break;
     else if(findme == "halp") print_help();
     else if(findme[0] == '+'){
       findme.cut_front();
       if(!D.lookup(findme)){
       D.add(findme);
       cout << findme << " added to table" << endl;
       }
       else cout << findme << " already in table" << endl;
     }
     else if(findme[0] == '#'){
         printf("%d words currently in table\n", D.get_count());
     }
     else if(findme[0] == '!'){
     findme.cut_front();
     D.permuter(findme, 0);
     }
     else{
      int found = D.lookup(findme);
         if(found > 0){
             cout << "Found it: " << findme << endl;
         }
         else if(found == 0) cout << findme << " not in table" << endl;
     }
 }

}

Dictionary::Dictionary(int num, char *infile){
    int i,j,k,l;
    
    word_count = 0;
    //set maximum number of letters 
    max_lets = num;

    //open the input file
    fin.open(infile, ios::in | ios::nocreate);

    //allocate hash table
    hash_table = new hash_node * [26*max_lets];
    for(i = 0; i < 26*max_lets; i++){
        hash_table[i] = NULL;
    }
    
    //allocate word table
    table = new let_node * [max_lets];
    for(i = 0; i < max_lets; i++){
       table[i] = new let_node [26];
       for(j = 0; j < 26; j++){
          table[i][j].letter = (char)(j + 97);
          for(k = 0; k < 26; k++){
             table[i][j].next_let[k] = NULL;
             table[i][j].len[k] = new int [max_lets - 1];
             for(l = 0; l < max_lets - 1; l++) table[i][j].len[k][l] = 0;
          }
       }

    }
}

void Dictionary::fill(){
 String temp(max_lets);
 while(fin >> temp) add(temp);
}

void Dictionary::add(String s){
    
    int i,j;    // for counters
    int temp_len = 0;
    int wordlen, hshcnt;
    unsigned hashval;
    char first_let; //the first letter of each word
    char nextlet; // for next letter
    char mot[32];

    //don't take words of size 1, should fix this
    if(s.length() > max_lets || s.length() == 1) return;
     s.to_lower();           //make word all lower case
     hashval = s.get_hash(); //get the word's hash
     wordlen = s.length();   //get word's length
     first_let = s[0];       //store first letter of the word

     //fill hash table here
     unsigned tmp;
     hash_node *newnode;

     //find which bucket to put hash in
     int hash_idx = (getidx(first_let))*max_lets + wordlen-1;

     //enter the hash into the table, at front of list
     //don't bother checking if it's already there,
     //in tests the hash function had less than 100 matches
     //in 25000+ words.
     newnode = new hash_node;
     newnode->hash_val = hashval;
     newnode->next = hash_table[hash_idx];
     hash_table[hash_idx] = newnode;
     newnode = NULL;

     //fill word table here
     for(i = 0; i < wordlen; i++){     //for each letter in word
         for(j = 0; j < max_lets - 1; j++){  
      
            //See if this word length is already in this letter's  
            //"words that start with first_let" slot
            if(table[i][getidx(s[i])].len[getidx(first_let)][j] == wordlen) break;
            else if(table[i][getidx(s[i])].len[getidx(first_let)][j] == 0){
                    table[i][getidx(s[i])].len[getidx(first_let)][j] = wordlen;
                    break;
            }
  
          }
     //Set the next letter pointer to the next letter
     if(i < s.length() - 1){
        nextlet = s[i + 1];
        table[i][getidx(s[i])].next_let[getidx(nextlet)] = &table[i+1][getidx(nextlet)];
     }
   }
word_count++;
}

int Dictionary::lookup(String s){
    int h,i,j,k;
    int len_find;
    int found_hash, hshcnt;
    int wordlen = s.length();
    unsigned hashval;
    for(h = 0; h < wordlen; h++){
        if(s[h] == '*'){
          wild_card(s,0);
          return -1;
        }
    }
    s.to_lower();
    char first = s[0];  //the first letter of the word
    //first check if the word's hash is in the hash table
    hashval = s.get_hash();
    int hash_idx = (getidx(first))*max_lets + wordlen-1;
    unsigned tmp;
    hash_node *looker;
    found_hash = 0;
    looker = hash_table[hash_idx];
    while(looker != NULL){
        if(looker->hash_val == hashval){
            found_hash = 1;
            break;
        }
     looker = looker->next;
    }
    if(found_hash == 0){
        return 0;
    }
    //hash is in the hash table, check word table
    for(i = 0; i < wordlen-1; i++){
        len_find = 0;
        for(j = 0; j < max_lets - 1; j++){
           if(table[i][getidx(s[i])].len[getidx(first)][j] == wordlen)
               len_find = wordlen;
        }   
        if((table[i][getidx(s[i])].next_let[getidx(s[i+1])] == NULL) || !len_find){
            return 0;
         }
     }
    len_find = 0;
    //now check the last letter's len array
    for(k = 0; k < max_lets - 1; k++){
        if(table[wordlen - 1][getidx(s[wordlen-1])].len[getidx(first)][k] == wordlen)
                len_find = wordlen;
    }
    if(!len_find){
        return 0;
    }
    else{
      return 1;   
    }
}

char *file_strip(char *ifile, char *ofile){
   ifstream fin;
   ofstream fout;
   char buf[256];
   fin.open(ifile, ios::in | ios::nocreate);
   fout.open(ofile, ios::out);
   while(fin.getline(buf,256)){
       fout << strtok(buf,"?:',-&.!") << endl;
   }
 return(ofile);
}

void Dictionary::wild_card(String s, int n){
  int i,j,k;
  if(n == s.length()){
      if(lookup(s)){
          cout << s << endl;
          return;
      }
      else return;
  }
     if(s[n] == '*'){
         for(i = 0; i < 26; i++){
              s.replace(n, char(i+97));
              wild_card(s,n+1);
         }
     }
     else wild_card(s,n+1); 
}

void Dictionary::permuter(String s, int n){
    int i;
    if( (lookup(s)) && (n == s.length()-1) )
    {
        cout << s << endl;
    } else {
        int k=s.length();
        for( i=n ; i<k ; i++ )
        {
            int j;
            for( j=k+n-i ; j<k ; j++ )
                if( s[n] == s[j]) break;
            if( j==k ) permuter( s,n+1);
            s.rotate(n);
        }
    }
}

Dictionary::~Dictionary(){
    int i,j,k,l;
    fin.close();
    hash_node *eraser, *trav; 
    for(l = 0; l < (max_lets*26); l++){
        while(hash_table[l] != NULL){
          eraser = hash_table[l];
          trav = eraser->next;
          hash_table[l] = trav;
          eraser->next = NULL;
          delete eraser;
        }
    }
    delete [] hash_table;
    for(i = 0; i < max_lets; i++){
        for(j = 0; j < 26; j++){
            for(k = 0; k < 26; k++){
               delete [] table[i][j].len[k];
            }
        }
      delete [] table[i];
    }
}

int getidx(char c){
  return((int)c - 97);
}
         
void print_help(){
cout << endl;
cout << "Commands: " << endl;
cout << "   # - prints the number of words in the table" << endl;
cout << "   <word> - indicates whether or not <word> is in the table" << endl;
cout << "   +<word> - adds <word> to the table if it is not already present" << endl;
cout << "   !<letters> - prints all permutations of <letters> that appear in the table" << endl;
cout << "   * - represents a wildcard for one letter, can be used multiple times in a word" << endl;
cout << "     For example, typing *** will print out every three letter word in the table" << endl;
cout << endl;
}






