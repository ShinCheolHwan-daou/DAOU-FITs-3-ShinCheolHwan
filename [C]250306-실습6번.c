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
	
	// 2
	char name[10];
	printf("이름은? ");
	gets_s(name, sizeof(name));
	// scanf_s("%s", name, sizeof(name));
	printf("입력한 이름: %s\n\n", name);

	// 3
	int nums[3];
	scanf_s("%d %d %d", &nums[0], &nums[1], &nums[2]);
	printf("%d %d %d", nums[0], nums[1], nums[2]);
}
