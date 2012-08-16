#ifndef lint
static char const 
yyrcsid[] = "$FreeBSD: src/usr.bin/yacc/skeleton.c,v 1.28 2000/01/17 02:04:06 bde Exp $";
#endif
#include <stdlib.h>
#define YYBYACC 1
#define YYMAJOR 1
#define YYMINOR 9
#define YYLEX yylex()
#define YYEMPTY -1
#define yyclearin (yychar=(YYEMPTY))
#define yyerrok (yyerrflag=0)
#define YYRECOVERING() (yyerrflag!=0)
static int yygrowstack();
#define YYPREFIX "yy"
#line 9 "calc.y"
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
#line 32 "y.tab.c"
#define YYERRCODE 256
#define NUM 257
#define DOT 258
#define VAR 259
#define EXP 260
#define LOG 261
#define SIN 262
#define COS 263
#define TAN 264
#define ASIN 265
#define ACOS 266
#define ATAN 267
#define ATAN2 268
#define DEF 269
#define QUOTE 270
#define SHOW 271
#define PRECISION 272
#define SINH 273
#define COSH 274
#define T 275
#define ANH 276
#define ASINH 277
#define ACOSH 278
#define ATANH 279
#define FAC 280
#define UMINUS 281
#define TANH 282
const short yylhs[] = {                                        -1,
    0,    0,    0,    1,    1,    1,    1,    1,    1,    3,
    3,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    2,    2,    2,    2,    2,    2,    2,    2,    2,
};
const short yylen[] = {                                         2,
    0,    2,    3,    2,    4,    2,    2,    3,    1,    1,
    2,    3,    5,    3,    3,    3,    3,    3,    2,    2,
    2,    2,    2,    2,    2,    2,    2,    3,    2,    2,
    2,    2,    2,    2,    2,    3,    1,    1,    1,
};
const short yydefred[] = {                                      1,
    0,    0,   39,   38,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    9,    0,    0,    2,    0,    0,
    3,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    6,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   35,    0,    0,    0,    0,    0,    4,
   11,    7,    0,    0,    0,    0,    8,   12,    0,    0,
    0,    0,    0,    0,    0,    5,    0,   13,
};
const short yydgoto[] = {                                       1,
   28,   29,   30,
};
const short yysindex[] = {                                      0,
 1036,   -4,    0,    0,  -53, 1189, 1189, 1189, 1189, 1189,
 1189, 1189, 1189, 1189, -250,    2,  -51, 1189, 1189, 1189,
 1189, 1189, 1189, 1189,    0, 1189, 1189,    0,    6,    1,
    0, 1189, -267, -267, -267, -267, -267, -267, -267, -267,
 1076, -256,    0, -242, -267, -267, -267, -267, -267, -267,
 -267,  -40,   61,    0, 1189, 1189, 1189, 1189, 1189,    0,
    0,    0,  454, 1189, -267,    7,    0,    0, 1189,   93,
   93,  -90,  -90, -267, -267,    0,  138,    0,
};
const short yyrindex[] = {                                      0,
    0,    0,    0,    0,  -10,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -241,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   16,   45,   72,  101,  127,  168,  194,  372,
    0,    0,    0,    0,  438,  464,  493,  520,  562,  642,
  763,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, 1118,    0,  832,    0,    0,    0,    0, 1163,
 1229,  805,  959,  887,  923,    0,    0,    0,
};
const short yygindex[] = {                                      0,
    0, 1506,    0,
};
#define YYTABLESIZE 1575
const short yytable[] = {                                      37,
   68,   57,   55,   59,   56,   31,   58,   32,   42,   44,
   62,   43,   54,   66,   67,   60,   76,   10,    0,    0,
    0,    0,    0,    0,    0,   20,    0,    0,    0,   37,
   37,   37,   37,   37,   37,    0,   37,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   57,   55,    0,
   56,    0,   58,   59,   21,   20,   20,   20,   20,   20,
   20,    0,   20,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   37,   22,   37,   37,   21,   21,   21,   21,   21,   21,
    0,   21,    0,    0,    0,    0,    0,    0,    0,   59,
    0,    0,   57,   55,   69,   56,   20,   58,   20,   20,
   23,   22,   22,   22,   22,   22,   22,    0,   22,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   57,   21,   24,   21,   21,   58,
   23,   23,   23,   23,   23,   23,    0,   23,    0,    0,
    0,    0,    0,    0,   59,    0,    0,    0,    0,    0,
    0,    0,   22,    0,   22,   22,   24,   24,   24,   24,
   24,   24,    0,   24,    0,    0,    0,   25,    0,   57,
   55,    0,   56,    0,   58,    0,   59,    0,    0,   54,
    0,   23,    0,   23,   23,    0,    0,    0,    0,    0,
    0,    0,    0,   26,    0,    0,    0,   25,   25,   25,
   25,   25,   25,    0,   25,    0,    0,   24,    0,   24,
   24,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   78,   59,    0,   26,   26,   26,   26,   26,   26,   54,
   26,    0,    0,    0,    0,    0,   37,   37,   37,   37,
   37,   37,   37,   37,   37,   37,   37,   37,   25,   61,
   25,   25,   37,   37,    0,    0,   37,   37,   37,   37,
    0,   37,   20,   20,   20,   20,   20,   20,   20,   20,
   20,   20,   20,   20,   26,   54,   26,   26,   20,   20,
    0,    0,   20,   20,   20,    0,    0,   20,    0,    0,
    0,   21,   21,   21,   21,   21,   21,   21,   21,   21,
   21,   21,   21,    0,    0,    0,    0,   21,   21,    0,
    0,   21,   21,   21,    0,    0,   21,    0,   22,   22,
   22,   22,   22,   22,   22,   22,   22,   22,   22,   22,
   54,    0,    0,    0,   22,   22,    0,    0,   22,   22,
   22,    0,    0,   22,    0,    0,    0,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,    0,
    0,    0,   54,   23,   23,    0,    0,   23,   23,   23,
    0,   27,   23,   24,   24,   24,   24,   24,   24,   24,
   24,   24,   24,   24,   24,    0,    0,    0,    0,   24,
   24,    0,    0,   24,   24,   24,    0,    0,   24,    0,
    0,   27,   27,   27,   27,   27,   27,   54,   27,    0,
    0,    0,    0,    0,   25,   25,   25,   25,   25,   25,
   25,   25,   25,   25,   25,   25,    0,    0,    0,    0,
   25,   25,    0,    0,   25,   25,   25,   29,    0,   25,
   26,   26,   26,   26,   26,   26,   26,   26,   26,   26,
   26,   26,   27,    0,   27,   27,   26,   26,    0,    0,
   26,   26,   26,   30,    0,   26,    0,   29,   29,   29,
   29,   29,   29,    0,   29,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   57,   55,    0,   56,    0,
   58,    0,   32,   30,   30,   30,   30,   30,   30,    0,
   30,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   29,   33,
   29,   29,   32,   32,   32,   32,   32,   32,    0,   32,
    0,    0,    0,    0,    0,    0,    0,   59,    0,    0,
    0,    0,    0,    0,   30,    0,   30,   30,    0,   33,
   33,   33,   33,   33,   33,    0,   33,    0,    0,    0,
    0,   34,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   32,    0,   32,   32,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   34,   34,   34,   34,   34,   34,    0,   34,    0,
   33,    0,   33,   33,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   27,   27,
   27,   27,   27,   27,   27,   27,   27,   27,   27,   27,
    0,    0,    0,    0,   27,   27,    0,    0,   27,   27,
   27,   19,   34,   27,   34,   34,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   19,   19,   19,   19,   19,   19,    0,   19,    0,
    0,    0,    0,    0,   29,   29,   29,   29,   29,   29,
   29,   29,   29,   29,   29,   29,    0,    0,    0,    0,
   29,   29,    0,    0,   29,   29,   29,    0,    0,   29,
   30,   30,   30,   30,   30,   30,   30,   30,   30,   30,
   30,   30,   19,   54,   19,   19,   30,   30,    0,    0,
   30,   30,   30,    0,    0,   30,    0,    0,    0,   32,
   32,   32,   32,   32,   32,   32,   32,   32,   32,   32,
   32,    0,    0,    0,    0,   32,   32,    0,    0,   32,
   32,   32,   31,    0,   32,    0,   33,   33,   33,   33,
   33,   33,   33,   33,   33,   33,   33,   33,    0,    0,
    0,    0,   33,   33,    0,    0,   33,   33,   33,    0,
    0,   33,   31,   31,   31,   31,   31,   31,    0,   31,
    0,    0,    0,    0,   14,    0,    0,    0,   34,   34,
   34,   34,   34,   34,   34,   34,   34,   34,   34,   34,
    0,    0,    0,    0,   34,   34,    0,    0,   34,   34,
   34,   28,    0,   34,   14,   14,   14,   14,   14,   14,
    0,   14,    0,   31,    0,   31,   31,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   28,   28,   28,   28,   28,   28,    0,   28,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   14,   18,   14,   19,   19,
   19,   19,   19,   19,   19,   19,   19,   19,   19,   19,
    0,    0,    0,    0,   19,   19,    0,    0,   19,   19,
   19,    0,   28,   19,   28,   28,   18,   18,   18,   18,
   18,   18,   19,   18,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   16,   19,   19,   16,   19,   16,   17,   19,
    0,    0,    0,    0,    0,    0,    0,   18,    0,   18,
   18,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   17,   17,
   17,   17,   17,   17,    0,   17,    0,    0,    0,    0,
    0,    0,    0,   16,    0,   19,   19,    0,    0,   31,
   31,   31,   31,   31,   31,   31,   31,   31,   31,   31,
   31,    0,    0,    0,    0,   31,   31,    0,    0,   31,
   31,   31,    0,    0,   31,   25,    0,    0,    0,   17,
    0,   17,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   14,   14,   14,   14,   14,   14,   14,   14,   14,
   14,   14,   14,    0,    0,   26,    0,   14,   14,    0,
   23,   14,   14,   14,    0,    0,   14,    0,   28,   28,
   28,   28,   28,   28,   28,   28,   28,   28,   28,   28,
    0,    0,    0,    0,   28,   28,    0,    0,   28,   28,
   28,    0,    0,   28,    0,   26,    0,   57,   55,    0,
   64,    0,   58,    0,    0,    0,   27,   36,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   18,   18,   18,   18,   18,   18,   18,
   18,   18,   18,   18,   18,    0,    0,   36,   36,   18,
   18,   36,    0,   18,   18,   18,   27,    0,   18,   59,
    0,    0,   15,    0,    0,    0,    0,    0,    0,   16,
   16,   16,   16,   16,   16,   16,   16,   16,   16,   16,
   16,    0,    0,    0,    0,   16,   16,    0,    0,   16,
   16,   16,   15,   15,   16,   15,   15,   15,   36,    0,
   36,    0,    0,    0,    0,   17,   17,   17,   17,   17,
   17,   17,   17,   17,   17,   17,   17,    0,   26,    0,
    0,   17,   17,   23,    0,   17,   17,   17,   16,    0,
   17,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   15,    0,   15,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   16,   16,
    0,   16,   16,   16,    0,    0,    0,    0,    0,   27,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    2,    3,    4,    5,    6,    7,    8,    9,   10,
   11,   12,   13,   14,   15,    0,   16,   17,   18,   19,
    0,    0,   20,   21,   22,    0,    0,   24,    0,   16,
    0,   16,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    3,    4,    5,    6,    7,    8,    9,   10,
   11,   12,   13,   14,    0,    0,    0,    0,   18,   19,
    0,    0,   20,   21,   22,   54,    0,   24,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   36,   36,   36,   36,   36,   36,
   36,   36,   36,   36,   36,   36,    0,    0,    0,    0,
   36,   36,    0,    0,   36,   36,   36,    0,    0,   36,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   15,
   15,   15,   15,   15,   15,   15,   15,   15,   15,   15,
   15,    0,    0,    0,    0,   15,   15,    0,    0,   15,
   15,   15,    0,    0,   15,    3,    4,    5,    6,    7,
    8,    9,   10,   11,   12,   13,   14,    0,    0,    0,
    0,   18,   19,    0,    0,   20,   21,   22,    0,    0,
   24,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   16,   16,   16,   16,   16,
   16,   16,   16,   16,   16,   16,   16,    0,    0,    0,
    0,   16,   16,    0,    0,   16,   16,   16,    0,    0,
   16,   33,   34,   35,   36,   37,   38,   39,   40,   41,
    0,    0,    0,   45,   46,   47,   48,   49,   50,   51,
    0,   52,   53,    0,    0,    0,    0,   63,    0,    0,
    0,    0,    0,    0,    0,    0,   65,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   70,   71,   72,   73,   74,    0,    0,    0,    0,   75,
    0,    0,    0,    0,   77,
};
const short yycheck[] = {                                      10,
   41,   42,   43,   94,   45,   10,   47,   61,  259,   61,
   10,   10,  280,  270,  257,   10,   10,  259,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   10,   -1,   -1,   -1,   40,
   41,   42,   43,   44,   45,   -1,   47,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   42,   43,   -1,
   45,   -1,   47,   94,   10,   40,   41,   42,   43,   44,
   45,   -1,   47,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   91,   10,   93,   94,   40,   41,   42,   43,   44,   45,
   -1,   47,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   94,
   -1,   -1,   42,   43,   44,   45,   91,   47,   93,   94,
   10,   40,   41,   42,   43,   44,   45,   -1,   47,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   42,   91,   10,   93,   94,   47,
   40,   41,   42,   43,   44,   45,   -1,   47,   -1,   -1,
   -1,   -1,   -1,   -1,   94,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   91,   -1,   93,   94,   40,   41,   42,   43,
   44,   45,   -1,   47,   -1,   -1,   -1,   10,   -1,   42,
   43,   -1,   45,   -1,   47,   -1,   94,   -1,   -1,  280,
   -1,   91,   -1,   93,   94,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   10,   -1,   -1,   -1,   40,   41,   42,
   43,   44,   45,   -1,   47,   -1,   -1,   91,   -1,   93,
   94,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   93,   94,   -1,   40,   41,   42,   43,   44,   45,  280,
   47,   -1,   -1,   -1,   -1,   -1,  257,  258,  259,  260,
  261,  262,  263,  264,  265,  266,  267,  268,   91,  259,
   93,   94,  273,  274,   -1,   -1,  277,  278,  279,  280,
   -1,  282,  257,  258,  259,  260,  261,  262,  263,  264,
  265,  266,  267,  268,   91,  280,   93,   94,  273,  274,
   -1,   -1,  277,  278,  279,   -1,   -1,  282,   -1,   -1,
   -1,  257,  258,  259,  260,  261,  262,  263,  264,  265,
  266,  267,  268,   -1,   -1,   -1,   -1,  273,  274,   -1,
   -1,  277,  278,  279,   -1,   -1,  282,   -1,  257,  258,
  259,  260,  261,  262,  263,  264,  265,  266,  267,  268,
  280,   -1,   -1,   -1,  273,  274,   -1,   -1,  277,  278,
  279,   -1,   -1,  282,   -1,   -1,   -1,  257,  258,  259,
  260,  261,  262,  263,  264,  265,  266,  267,  268,   -1,
   -1,   -1,  280,  273,  274,   -1,   -1,  277,  278,  279,
   -1,   10,  282,  257,  258,  259,  260,  261,  262,  263,
  264,  265,  266,  267,  268,   -1,   -1,   -1,   -1,  273,
  274,   -1,   -1,  277,  278,  279,   -1,   -1,  282,   -1,
   -1,   40,   41,   42,   43,   44,   45,  280,   47,   -1,
   -1,   -1,   -1,   -1,  257,  258,  259,  260,  261,  262,
  263,  264,  265,  266,  267,  268,   -1,   -1,   -1,   -1,
  273,  274,   -1,   -1,  277,  278,  279,   10,   -1,  282,
  257,  258,  259,  260,  261,  262,  263,  264,  265,  266,
  267,  268,   91,   -1,   93,   94,  273,  274,   -1,   -1,
  277,  278,  279,   10,   -1,  282,   -1,   40,   41,   42,
   43,   44,   45,   -1,   47,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   42,   43,   -1,   45,   -1,
   47,   -1,   10,   40,   41,   42,   43,   44,   45,   -1,
   47,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   91,   10,
   93,   94,   40,   41,   42,   43,   44,   45,   -1,   47,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   94,   -1,   -1,
   -1,   -1,   -1,   -1,   91,   -1,   93,   94,   -1,   40,
   41,   42,   43,   44,   45,   -1,   47,   -1,   -1,   -1,
   -1,   10,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   91,   -1,   93,   94,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   40,   41,   42,   43,   44,   45,   -1,   47,   -1,
   91,   -1,   93,   94,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,
  259,  260,  261,  262,  263,  264,  265,  266,  267,  268,
   -1,   -1,   -1,   -1,  273,  274,   -1,   -1,  277,  278,
  279,   10,   91,  282,   93,   94,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   40,   41,   42,   43,   44,   45,   -1,   47,   -1,
   -1,   -1,   -1,   -1,  257,  258,  259,  260,  261,  262,
  263,  264,  265,  266,  267,  268,   -1,   -1,   -1,   -1,
  273,  274,   -1,   -1,  277,  278,  279,   -1,   -1,  282,
  257,  258,  259,  260,  261,  262,  263,  264,  265,  266,
  267,  268,   91,  280,   93,   94,  273,  274,   -1,   -1,
  277,  278,  279,   -1,   -1,  282,   -1,   -1,   -1,  257,
  258,  259,  260,  261,  262,  263,  264,  265,  266,  267,
  268,   -1,   -1,   -1,   -1,  273,  274,   -1,   -1,  277,
  278,  279,   10,   -1,  282,   -1,  257,  258,  259,  260,
  261,  262,  263,  264,  265,  266,  267,  268,   -1,   -1,
   -1,   -1,  273,  274,   -1,   -1,  277,  278,  279,   -1,
   -1,  282,   40,   41,   42,   43,   44,   45,   -1,   47,
   -1,   -1,   -1,   -1,   10,   -1,   -1,   -1,  257,  258,
  259,  260,  261,  262,  263,  264,  265,  266,  267,  268,
   -1,   -1,   -1,   -1,  273,  274,   -1,   -1,  277,  278,
  279,   10,   -1,  282,   40,   41,   42,   43,   44,   45,
   -1,   47,   -1,   91,   -1,   93,   94,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   40,   41,   42,   43,   44,   45,   -1,   47,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   91,   10,   93,  257,  258,
  259,  260,  261,  262,  263,  264,  265,  266,  267,  268,
   -1,   -1,   -1,   -1,  273,  274,   -1,   -1,  277,  278,
  279,   -1,   91,  282,   93,   94,   40,   41,   42,   43,
   44,   45,   10,   47,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   40,   41,   42,   43,   44,   45,   10,   47,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   91,   -1,   93,
   94,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   40,   41,
   42,   43,   44,   45,   -1,   47,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   91,   -1,   93,   94,   -1,   -1,  257,
  258,  259,  260,  261,  262,  263,  264,  265,  266,  267,
  268,   -1,   -1,   -1,   -1,  273,  274,   -1,   -1,  277,
  278,  279,   -1,   -1,  282,   10,   -1,   -1,   -1,   91,
   -1,   93,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  257,  258,  259,  260,  261,  262,  263,  264,  265,
  266,  267,  268,   -1,   -1,   40,   -1,  273,  274,   -1,
   45,  277,  278,  279,   -1,   -1,  282,   -1,  257,  258,
  259,  260,  261,  262,  263,  264,  265,  266,  267,  268,
   -1,   -1,   -1,   -1,  273,  274,   -1,   -1,  277,  278,
  279,   -1,   -1,  282,   -1,   40,   -1,   42,   43,   -1,
   45,   -1,   47,   -1,   -1,   -1,   91,   10,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  257,  258,  259,  260,  261,  262,  263,
  264,  265,  266,  267,  268,   -1,   -1,   40,   41,  273,
  274,   44,   -1,  277,  278,  279,   91,   -1,  282,   94,
   -1,   -1,   10,   -1,   -1,   -1,   -1,   -1,   -1,  257,
  258,  259,  260,  261,  262,  263,  264,  265,  266,  267,
  268,   -1,   -1,   -1,   -1,  273,  274,   -1,   -1,  277,
  278,  279,   40,   41,  282,   43,   44,   45,   91,   -1,
   93,   -1,   -1,   -1,   -1,  257,  258,  259,  260,  261,
  262,  263,  264,  265,  266,  267,  268,   -1,   40,   -1,
   -1,  273,  274,   45,   -1,  277,  278,  279,   10,   -1,
  282,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   91,   -1,   93,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   40,   41,
   -1,   43,   44,   45,   -1,   -1,   -1,   -1,   -1,   91,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  256,  257,  258,  259,  260,  261,  262,  263,  264,
  265,  266,  267,  268,  269,   -1,  271,  272,  273,  274,
   -1,   -1,  277,  278,  279,   -1,   -1,  282,   -1,   91,
   -1,   93,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  257,  258,  259,  260,  261,  262,  263,  264,
  265,  266,  267,  268,   -1,   -1,   -1,   -1,  273,  274,
   -1,   -1,  277,  278,  279,  280,   -1,  282,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  257,  258,  259,  260,  261,  262,
  263,  264,  265,  266,  267,  268,   -1,   -1,   -1,   -1,
  273,  274,   -1,   -1,  277,  278,  279,   -1,   -1,  282,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,
  258,  259,  260,  261,  262,  263,  264,  265,  266,  267,
  268,   -1,   -1,   -1,   -1,  273,  274,   -1,   -1,  277,
  278,  279,   -1,   -1,  282,  257,  258,  259,  260,  261,
  262,  263,  264,  265,  266,  267,  268,   -1,   -1,   -1,
   -1,  273,  274,   -1,   -1,  277,  278,  279,   -1,   -1,
  282,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  258,  259,  260,  261,
  262,  263,  264,  265,  266,  267,  268,   -1,   -1,   -1,
   -1,  273,  274,   -1,   -1,  277,  278,  279,   -1,   -1,
  282,    6,    7,    8,    9,   10,   11,   12,   13,   14,
   -1,   -1,   -1,   18,   19,   20,   21,   22,   23,   24,
   -1,   26,   27,   -1,   -1,   -1,   -1,   32,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   41,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   55,   56,   57,   58,   59,   -1,   -1,   -1,   -1,   64,
   -1,   -1,   -1,   -1,   69,
};
#define YYFINAL 1
#ifndef YYDEBUG
#define YYDEBUG 0
#endif
#define YYMAXTOKEN 282
#if YYDEBUG
const char * const yyname[] = {
"end-of-file",0,0,0,0,0,0,0,0,0,"'\\n'",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,"'('","')'","'*'","'+'","','","'-'",0,"'/'",0,0,0,0,0,0,0,0,0,
0,0,0,0,"'='",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,"'['",0,
"']'","'^'",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,"NUM","DOT","VAR","EXP","LOG","SIN","COS","TAN","ASIN","ACOS",
"ATAN","ATAN2","DEF","QUOTE","SHOW","PRECISION","SINH","COSH","T","ANH","ASINH",
"ACOSH","ATANH","FAC","UMINUS","TANH",
};
const char * const yyrule[] = {
"$accept : list",
"list :",
"list : list cmd",
"list : list error '\\n'",
"cmd : expr '\\n'",
"cmd : DEF VAR QUOTE '\\n'",
"cmd : SHOW '\\n'",
"cmd : show '\\n'",
"cmd : PRECISION '=' NUM",
"cmd : '\\n'",
"show : SHOW",
"show : show VAR",
"expr : '(' expr ')'",
"expr : '[' expr ',' expr ']'",
"expr : expr '*' expr",
"expr : expr '+' expr",
"expr : expr '-' expr",
"expr : expr '/' expr",
"expr : expr '^' expr",
"expr : '-' expr",
"expr : EXP expr",
"expr : LOG expr",
"expr : SIN expr",
"expr : COS expr",
"expr : TAN expr",
"expr : ASIN expr",
"expr : ACOS expr",
"expr : ATAN expr",
"expr : ATAN2 expr expr",
"expr : SINH expr",
"expr : COSH expr",
"expr : TANH expr",
"expr : ASINH expr",
"expr : ACOSH expr",
"expr : ATANH expr",
"expr : expr FAC",
"expr : VAR '=' expr",
"expr : VAR",
"expr : DOT",
"expr : NUM",
};
#endif
#ifndef YYSTYPE
typedef int YYSTYPE;
#endif
#if YYDEBUG
#include <stdio.h>
#endif
#ifdef YYSTACKSIZE
#undef YYMAXDEPTH
#define YYMAXDEPTH YYSTACKSIZE
#else
#ifdef YYMAXDEPTH
#define YYSTACKSIZE YYMAXDEPTH
#else
#define YYSTACKSIZE 10000
#define YYMAXDEPTH 10000
#endif
#endif
#define YYINITSTACKSIZE 200
int yydebug;
int yynerrs;
int yyerrflag;
int yychar;
short *yyssp;
YYSTYPE *yyvsp;
YYSTYPE yyval;
YYSTYPE yylval;
short *yyss;
short *yysslim;
YYSTYPE *yyvs;
int yystacksize;
#line 164 "calc.y"
#include "calc.l.c"

#line 523 "y.tab.c"
/* allocate initial stack or double stack size, up to YYMAXDEPTH */
static int yygrowstack()
{
    int newsize, i;
    short *newss;
    YYSTYPE *newvs;

    if ((newsize = yystacksize) == 0)
        newsize = YYINITSTACKSIZE;
    else if (newsize >= YYMAXDEPTH)
        return -1;
    else if ((newsize *= 2) > YYMAXDEPTH)
        newsize = YYMAXDEPTH;
    i = yyssp - yyss;
    newss = yyss ? (short *)realloc(yyss, newsize * sizeof *newss) :
      (short *)malloc(newsize * sizeof *newss);
    if (newss == NULL)
        return -1;
    yyss = newss;
    yyssp = newss + i;
    newvs = yyvs ? (YYSTYPE *)realloc(yyvs, newsize * sizeof *newvs) :
      (YYSTYPE *)malloc(newsize * sizeof *newvs);
    if (newvs == NULL)
        return -1;
    yyvs = newvs;
    yyvsp = newvs + i;
    yystacksize = newsize;
    yysslim = yyss + newsize - 1;
    return 0;
}

#define YYABORT goto yyabort
#define YYREJECT goto yyabort
#define YYACCEPT goto yyaccept
#define YYERROR goto yyerrlab

#ifndef YYPARSE_PARAM
#if defined(__cplusplus) || __STDC__
#define YYPARSE_PARAM_ARG void
#define YYPARSE_PARAM_DECL
#else	/* ! ANSI-C/C++ */
#define YYPARSE_PARAM_ARG
#define YYPARSE_PARAM_DECL
#endif	/* ANSI-C/C++ */
#else	/* YYPARSE_PARAM */
#ifndef YYPARSE_PARAM_TYPE
#define YYPARSE_PARAM_TYPE void *
#endif
#if defined(__cplusplus) || __STDC__
#define YYPARSE_PARAM_ARG YYPARSE_PARAM_TYPE YYPARSE_PARAM
#define YYPARSE_PARAM_DECL
#else	/* ! ANSI-C/C++ */
#define YYPARSE_PARAM_ARG YYPARSE_PARAM
#define YYPARSE_PARAM_DECL YYPARSE_PARAM_TYPE YYPARSE_PARAM;
#endif	/* ANSI-C/C++ */
#endif	/* ! YYPARSE_PARAM */

int
yyparse (YYPARSE_PARAM_ARG)
    YYPARSE_PARAM_DECL
{
    register int yym, yyn, yystate;
#if YYDEBUG
    register const char *yys;

    if ((yys = getenv("YYDEBUG")))
    {
        yyn = *yys;
        if (yyn >= '0' && yyn <= '9')
            yydebug = yyn - '0';
    }
#endif

    yynerrs = 0;
    yyerrflag = 0;
    yychar = (-1);

    if (yyss == NULL && yygrowstack()) goto yyoverflow;
    yyssp = yyss;
    yyvsp = yyvs;
    *yyssp = yystate = 0;

yyloop:
    if ((yyn = yydefred[yystate])) goto yyreduce;
    if (yychar < 0)
    {
        if ((yychar = yylex()) < 0) yychar = 0;
#if YYDEBUG
        if (yydebug)
        {
            yys = 0;
            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
            if (!yys) yys = "illegal-symbol";
            printf("%sdebug: state %d, reading %d (%s)\n",
                    YYPREFIX, yystate, yychar, yys);
        }
#endif
    }
    if ((yyn = yysindex[yystate]) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
    {
#if YYDEBUG
        if (yydebug)
            printf("%sdebug: state %d, shifting to state %d\n",
                    YYPREFIX, yystate, yytable[yyn]);
#endif
        if (yyssp >= yysslim && yygrowstack())
        {
            goto yyoverflow;
        }
        *++yyssp = yystate = yytable[yyn];
        *++yyvsp = yylval;
        yychar = (-1);
        if (yyerrflag > 0)  --yyerrflag;
        goto yyloop;
    }
    if ((yyn = yyrindex[yystate]) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
    {
        yyn = yytable[yyn];
        goto yyreduce;
    }
    if (yyerrflag) goto yyinrecovery;
#if defined(lint) || defined(__GNUC__)
    goto yynewerror;
#endif
yynewerror:
    yyerror("syntax error");
#if defined(lint) || defined(__GNUC__)
    goto yyerrlab;
#endif
yyerrlab:
    ++yynerrs;
yyinrecovery:
    if (yyerrflag < 3)
    {
        yyerrflag = 3;
        for (;;)
        {
            if ((yyn = yysindex[*yyssp]) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
#if YYDEBUG
                if (yydebug)
                    printf("%sdebug: state %d, error recovery shifting\
 to state %d\n", YYPREFIX, *yyssp, yytable[yyn]);
#endif
                if (yyssp >= yysslim && yygrowstack())
                {
                    goto yyoverflow;
                }
                *++yyssp = yystate = yytable[yyn];
                *++yyvsp = yylval;
                goto yyloop;
            }
            else
            {
#if YYDEBUG
                if (yydebug)
                    printf("%sdebug: error recovery discarding state %d\n",
                            YYPREFIX, *yyssp);
#endif
                if (yyssp <= yyss) goto yyabort;
                --yyssp;
                --yyvsp;
            }
        }
    }
    else
    {
        if (yychar == 0) goto yyabort;
#if YYDEBUG
        if (yydebug)
        {
            yys = 0;
            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
            if (!yys) yys = "illegal-symbol";
            printf("%sdebug: state %d, error recovery discards token %d (%s)\n",
                    YYPREFIX, yystate, yychar, yys);
        }
#endif
        yychar = (-1);
        goto yyloop;
    }
yyreduce:
#if YYDEBUG
    if (yydebug)
        printf("%sdebug: state %d, reducing by rule %d (%s)\n",
                YYPREFIX, yystate, yyn, yyrule[yyn]);
#endif
    yym = yylen[yyn];
    yyval = yyvsp[1-yym];
    switch (yyn)
    {
case 1:
#line 26 "calc.y"
{
        for( vari=0 ; vari<100 ; vari++ ){
                variable[vari] = 0.0;
                def[vari] = 0;
                }
        pvar = vardex;
        qnext=qstore;
        deflag=0;
        degree=1.0;
        precision=3;
        }
break;
case 3:
#line 38 "calc.y"
{
                pvar = vardex;
                deflag = 0;
                yyerrok;
                }
break;
case 4:
#line 44 "calc.y"
{
                last = yyvsp[-1];
                printf("%.*g\n",precision,yyvsp[-1]);
                }
break;
case 5:
#line 48 "calc.y"
{
               strcpy(qnext,qbuf);
                def[*--pvar] = qnext;
                while(*qnext++);
                deflag = 0;
                }
break;
case 6:
#line 54 "calc.y"
{
                for( vari=0 ; vari<100 ; vari++ ) if( def[vari] ){
                        printf("%s\t%s\n",var_name[vari],def[vari]);
                }
                deflag = 0;
                }
break;
case 7:
#line 60 "calc.y"
{
                deflag = 0;
                }
break;
case 8:
#line 63 "calc.y"
{
                precision = yyvsp[0] ;
                }
break;
case 11:
#line 69 "calc.y"
{
                if( def[*--pvar] == 0 ){
                        printf("%s not defined\n", var_name[*pvar]);
                }
                else {
                        printf("%s\t%s\n",var_name[*pvar],def[*pvar]);
                }
        }
break;
case 12:
#line 78 "calc.y"
{
                yyval = yyvsp[-1];
                }
break;
case 13:
#line 81 "calc.y"
{
                yyval = exp( gamma( yyvsp[-3] + 1)-gamma(yyvsp[-3]- yyvsp[-1] +1) -gamma(yyvsp[-1]+1));
                }
break;
case 14:
#line 84 "calc.y"
{
                yyval = yyvsp[-2] * yyvsp[0];
                }
break;
case 15:
#line 87 "calc.y"
{
                yyval = yyvsp[-2] + yyvsp[0];
                }
break;
case 16:
#line 90 "calc.y"
{
                yyval = yyvsp[-2] - yyvsp[0];
                }
break;
case 17:
#line 93 "calc.y"
{
                yyval = yyvsp[-2] / yyvsp[0];
                }
break;
case 18:
#line 96 "calc.y"
{
               yyval = pow(yyvsp[-2],yyvsp[0]);
                }
break;
case 19:
#line 99 "calc.y"
{
                yyval = -yyvsp[0];
                }
break;
case 20:
#line 102 "calc.y"
{
                yyval = exp(yyvsp[0]);
                }
break;
case 21:
#line 105 "calc.y"
{
                yyval = log(yyvsp[0]);
                }
break;
case 22:
#line 108 "calc.y"
{
                yyval = sin(degree*yyvsp[0]);
                }
break;
case 23:
#line 111 "calc.y"
{
                yyval = cos(degree*yyvsp[0]);
                }
break;
case 24:
#line 114 "calc.y"
{
                yyval = tan(degree*yyvsp[0]);
                }
break;
case 25:
#line 117 "calc.y"
{
                yyval = asin(yyvsp[0])/degree;
                }
break;
case 26:
#line 120 "calc.y"
{
                yyval = acos(yyvsp[0])/degree;
                }
break;
case 27:
#line 123 "calc.y"
{
                yyval = atan(yyvsp[0])/degree;
                }
break;
case 28:
#line 126 "calc.y"
{
                yyval = atan2(yyvsp[-1],yyvsp[0])/degree;
                }
break;
case 29:
#line 129 "calc.y"
{
                yyval = sinh(yyvsp[0]);
                }
break;
case 30:
#line 132 "calc.y"
{
                yyval = cosh(yyvsp[0]);
                }
break;
case 31:
#line 135 "calc.y"
{
                yyval = tanh(yyvsp[0]);
                }
break;
case 32:
#line 138 "calc.y"
{
                yyval = asinh(yyvsp[0]);
                }
break;
case 33:
#line 141 "calc.y"
{
                yyval = acosh(yyvsp[0]);
                }
break;
case 34:
#line 144 "calc.y"
{
                yyval = atanh(yyvsp[0]);
                }
break;
case 35:
#line 147 "calc.y"
{
                yyval = exp(gamma(yyvsp[-1]+1))*signgam;
                }
break;
case 36:
#line 150 "calc.y"
{
                variable[*--pvar] = yyvsp[0];
                yyval = yyvsp[0];
                }
break;
case 37:
#line 154 "calc.y"
{
                yyval = variable[*--pvar];
                }
break;
case 38:
#line 157 "calc.y"
{
                yyval = last;
                }
break;
case 39:
#line 160 "calc.y"
{
                yyval = yyvsp[0];
                }
break;
#line 957 "y.tab.c"
    }
    yyssp -= yym;
    yystate = *yyssp;
    yyvsp -= yym;
    yym = yylhs[yyn];
    if (yystate == 0 && yym == 0)
    {
#if YYDEBUG
        if (yydebug)
            printf("%sdebug: after reduction, shifting from state 0 to\
 state %d\n", YYPREFIX, YYFINAL);
#endif
        yystate = YYFINAL;
        *++yyssp = YYFINAL;
        *++yyvsp = yyval;
        if (yychar < 0)
        {
            if ((yychar = yylex()) < 0) yychar = 0;
#if YYDEBUG
            if (yydebug)
            {
                yys = 0;
                if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
                if (!yys) yys = "illegal-symbol";
                printf("%sdebug: state %d, reading %d (%s)\n",
                        YYPREFIX, YYFINAL, yychar, yys);
            }
#endif
        }
        if (yychar == 0) goto yyaccept;
        goto yyloop;
    }
    if ((yyn = yygindex[yym]) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn];
    else
        yystate = yydgoto[yym];
#if YYDEBUG
    if (yydebug)
        printf("%sdebug: after reduction, shifting from state %d \
to state %d\n", YYPREFIX, *yyssp, yystate);
#endif
    if (yyssp >= yysslim && yygrowstack())
    {
        goto yyoverflow;
    }
    *++yyssp = yystate;
    *++yyvsp = yyval;
    goto yyloop;
yyoverflow:
    yyerror("yacc stack overflow");
yyabort:
    return (1);
yyaccept:
    return (0);
}
