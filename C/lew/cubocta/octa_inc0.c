#include "struct_defs.c"

#define NUMV 6

struct vertex octa[] =
{
{1,0,0},
{-1,0,0},
{0,1,0},
{0,-1,0},
{0,0,1},
{0,0,-1},
};

struct edgeorder edges [NUMV]=
{
{2,4,3,5},
{2,5,3,4},
{0,5,1,4},
{0,4,1,5},
{0,2,1,3},
{0,3,1,2},
};

struct edge edgelist[2*NUMV] ;

struct edgeorder newedges [ 2*NUMV ];

