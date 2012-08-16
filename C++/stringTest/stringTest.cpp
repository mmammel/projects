#include <string>
#include <iostream>
#include <list>

void popRef( std::list<int> &thelist );
void popVal( std::list<int> thelist );
void popPtr( std::list<int> *thelist );

int
main()
{
  std::list<int> list1;
  std::list<int> *list2 = new std::list<int>;

  list1.push_back(1);
  list1.push_back(2);
  list1.push_back(3);
  list1.push_back(4);
  list1.push_back(5);

  list2->push_back(1);
  list2->push_back(2);
  list2->push_back(3);
  list2->push_back(4);
  list2->push_back(5);

  std::cout << "list1 size before popRef: " << list1.size() << std::endl;
  popRef( list1 );
  std::cout << "list1 size after popRef: " << list1.size() << std::endl;

  std::cout << "list1 size before popVal: " << list1.size() << std::endl;
  popVal( list1 );
  std::cout << "list1 size after popVal: " << list1.size() << std::endl;

  std::cout << "list1 size before popPtr: " << list1.size() << std::endl;
  popPtr( &list1 );
  std::cout << "list1 size after popPtr: " << list1.size() << std::endl;

  std::cout << "list2 size before popRef: " << list2->size() << std::endl;
  popRef( *list2 );
  std::cout << "list2 size after popRef: " << list2->size() << std::endl;

  std::cout << "list2 size before popVal: " << list2->size() << std::endl;
  popVal( *list2 );
  std::cout << "list2 size after popVal: " << list2->size() << std::endl;

  std::cout << "list2 size before popPtr: " << list2->size() << std::endl;
  popPtr( list2 );
  std::cout << "list2 size after popPtr: " << list2->size() << std::endl;

  return 0;
}

void popRef( std::list<int> &thelist )
{
  thelist.pop_back();
  std::cout << "Popped one in popRef, size is: " << thelist.size() << std::endl;
}

void popVal( std::list<int> thelist )
{
  thelist.pop_back();
  std::cout << "Popped one in popVal, size is: " << thelist.size() << std::endl;
}

void popPtr( std::list<int> *thelist )
{
  thelist->pop_back();
  std::cout << "Popped one in popPtr, size is: " << thelist->size() << std::endl;
}
