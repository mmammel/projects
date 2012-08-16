#include <iostream.h>

class String{
    friend int operator==(const String&, const char*);
    friend int operator==(const String&, const String&);
    friend int operator!=(const String&, const String&);
    friend int operator<(const String&, const String&);
    friend int operator<=(const String&, const String&);
    friend int operator>(const String&, const String&);
    friend int operator>=(const String&, const String&);
    friend ostream& operator<<(ostream&, const String&);
    friend istream& operator>>(istream&, String&);
    friend String operator+(const String&, const String&);
public:
    String(unsigned =0);                 //default constructor
    String(char, unsigned);              //constructor
    String(const char*);                 //constructor
    String(const String&);               //copy constructor
    ~String();                           //destructor
    String& operator=(const String&);    //assignment
    String& operator+=(const String&);   //append
    String& operator+=(const char&);     //append char
    operator char*() const;              //conversion
    char& operator[](unsigned) const;    //subscript
    unsigned length() const;             //access function
    void fill(char);
    String* doubles();                   //returns array of size len-1
    void printdoubles();
    void to_lower();                     //turn all letters to lower case
    char cut_front();                    //remove first char of String
    char cut_end();                      //remove last char of String
    unsigned get_hash();                 //For dictionary program
    void replace(int idx, char let);     //set String[idx] to let
    void rotate(int idx);                //rotate from S[idx] on
private:
    unsigned len;        //length of string
    char *buf;           //the string
    };




