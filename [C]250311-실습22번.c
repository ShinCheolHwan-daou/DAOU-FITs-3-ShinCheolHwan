#include <stdio.h>

int main(void) {
    double d = 100.0;
    double *dpoint = &d;
    printf("변수 d의 값 : %f\n", d);
    printf("변수 d의 주소 : %p\n", &d);
    printf("dpoint의 값 : %p\n", dpoint);
    printf("*dpoint의 값 : %f\n", *dpoint);
    printf("변수 d의 크기 : %d\n", sizeof(d));
    printf("변수 d 주소의 크기 : %d\n", sizeof(&d));
    printf("dpoint의 크기 : %d\n", sizeof(dpoint));
    printf("*dpoint의 크기 : %d\n", sizeof(*dpoint));
}
