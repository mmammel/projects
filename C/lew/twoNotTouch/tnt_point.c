#include <stdio.h>
#include <stdlib.h>

#include "./blocks.h" /* contains ten strings of 10 bnames */

/* Use a 10 bit integer representation ( with two 1-bits ) for each row,
with the built-in property of no adjacent bits. There are 36 of these */

unsigned short twobits[36]; /* These are the possible row values */

unsigned short onebits[10] = {

    0x001, 0x002, 0x004, 0x008, 0x010, 0x020, 0x040, 0x080, 0x100, 0x200

};

unsigned short brd[10];

/* col_cnt[ row ][ col ] and bcnt[ row ][ bdex ]
   stored in a block of 100 chars and referenced via char* variables.
*/

char col_store[100];
char b_store[100];

/*
Both remain all 0 for row == 0. try(row) uses the values passed,
and updates the row+1 values before calling try(row+1)
*/

unsigned short blim[10];

unsigned short coldex1[36];
unsigned short coldex2[36];

char *bnames = "ABCDEFGHIJ";

int main()
{
        int i, j, n;

        int rowdex;
        void
        try
                ();
 
        /* set up board layout */

        for (i = n = 0; i < 8; i++)
                for (j = i + 2; j < 10; j++)
                {

                        /* These are the possible bit pairs in a row with no two adjacent */

                        coldex1[n] = i;
                        coldex2[n] = j;

                        printf("%03x %d %d\n", twobits[n] = onebits[i] | onebits[j], i, j);
                        ++n;
                }

        /* set up blim[10], containing the largest row value for each block */

        for (i = 0; i < 10; i = i + 1)
                for (j = 0; j < 10; j = j + 1)
                        blim[blocks[i][j] - 'A'] = i;

        printf("blim[] = ");
        for (i = 0; i < 10; i++)
                printf("%d", blim[i]);
        printf("\n");

        /*
for( i=0 ; i<10 ; i=i+1 ) printf("%d ",blim[i]);
printf("\n");
exit(0);
*/

        for (rowdex = 0; rowdex < 36; ++rowdex)

                try
                        (0, rowdex, &col_store[0], &b_store[0]);
}

void
try
        (row, rowdex, col_cnt, blk_cnt)

            int row;
int rowdex;
char *col_cnt, *blk_cnt;

/* row is 0 thru 9, rowdex is one of the 36 possible row values */

/* col_cnt abd blk_cnt are counts ( 0, 1, or 2 ) of placements, so far, of "stars" in
        columns and blocks.
*/

{

       void
        printSolution();
        int i; /* utility variable */

        /* nxt_col and nxt_blk point to the next ten column and block indexed counts
   which are updates of the passed values pointed to by col_cnt and blk_cnt */

        char *nxt_col = col_cnt + 10;
        char *nxt_blk = blk_cnt + 10;

        printf("try ( %d, %d )\n", row, rowdex);

        /* These are the TWO ( per column ) tests */
        printf("colcnt[ %d ] = %d\n", coldex1[rowdex], col_cnt[coldex1[rowdex]]);
        printf("colcnt[ %d ] = %d\n", coldex2[rowdex], col_cnt[coldex2[rowdex]]);

        if (col_cnt[coldex1[rowdex]] == 2)
                return;
        if (col_cnt[coldex2[rowdex]] == 2)
                return;

        brd[row] = twobits[rowdex];

        /* These are the NOT TOUCH tests */

        printf("chk brd[%d] = %04x\n", row, brd[row]);

        if (row != 0)
        {
                if ((brd[row] & brd[row - 1]) != 0)
                        return;
                if ((brd[row] & (brd[row - 1] >> 1)) != 0)
                        return;
                if ((brd[row - 1] & (brd[row] >> 1)) != 0)
                        return;
        }

        /* These are the TWO per block tests */

        printf("blocks = %c %c\n", blocks[row][coldex1[rowdex]], blocks[row][coldex2[rowdex]]);

        if (blocks[row][coldex1[rowdex]] == blocks[row][coldex2[rowdex]])
        {

                /* both bits in same block */

                if (blk_cnt[blocks[row][coldex1[rowdex]] - 'A'] != 0)
                        return;
        }
        else
        {
                printf("else bcnt [ %d ]\n", blocks[row][coldex1[rowdex]] - 'A');

                if (blk_cnt[blocks[row][coldex1[rowdex]] - 'A'] == 2)
                        return;
                printf("else bcnt [ %d ]\n", blocks[row][coldex2[rowdex]] - 'A');
                if (blk_cnt[blocks[row][coldex2[rowdex]] - 'A'] == 2)
                        return;
        }

        /* found a valid row value , pending limit check */
        for (i = 0; i < 10; i++)
        {
                nxt_col[i] = col_cnt[i];
                nxt_blk[i] = blk_cnt[i];
        }

        printf("Increment bcnt\n");
        printf(" rowdex = %d\n", rowdex);
        printf("nxt_blk[ %c ]\n", blocks[row][coldex1[rowdex]]);
        ++nxt_blk[blocks[row][coldex1[rowdex]] - 'A'];
        printf("nxt_blk[ %c ]\n", blocks[row][coldex2[rowdex]]);
        ++nxt_blk[blocks[row][coldex2[rowdex]] - 'A'];

        /* Do limit check. If we are on the last row of a block, bcnt must be 2 */

        for (i = 0; i < 10; i++)
                if (blim[i] == row && nxt_blk[i] != 2)
                        return;

        printf("Increment colcnt\n");
        ++nxt_col[coldex1[rowdex]];
        ++nxt_col[coldex2[rowdex]];

        printf(" %03x", brd[row]);
        printf("\n");

        if (row == 9)
        {
                printf("SOLUTION: ");
                printSolution();
                for (i = 0; i < 10; i++)
                        printf("%03x ", brd[i]);
                printf("\n");
                return;
        }

        {
                int nxt_rowdex;

                for (nxt_rowdex = 0; nxt_rowdex < 36; ++nxt_rowdex)

                        try
                                (row + 1, nxt_rowdex, nxt_col, nxt_blk);
        }
}

void 
printSolution()
{
        printf("\n+---+---+---+---+---+---+---+---+---+---+\n");
        int needCap = 0;
        for (int i = 0; i < 10; i++)
        {
                printf("|");

                for (int j = 0; j < 10; j++)
                {
                        printf((brd[i] & (1<<j)) != 0 ? " * " : "   ");
                        if (j < 9)
                        {
                                printf(blocks[i][j] != blocks[i][j + 1] ? "|" : " ");
                        }
                }
                printf("|\n");
                needCap = 1;
                for (int j = 0; j < 10; j++)
                {
                        if( j > 0) {
                                if( blocks[i][j] != blocks[i][j-1] )
                                        needCap = 1;
                        }
                        if (i < 9)
                        {
                                if( blocks[i][j] != blocks[i + 1][j] ) {
                                  printf( "+---" );
                                  needCap = 1;
                                } else if( needCap == 1 ) {
                                  printf( "+   " );
                                  needCap = 0;
                                } else {
                                  printf("    ");
                                }
                        } else {
                                printf("+---");
                        }
                }

                printf("+\n");
        }
}
