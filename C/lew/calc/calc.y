%token NUM,DOT,VAR,EXP,LOG,SIN,COS,TAN,ASIN,ACOS,ATAN,ATAN2,DEF,QUOTE,SHOW,PRECISION,SINH,COSH,T
ANH,ASINH,ACOSH,ATANH,FAC
%right '='
%left '+' '-'
%left '*' '/'
%left '^'
%left UMINUS SIN COS TAN ASIN ACOS ATAN ATAN2 EXP LOG SINH COSH TANH ASINH ACOSH ATANH
%{
#define YYSTYPE double
#include <math.h>
#include <stdio.h>
double pow(),exp(),log(),variable[100];
int vardex[20],*pvar,vari,deflag;
char qbuf[100],qstore[1000],*def[100],*qnext;
char store[500], *var_name[100]; /* storage for variable names */
double last;
double degree;
int precision;
extern int signgam;
double acosh(x) double x;{ return( log( x + sqrt(x*x-1.0) ) ); }
double asinh(x) double x;{ return( log( x + sqrt(x*x+1.0) ) ); }
double atanh(x) double x;{ return( log( (1.0+x)/(1.0-x) )/2.0 ); }
%}
%%
list:
        ={
        for( vari=0 ; vari<100 ; vari++ ){
                variable[vari] = 0.0;
                def[vari] = 0;
                }
        pvar = vardex;
        qnext=qstore;
        deflag=0;
        degree=1.0;
        precision=3;
        }|
        list cmd;|
        list error '\n' ={
                pvar = vardex;
                deflag = 0;
                yyerrok;
                }
cmd:
        expr '\n'       ={
                last = $1;
                printf("%.*g\n",precision,$1);
                }|
        DEF VAR QUOTE '\n' ={
               strcpy(qnext,qbuf);
                def[*--pvar] = qnext;
                while(*qnext++);
                deflag = 0;
                }|
        SHOW '\n' ={
                for( vari=0 ; vari<100 ; vari++ ) if( def[vari] ){
                        printf("%s\t%s\n",var_name[vari],def[vari]);
                }
                deflag = 0;
                }|
        show '\n' ={
                deflag = 0;
                }|
        PRECISION '=' NUM ={
                precision = $3 ;
                }|
        '\n';
show:
        SHOW ;|
        show VAR ={
                if( def[*--pvar] == 0 ){
                        printf("%s not defined\n", var_name[*pvar]);
                }
                else {
                        printf("%s\t%s\n",var_name[*pvar],def[*pvar]);
                }
        }
expr:
        '(' expr ')' ={
                $$ = $2;
                }|
        '[' expr ',' expr ']'   ={
                $$ = exp( gamma( $2 + 1)-gamma($2- $4 +1) -gamma($4+1));
                }|
        expr '*' expr   ={
                $$ = $1 * $3;
                }|
        expr '+' expr ={
                $$ = $1 + $3;
                }|
        expr '-' expr ={
                $$ = $1 - $3;
                }|
        expr '/' expr ={
                $$ = $1 / $3;
                }|
        expr '^' expr ={
               $$ = pow($1,$3);
                }|
        '-' expr %prec UMINUS ={
                $$ = -$2;
                }|
        EXP expr  ={
                $$ = exp($2);
                }|
        LOG expr  ={
                $$ = log($2);
                }|
        SIN expr  ={
                $$ = sin(degree*$2);
                }|
        COS expr  ={
                $$ = cos(degree*$2);
                }|
        TAN expr  ={
                $$ = tan(degree*$2);
                }|
        ASIN expr  ={
                $$ = asin($2)/degree;
                }|
        ACOS expr  ={
                $$ = acos($2)/degree;
                }|
        ATAN expr  ={
                $$ = atan($2)/degree;
                }|
        ATAN2 expr expr  ={
                $$ = atan2($2,$3)/degree;
                }|
        SINH expr  ={
                $$ = sinh($2);
                }|
        COSH expr  ={
                $$ = cosh($2);
                }|
        TANH expr  ={
                $$ = tanh($2);
                }|
        ASINH expr  ={
                $$ = asinh($2);
                }|
        ACOSH expr  ={
                $$ = acosh($2);
                }|
        ATANH expr  ={
                $$ = atanh($2);
                }|
        expr FAC ={
                $$ = exp(gamma($1+1))*signgam;
                }|
        VAR '=' expr    ={
                variable[*--pvar] = $3;
                $$ = $3;
                }|
        VAR     ={
                $$ = variable[*--pvar];
                }|
        DOT     ={
                $$ = last;
                }|
        NUM     ={
                $$ = $1;
                }
%%
#include "calc.l.c"

