package test.revolut.exception;

public class AppException extends Exception {
    String msg;
    public AppException(){

    }

    public String getMsg(){
       return this.msg;
    }
}
