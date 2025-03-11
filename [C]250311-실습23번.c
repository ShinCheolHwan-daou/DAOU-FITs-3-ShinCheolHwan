#include <stdio.h>

int main(void) {
    int i, *ip = &i;
    int sum = 0, *sump = &sum;

    for (*ip = 1; *ip <= 100; (*ip)++) {
        *sump += *ip;
    }
    printf("포인터 변수를 사용한 1~100까지의 합은 : %d\n", sum);
}
