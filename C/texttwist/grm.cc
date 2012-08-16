#include <string.h>
#include <stdio.h>
#include <stdlib.h>

void anagram(const char *letters, char *buffer, size_t bufferLength) {
     int i;
     if(letters[0] == '\0') {
         printf("%s\n", buffer);
     } else {
         for(i = 0; i < bufferLength && letters[0] != buffer[i]; i++) {
             if(buffer[i] == '\0') {
                 buffer[i] = letters[0];
                 anagram(letters + 1, buffer, bufferLength);
                 buffer[i] = '\0';
             }
         }
     }
}

int main(int argc, char *argv[0]) {
     char *buffer;
     size_t length;

     if(argc < 2) {
         fprintf(stderr, "Usage: %s word\n", argv[0]);
         return EXIT_FAILURE;
      }

     length = strlen(argv[1]);

     buffer = calloc(length + 1);
     if(buffer == NULL) {
         fprintf(stderr, "Can't allocate\n");
         return EXIT_FAILURE;
     }

     anagram(argv[1], buffer, length);

     return EXIT_SUCCESS;
}
