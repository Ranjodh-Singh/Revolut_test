package test.revolut.exception;

public class InvalidBankTransaction extends AppException {
    String msg = "invalid transaction.";

    public InvalidBankTransaction(String msg) {

        this.msg = msg;
    }

    public InvalidBankTransaction() {
        this.msg = msg;
    }
    public String getMsg(){
        return msg;
    }
}
