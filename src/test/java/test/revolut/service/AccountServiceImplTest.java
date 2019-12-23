package test.revolut.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import test.revolut.db.dao.AccountDAO;
import test.revolut.db.entity.Account;
import test.revolut.exception.InvalidAccountException;

import java.util.Arrays;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountDAO accountDAO;
    private Account account;
    private final int accountId=1;

    @Before
    public void setUp() throws InvalidAccountException {
        /*accountService = new AccountServiceImpl();*/
        account = new Account();
        fakeAccount(account);
        Mockito.when(accountDAO.getAccounts()).thenReturn(Arrays.asList(account));
        Mockito.when(accountDAO.getAccount(accountId)).thenReturn(account);
        Mockito.doNothing().when(accountDAO).saveAccount(account);
        Mockito.doNothing().when(accountDAO).saveALlAccount(Arrays.asList(account));
    }

    private void fakeAccount(Account account) {
        account.setId(1);
        account.setFirstName("ranjodh");
        account.setLastName("singh");
        account.setEmail("ranjodh.87@gmail.com");
    }

    @Test
    public void testTest(){
        Assert.assertEquals(9,5+4);
    }
    @Test
    public void testGetAccounts(){
        Assert.assertEquals(Arrays.asList(account),accountService.getAccounts());
    }

    @Test
    public void testGetAccount() throws InvalidAccountException {
        Assert.assertEquals(account,accountService.getAccount(1));
    }
    @Test
    public void testSaveAccount() {
        accountService.saveAccount(account);
        verify(accountDAO, times(1)).saveAccount(account);
    }

    @Test
    public void testCreateAccount(){
        Assert.assertEquals(account,accountService.createAccount("ranjodh","singh","ranjodh.87@gmail.com"));
    }

    @Test
    public void testSaveAllAccount() {
        accountService.saveAllAccount(Arrays.asList(account));
        verify(accountDAO, times(1)).saveALlAccount(Arrays.asList(account));
    }
}
