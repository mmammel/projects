#!/bin/bash

cp octa_inc0.c octa_inc.c;
count=0; while [ $count -lt $1 ]; do gcc -o octarun octa_run.c; ./octarun > octa_inc.c; let count=count+1; done

./octarun;
