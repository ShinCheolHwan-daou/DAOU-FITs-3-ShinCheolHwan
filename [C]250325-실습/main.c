#define CRT_SECURE_NO_WARNINGS
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "transaction.h"

int main(void) {
    // 1. initialize
    Node *transactions = NULL;
    initialize_transactions(&transactions);
    if (transactions == NULL) {
        printf("저장된 거래 내역이 없습니다. 새로 시작합니다.");
    } else {
        printf("저장된 거래 내역을 불러옵니다.\n");
        get_transactions(transactions);
    }

    // 2. choice
    while (true) {
        printf("\n");
        printf("========주식 거래 시스템========\n");
        printf("1. 거래 추가\n");
        printf("2. 모든 거래 조회\n");
        printf("3. 고객 거래 검색\n");
        printf("4. 거래 수정\n");
        printf("5. 거래 삭제\n");
        printf("6. 종료\n");
        printf("선택: ");

        fflush(stdin);
        int choice;
        scanf("%d", &choice);

        switch (choice) {
            case 1:
                // 1. 거래 추가
                add_transaction(&transactions);
                break;
            case 2:
                // 2. 모든 거래 조회
                get_transactions(transactions);
                break;
            case 3:
                // 3. 고객 거래 검색
                search_transactions(transactions);
                break;
            case 4:
                // 4. 거래 수정
                update_transaction(transactions);
                break;
            case 5:
                // 5. 거래 삭제
                delete_transaction(&transactions);
                break;
            case 6:
                // 6. 종료
                clear_transactions(transactions);
                exit(0);
            default:
                printf("잘못된 선택입니다.\n");
                break;
        }
    }
}
