#include <iostream.h>
#include <stdlib.h>

struct fibEle
{
  int val;
  fibEle *next;
};

void fib(int limit);
fibEle *getElement( int value );


fibEle *getElement( int value )
{
  fibEle *temp = new fibEle();
  temp->val = value;
  temp->next = NULL;
  return temp;
}

void fib(int limit)
{
  fibEle *head;
  fibEle *prev;
  fibEle *curr;

  prev = new fibEle;
  prev->val = 1;
  
  curr = new fibEle;
  curr->val = 1;
  prev->next = curr;
  head = prev;
  
  int temp = 0;

  while( (prev->val + curr->val) <= limit )
  {
    temp = prev->val + curr->val;
    prev = curr;
    curr = getElement( temp );
    prev->next = curr;
  } 

  fibEle *deleter = NULL;
  fibEle *printer = head;

  while( printer != NULL )
  {
    deleter = printer;
    cout << printer->val << " ";
    printer = printer->next;
    delete deleter;
  }

  cout << endl;
}

int
main( int argc, char **argv)
{
  int value = 0;
  int retVal = 0;

  if( argc != 2 )
  {
    cout << "Usage: fib.exe <limit>" << endl;
    retVal = 1;
  }
  else
  {
    value = atoi( argv[1] );
    fib(value);
  }

  return retVal;
}
