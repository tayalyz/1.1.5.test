package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();
    private Transaction transaction;


    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.createSQLQuery("""
                     CREATE TABLE IF NOT EXISTS userS (
                       id INT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(50) NULL,
                       lastName VARCHAR(50) NULL,
                       age TINYINT CHECK (age>0 and age<128) NULL,
                       PRIMARY KEY (id))""").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS userS").executeUpdate();
            transaction.commit();
        }catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.get(User.class, id));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userS = null;
        try (Session session = sessionFactory.openSession()) {
            userS = session.createNativeQuery("SELECT * FROM userS", User.class)
                    .getResultList();
            System.out.println(userS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userS;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE User ").executeUpdate();
            transaction.commit();
        }  catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
