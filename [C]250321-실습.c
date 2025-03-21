#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <oci.h>


void check_error(OCIError *errhp) {
    text errbuf[2048];
    sb4 errcode = 0;

    OCIErrorGet(errhp, 1, NULL, &errcode, errbuf, sizeof(errbuf), OCI_HTYPE_ERROR);
    char utf8_buf[512];

    printf("Oracle Error: %s\n", errbuf);
}

int main() {
    OCIEnv *envhp;
    OCIError *errhp;
    OCISvcCtx *svchp;
    OCISession *usrhp;
    OCIServer *srvhp;
    OCIStmt *stmthp;
    OCIDefine *def1 = NULL, *def2 = NULL, *def3 = NULL, *def4 = NULL;
    sword status;

    // DB 로그인 정보
    char *username = "C##C_PRACTICE";
    char *password = "1234";
    char *dbname = "localhost:1521/xe";

    // 환경 핸들 초기화
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
    printf("✅ Oracle DB 연결 성공!\n");

    // 0. TEST_CUSTOMER TABLE 초기화
    char *truncate_sql = "TRUNCATE TABLE TEST_CUSTOMER";
    OCIHandleAlloc(envhp, (void **) &stmthp, OCI_HTYPE_STMT, 0, NULL);
    OCIStmtPrepare(stmthp, errhp, (text *) truncate_sql, strlen(truncate_sql), OCI_NTV_SYNTAX, OCI_DEFAULT);

    status = OCIStmtExecute(svchp, stmthp, errhp, 1, 0, NULL, NULL, OCI_DEFAULT);
    if (status != OCI_SUCCESS) {
        check_error(errhp);
        exit(1);
    }
    printf("✅ 테이블 TRUNCATE 성공\n");

    // 1. SELECT 문 실행
    char *select_sql = "SELECT CUSTOMER_ID, NAME, PHONE, TO_CHAR(BIRTH_DATE, 'YYYY-MM-DD') FROM TEST_CUSTOMER";
    OCIHandleAlloc(envhp, (void **) &stmthp, OCI_HTYPE_STMT, 0, NULL);
    OCIStmtPrepare(stmthp, errhp, (text *) select_sql, strlen(select_sql), OCI_NTV_SYNTAX, OCI_DEFAULT);
    // 2. 쿼리 실행
    OCIStmtExecute(svchp, stmthp, errhp, 0, 0, NULL, NULL, OCI_DEFAULT);
    // 3. 데이터 바인딩 (결과를 받을 변수)
    int id;
    char name[50];
    char phone[50];
    char birth_date[50];
    OCIDefineByPos(stmthp, &def1, errhp, 1, &id, sizeof(id), SQLT_INT, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def2, errhp, 2, name, sizeof(name), SQLT_STR, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def3, errhp, 3, phone, sizeof(phone), SQLT_STR, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def4, errhp, 4, &birth_date, sizeof(birth_date), SQLT_STR, NULL, NULL, NULL, OCI_DEFAULT);
    // <-- 여기 코드 추가 ⓑ
    printf("✅ 테이블 조회 결과:\n"); // <---여기 코드 추가 ⓒ
    printf("----------------------------------------------------------------------------\n");
    printf("| %4s | %18s | %18s | %18s |\n", "ID", "NAME", "PHONE", "BIRTH_DATE");
    printf("----------------------------------------------------------------------------\n");
    // 4. 데이터 가져오기 <---여기 코드 추가 ⓓ
    status = OCIStmtFetch2(stmthp, errhp, 1, OCI_DEFAULT, 0, OCI_DEFAULT);
    while (status == OCI_SUCCESS || status == OCI_SUCCESS_WITH_INFO) {
        printf("| %4d | %18s | %18s | %18s |\n", id, name, phone, birth_date); //<-- 여기 코드 추가 조회 후 출력하기
        status = OCIStmtFetch2(stmthp, errhp, 1, OCI_DEFAULT, 0, OCI_DEFAULT);
    }
    printf("----------------------------------------------------------------------------\n");


    /*** 데이터 삽입 (INSERT) ***/
    char *insert_sql = "INSERT INTO TEST_CUSTOMER (CUSTOMER_ID, NAME, PHONE, BIRTH_DATE) VALUES (:1, :2, :3, :4)";
    OCIHandleAlloc(envhp, (void **) &stmthp, OCI_HTYPE_STMT, 0, NULL);
    OCIStmtPrepare(stmthp, errhp, (text *) insert_sql, strlen(insert_sql), OCI_NTV_SYNTAX, OCI_DEFAULT);
    int insert_id = 4;
    char insert_name[50] = "cheolhwan";
    char insert_phone[50] = "010-6315-7372";
    char insert_birth_date[50] = "1997-11-14";
    OCIBind *bnd1 = NULL, *bnd2 = NULL, *bnd3 = NULL, *bnd4 = NULL;
    // 바인딩 변수 설정 (INSERT)
    OCIBindByPos(stmthp, &bnd1, errhp, 1, &insert_id, sizeof(insert_id), SQLT_INT, NULL, NULL, NULL, 0, NULL,
                 OCI_DEFAULT);
    OCIBindByPos(stmthp, &bnd2, errhp, 2, insert_name, sizeof(insert_name), SQLT_STR, NULL, NULL, NULL, 0, NULL,
                 OCI_DEFAULT);
    OCIBindByPos(stmthp, &bnd3, errhp, 3, insert_phone, sizeof(insert_phone), SQLT_STR, NULL, NULL, NULL, 0, NULL,
                 OCI_DEFAULT);
    OCIBindByPos(stmthp, &bnd2, errhp, 4, insert_birth_date, sizeof(insert_birth_date), SQLT_STR, NULL, NULL, NULL, 0,
                 NULL,
                 OCI_DEFAULT);
    if (OCIStmtExecute(svchp, stmthp, errhp, 1, 0, NULL, NULL, OCI_COMMIT_ON_SUCCESS) != OCI_SUCCESS) {
        check_error(errhp);
    } else {
        printf("✅ 데이터 삽입 완료!\n");
    }

    OCIHandleAlloc(envhp, (void **) &stmthp, OCI_HTYPE_STMT, 0, NULL);
    OCIStmtPrepare(stmthp, errhp, (text *) select_sql, strlen(select_sql), OCI_NTV_SYNTAX, OCI_DEFAULT);
    // 2. 쿼리 실행
    OCIStmtExecute(svchp, stmthp, errhp, 0, 0, NULL, NULL, OCI_DEFAULT);
    // 3. 데이터 바인딩 (결과를 받을 변수)
    OCIDefineByPos(stmthp, &def1, errhp, 1, &id, sizeof(id), SQLT_INT, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def2, errhp, 2, name, sizeof(name), SQLT_STR, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def3, errhp, 3, phone, sizeof(phone), SQLT_STR, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def4, errhp, 4, &birth_date, sizeof(birth_date), SQLT_STR, NULL, NULL, NULL, OCI_DEFAULT);
    // <-- 여기 코드 추가 ⓑ
    printf("✅ 테이블 조회 결과:\n"); // <---여기 코드 추가 ⓒ
    printf("----------------------------------------------------------------------------\n");
    printf("| %4s | %18s | %18s | %18s |\n", "ID", "NAME", "PHONE", "BIRTH_DATE");
    printf("----------------------------------------------------------------------------\n");
    // 4. 데이터 가져오기 <---여기 코드 추가 ⓓ
    status = OCIStmtFetch2(stmthp, errhp, 1, OCI_DEFAULT, 0, OCI_DEFAULT);
    while (status == OCI_SUCCESS || status == OCI_SUCCESS_WITH_INFO) {
        printf("| %4d | %18s | %18s | %18s |\n", id, name, phone, birth_date); //<-- 여기 코드 추가 조회 후 출력하기
        status = OCIStmtFetch2(stmthp, errhp, 1, OCI_DEFAULT, 0, OCI_DEFAULT);
    }
    printf("----------------------------------------------------------------------------\n");

    /*** 데이터 수정 (UPDATE) <--- 이 부분이 추가됨 ***/
    char *update_sql = "UPDATE TEST_CUSTOMER SET NAME = :1 WHERE CUSTOMER_ID = :2";
    OCIHandleAlloc(envhp, (void **) &stmthp, OCI_HTYPE_STMT, 0, NULL);
    OCIStmtPrepare(stmthp, errhp, (text *) update_sql, strlen(update_sql),
                   OCI_NTV_SYNTAX, OCI_DEFAULT);
    char updated_name[50] = "cheolhwan Updated"; // 수정할 이름
    int update_id = 4; // 수정할 ID
    // 바인딩 변수 설정 (UPDATE)
    // 기존에 선언한 bnd1, bnd2를 재사용
    OCIBindByPos(stmthp, &bnd1, errhp, 1, updated_name, sizeof(updated_name),
                 SQLT_STR, NULL, NULL, NULL, 0, NULL, OCI_DEFAULT);
    OCIBindByPos(stmthp, &bnd2, errhp, 2, &update_id, sizeof(update_id), SQLT_INT, NULL, NULL, NULL, 0, NULL,
                 OCI_DEFAULT);
    // SQL 실행
    if (OCIStmtExecute(svchp, stmthp, errhp, 1, 0, NULL, NULL, OCI_COMMIT_ON_SUCCESS) != OCI_SUCCESS) {
        check_error(errhp);
    } else {
        printf("✅ 데이터 수정 완료!\n");
    }

    OCIHandleAlloc(envhp, (void **) &stmthp, OCI_HTYPE_STMT, 0, NULL);
    OCIStmtPrepare(stmthp, errhp, (text *) select_sql, strlen(select_sql), OCI_NTV_SYNTAX, OCI_DEFAULT);
    // 2. 쿼리 실행
    OCIStmtExecute(svchp, stmthp, errhp, 0, 0, NULL, NULL, OCI_DEFAULT);
    // 3. 데이터 바인딩 (결과를 받을 변수)
    OCIDefineByPos(stmthp, &def1, errhp, 1, &id, sizeof(id), SQLT_INT, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def2, errhp, 2, name, sizeof(name), SQLT_STR, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def3, errhp, 3, phone, sizeof(phone), SQLT_STR, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def4, errhp, 4, &birth_date, sizeof(birth_date), SQLT_STR, NULL, NULL, NULL, OCI_DEFAULT);
    // <-- 여기 코드 추가 ⓑ
    printf("✅ 테이블 조회 결과:\n"); // <---여기 코드 추가 ⓒ
    printf("----------------------------------------------------------------------------\n");
    printf("| %4s | %18s | %18s | %18s |\n", "ID", "NAME", "PHONE", "BIRTH_DATE");
    printf("----------------------------------------------------------------------------\n");
    // 4. 데이터 가져오기 <---여기 코드 추가 ⓓ
    status = OCIStmtFetch2(stmthp, errhp, 1, OCI_DEFAULT, 0, OCI_DEFAULT);
    while (status == OCI_SUCCESS || status == OCI_SUCCESS_WITH_INFO) {
        printf("| %4d | %18s | %18s | %18s |\n", id, name, phone, birth_date); //<-- 여기 코드 추가 조회 후 출력하기
        status = OCIStmtFetch2(stmthp, errhp, 1, OCI_DEFAULT, 0, OCI_DEFAULT);
    }
    printf("----------------------------------------------------------------------------\n");

    // 삭제
    char *delete_sql = "DELETE FROM TEST_CUSTOMER WHERE CUSTOMER_ID = :1"; // ID를 기준으로 삭제
    OCIHandleAlloc(envhp, (void **) &stmthp, OCI_HTYPE_STMT, 0, NULL);
    OCIStmtPrepare(stmthp, errhp, (text *) delete_sql, strlen(delete_sql), OCI_NTV_SYNTAX, OCI_DEFAULT);
    int delete_id = 4; // 삭제할 ID 값 (예: 2번 ID)
    // 바인딩 변수 설정 (DELETE)
    OCIBindByPos(stmthp, &bnd1, errhp, 1, &delete_id, sizeof(delete_id), SQLT_INT, NULL, NULL, NULL, 0, NULL,
                 OCI_DEFAULT);
    // SQL 실행
    if (OCIStmtExecute(svchp, stmthp, errhp, 1, 0, NULL, NULL, OCI_COMMIT_ON_SUCCESS) != OCI_SUCCESS) {
        check_error(errhp);
    } else {
        printf("✅ 데이터 삭제 완료!\n");
    }

    OCIHandleAlloc(envhp, (void **) &stmthp, OCI_HTYPE_STMT, 0, NULL);
    OCIStmtPrepare(stmthp, errhp, (text *) select_sql, strlen(select_sql), OCI_NTV_SYNTAX, OCI_DEFAULT);
    // 2. 쿼리 실행
    OCIStmtExecute(svchp, stmthp, errhp, 0, 0, NULL, NULL, OCI_DEFAULT);
    // 3. 데이터 바인딩 (결과를 받을 변수)
    OCIDefineByPos(stmthp, &def1, errhp, 1, &id, sizeof(id), SQLT_INT, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def2, errhp, 2, name, sizeof(name), SQLT_STR, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def3, errhp, 3, phone, sizeof(phone), SQLT_STR, NULL, NULL, NULL, OCI_DEFAULT);
    OCIDefineByPos(stmthp, &def4, errhp, 4, &birth_date, sizeof(birth_date), SQLT_STR, NULL, NULL, NULL, OCI_DEFAULT);
    // <-- 여기 코드 추가 ⓑ
    printf("✅ 테이블 조회 결과:\n"); // <---여기 코드 추가 ⓒ
    printf("----------------------------------------------------------------------------\n");
    printf("| %4s | %18s | %18s | %18s |\n", "ID", "NAME", "PHONE", "BIRTH_DATE");
    printf("----------------------------------------------------------------------------\n");
    // 4. 데이터 가져오기 <---여기 코드 추가 ⓓ
    status = OCIStmtFetch2(stmthp, errhp, 1, OCI_DEFAULT, 0, OCI_DEFAULT);
    while (status == OCI_SUCCESS || status == OCI_SUCCESS_WITH_INFO) {
        printf("| %4d | %18s | %18s | %18s |\n", id, name, phone, birth_date); //<-- 여기 코드 추가 조회 후 출력하기
        status = OCIStmtFetch2(stmthp, errhp, 1, OCI_DEFAULT, 0, OCI_DEFAULT);
    }
    printf("----------------------------------------------------------------------------\n");

    // 5. 연결 종료
    OCIHandleFree(stmthp, OCI_HTYPE_STMT);
    OCILogoff(svchp, errhp);
    OCIHandleFree(usrhp, OCI_HTYPE_SESSION);
    OCIHandleFree(svchp, OCI_HTYPE_SVCCTX);
    OCIHandleFree(srvhp, OCI_HTYPE_SERVER);
    OCIHandleFree(errhp, OCI_HTYPE_ERROR);
    OCIHandleFree(envhp, OCI_HTYPE_ENV);
    printf("✅ 연결 종료\n");
    return 0;
}
