struct vertex{
int coord[3];
};

struct edge {
/* The vertices defining the edge */
int v1; int v2;
};


/* edgeorder is the sequential rotation of the edges
   around each vertex. This defines the new edges to
   be drawn, and also allows the construction of the
   new edgeorder array, so the next cubocta construction
   can be iterated.
*/


struct edgeorder { int index [4]; };

