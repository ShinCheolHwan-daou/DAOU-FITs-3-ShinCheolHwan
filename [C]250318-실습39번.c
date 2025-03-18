#include <stdio.h>
#include <stdlib.h>


int main(void) {
    int *p1 = malloc(sizeof(int));
    int *p2 = calloc(1, sizeof(int));

    printf("%d, %d", *p1, *p2);

    free(p1);
    free(p2);
}
