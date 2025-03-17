#include <stdio.h>
#include <time.h>

int main() {
    time_t now;
    struct tm t;

    time(&now);
    printf("1970년 1월 1일부터 현재까지의 초는 %d초입니다.\n", now);
    t = *localtime(&now);
    puts(asctime(&t));
    printf("현재의 연도 : %d\n", t.tm_year + 1900);
    printf("현재의 월 : %d\n", t.tm_mon + 1);
    printf("현재의 일 : %d\n", t.tm_mday);
    printf("현재의 요일 : %d\n", t.tm_wday);
}
