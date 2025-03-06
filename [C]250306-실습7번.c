#include <stdio.h>

int main(void)
{
	// 1
	char gender;
	int age;
	float height;
	printf("성별은? (남자라면 M 여자라면 F) ");
	scanf_s("%c", &gender, sizeof(char));
	getchar();
	printf("나이는? ");
	scanf_s("%d", &age);
	getchar();
	printf("키는? ");
	scanf_s("%f", &height);
	getchar();
	printf("\n================================\n");
	printf("성별: %c\n", gender);
	printf("나이: %3d\n", age);
	printf("키: %.1f\n\n", height);
}
