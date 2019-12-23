package test.revolut.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import test.revolut.db.dao.TransactionDAO;
import test.revolut.db.entity.Account;
import test.revolut.db.entity.Transaction;
import test.revolut.util.BankConstants;

import java.math.BigDecimal;
import java.sql.Timestamp;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl  transactionService;

    @Mock
    private TransactionDAO transactionDAO;

    @Mock
    private AccountServiceImpl accountService;

    private Account account;
    test.revolut.db.entity.Transaction creditTransaction;

    private long milliSeconds = 1576789081878l;

    @Before
    public void setUp(){
        account = new Account();
        fakeAccount(account);
        creditTransaction = new Transaction();
        fakeTransaction(creditTransaction);
    }

    private void fakeTransaction(Transaction transaction) {
        transaction.setId(1);
        transaction.setValid(true);
        transaction.setRemarks("new transaction");
        transaction.setAction(BankConstants.Action.CREDIT.name());
        transaction.setCurrency("USD");
        transaction.setToAccount(account);
        transaction.setAmount(new BigDecimal(1000));
        transaction.setBalance(new BigDecimal(10000));
        transaction.setStatus("completed");
        transaction.setCreatedTime(new Timestamp(milliSeconds));
    }

    private void fakeAccount(Account account) {
        account.setId(1);
        account.setFirstName("ranjodh");
        account.setLastName("singh");
        account.setEmail("ranjodh.87@gmail.com");
    }

    @Test
    public void testSaveTransaction(){
        System.out.println(milliSeconds);
    }

}
