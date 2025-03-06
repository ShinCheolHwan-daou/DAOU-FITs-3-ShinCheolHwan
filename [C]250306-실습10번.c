#include <stdio.h>
#define _USE_MATH_DEFINES
#include <math.h>

void p1(void);
void p2(void);

int main(void)
{
	p1();
	p2();
}

void p1(void)
{
	printf("========================\n");
	printf("1. 원의 둘레 구하기\n");
	printf("2. 원의 넓이 구하기\n");
	printf("3. 구의 부피 구하기\n");
	printf("4. 그만두기\n");
	printf("========================\n");

	int menu, r;
	while (1)
	{
		printf("원하는 메뉴는 ? ");
		scanf_s("%d", &menu);

		switch (menu)
		{
		case 1:
			printf(">> 반지름은 ? ");
			scanf_s("%d", &r);
			printf(">> 반지름이 %d인 원의 둘레는 %.2f\n", r, 2 * M_PI * r);
			break;
		case 2:
			printf(">> 반지름은 ? ");
			scanf_s("%d", &r);
			printf(">> 반지름이 %d인 원의 넓이는 %.2f\n", r, r * r * M_PI);
			break;
		case 3:
			printf(">> 반지름은 ? ");
			scanf_s("%d", &r);
			printf(">> 반지름이 %d인 구의 부피는 %.2f\n", r, 4.0 / 3 * M_PI * r * r * r);
			break;
		default:
			return;
		}
	}
}

void p2(void)
{
	int n, sum = 0;
	printf("정수 n을 입력: ");
	scanf_s("%d", &n);

	for (int i = 1; i <= n; i++)
	{
		if (i % 2 == 0)
		{
			sum += i;
		}
	}
	printf("정수 1에서 %d 이하 짝수들의 합은 %d입니다.\n", n, sum);
}
