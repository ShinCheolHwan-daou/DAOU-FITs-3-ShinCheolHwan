#include <stdio.h>
#include <errno.h>
#include <string.h>

int main() {
    FILE *f = fopen("D:\\TestFile.txt", "wb+");
    char str[128];
    if (f) {
        fputs("0123456789\r\n0123456789", f);
        fgets(str, sizeof(str), f);
        printf("read: %s", str);

        fclose(f);
    } else {
        printf("Error: %d %s\n", errno, strerror(errno));
    }
}