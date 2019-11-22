#include "stdio.h"
#include "./config.h"

#ifndef FOOBAR
  void printsomething( str )
   char * str; {
    printf("The string: %s\n", str );
  }
#else
  void printsomething( str )
   char * str; {
    FOOBAR("The strang: %s\n", str );
  }
#endif

int
main( int argv, char ** argc ) {
  printsomething( "foo" );
}
