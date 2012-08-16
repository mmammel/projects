%%
        char *myp;
[0-9]+\.?[0-9]*(e(\+|-)?[0-9]+)? {
        sscanf(yytext,"%lf",&yylval);
        return(NUM);
        }
[-+*/^\n=()\[\]\,]      {
        return(yytext[0]);
        }
exp     {return(EXP);}
log     {return(LOG);}
sin     {return(SIN);}
cos     {return(COS);}
tan     {return(TAN);}
asin    {return(ASIN);}
acos    {return(ACOS);}
atan    {return(ATAN);}
atan2   {return(ATAN2);}
sinh    {return(SINH);}
cosh    {return(COSH);}
tanh    {return(TANH);}
asinh   {return(ASINH);}
acosh   {return(ACOSH);}
atanh   {return(ATANH);}
\.      {return(DOT);}
!       {return(FAC);}
pi      {yylval=acos(-1.0); return(NUM);}
define  { deflag = 1; return(DEF); }
show    { deflag = 1; return(SHOW);}
degree  {degree=acos(-1.0)/180;}
radian  {degree=1.0;}
precision {return(PRECISION);}
\"[^"\n]*\"     {qsave(); return(QUOTE); }
[a-zA-Z]+[a-zA-Z0-9]*   {
        *pvar = var(yytext);
        if( deflag || def[*pvar] == 0 ){
                ++pvar;
                return(VAR);
                }
        else{
                for( myp = def[*pvar] ; *myp ; ++myp );
                unput(')');
                while( --myp >= def[*pvar] ) unput(*myp);
                unput('(');
                }
        }
. ;
%%
var(s)
char *s;
{
static char *next;
static int n=0;
int i;
for( i=0 ; i<n ; i++ )
        if(strcmp(s,var_name[i]) == 0) return(i);
if( i==0 ) next=store;
strcpy(next,s);
var_name[n++]=next;
while(*next++);
return(n-1);
}
qsave(){
char *p,*q;
q=qbuf;
for( p=yytext+1 ; *p != '"' ; ++p ) *q++ = *p;
*q=0;
}

