ALTER SESSION SET STATISTICS_LEVEL = ALL;

-- 1번 튜닝
SELECT * FROM EMPLOYEES
WHERE EMP_NO LIKE '101%' OR EMP_NO LIKE '103%';

-------------------------------------------------------------------------------------------
--| Id  | Operation         | Name      | Starts | E-Rows | A-Rows |   A-Time   | Buffers |
-------------------------------------------------------------------------------------------
--|   0 | SELECT STATEMENT  |           |      1 |        |     50 |00:00:00.01 |       4 |
--|*  1 |  TABLE ACCESS FULL| EMPLOYEES |      1 |    523 |     50 |00:00:00.01 |       4 |
-------------------------------------------------------------------------------------------

SELECT * FROM EMPLOYEES
WHERE EMP_NO BETWEEN 10100 AND 10199 
    OR EMP_NO BETWEEN 10300 AND 10399;
------------------------------------------------------------------------------------------------------------------------------------------
--| Id  | Operation                           | Name        | Starts | E-Rows | A-Rows |   A-Time   | Buffers |  OMem |  1Mem | Used-Mem |
------------------------------------------------------------------------------------------------------------------------------------------
--|   0 | SELECT STATEMENT                    |             |      1 |        |     50 |00:00:00.01 |       5 |       |       |          |
--|   1 |  TABLE ACCESS BY INDEX ROWID BATCHED| EMPLOYEES   |      1 |    523 |     50 |00:00:00.01 |       5 |       |       |          |
--|   2 |   BITMAP CONVERSION TO ROWIDS       |             |      1 |        |     50 |00:00:00.01 |       4 |       |       |          |
--|   3 |    BITMAP OR                        |             |      1 |        |      1 |00:00:00.01 |       4 |       |       |          |
--|   4 |     BITMAP CONVERSION FROM ROWIDS   |             |      1 |        |      1 |00:00:00.01 |       2 |       |       |          |
--|   5 |      SORT ORDER BY                  |             |      1 |        |    100 |00:00:00.01 |       2 | 73728 | 73728 |          |
--|*  6 |       INDEX RANGE SCAN              | SYS_C008377 |      1 |        |    100 |00:00:00.01 |       2 |       |       |          |
--|   7 |     BITMAP CONVERSION FROM ROWIDS   |             |      1 |        |      1 |00:00:00.01 |       2 |       |       |          |
--|   8 |      SORT ORDER BY                  |             |      1 |        |    100 |00:00:00.01 |       2 | 86016 | 86016 |75776  (0)|
--|*  9 |       INDEX RANGE SCAN              | SYS_C008377 |      1 |        |    100 |00:00:00.01 |       2 |       |       |          |
------------------------------------------------------------------------------------------------------------------------------------------

SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY_CURSOR(NULL,NULL, 'ALLSTATS LAST'));

-- 2번 튜닝
SELECT NVL(GENDER, 'NON') AS GENDER, COUNT(*)
FROM EMPLOYEES
GROUP BY NVL(GENDER, 'NON');

-- HASH GROUP BY 무거움
------------------------------------------------------------------------------------------------
--| Id  | Operation             | Name       | Starts | E-Rows | A-Rows |   A-Time   | Buffers |
------------------------------------------------------------------------------------------------
--|   0 | SELECT STATEMENT      |            |      1 |        |      2 |00:00:00.01 |      43 |
--|   1 |  HASH GROUP BY        |            |      1 |  21859 |      2 |00:00:00.01 |      43 |
--|   2 |   INDEX FAST FULL SCAN| IDX_GENDER |      1 |  21859 |  20000 |00:00:00.01 |      43 |
------------------------------------------------------------------------------------------------

SELECT /*+INDEX(EMPLOYEES IDX_GENDER)*/ GENDER, COUNT(*)
FROM EMPLOYEES
GROUP BY GENDER;
-----------------------------------------------------------------------------------------------
--| Id  | Operation            | Name       | Starts | E-Rows | A-Rows |   A-Time   | Buffers |
-----------------------------------------------------------------------------------------------
--|   0 | SELECT STATEMENT     |            |      1 |        |      2 |00:00:00.01 |      38 |
--|   1 |  SORT GROUP BY NOSORT|            |      1 |  21859 |      2 |00:00:00.01 |      38 |
--|   2 |   INDEX FULL SCAN    | IDX_GENDER |      1 |  21859 |  20000 |00:00:00.01 |      38 |
-----------------------------------------------------------------------------------------------

SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY_CURSOR(NULL,NULL, 'ALLSTATS LAST'));

-- 3번 튜닝
SELECT * FROM EMPLOYEES WHERE GENDER ||' '|| LAST_NAME = 'M Radwan';
----------------------------------------------------------------------------------------------------
--| Id  | Operation         | Name      | Starts | E-Rows | A-Rows |   A-Time   | Buffers | Reads  |
----------------------------------------------------------------------------------------------------
--|   0 | SELECT STATEMENT  |           |      1 |        |      5 |00:00:00.01 |     159 |     14 |
--|*  1 |  TABLE ACCESS FULL| EMPLOYEES |      1 |      4 |      5 |00:00:00.01 |     159 |     14 |
----------------------------------------------------------------------------------------------------

SELECT * FROM EMPLOYEES WHERE GENDER = 'M' AND LAST_NAME = 'Radwan';
---------------------------------------------------------------------------------------------------------------------------------
--| Id  | Operation                           | Name                 | Starts | E-Rows | A-Rows |   A-Time   | Buffers | Reads  |
---------------------------------------------------------------------------------------------------------------------------------
--|   0 | SELECT STATEMENT                    |                      |      1 |        |      5 |00:00:00.01 |       7 |      1 |
--|   1 |  TABLE ACCESS BY INDEX ROWID BATCHED| EMPLOYEES            |      1 |      4 |      5 |00:00:00.01 |       7 |      1 |
--|*  2 |   INDEX RANGE SCAN                  | IDX_GENDER_LAST_NAME |      1 |      4 |      5 |00:00:00.01 |       2 |      1 |
---------------------------------------------------------------------------------------------------------------------------------

SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY_CURSOR(NULL,NULL, 'ALLSTATS LAST'));

-- 튜닝 3번
SELECT FIRST_NAME, LAST_NAME, EMP_NO
FROM EMPLOYEES
WHERE SUBSTR(EMP_NO, 1, 4) = 1030;
-------------------------------------------------------------------------------------------
--| Id  | Operation         | Name      | Starts | E-Rows | A-Rows |   A-Time   | Buffers |
-------------------------------------------------------------------------------------------
--|   0 | SELECT STATEMENT  |           |      1 |        |     10 |00:00:00.01 |     159 |
--|*  1 |  TABLE ACCESS FULL| EMPLOYEES |      1 |      3 |     10 |00:00:00.01 |     159 |
-------------------------------------------------------------------------------------------
SELECT FIRST_NAME, LAST_NAME, EMP_NO
FROM EMPLOYEES
WHERE EMP_NO >= 10300 AND EMP_NO < 10310;
---------------------------------------------------------------------------------------------------------------
--| Id  | Operation                           | Name        | Starts | E-Rows | A-Rows |   A-Time   | Buffers |
---------------------------------------------------------------------------------------------------------------
--|   0 | SELECT STATEMENT                    |             |      1 |        |     10 |00:00:00.01 |       3 |
--|   1 |  TABLE ACCESS BY INDEX ROWID BATCHED| EMPLOYEES   |      1 |     10 |     10 |00:00:00.01 |       3 |
--|*  2 |   INDEX RANGE SCAN                  | SYS_C008377 |      1 |     10 |     10 |00:00:00.01 |       2 |
---------------------------------------------------------------------------------------------------------------

SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY_CURSOR(NULL,NULL, 'ALLSTATS LAST'));

-- 튜닝 4번
SELECT DISTINCT E.EMP_NO, E.FIRST_NAME, E.LAST_NAME, D.DEPT_NO
FROM EMPLOYEES E, DEPT_MANAGER D
WHERE E.EMP_NO = D.EMP_NO;
------------------------------------------------------------------------------------------------------------------
--| Id  | Operation                     | Name        | Starts | E-Rows | A-Rows |   A-Time   | Buffers | Reads  |
------------------------------------------------------------------------------------------------------------------
--|   0 | SELECT STATEMENT              |             |      1 |        |     24 |00:00:00.01 |      51 |      1 |
--|   1 |  HASH UNIQUE                  |             |      1 |     24 |     24 |00:00:00.01 |      51 |      1 |
--|   2 |   NESTED LOOPS                |             |      1 |     24 |     24 |00:00:00.01 |      51 |      1 |
--|   3 |    NESTED LOOPS               |             |      1 |     24 |     24 |00:00:00.01 |      27 |      1 |
--|   4 |     INDEX FAST FULL SCAN      | SYS_C008365 |      1 |     24 |     24 |00:00:00.01 |       4 |      1 |
--|*  5 |     INDEX UNIQUE SCAN         | SYS_C008377 |     24 |      1 |     24 |00:00:00.01 |      23 |      0 |
--|   6 |    TABLE ACCESS BY INDEX ROWID| EMPLOYEES   |     24 |      1 |     24 |00:00:00.01 |      24 |      0 |
------------------------------------------------------------------------------------------------------------------

SELECT E.EMP_NO, E.FIRST_NAME, E.LAST_NAME, D.DEPT_NO
FROM EMPLOYEES E, DEPT_MANAGER D
WHERE E.EMP_NO = D.EMP_NO;
--------------------------------------------------------------------------------------------------------
--| Id  | Operation                    | Name        | Starts | E-Rows | A-Rows |   A-Time   | Buffers |
--------------------------------------------------------------------------------------------------------
--|   0 | SELECT STATEMENT             |             |      1 |        |     24 |00:00:00.01 |      51 |
--|   1 |  NESTED LOOPS                |             |      1 |     24 |     24 |00:00:00.01 |      51 |
--|   2 |   NESTED LOOPS               |             |      1 |     24 |     24 |00:00:00.01 |      27 |
--|   3 |    INDEX FAST FULL SCAN      | SYS_C008365 |      1 |     24 |     24 |00:00:00.01 |       4 |
--|*  4 |    INDEX UNIQUE SCAN         | SYS_C008377 |     24 |      1 |     24 |00:00:00.01 |      23 |
--|   5 |   TABLE ACCESS BY INDEX ROWID| EMPLOYEES   |     24 |      1 |     24 |00:00:00.01 |      24 |
--------------------------------------------------------------------------------------------------------
SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY_CURSOR(NULL,NULL, 'ALLSTATS LAST'));

-- 실습 5번
SELECT COUNT(DISTINCT E.EMP_NO) AS CNT
FROM EMPLOYEES E, (
    SELECT EMP_NO
    FROM SALARIES
    WHERE SALARY > 50000
) S
WHERE E.EMP_NO = S.EMP_NO;
----------------------------------------------------------------------------------------------------------------------------------------
--| Id  | Operation                | Name        | Starts | E-Rows | A-Rows |   A-Time   | Buffers | Reads  |  OMem |  1Mem | Used-Mem |
----------------------------------------------------------------------------------------------------------------------------------------
--|   0 | SELECT STATEMENT         |             |      1 |        |      1 |00:00:00.01 |     934 |     18 |       |       |          |
--|   1 |  SORT AGGREGATE          |             |      1 |      1 |      1 |00:00:00.01 |     934 |     18 |       |       |          |
--|   2 |   VIEW                   | VM_NWVW_1   |      1 |   1440 |  17484 |00:00:00.01 |     934 |     18 |       |       |          |
--|   3 |    HASH GROUP BY         |             |      1 |   1440 |  17484 |00:00:00.01 |     934 |     18 |  2406K|  2406K| 2125K (0)|
--|*  4 |     HASH JOIN SEMI       |             |      1 |   1440 |  17484 |00:00:00.01 |     934 |     18 |  2801K|  2801K| 2165K (0)|
--|   5 |      INDEX FAST FULL SCAN| SYS_C008377 |      1 |  21859 |  20000 |00:00:00.01 |      55 |     11 |       |       |          |
--|*  6 |      TABLE ACCESS FULL   | SALARIES    |      1 |    135K|    144K|00:00:00.01 |     879 |      7 |       |       |          |
----------------------------------------------------------------------------------------------------------------------------------------

SELECT COUNT(E.EMP_NO) AS CNT
FROM EMPLOYEES E
WHERE EXISTS (SELECT 1 FROM SALARIES S WHERE S.EMP_NO = E.EMP_NO AND S.SALARY > 50000);
-----------------------------------------------------------------------------------------------------------------------------
--| Id  | Operation              | Name        | Starts | E-Rows | A-Rows |   A-Time   | Buffers |  OMem |  1Mem | Used-Mem |
-----------------------------------------------------------------------------------------------------------------------------
--|   0 | SELECT STATEMENT       |             |      1 |        |      1 |00:00:00.03 |     934 |       |       |          |
--|   1 |  SORT AGGREGATE        |             |      1 |      1 |      1 |00:00:00.03 |     934 |       |       |          |
--|*  2 |   HASH JOIN SEMI       |             |      1 |   1440 |  17484 |00:00:00.03 |     934 |  2801K|  2801K| 1857K (0)|
--|   3 |    INDEX FAST FULL SCAN| SYS_C008377 |      1 |  21859 |  20000 |00:00:00.01 |      55 |       |       |          |
--|*  4 |    TABLE ACCESS FULL   | SALARIES    |      1 |    135K|    144K|00:00:00.01 |     879 |       |       |          |
-----------------------------------------------------------------------------------------------------------------------------

SELECT * FROM TABLE(DBMS_XPLAN.DISPLAY_CURSOR(NULL,NULL, 'ALLSTATS LAST'));
