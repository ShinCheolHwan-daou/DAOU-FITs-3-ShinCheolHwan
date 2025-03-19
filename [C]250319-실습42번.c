#include <stdio.h>

struct Flags {
    unsigned int isVisible: 1;
    unsigned int isActive: 1;
    unsigned int isDeleted: 1;
    unsigned int type: 2;
    
};

int main(){
    struct Flags flag = {1, 0, 0, 2};

    printf("isVisible: %u\n",  flag.isVisible);
    printf("isActive: %u\n",  flag.isActive);
    printf("isDeleted: %u\n",  flag.isDeleted);
    printf("type: %u\n",  flag.type);

    return 0;
}