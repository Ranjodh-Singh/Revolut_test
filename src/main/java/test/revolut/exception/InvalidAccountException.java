package test.revolut.exception;

public class InvalidAccountException extends AppException {
    private String msg="Account is Not a valid one.";

    public InvalidAccountException(){
        super();

    }
    public InvalidAccountException(String msg) {

        this.msg = msg;
    }


    public String getMsg(){
        return msg;
    }
}
