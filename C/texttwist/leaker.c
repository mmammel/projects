#include <stdlib.h>

int
main(){
int i;
void *ptr = malloc(4);

for(i = 0; i < 10; i++) ptr = malloc(256);

return 0;
}
