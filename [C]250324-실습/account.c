#include <stdio.h>
#include "account.h"

// 계좌 생성
void createAccount(Account *acc, int number, double initial_balance) {
    acc->account_number = number;
    acc->balance = initial_balance;
}

// 입금 기능
void deposit(Account *acc, double amount) {
    if (amount > 0) {
        acc->balance += amount;
    }
}

// 출금 기능
void withdraw(Account *acc, double amount) {
    if (amount > 0 && acc->balance >= amount) {
        acc->balance -= amount;
    }
}

// 계좌 정보 출력
void printAccountInfo(const Account *acc) {
    printf("계좌번호: %d, 금액: %.2f\n", acc->account_number, acc->balance);
}
