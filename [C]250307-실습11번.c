#include <stdio.h>

int main(void) {
    // 1
    int a, b;
    printf("가감승제를 원하는 두 수를 입력하세요 : ");
    scanf("%d %d", &a, &b);
    printf("%d + %d = %d\n", a, b, a + b);
    printf("%d - %d = %d\n", a, b, a - b);
    printf("%d * %d = %d\n", a, b, a * b);
    printf("%d / %d = %lf\n", a, b, (double) a / b);


    // 2
    int n;
    printf("구구단 몇단? ");
    scanf("%d", &n);
    for (int i = 1; i < 10; i++) {
        printf("%d * %d = %d\n", n, i, n * i);
    }

    // 3
    char input[20];
    scanf("%s", input);
    char *ptr = input;
    while (*ptr != '\0') {
        if (*ptr >= 'a' && *ptr <= 'z') {
            printf("%c", *ptr - 32);
        } else if (*input >= 'A' && *input <= 'Z') {
            printf("%c", *ptr + 32);
        } else {
            printf("%c", *ptr);
        }
        ptr++;
    }
}
