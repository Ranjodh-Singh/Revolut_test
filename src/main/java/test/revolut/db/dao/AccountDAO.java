package test.revolut.db.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.revolut.config.DatabaseConfig;
import test.revolut.db.entity.Account;
import test.revolut.exception.InvalidAccountException;
import test.revolut.util.BankConstants;

import java.util.List;


public class AccountDAO {
    static Logger logger = LoggerFactory.getLogger(AccountDAO.class);
    public void saveAccount(Account account) {
        Transaction transaction = null;
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.save(account);

            transaction.commit();
        } catch (Exception e) {
            logger.error(BankConstants.LOG,e);
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    public void updateAccount(Account account) {
        Transaction transaction = null;
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.update(account);

            transaction.commit();
        } catch (Exception e) {
            logger.error(BankConstants.LOG,e);
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List< Account > getAccounts() {
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Account").list();
        }
    }
    public Account getAccount(int accountId) throws InvalidAccountException {
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery("from Account where id=:accountId");
            query.setParameter("accountId",accountId);
            List list = query.list();
            for(Object ob:list){
                        Account account = (Account)ob;
                        return account;
            }
        }
        throw new InvalidAccountException();
    }
    public void saveALlAccount(List<Account> accounts) {
        Transaction transaction = null;
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            accounts.forEach(a->session.save(a));

            transaction.commit();
        } catch (Exception e) {
            logger.error(BankConstants.LOG,e);
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }



}