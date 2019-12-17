package test.revolut.db.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import test.revolut.config.DatabaseConfig;
import test.revolut.db.entity.Account;

import java.util.List;


public class AccountDAO {
    public void saveAccount(Account account) {
        Transaction transaction = null;
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.save(account);

            transaction.commit();
        } catch (Exception e) {
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
}