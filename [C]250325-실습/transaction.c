#define CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <oci.h>

#include "transaction.h"

void check_error(OCIError *errhp) {
    text errbuf[2048];
    sb4 errcode = 0;

    OCIErrorGet(errhp, 1, NULL, &errcode, errbuf, sizeof(errbuf), OCI_HTYPE_ERROR);
    printf("Oracle Error: %s\n", errbuf);
}

int cur_id = 0;

int get_next_id() {
    return cur_id + 1;
}

void get_db(Node **transactions) {
    // 1. 환경 핸들 초기화
    OCIEnv *envhp;
    OCIError *errhp;
    OCISvcCtx *svchp;
    OCISession *usrhp;
    OCIServer *srvhp;
    OCIStmt *stmthp;
    sword status;

    char *username = "C##C_PRACTICE";
    char *password = "1234";
    char *dbname = "localhost:1521/xe";

    OCIEnvCreate(&envhp, OCI_DEFAULT, NULL, NULL, NULL, NULL, 0, NULL);
    OCIHandleAlloc(envhp, (void **) &errhp, OCI_HTYPE_ERROR, 0, NULL);
    OCIHandleAlloc(envhp, (void **) &srvhp, OCI_HTYPE_SERVER, 0, NULL);
    OCIServerAttach(srvhp, errhp, (OraText *) dbname, strlen(dbname), OCI_DEFAULT);
    OCIHandleAlloc(envhp, (void **) &svchp, OCI_HTYPE_SVCCTX, 0, NULL);
    OCIAttrSet(svchp, OCI_HTYPE_SVCCTX, srvhp, 0, OCI_ATTR_SERVER, errhp);
    OCIHandleAlloc(envhp, (void **) &usrhp, OCI_HTYPE_SESSION, 0, NULL);
    OCIAttrSet(usrhp, OCI_HTYPE_SESSION, username, strlen(username),
               OCI_ATTR_USERNAME, errhp);
    OCIAttrSet(usrhp, OCI_HTYPE_SESSION, password, strlen(password),
               OCI_ATTR_PASSWORD, errhp);
    OCISessionBegin(svchp, errhp, usrhp, OCI_CRED_RDBMS, OCI_DEFAULT);
    OCIAttrSet(svchp, OCI_HTYPE_SVCCTX, usrhp, 0, OCI_ATTR_SESSION, errhp);

    // 2. SELECT
    TransactionData new_transaction_data;
    char *select_sql = "SELECT ID, CUSTOMER_NAME, STOCK_NAME, TYPE, AMOUNT, PRICE FROM TRANSACTION";
    OCIHandleAlloc(envhp, (void **) &stmthp, OCI_HTYPE_STMT, 0, NULL);
    OCIStmtPrepare(stmthp, errhp, (text *) select_sql, strlen(select_sql), OCI_NTV_SYNTAX, OCI_DEFAULT);
    OCIStmtExecute(svchp, stmthp, errhp, 0, 0, NULL, NULL, OCI_DEFAULT);

    OCIDefine *def1 = NULL, *def2 = NULL, *def3 = NULL, *def4 = NULL, *def5 = NULL, *def6 = NULL;
    OCIDefineByPos(stmthp, &def1, errhp, 1, &new_transaction_data.id, sizeof(new_transaction_data.id),
                   SQLT_INT, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def2, errhp, 2, new_transaction_data.customer_name,
                   sizeof(new_transaction_data.customer_name), SQLT_STR, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def3, errhp, 3, new_transaction_data.stock_name, sizeof(new_transaction_data.stock_name),
                   SQLT_STR, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def4, errhp, 4, &new_transaction_data.type, sizeof(new_transaction_data.type),
                   SQLT_INT, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def5, errhp, 5, &new_transaction_data.amount, sizeof(new_transaction_data.amount),
                   SQLT_INT, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def6, errhp, 6, &new_transaction_data.price, sizeof(new_transaction_data.price),
                   SQLT_FLT, NULL, NULL, NULL, OCI_DEFAULT);
    status = OCIStmtFetch2(stmthp, errhp, 1, OCI_DEFAULT, 0, OCI_DEFAULT);
    Node *cur_transaction = NULL;
    while (status == OCI_SUCCESS || status == OCI_SUCCESS_WITH_INFO) {
        cur_id = cur_id < new_transaction_data.id ? new_transaction_data.id : cur_id;
        Node *new_transaction = malloc(sizeof(Node));
        if (new_transaction == NULL) {
            printf("memory allocation fail.\n");
            return;
        }
        new_transaction->next = NULL;
        memcpy(&new_transaction->data, &new_transaction_data, sizeof(TransactionData));

        if (cur_transaction == NULL) {
            *transactions = new_transaction;
        } else {
            cur_transaction->next = new_transaction;
        }
        cur_transaction = new_transaction;
        status = OCIStmtFetch2(stmthp, errhp, 1, OCI_DEFAULT, 0, OCI_DEFAULT);
    }

    // 3. 연결 종료
    OCIHandleFree(stmthp, OCI_HTYPE_STMT);
    OCILogoff(svchp, errhp);
    OCIHandleFree(usrhp, OCI_HTYPE_SESSION);
    OCIHandleFree(svchp, OCI_HTYPE_SVCCTX);
    OCIHandleFree(srvhp, OCI_HTYPE_SERVER);
    OCIHandleFree(errhp, OCI_HTYPE_ERROR);
    OCIHandleFree(envhp, OCI_HTYPE_ENV);
}

void initialize_transactions(Node **transactions) {
    get_db(transactions);
}

void add_transaction(Node **transactions) {
    Node *new_transaction = malloc(sizeof(Node));
    if (new_transaction == NULL) {
        printf("memory allocation fail.\n");
        return;
    }
    new_transaction->data.id = get_next_id();
    printf("고객 이름: ");
    scanf("%s", new_transaction->data.customer_name);
    printf("주식 종목명: ");
    scanf("%s", new_transaction->data.stock_name);
    printf("거래 유형 (0: 매수, 1: 매도): ");
    scanf("%d", &new_transaction->data.type);
    printf("거래 수량: ");
    scanf("%d", &new_transaction->data.amount);
    printf("거래 가격: ");
    scanf("%lf", &new_transaction->data.price);
    new_transaction->next = NULL;

    if (*transactions == NULL) {
        *transactions = new_transaction;
    } else {
        Node *cur_transaction = *transactions;
        while (cur_transaction->next != NULL) {
            cur_transaction = cur_transaction->next;
        }
        cur_transaction->next = new_transaction;
    }
    cur_id++;
}

void get_transactions(Node *transactions) {
    bool find = false;
    Node *cur_transaction = transactions;
    while (cur_transaction != NULL) {
        find = true;
        printf("ID: %d, 고객: %s, 종목: %s, 유형: %s, 수량: %d, 가격: %.2lf원\n",
               cur_transaction->data.id,
               cur_transaction->data.customer_name,
               cur_transaction->data.stock_name,
               cur_transaction->data.type == BUY ? "매수" : "매도",
               cur_transaction->data.amount,
               cur_transaction->data.price
        );
        cur_transaction = cur_transaction->next;
    }
    if (!find) {
        printf("해당하는 거래가 없습니다.\n");
    }
}

void search_transactions(Node *transactions) {
    bool find = false;
    char search_name[CUSTOMER_NAME_LENGTH];
    printf("검색할 고객 이름:");
    scanf("%s", search_name);

    Node *cur_transaction = transactions;
    while (cur_transaction != NULL) {
        if (strcmp(cur_transaction->data.customer_name, search_name) == 0) {
            find = true;
            printf("ID: %d, 고객: %s, 종목: %s, 유형: %s, 수량: %d, 가격: %.2lf원\n",
                   cur_transaction->data.id,
                   cur_transaction->data.customer_name,
                   cur_transaction->data.stock_name,
                   cur_transaction->data.type == BUY ? "매수" : "매도",
                   cur_transaction->data.amount,
                   cur_transaction->data.price
            );
        }
        cur_transaction = cur_transaction->next;
    }
    if (!find) {
        printf("해당하는 거래가 없습니다.\n");
    }
}

void update_transaction(Node *transactions) {
    bool find = false;
    int update_id, update_amount;
    double update_price;
    printf("수정할 거래 ID 입력:");
    scanf("%d", &update_id);
    printf("새 수량: ");
    scanf("%d", &update_amount);
    printf("새 가격: ");
    scanf("%lf", &update_price);

    Node *cur_transaction = transactions;
    while (cur_transaction != NULL) {
        if (cur_transaction->data.id == update_id) {
            find = true;
            break;
        }
        cur_transaction = cur_transaction->next;
    }
    if (!find) {
        printf("해당하는 거래가 없습니다.\n");
        return;
    }

    cur_transaction->data.amount = update_amount;
    cur_transaction->data.price = update_price;
    printf("거래 수정 완료!\n");
}

void delete_transaction(Node **transactions) {
    Node *temp = *transactions;
    Node *prev = NULL;

    int delete_id;
    printf("삭제할 거래 ID 입력:");
    scanf("%d", &delete_id);

    if (temp != NULL && temp->data.id == delete_id) {
        *transactions = temp->next;
        free(temp);
        printf("거래 삭제 완료!\n");
        return;
    }

    while (temp != NULL && temp->data.id != delete_id) {
        prev = temp;
        temp = temp->next;
    }

    if (temp == NULL) {
        printf("해당하는 거래가 없습니다.\n");
        return;
    }

    prev->next = temp->next;
    free(temp);
    printf("거래 삭제 완료!\n");
}

void save_db(Node *transactions) {
    // 1. 환경 핸들 초기화
    OCIEnv *envhp;
    OCIError *errhp;
    OCISvcCtx *svchp;
    OCISession *usrhp;
    OCIServer *srvhp;
    OCIStmt *stmthp;
    sword status;

    char *username = "C##C_PRACTICE";
    char *password = "1234";
    char *dbname = "localhost:1521/xe";

    OCIEnvCreate(&envhp, OCI_DEFAULT, NULL, NULL, NULL, NULL, 0, NULL);
    OCIHandleAlloc(envhp, (void **) &errhp, OCI_HTYPE_ERROR, 0, NULL);
    OCIHandleAlloc(envhp, (void **) &srvhp, OCI_HTYPE_SERVER, 0, NULL);
    OCIServerAttach(srvhp, errhp, (OraText *) dbname, strlen(dbname), OCI_DEFAULT);
    OCIHandleAlloc(envhp, (void **) &svchp, OCI_HTYPE_SVCCTX, 0, NULL);
    OCIAttrSet(svchp, OCI_HTYPE_SVCCTX, srvhp, 0, OCI_ATTR_SERVER, errhp);
    OCIHandleAlloc(envhp, (void **) &usrhp, OCI_HTYPE_SESSION, 0, NULL);
    OCIAttrSet(usrhp, OCI_HTYPE_SESSION, username, strlen(username),
               OCI_ATTR_USERNAME, errhp);
    OCIAttrSet(usrhp, OCI_HTYPE_SESSION, password, strlen(password),
               OCI_ATTR_PASSWORD, errhp);
    OCISessionBegin(svchp, errhp, usrhp, OCI_CRED_RDBMS, OCI_DEFAULT);
    OCIAttrSet(svchp, OCI_HTYPE_SVCCTX, usrhp, 0, OCI_ATTR_SESSION, errhp);

    // 2. TRUNCATE
    char *truncate_sql = "TRUNCATE TABLE TRANSACTION";
    OCIHandleAlloc(envhp, (void **) &stmthp, OCI_HTYPE_STMT, 0, NULL);
    OCIStmtPrepare(stmthp, errhp, (text *) truncate_sql, strlen(truncate_sql), OCI_NTV_SYNTAX, OCI_DEFAULT);

    status = OCIStmtExecute(svchp, stmthp, errhp, 1, 0, NULL, NULL, OCI_DEFAULT);
    if (status != OCI_SUCCESS) {
        check_error(errhp);
        return;
    }

    // 3. INSERT
    Node *cur_transaction = transactions;
    while (cur_transaction != NULL) {
        char *insert_sql =
                "INSERT INTO TRANSACTION (ID, CUSTOMER_NAME, STOCK_NAME, TYPE, AMOUNT, PRICE) VALUES (:1, :2, :3, :4, :5, :6)";
        OCIHandleAlloc(envhp, (void **) &stmthp, OCI_HTYPE_STMT, 0, NULL);
        OCIStmtPrepare(stmthp, errhp, (text *) insert_sql, strlen(insert_sql), OCI_NTV_SYNTAX, OCI_DEFAULT);
        OCIBind *bnd1 = NULL, *bnd2 = NULL, *bnd3 = NULL, *bnd4 = NULL;
        OCIBindByPos(stmthp, &bnd1, errhp, 1, &cur_transaction->data.id, sizeof(cur_transaction->data.id),
                     SQLT_INT,NULL, NULL, NULL, 0, NULL,OCI_DEFAULT);
        OCIBindByPos(stmthp, &bnd2, errhp, 2, cur_transaction->data.customer_name,
                     sizeof(cur_transaction->data.customer_name),
                     SQLT_STR, NULL, NULL, NULL, 0, NULL,OCI_DEFAULT);
        OCIBindByPos(stmthp, &bnd3, errhp, 3, cur_transaction->data.stock_name,
                     sizeof(cur_transaction->data.stock_name),
                     SQLT_STR,NULL, NULL, NULL, 0, NULL,OCI_DEFAULT);
        OCIBindByPos(stmthp, &bnd2, errhp, 4, &cur_transaction->data.type, sizeof(cur_transaction->data.type),
                     SQLT_INT,NULL, NULL, NULL, 0,NULL,OCI_DEFAULT);
        OCIBindByPos(stmthp, &bnd2, errhp, 5, &cur_transaction->data.amount, sizeof(cur_transaction->data.amount),
                     SQLT_INT,NULL, NULL, NULL, 0,NULL,OCI_DEFAULT);
        OCIBindByPos(stmthp, &bnd2, errhp, 6, &cur_transaction->data.price, sizeof(cur_transaction->data.price),
                     SQLT_FLT,NULL, NULL, NULL, 0,NULL,OCI_DEFAULT);
        if (OCIStmtExecute(svchp, stmthp, errhp, 1, 0, NULL, NULL, OCI_COMMIT_ON_SUCCESS) != OCI_SUCCESS) {
            check_error(errhp);
            return;
        }
        cur_transaction = cur_transaction->next;
    }

    // 4. 연결 종료
    OCIHandleFree(stmthp, OCI_HTYPE_STMT);
    OCILogoff(svchp, errhp);
    OCIHandleFree(usrhp, OCI_HTYPE_SESSION);
    OCIHandleFree(svchp, OCI_HTYPE_SVCCTX);
    OCIHandleFree(srvhp, OCI_HTYPE_SERVER);
    OCIHandleFree(errhp, OCI_HTYPE_ERROR);
    OCIHandleFree(envhp, OCI_HTYPE_ENV);
}

void save_csv(Node *transactions) {
    FILE *f = fopen("stocks.csv", "w");
    if (f == NULL) {
        printf("can't open csv file.\n");
        return;
    }
    Node *cur_transaction = transactions;
    fprintf(f, "\"ID\", \"CUSTOMER_NAME\", \"STOCK_NAME\", \"TYPE\", \"AMOUNT\"\n");

    while (cur_transaction != NULL) {
        fprintf(f, "%d, \"%s\", \"%s\", %d, %d, %lf\n",
                cur_transaction->data.id,
                cur_transaction->data.customer_name,
                cur_transaction->data.stock_name,
                cur_transaction->data.type,
                cur_transaction->data.amount,
                cur_transaction->data.price
        );

        cur_transaction = cur_transaction->next;
    }

    fclose(f);
}

void save_binary(Node *transactions) {
    FILE *f = fopen("stocks.dat", "wb");
    if (f == NULL) {
        printf("can't open binary file.\n");
        return;
    }
    Node *cur_transaction = transactions;

    while (cur_transaction != NULL) {
        fwrite(&cur_transaction->data, sizeof(TransactionData), 1, f);
        cur_transaction = cur_transaction->next;
    }

    fclose(f);
}

void free_transactions(Node *transactions) {
    Node *cur_transaction = transactions;
    while (cur_transaction != NULL) {
        Node *next_transaction = cur_transaction->next;
        free(cur_transaction);
        cur_transaction = next_transaction;
    }
}

void clear_transactions(Node *transactions) {
    save_db(transactions);
    save_csv(transactions);
    save_binary(transactions);
    free_transactions(transactions);
}
