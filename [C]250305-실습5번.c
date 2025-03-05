#include <stdio.h>

int main(void)
{
	// 변수 선언
	int std_no, kor, world, math;
	double ave;

	std_no = 2013901;
	kor = 89;
	world = 100;
	math = 95;

	ave = (kor + world + math) / 3.0;

	printf("%-6s: %d \n", "학번", std_no);
	printf("==========================\n");
	printf("%-6s: %4d(점) \n", "국어", kor);
	printf("%-6s: %4d(점) \n", "세계사", world);
	printf("%-6s: %4d(점) \n", "수학", math);
	printf("==========================");
	printf("%-6s: %7.2lf \n", "평균", ave);

}
