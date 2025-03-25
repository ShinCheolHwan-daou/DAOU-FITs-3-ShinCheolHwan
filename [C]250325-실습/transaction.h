//
// Created by daou_tlscjfghks on 25. 3. 24.
//

#ifndef TRANSACTION_H
#define TRANSACTION_H
#define CUSTOMER_NAME_LENGTH 128
#define STOCK_NAME_LENGTH 128

typedef enum {
    BUY,
    SELL
} TRANSACTION_TYPE;

typedef struct {
    int id;
    char customer_name[CUSTOMER_NAME_LENGTH];
    char stock_name[STOCK_NAME_LENGTH];
    TRANSACTION_TYPE type;
    int amount;
    double price;
} TransactionData;

// Linked List
typedef struct Node {
    TransactionData data;
    struct Node *next;
} Node;

void initialize_transactions(Node **transactions);

void add_transaction(Node **transactions);

void get_transactions(Node *transactions);

void search_transactions(Node *transactions);

void update_transaction(Node *transactions);

void delete_transaction(Node **transactions);

void clear_transactions(Node *transactions);

#endif //TRANSACTION_H
