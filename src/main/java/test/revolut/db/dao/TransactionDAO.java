package test.revolut.db.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.revolut.config.DatabaseConfig;
import test.revolut.db.entity.Account;
import test.revolut.exception.InvalidAccountException;
import test.revolut.exception.InvalidBankTransaction;
import test.revolut.util.BankConstants;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TransactionDAO {
    //TODO: DEPENDENCY INJECTION NEEDED.
    private AccountDAO accountDAO = new AccountDAO();
    static Logger logger = LoggerFactory.getLogger(TransactionDAO.class);

    public void saveTransaction(test.revolut.db.entity.Transaction moneyTransaction) {
        Transaction transaction = null;
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.save(moneyTransaction);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    //test method.
    public List<test.revolut.db.entity.Transaction> getTransactions() {
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {
            return session.createQuery("from test.revolut.db.entity.Transaction").list();
        }
    }

    public Optional<test.revolut.db.entity.Transaction> getLatestTransactionFor(int accountId) throws InvalidAccountException {

        Account account = accountDAO.getAccount(accountId);
        if(null == account){
             throw new InvalidAccountException();
        }
        return getTransaction(account);
    }

    public Optional<test.revolut.db.entity.Transaction> getTransaction(Account account) {
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery("from test.revolut.db.entity.Transaction where valid=true and status=:status and toAccount=:account");
            query.setParameter("account",account);
            query.setParameter("status", BankConstants.Status.COMPLETED.name().toLowerCase());
            List<test.revolut.db.entity.Transaction> transactions =  query.list();
            return getLatest(transactions);
        }
    }

    private Optional<test.revolut.db.entity.Transaction> getLatest(List<test.revolut.db.entity.Transaction> transactions) {
        if(CollectionUtils.isNotEmpty(transactions)){
            Optional<test.revolut.db.entity.Transaction> first = transactions.stream().sorted(Comparator.comparing(test.revolut.db.entity.Transaction::getCreatedTime).reversed()).findFirst();
            if(first.isPresent()){
                logger.info("latest transaction found {}",first.get().getId());
                return first;
            }
        }
        return Optional.empty();
    }



    public List<test.revolut.db.entity.Transaction> getAllTransactionsFor(Integer accountId) throws InvalidAccountException{
        Account account = accountDAO.getAccount(accountId);
        if(null == account){
            throw new InvalidAccountException();
        }
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery("from test.revolut.db.entity.Transaction where valid=true and toAccount=:account");
            query.setParameter("account",account);
            List<test.revolut.db.entity.Transaction> transactions =  query.list();
            return transactions;
        }
    }
}