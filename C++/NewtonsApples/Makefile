# Makefile  for line models

LIBDIR = /usr/lib
INCDIR = /usr/include
STLDIR = /usr/include

#CFLAGS = -g -I$(INCDIR) -I$(STLDIR) -L$(LIBDIR) -Wno-deprecated 
#
CFLAGS = -O3 -I$(INCDIR) -I$(STLDIR) -L$(LIBDIR) -Wno-deprecated 
LIBS   = -lm 
OBJS  = control.o NewtonApple_hull3D.o
HEADS = NewtonApple_hull3D.h

CC = gcc


# Building rules for Makefile

NewtAppWrap3D: $(OBJS) $(HEADS)
	g++ $(CFLAGS)  -o $@ $(OBJS)  $(LIBS)

%.o: %.cpp
	g++ $(CFLAGS) -c  $< -o $@

clean:
	-rm *.o NewtAppWrap3D

control.o:	control.cpp $(HEADS)

NewtonApple_hull3D.o:	NewtonApple_hull3D.cpp $(HEADS)




