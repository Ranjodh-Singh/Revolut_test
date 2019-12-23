package test.revolut.service;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.revolut.db.dao.TransactionDAO;
import test.revolut.db.entity.Account;
import test.revolut.db.entity.Transaction;
import test.revolut.exception.InvalidAccountException;
import test.revolut.exception.InvalidBankTransaction;
import test.revolut.util.BankConstants;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TransactionServiceImpl {
    private AccountServiceImpl accountService;
    private TransactionDAO transactionDAO;
    static Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    public TransactionServiceImpl(){
        this.accountService = new AccountServiceImpl();
        this.transactionDAO = new TransactionDAO();
    }

    public void saveTransaction(test.revolut.db.entity.Transaction moneyTransaction){
        this.transactionDAO.saveTransaction(moneyTransaction);
    }
    public List<Transaction> getTransactions(){
        return this.transactionDAO.getTransactions();
    }

    public Optional<Transaction> getLatestTransactionFor(int accountId) throws InvalidAccountException {
       return this.transactionDAO.getLatestTransactionFor(accountId);
    }
    public List<test.revolut.db.entity.Transaction> getAllTransactionsFor(Integer accountId) throws InvalidAccountException {
        return this.transactionDAO.getAllTransactionsFor(accountId);
    }
    public Optional<test.revolut.db.entity.Transaction> getTransaction(Account account){
        return this.transactionDAO.getLastSuccessFulTransaction(account);
    }
    public Optional<test.revolut.db.entity.Transaction> createTransaction(Account account, int amount, String currency, String action) throws InvalidBankTransaction {
        test.revolut.db.entity.Transaction newTransaction;
        Optional<test.revolut.db.entity.Transaction> transaction = this.transactionDAO.getLastSuccessFulTransaction(account);
        if(transaction.isPresent()){
            BigDecimal newBalance;
            test.revolut.db.entity.Transaction lastTransaction = transaction.get();
            if(action.equalsIgnoreCase(BankConstants.Action.CREDIT.name())) {
                newBalance = lastTransaction.getBalance().add(new BigDecimal(amount));
            }else if(action.equalsIgnoreCase(BankConstants.Action.DEBIT.name())){
                newBalance = lastTransaction.getBalance().subtract(new BigDecimal(amount));
                if(newBalance.compareTo(BigDecimal.ZERO) < 0){
                    throw new InvalidBankTransaction(" Insufficient funds in the account. Current balance is :"+lastTransaction.getBalance());
                }
            }else{
                throw new InvalidBankTransaction("Invalid Operation. you can only credit or debit from the account.");
            }
            newTransaction = SerializationUtils.clone(lastTransaction);
            createNewTransactionEntity(account, amount, currency, action, newTransaction, newBalance,"new Transaction" );
        }else{
            newTransaction = new test.revolut.db.entity.Transaction();
            createNewTransactionEntity(account, amount, currency, action, newTransaction, new BigDecimal(amount), "new Transaction");
        }
        saveTransaction(newTransaction);
        account.setLatestTransactionState(newTransaction.getStatus());
        accountService.updateAccount(account);
        logger.info("new transaction has been saved:{}",newTransaction);
        logger.info("your account has been updated with the transaction status :"+ account);
        return Optional.of(newTransaction);
    }

    private void createNewTransactionEntity(Account toAccount, int amount, String currency, String action, Transaction newTransaction, BigDecimal newBalance, String remark) {
        newTransaction.setBalance(newBalance);
        newTransaction.setAmount(new BigDecimal(amount));
        newTransaction.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        newTransaction.setCurrency(currency);
        newTransaction.setStatus(BankConstants.Status.COMPLETED.name().toLowerCase());
        newTransaction.setToAccount(toAccount);
        newTransaction.setAction(action);
        newTransaction.setValid(true);
        newTransaction.setRemarks(remark);
        newTransaction.setId(getNewTransactionId());
    }
    public static synchronized long getNewTransactionId(){
        return BankConstants.globalTransactionCounter++;
    }

    public Boolean transferFund(Account fromAccountObj, Account toAccountObj, int amount, String defaultCurrency) throws InvalidBankTransaction {
        Optional<test.revolut.db.entity.Transaction> transaction = this.transactionDAO.getLastSuccessFulTransaction(fromAccountObj);
        test.revolut.db.entity.Transaction newTransaction;
        if(transaction.isPresent()){
            BigDecimal newBalance;
            test.revolut.db.entity.Transaction lastTransaction = transaction.get();
            newBalance = lastTransaction.getBalance().subtract(new BigDecimal(amount));
            if(newBalance.compareTo(BigDecimal.ZERO) < 0){
                throw new InvalidBankTransaction(" Insufficient funds in the account. Current balance is :"+lastTransaction.getBalance());
            }
            newTransaction = SerializationUtils.clone(lastTransaction);
            createNewTransactionEntity(fromAccountObj, amount, defaultCurrency, BankConstants.Action.DEBIT.name(), newTransaction, newBalance,"transferred" );
        }else{
            newTransaction = new test.revolut.db.entity.Transaction();
            createNewTransactionEntity(fromAccountObj, amount, defaultCurrency, BankConstants.Action.DEBIT.name(), newTransaction, new BigDecimal(amount),"transferred" );
        }
        //created transaction entity from account.
        BigDecimal newBalance;
        Optional<test.revolut.db.entity.Transaction> toAccountLastTransaction = this.transactionDAO.getLastSuccessFulTransaction(toAccountObj);
        if(toAccountLastTransaction.isPresent()){
            test.revolut.db.entity.Transaction lastTransaction = transaction.get();
            newBalance = lastTransaction.getBalance().add(new BigDecimal(amount));
        } else{
            newBalance = new BigDecimal(amount);
        }
        Transaction newTransactionForToAccount = new test.revolut.db.entity.Transaction();
        createNewTransactionEntity(toAccountObj, amount, defaultCurrency, BankConstants.Action.CREDIT.name(), newTransactionForToAccount, newBalance, "transferred" );
        saveAllTransactions(Arrays.asList(newTransaction,newTransactionForToAccount));
        fromAccountObj.setLatestTransactionState(newTransaction.getStatus());
        toAccountObj.setLatestTransactionState(newTransactionForToAccount.getStatus());
        accountService.saveAllAccount(Arrays.asList(fromAccountObj,toAccountObj));
        return true;
    }

    private void saveAllTransactions(List<Transaction> asList) {
        this.transactionDAO.saveAllTransactions(asList);
    }

    public AccountServiceImpl getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    public TransactionDAO getTransactionDAO() {
        return transactionDAO;
    }

    public void setTransactionDAO(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }
}
