#include <iostream>
//#include <hash_set>
#include <set>
#include <vector>
#include <fstream>
#include <stdlib.h>
#include <math.h>

#include <memory.h>
//#include <malloc.h>
#include <ctype.h>
#include <string.h>
#include <string>
#include <strings.h>
#include <sys/types.h>


#include "NewtonApple_hull3D.h"

/* copyright 2016 Dr David Sinclair
   david@newtonapples.net 
   all rights reserved.
 

   this code is released under GPL3,
   a copy of the license can be found at 
   http://www.gnu.org/licenses/gpl-3.0.html

   you can purchase a un-restricted license from
   http://newtonapples.net

   where algorithm details are explained.

   If you do choose to purchase a license I will do my best 
   to post you a free bag of Newton Apple Chocolates, 
   the cleverest chocolates on the Internet.



 */


#include <sys/time.h>

using namespace std;

int main(int argc, char *argv[])
{



   if( argc == 1 ){
      cerr << "NewtonAppleWrapper Delaunay triangulation demo" << endl;
      cerr << "usage: />   shullpro3D <points_file> <triangles_file> " << endl;

      float goat =  ( 2147483648.0-1) /100.0;

      std::vector<R3> pts, pts2;
      R3 pt;
      srandom(1);
      pts.clear();

      //      for(int v=0; v<100000; v++){
      //
      for(int v=0; v<100; v++){
	// for(int v=0; v<1000000; v++){
	//for(int v=0; v<2000000; v++){
	
	pt.id = v;
	pt.r = (((float) rand() / RAND_MAX ) * 500)-250; // pts.txt
	pt.c = (((float) rand() / RAND_MAX ) * 500)-250;
	//pt.r = (((float) rand() / RAND_MAX ) * 50000)-20000; // pts.txt
	//pt.c = (((float) rand() / RAND_MAX ) * 50000)-20000;
       

	//pt.z = (int)(((float) rand() / RAND_MAX ) * 500);  // for 3D points in box.	
	pt.z = pt.r*pt.r + pt.c*pt.c;   // for beautiful delaunay triangulations.
	
	pts.push_back(pt);
      }
      
      std::vector<Tri> tris;
      
      std::vector<int> outx;
      int nx = de_duplicateR3( pts, outx, pts2);
      pts.clear();
      
      write_R3(pts2, "pts.mat");
      cerr << pts2.size() << " randomly generated points int R2/R3 written to pts.mat" << endl;
      
      
      struct timeval tv1, tv2; // slight swizzle as pt set is now sorted.
      gettimeofday(&tv1, NULL); 
      
      
      //int ts = NewtonApple_Delaunay( pts2, tris);
      int ts = NewtonApple_hull_3D( pts2, tris);
      
      
      gettimeofday(&tv2, NULL);
      float tx =  (tv2.tv_sec + tv2.tv_usec / 1000000.0) - ( tv1.tv_sec + tv1.tv_usec / 1000000.0);
      pts2.clear();
      
      cerr <<  tx << " seconds for triangulation" << endl;
      
      
      write_Tris(tris, "triangles.mat");
      cerr << tris.size() << " triangles written to triangles.mat" << endl;
      
      
      exit(0);
   }
   else if( argc > 1){

     cerr << "reading points from " << argv[1] << endl;
     std::vector<R3> pts4, pts3;
     
     int nump = read_R3(pts4, argv[1]);
     cerr << nump << " points read" << endl;

     // check for duplicates.
     std::vector<int> dupes;
     int numd = de_duplicateR3( pts4, dupes, pts3);
     cerr << "duplicates filtered  " << numd << endl;
     cerr << "writing point ro pts.mat " << endl;

     write_R3(pts3, "pts.mat");


     struct timeval tv1, tv2;
     gettimeofday(&tv1, NULL);
     
     std::vector<Tri> tris;
     // 
    //int ts = NewtonApple_Delaunay( pts3, tris);
         int ts = NewtonApple_hull_3D( pts3, tris);
     
     
     gettimeofday(&tv2, NULL);
     float tx =  (tv2.tv_sec + tv2.tv_usec / 1000000.0) - ( tv1.tv_sec + tv1.tv_usec / 1000000.0);
     
     cerr <<  tx << " seconds for triangulation" << endl;
     
     
     if( argc == 2 ){
       write_Tris(tris, "triangles.mat");
       cerr << tris.size() << " triangles written to triangles.mat" << endl;
     }
     else{
         write_Tris(tris, argv[2]);
	 cerr << tris.size() << " triangles written to " << argv[2] << endl;
     }
   }




   exit(0);
}



#include <sys/time.h>
//static inline 
double getpropertime()
{
    struct timeval tv;
    gettimeofday(&tv, NULL);
    return tv.tv_sec + tv.tv_usec / 1000000.0;
}

