package test.revolut.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.revolut.db.dao.AccountDAO;
import test.revolut.db.entity.Account;
import test.revolut.exception.InvalidAccountException;
import test.revolut.util.BankConstants;

import java.util.List;

public class AccountServiceImpl {

    private AccountDAO accountDAO;

    static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    public AccountServiceImpl(){
        this.accountDAO = new AccountDAO();
    }

    public void saveAccount(Account account){
        this.accountDAO.saveAccount(account);
    }

    public void updateAccount(Account account){
        this.accountDAO.updateAccount(account);
    }
    public List< Account > getAccounts(){
        return this.accountDAO.getAccounts();
    }

    public Account getAccount(int accountId) throws InvalidAccountException{
        return this.accountDAO.getAccount(accountId);
    }

    public Account createAccount(String firstName, String lastName, String email) {
        Account account = new Account();
        account.setEmail(email);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setId(getNewAccountId());
        saveAccount(account);
        logger.info("New account has been created :{}",account);
        return account;
    }
    public static synchronized int getNewAccountId(){
        return BankConstants.getGlobalAccountCounter++;
    }
}
