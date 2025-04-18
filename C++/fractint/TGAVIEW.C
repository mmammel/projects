
/* Routine to decode Targa 16 bit RGB file
   */

/* 16 bit .tga files were generated for continuous potential "potfile"s
   from version 9.? thru version 14.  Replaced by double row gif type
   file (.pot) in version 15.  Delete this code after a few more revs.
*/


  /* see Fractint.c for a description of the "include"  hierarchy */
#include "port.h"
#include "prototyp.h"
#include "targa_lc.h"

static FILE *fptarga = NULL;            /* FILE pointer           */

/* Main entry decoder */
tgaview()
{
   int i;
   int cs;
   unsigned int width;
   struct fractal_info info;

   if((fptarga = t16_open(readname, (int *)&width, (int *)&height, &cs, (U8 *)&info))==NULL)
      return(-1);

   rowcount = 0;
   for (i=0; i<(int)height; ++i)
   {
       t16_getline(fptarga, width, (U16 *)boxx);
       if ((*outln)((void *)boxx,width))
       {
          fclose(fptarga);
          fptarga = NULL;
          return(-1);
       }
       if (keypressed())
       {
          fclose(fptarga);
          fptarga = NULL;
          return(-1);
       }
   }
   fclose(fptarga);
   fptarga = NULL;
   return(0);
}

/* Outline function for 16 bit data with 8 bit fudge */
outlin16(BYTE *buffer,int linelen)
{
    int i;
    U16 *buf;
    buf = (U16 *)buffer;
    for(i=0;i<linelen;i++)
       putcolor(i,rowcount,buf[i]>>8);
    rowcount++;
    return(0);
}
