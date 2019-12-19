package test.revolut.exception;

public class InvalidBalanceException extends AppException {
    String msg = "Invalid Balance. Please provide value greater than zero.";

    public InvalidBalanceException() {
    }

    public InvalidBalanceException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
