package test.revolut.db.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import test.revolut.config.DatabaseConfig;
import test.revolut.db.entity.Account;

import java.util.List;
public class TransactionDAO {
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
    public List< Account > getTransactions() {
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Transaction").list();
        }
    }
}