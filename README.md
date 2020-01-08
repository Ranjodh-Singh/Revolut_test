Simple Application which can do following -
1. Created a new account.
2. add money to your account.
3. debit money from your account.
4. list all the transactions/statement.
5. provide very rudimantory level of concurrency solution using db status.

To run-(java 8 has been used and latest maven version.)
>maven clean install

>cd target

>java -jar bank-1.0.jar


or for custom port.


> java -jar bank-1.0.jar 8080


Technologies Used:
- java
- jersey 
- apache commons libraries
- Gson
- hsqldb
- hibernate
- maven
- jersery embedded server
- Junit
- slf4j
- No DI framework is used to keep it simple.

# API Specs
Please go to this location for Sample API-
https://documenter.getpostman.com/view/4054781/SWEDyDvY?version=latest#5056c527-a007-4fc0-b98d-d93c45457fa6

