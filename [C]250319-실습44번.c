# define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <errno.h>
#include <string.h>

int main() {
    FILE *f = fopen("D:\\NewTestFile.txt", "wb");
    if (f) {
        fclose(f);
    } else {
        printf("Error:%d, %s", errno, strerror(errno));
    }
}
