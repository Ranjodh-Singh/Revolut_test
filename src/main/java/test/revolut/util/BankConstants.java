package test.revolut.util;

public class BankConstants {
    public static final String LOG = "BankLog";
    public static final String DEFAULT_CURRENCY = "USD";
    public enum Action{
        CREDIT,DEBIT,TRANSFERRED
    }
    public enum Status{
        COMPLETED,IN_PROGRESS,INITIATED
    }
    public static long globalTransactionCounter;
    public static int getGlobalAccountCounter;
}
