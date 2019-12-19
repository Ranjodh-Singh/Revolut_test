package test.revolut.exception;

public class UnsettledTransactionException extends AppException {
    private String msg ="There are unsettled Transactions.";

    public UnsettledTransactionException(){

    }
public UnsettledTransactionException(String msg){
    this.msg = msg;
}
    public String getMsg(){
        return msg;
    }
}
