int
main( int argc, char ** argv )
{
  char * cptr = (char*)malloc(4*sizeof(char));
  printf( "cptr: %p\n", cptr );
  cptr++;
  int * iptr = (int*)cptr;
  printf( "cptr: %p, iptr: %p\n", cptr, iptr );
  *iptr = 256;
  printf( "value of iptr: %d\n", *iptr );
  cptr--;
  iptr = (int*)cptr;
  *iptr = 327;
  printf( "value of iptr: %d\n", *iptr );
  cptr++;
  iptr = (int*)cptr;
  printf( "value of iptr: %d\n", *iptr );
}
