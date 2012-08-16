main(argc,argv)
int argc;
char **argv;
{
int i,N;
double drand48();
N=atoi(argv[1]);
srand48(getpid());
for( i=0 ; i<N ; i++ ){
printf("%.0f %.0f\n",drand48()*1000000000, drand48()*1000000000);
}
}
