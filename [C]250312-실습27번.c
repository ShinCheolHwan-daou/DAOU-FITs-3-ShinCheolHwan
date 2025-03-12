#include <stdio.h>

int max(int, int);

int min(int, int);

int main(void) {
    int (*fpmm)(int, int);
    int num1, num2, flag;
    printf("두 개의 숫자를 입력 : ");
    scanf("%d %d", &num1, &num2);
    printf("원하는 값을 입력 (1.큰값, 2:작은값): ");
    scanf("%d", &flag);

    if (flag == 1) {
        fpmm = max;
        printf("두 수중 큰 값은 : %d\n", fpmm(num1, num2));
    } else if (flag == 2) {
        fpmm = min;
        printf("두 수중 작은 값은 : %d\n", fpmm(num1, num2));
    } else {
        printf("잘못된 숫자 입력입니다.\n");
    }
}

int max(int a, int b) {
    return a > b ? a : b;
}

int min(int a, int b) {
    return a < b ? a : b;
}
