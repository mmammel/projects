///////////////////////////////////////////////////////////////
//
//   Member functions for class String
//
//////////////////////////////////////////////////////////////


#include "String.h"
#include <iostream.h>
#include <string.h>
#include <ctype.h>
#include <math.h>
#include <stdlib.h>

///////////////////default constructor//////////////////////////////

String::String(unsigned n) : len(n){
  buf = new char[len + 1];
  for(int i = 0; i < len; i++) buf[i] = ' ';
  buf[len] = '\0';
}

////////////////////////constructor////////////////////////////////

String::String(char c, unsigned n) : len(n){
  buf = new char [len+1];
  for(int i = 0; i < len; i++) buf[i] = c;
  buf[len] = '\0';
}

/////////////////////// constructor///////////////////////////

String::String(const char* s){
  len = strlen(s);
  buf = new char [len+1];
  for (int i = 0; i < len; i++) buf[i] = s[i];
  buf[len] = '\0';
}

//////////////////////destructor///////////////////////////////////

String::~String(){
  delete [] buf;
}

/////////////////////copy constructor//////////////////////////////

String::String(const String& s) : len(s.len){
  buf = new char [len+1];
  for(int i = 0; i < len; i++) buf[i] = s.buf[i];
  buf[len] = '\0';
}

////////////////////output////////////////////////////////////////

ostream& operator<<(ostream& ostr, const String& s){
  return ostr << s.buf;
}


//////////////////input//////////////////////////////////////////

istream& operator>>(istream& istr, String& s){
  char buffer[256];
  istr >> buffer;
  s.len = strlen(buffer);
  delete [] s.buf;
  s.buf = new char [s.len+1];
  for(int i = 0; i < s.len; i++) s.buf[i] = buffer[i];
  s.buf[s.len] = '\0';
  return istr;
}

///////////////////logical operations////////////////////////////

int operator==(const String& s1, const String& s2){
  return (strcmp(s1.buf, s2.buf) == 0);
}

int operator==(const String& s1, const char *p){
  return (strcmp(s1.buf, p) == 0);
}

int operator!=(const String& s1, const String& s2){
  return (strcmp(s1.buf, s2.buf) != 0);
}

int operator<(const String& s1, const String& s2){
  return (strcmp(s1.buf, s2.buf) < 0);
}

int operator<=(const String& s1, const String& s2){
  return (strcmp(s1.buf, s2.buf) <= 0);
}

int operator>(const String& s1, const String& s2){
  return (strcmp(s1.buf, s2.buf) > 0);
}

int operator>=(const String& s1, const String& s2){
  return (strcmp(s1.buf, s2.buf) >= 0);
}

/////////////////////////append operator////////////////////////

String operator+(const String& s1, const String& s2){
   String temp(s1.len + s2.len);
   strcpy(temp.buf, s1.buf);
   strcat(temp.buf, s2.buf);
   return temp;
}

/////////////////////////assignment operator///////////////////

String& String::operator=(const String& s){
  if (&s == this) return *this;
  len = s.len;
  delete [] buf;
  buf = new char [s.len + 1];
  strcpy(buf, s.buf);
  return *this;
}

////////////////////////append assignment////////////////////

String& String::operator+=(const String& s){
  len += s.len;
  char* tempbuf = new char [len + 1];
  strcpy(tempbuf, buf);
  strcat(tempbuf, s.buf);
  delete [] buf;
  buf = new char [len + 1];
  strcpy(buf, tempbuf);
  delete [] tempbuf;
  return *this;
}

String& String::operator+=(const char& c){
  int i;
  len += 1;
  char *tempbuf = new char [len + 1];
  for(i = 0; i < (len-1); i++) tempbuf[i] = buf[i];
  tempbuf[len - 1] = c;
  tempbuf[len] = '\0';
  delete [] buf;
  buf = new char [len + 1];
  strcpy(buf, tempbuf);
  delete [] tempbuf;
  return *this;
}

///////////////////////access to length//////////////////////////

unsigned String::length() const{
  return len;
}

///////////////////////index operator///////////////////////////

char& String::operator[](unsigned i) const{
  return buf[i];
}

/////////////////////////conversion operator////////////////////

String::operator char*() const{
  return buf;
}

/////////////////////////fill up with char//////////////////////

void String::fill(char c){
for(int i = 0; i < len; i++) buf[i] = c;
buf[len] = '\0';
}


String* String::doubles(){
String* p = new String[len-1];
 String temp(2); 
 for(int i = 0; i < len - 1; i++){
   temp[0] = buf[i];
   temp[1] = buf[i+1];
   p[i] = temp;
  }
return(p);
}

void String::printdoubles(){
 String* p = doubles();
 for(int i = 0; i < len - 1; i++) cout << p[i] << endl;
 }

void String::to_lower(){
  int i;
  for(i = 0; i < len; i++){
    buf[i] = char(tolower((int)buf[i]));
  }
}

//take one character from the front of the string

char String::cut_front(){
  char c;
  int i;
  if(len == 1) return 0;
  len--;
  c = buf[0];
  for(i = 0; i <= len; i++)
      buf[i] = buf[i+1];
  return c;
}

//cut one character from end of string

char String::cut_end(){
  char c;
  if(len == 1) return 0;
  c = buf[--len];
  buf[len] = '\0';
  return c;
}

//For experimental dictionary prog
//BEWARE!! Sets string to all lower case!

unsigned String::get_hash(){
to_lower();
unsigned hash = 0;
  for(int i = 0; i < len; i++){
   hash += (unsigned)(pow((((int)buf[i] - 96)),4) * (i+1));
  }
return hash;
}

void String::replace(int idx, char let){
  buf[idx] = let;
}

void String::rotate(int idx) {
    if(idx >= len) return;
    char temp;
    int i = idx;
    temp = buf[idx];
    for(i = idx; i<len-1; i++) buf[i] = buf[i+1];
    buf[len-1] = temp;
}
    







