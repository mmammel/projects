/*
    rotate.c - Routines that manipulate the Video DAC on VGA Adapters
*/

#include <string.h>
#include <time.h>

  /* see Fractint.c for a description of the "include"  hierarchy */
#include "port.h"
#include "prototyp.h"
#include "helpdefs.h"

/* routines in this module      */

static void pauserotate(void);
static void set_palette(BYTE start[3], BYTE finish[3]);
static void set_palette2(BYTE start[3], BYTE finish[3]);
static void set_palette3(BYTE start[3], BYTE middle[3], BYTE finish[3]);

static int paused;                      /* rotate-is-paused flag */
static BYTE Red[3]    = {63, 0, 0};     /* for shifted-Fkeys */
static BYTE Green[3]  = { 0,63, 0};
static BYTE Blue[3]   = { 0, 0,63};
static BYTE Black[3]  = { 0, 0, 0};
static BYTE White[3]  = {63,63,63};
static BYTE Yellow[3] = {63,63, 0};
static BYTE Brown[3]  = {31,31, 0};

char mapmask[13] = {"*.map"};

void rotate(int direction)      /* rotate-the-palette routine */
{
int  kbdchar, more, last, next;
int fkey, step, fstep, istep, jstep, oldstep;
int incr, fromred=0, fromblue=0, fromgreen=0, tored=0, toblue=0, togreen=0;
int i, changecolor, changedirection;
int oldhelpmode;
int rotate_max,rotate_size;

static int fsteps[] = {2,4,8,12,16,24,32,40,54,100}; /* (for Fkeys) */

   if (gotrealdac == 0                  /* ??? no DAC to rotate! */
     || colors < 16) {                  /* strange things happen in 2x modes */
      buzzer(2);
      return;
      }

   oldhelpmode = helpmode;              /* save the old help mode       */
   helpmode = HELPCYCLING;              /* new help mode                */

   paused = 0;                          /* not paused                   */
   fkey = 0;                            /* no random coloring           */
   oldstep = step = 1;                  /* single-step                  */
   fstep = 1;
   changecolor = -1;                    /* no color (rgb) to change     */
   changedirection = 0;                 /* no color direction to change */
   incr = 999;                          /* ready to randomize           */
   srand((unsigned)time(NULL));         /* randomize things             */

   if (direction == 0) {                /* firing up in paused mode?    */
      pauserotate();                    /* then force a pause           */
      direction = 1;                    /* and set a rotate direction   */
      }

   rotate_max = (rotate_hi < colors) ? rotate_hi : colors-1;
   rotate_size = rotate_max - rotate_lo + 1;
   last = rotate_max;                   /* last box that was filled     */
   next = rotate_lo;                    /* next box to be filled        */
   if (direction < 0) {
      last = rotate_lo;
      next = rotate_max;
      }

   more = 1;
   while (more) {
      if (dotmode == 11) {
         if (!paused)
            pauserotate();
         }
      else while(!keypressed()) { /* rotate until key hit, at least once so step=oldstep ok */
         if (fkey > 0) {                /* randomizing is on */
            for (istep = 0; istep < step; istep++) {
               jstep = next + (istep * direction);
               while (jstep < rotate_lo)  jstep += rotate_size;
               while (jstep > rotate_max) jstep -= rotate_size;
               if (++incr > fstep) {    /* time to randomize */
                  incr = 1;
                  fstep = ((fsteps[fkey-1]* (rand15() >> 8)) >> 6) + 1;
                  fromred   = dacbox[last][0];
                  fromgreen = dacbox[last][1];
                  fromblue  = dacbox[last][2];
                  tored     = rand15() >> 9;
                  togreen   = rand15() >> 9;
                  toblue    = rand15() >> 9;
                  }
               dacbox[jstep][0] = (BYTE)(fromred   + (((tored    - fromred  )*incr)/fstep));
               dacbox[jstep][1] = (BYTE)(fromgreen + (((togreen - fromgreen)*incr)/fstep));
               dacbox[jstep][2] = (BYTE)(fromblue  + (((toblue  - fromblue )*incr)/fstep));
               }
            }
         if (step >= rotate_size) step = oldstep;
         spindac(direction, step);
         }
      if (step >= rotate_size) step = oldstep;
      kbdchar = getakey();
      if (paused && (kbdchar != ' '
                 && kbdchar != 'c'
                 && kbdchar != HOME
                 && kbdchar != 'C' ))
         paused = 0;                    /* clear paused condition       */
      switch (kbdchar) {
         case '+':                      /* '+' means rotate forward     */
         case RIGHT_ARROW:              /* RightArrow = rotate fwd      */
            fkey = 0;
            direction = 1;
            last = rotate_max;
            next = rotate_lo;
            incr = 999;
            break;
         case '-':                      /* '-' means rotate backward    */
         case LEFT_ARROW:               /* LeftArrow = rotate bkwd      */
            fkey = 0;
            direction = -1;
            last = rotate_lo;
            next = rotate_max;
            incr = 999;
            break;
         case UP_ARROW:                 /* UpArrow means speed up       */
            daclearn = 1;
            if (++daccount >= colors) --daccount;
            break;
         case DOWN_ARROW:               /* DownArrow means slow down    */
            daclearn = 1;
            if (daccount > 1) daccount--;
            break;
         case '1':
         case '2':
         case '3':
         case '4':
         case '5':
         case '6':
         case '7':
         case '8':
         case '9':
            step = kbdchar - '0';   /* change step-size */
            if (step > rotate_size) step = rotate_size;
            break;
         case F1:                       /* F1 - F10:                    */
         case F2:                       /* select a shading factor      */
         case F3:
         case F4:
         case F5:
         case F6:
         case F7:
         case F8:
         case F9:
         case F10:
#ifndef XFRACT
            fkey = kbdchar-1058;
#else
            switch (kbdchar) {
               case F1:
                  fkey = 1;break;
               case F2:
                  fkey = 2;break;
               case F3:
                  fkey = 3;break;
               case F4:
                  fkey = 4;break;
               case F5:
                  fkey = 5;break;
               case F6:
                  fkey = 6;break;
               case F7:
                  fkey = 7;break;
               case F8:
                  fkey = 8;break;
               case F9:
                  fkey = 9;break;
               case F10:
                  fkey = 10;break;
            }
#endif
            if (reallyega) fkey = (fkey+1)>>1; /* limit on EGA */
            fstep = 1;
            incr = 999;
            break;
         case ENTER:                    /* enter key: randomize all colors */
         case ENTER_2:                  /* also the Numeric-Keypad Enter */
            fkey = rand15()/3277 + 1;
            if (reallyega)              /* limit on EGAs */
               fkey = (fkey+1)>>1;
            fstep = 1;
            incr = 999;
            oldstep = step;
            step = rotate_size;
            break;
         case 'r':                      /* color changes */
            if (changecolor    == -1) changecolor = 0;
         case 'g':                      /* color changes */
            if (changecolor    == -1) changecolor = 1;
         case 'b':                      /* color changes */
            if (changecolor    == -1) changecolor = 2;
            if (changedirection == 0) changedirection = -1;
         case 'R':                      /* color changes */
            if (changecolor    == -1) changecolor = 0;
         case 'G':                      /* color changes */
            if (changecolor    == -1) changecolor = 1;
         case 'B':                      /* color changes */
            if (dotmode == 11) break;
            if (changecolor    == -1) changecolor = 2;
            if (changedirection == 0) changedirection = 1;
            if (reallyega) break;       /* no sense on real EGAs */
            for (i = 1; i < 256; i++) {
               dacbox[i][changecolor] = (BYTE)(dacbox[i][changecolor] + changedirection);
               if (dacbox[i][changecolor] == 64)
               dacbox[i][changecolor] = 63;
               if (dacbox[i][changecolor] == 255)
                  dacbox[i][changecolor] = 0;
               }
            changecolor    = -1;        /* clear flags for next time */
            changedirection = 0;
            paused          = 0;        /* clear any pause */
         case ' ':                      /* use the spacebar as a "pause" toggle */
         case 'c':                      /* for completeness' sake, the 'c' too */
         case 'C':
            pauserotate();              /* pause */
            break;
         case '>':                      /* single-step */
         case '.':
         case '<':
         case ',':
            if (kbdchar == '>' || kbdchar == '.') {
               direction = 1;
               last = rotate_max;
               next = rotate_lo;
               incr = 999;
               }
            else {
               direction = -1;
               last = rotate_lo;
               next = rotate_max;
               incr = 999;
               }
            fkey = 0;
            spindac(direction,1);
            if (! paused)
               pauserotate();           /* pause */
            break;
         case 'd':                      /* load colors from "default.map" */
         case 'D':
            if (ValidateLuts("default") != 0)
               break;
            fkey = 0;                   /* disable random generation */
            pauserotate();              /* update palette and pause */
            break;
         case 'a':                      /* load colors from "altern.map" */
         case 'A':
            if (ValidateLuts("altern") != 0)
               break;
            fkey = 0;                   /* disable random generation */
            pauserotate();              /* update palette and pause */
            break;
         case 'l':                      /* load colors from a specified map */
#ifndef XFRACT /* L is used for RIGHT_ARROW in Unix keyboard mapping */
         case 'L':
#endif
            load_palette();
            fkey = 0;                   /* disable random generation */
            pauserotate();              /* update palette and pause */
            break;
         case 's':                      /* save the palette */
         case 'S':
            save_palette();
            fkey = 0;                   /* disable random generation */
            pauserotate();              /* update palette and pause */
            break;
         case ESC:                      /* escape */
            more = 0;                   /* time to bail out */
            break;
         case HOME:                     /* restore palette */
            memcpy(dacbox,olddacbox,256*3);
            pauserotate();              /* pause */
            break;
         default:                       /* maybe a new palette */
            if (reallyega) break;       /* no sense on real EGAs */
            fkey = 0;                   /* disable random generation */
            if (kbdchar == 1084) set_palette(Black, White);
            if (kbdchar == SF2) set_palette(Red, Yellow);
            if (kbdchar == SF3) set_palette(Blue, Green);
            if (kbdchar == SF4) set_palette(Black, Yellow);
            if (kbdchar == SF5) set_palette(Black, Red);
            if (kbdchar == SF6) set_palette(Black, Blue);
            if (kbdchar == SF7) set_palette(Black, Green);
            if (kbdchar == SF8) set_palette(Blue, Yellow);
            if (kbdchar == SF9) set_palette(Red, Green);
            if (kbdchar == 1093) set_palette(Green, White);
            if (kbdchar == 1094) set_palette2(Black, White);
            if (kbdchar == 1095) set_palette2(Red, Yellow);
            if (kbdchar == 1096) set_palette2(Blue, Green);
            if (kbdchar == 1097) set_palette2(Black, Yellow);
            if (kbdchar == 1098) set_palette2(Black, Red);
            if (kbdchar == 1099) set_palette2(Black, Blue);
            if (kbdchar == 1100) set_palette2(Black, Green);
            if (kbdchar == 1101) set_palette2(Blue, Yellow);
            if (kbdchar == 1102) set_palette2(Red, Green);
            if (kbdchar == 1103) set_palette2(Green, White);
            if (kbdchar == 1104) set_palette3(Blue, Green, Red);
            if (kbdchar == 1105) set_palette3(Blue, Yellow, Red);
            if (kbdchar == 1106) set_palette3(Red, White, Blue);
            if (kbdchar == 1107) set_palette3(Red, Yellow, White);
            if (kbdchar == 1108) set_palette3(Black, Brown, Yellow);
            if (kbdchar == 1109) set_palette3(Blue, Brown, Green);
            if (kbdchar == 1110) set_palette3(Blue, Green, Green);
            if (kbdchar == 1111) set_palette3(Blue, Green, White);
            if (kbdchar == 1112) set_palette3(Green, Green, White);
            if (kbdchar == 1113) set_palette3(Red, Blue, White);
            pauserotate();  /* update palette and pause */
            break;
         }
      }

   helpmode = oldhelpmode;              /* return to previous help mode */
}

static void pauserotate()               /* pause-the-rotate routine */
{
int olddaccount;                        /* saved dac-count value goes here */
BYTE olddac0,olddac1,olddac2;

   if (paused)                          /* if already paused , just clear */
      paused = 0;
   else {                               /* else set border, wait for a key */
      olddaccount = daccount;
      olddac0 = dacbox[0][0];
      olddac1 = dacbox[0][1];
      olddac2 = dacbox[0][2];
      daccount = 256;
      dacbox[0][0] = 48;
      dacbox[0][1] = 48;
      dacbox[0][2] = 48;
      spindac(0,1);                     /* show white border */
      if (dotmode == 11)
      {
         static FCODE o_msg[] = {" Paused in \"color cycling\" mode "};
         char msg[sizeof(o_msg)];
         far_strcpy(msg,o_msg);
         dvid_status(100,msg);
      }
#ifndef XFRACT
      while (!keypressed());          /* wait for any key */
#else
      waitkeypressed(0);                /* wait for any key */
#endif
      if (dotmode == 11)
         dvid_status(0,"");
      dacbox[0][0] = olddac0;
      dacbox[0][1] = olddac1;
      dacbox[0][2] = olddac2;
      spindac(0,1);                     /* show black border */
      daccount = olddaccount;
      paused = 1;
      }
}

static void set_palette(BYTE start[3], BYTE finish[3])
{
   int i, j;
   dacbox[0][0] = dacbox[0][1] = dacbox[0][2] = 0;
   for(i=1;i<=255;i++)                  /* fill the palette     */
      for (j = 0; j < 3; j++)
#ifdef __SVR4
         dacbox[i][j] = (BYTE)((int)(i*start[j] + (256-i)*finish[j])/255);
#else
         dacbox[i][j] = (BYTE)((i*start[j] + (256-i)*finish[j])/255);
#endif
}

static void set_palette2(BYTE start[3], BYTE finish[3])
{
   int i, j;
   dacbox[0][0] = dacbox[0][1] = dacbox[0][2] = 0;
   for(i=1;i<=128;i++)
      for (j = 0; j < 3; j++) {
#ifdef __SVR4
         dacbox[i][j]     = (BYTE)((int)(i*finish[j] + (128-i)*start[j] )/128);
         dacbox[i+127][j] = (BYTE)((int)(i*start[j]  + (128-i)*finish[j])/128);
#else
         dacbox[i][j]     = (BYTE)((i*finish[j] + (128-i)*start[j] )/128);
         dacbox[i+127][j] = (BYTE)((i*start[j]  + (128-i)*finish[j])/128);
#endif
      }
}

static void set_palette3(BYTE start[3], BYTE middle[3], BYTE finish[3])
{
   int i, j;
   dacbox[0][0] = dacbox[0][1] = dacbox[0][2] = 0;
   for(i=1;i<=85;i++)
      for (j = 0; j < 3; j++) {
#ifdef __SVR4
         dacbox[i][j]     = (BYTE)((int)(i*middle[j] + (86-i)*start[j] )/85);
         dacbox[i+85][j]  = (BYTE)((int)(i*finish[j] + (86-i)*middle[j])/85);
         dacbox[i+170][j] = (BYTE)((int)(i*start[j]  + (86-i)*finish[j])/85);
#else
         dacbox[i][j]     = (BYTE)((i*middle[j] + (86-i)*start[j] )/85);
         dacbox[i+85][j]  = (BYTE)((i*finish[j] + (86-i)*middle[j])/85);
         dacbox[i+170][j] = (BYTE)((i*start[j]  + (86-i)*finish[j])/85);
#endif
      }
}


void save_palette()
{
   char palname[FILE_MAX_PATH];
   static FCODE o_msg[] = {"Name of map file to write"};
   char msg[sizeof(o_msg)];
   FILE *dacfile;
   int i,oldhelpmode;
   far_strcpy(msg,o_msg);
   strcpy(palname,MAP_name);
   oldhelpmode = helpmode;
   stackscreen();
   temp1[0] = 0;
   helpmode = HELPCOLORMAP;
   i = field_prompt(0,msg,NULL,temp1,60,NULL);
   unstackscreen();
   if (i != -1 && temp1[0]) {
      if (strchr(temp1,'.') == NULL)
         strcat(temp1,".map");
      merge_pathnames(palname,temp1,2);
      dacfile = fopen(palname,"w");
      if (dacfile == NULL)
         buzzer(2);
      else {
#ifndef XFRACT
         for (i = 0; i < colors; i++)
#else
         for (i = 0; i < 256; i++)
#endif
            fprintf(dacfile, "%3d %3d %3d\n",
                    dacbox[i][0] << 2,
                    dacbox[i][1] << 2,
                    dacbox[i][2] << 2);
         memcpy(olddacbox,dacbox,256*3);
         colorstate = 2;
         strcpy(colorfile,temp1);
         }
      fclose(dacfile);
      }
   helpmode = oldhelpmode;
}


void load_palette(void)
{
   int i,oldhelpmode;
   char filename[80];
   oldhelpmode = helpmode;
   strcpy(filename,MAP_name);
   stackscreen();
   helpmode = HELPCOLORMAP;
   i = getafilename("Select a MAP File",mapmask,filename);
   unstackscreen();
   if (i >= 0)
   {
      if (ValidateLuts(filename) == 0)
         memcpy(olddacbox,dacbox,256*3);
      merge_pathnames(MAP_name,filename,0);
   }
   helpmode = oldhelpmode;
}


