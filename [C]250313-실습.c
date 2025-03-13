#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define ROWS 3
#define COLS 3

void doublePointer3();

void doublePointer4();

void sum(int **matrix, int m, int n);

void max(int **matrix, int m, int n);

void min(int **matrix, int m, int n);

void square(int **matrix, int m, int n);


int main(void) {
    // doublePointer3();
    // doublePointer4();
}

void doublePointer3() {
    int N, M;
    printf("행과 열의 수를 입력하세요?");
    scanf("%d %d", &N, &M);
    printf("배열의 초기값는 자동으로 입력됩니다.\n");
    int arr[N][M];
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            arr[i][j] = i * 10 + j;
        }
    }
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            printf("%d ", arr[i][j]);
        }
        printf("\n");
    }

    int delete_row;
    int *result[N];
    for (int i = 0; i < N; i++) {
        result[i] = arr[i];
    }

    printf("삭제하려는 행의 인덱스 번호를 입력하세요?");
    scanf("%d", &delete_row);

    for (int i = delete_row; i < N - 1; i++) {
        result[i] = result[i + 1];
    }
    result[N - 1] = NULL;

    for (int i = 0; i < N - 1; i++) {
        for (int j = 0; j < M; j++) {
            printf("%d ", result[i][j]);
        }
        printf("\n");
    }
}

void doublePointer4() {
    int select;
    void (*f[])(int **, int, int) = {sum, max, min, square};
    int array[ROWS][COLS] = {
        {0, 1, 2},
        {3, 4, 5},
        {6, 7, 8}
    };
    int *dp[ROWS];
    for (int i = 0; i < ROWS; i++) {
        dp[i] = array[i];
    }

    printf("연산 방법을 선택하기");
    printf("(0: 합, 1: 최대값, 2: 최소값, 3: 제곱): ");
    scanf("%d", &select);

    if (select < 0 || select > 3) {
        return;
    }

    f[select](dp, ROWS, COLS);
}

void sum(int **matrix, int m, int n) {
    int total = 0;

    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            total += matrix[i][j];
        }
    }

    printf("배열의 합은 %d\n", total);
}

void max(int **matrix, int m, int n) {
    int max = INT_MIN;

    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if (matrix[i][j] > max) {
                max = matrix[i][j];
            }
        }
    }
    printf("배열의 최대값은 %d\n", max);
}

void min(int **matrix, int m, int n) {
    int min = INT_MAX;
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if (matrix[i][j] < min) {
                min = matrix[i][j];
            }
        }
    }
    printf("배열의 최소값은 %d\n", min);
}

void square(int **matrix, int m, int n) {
    int new_matrix[m][n];
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            new_matrix[i][j] = matrix[i][j] * matrix[i][j];
        }
    }

    printf("배열의 제곱값은\n");
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            printf("%d ", new_matrix[i][j]);
        }
        printf("\n");
    }
}
