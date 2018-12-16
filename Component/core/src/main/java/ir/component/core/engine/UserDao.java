package ir.component.core.engine;

import ir.component.core.dao.model.Car;
import ir.component.core.dao.model.Login;
import ir.component.core.dao.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.List;

/**
 * @author Mohammad Yasin Kaji
 */

@Repository
@Transactional
public class UserDao extends AbstractDao {

    @Transactional
    public void persist(final User user) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                entityManager.persist(user);
            }
        });
    }

    @Transactional
    public User validateUser(Login login) {
        return transactionTemplate.execute(new TransactionCallback<List<User>>() {
            public List<User> doInTransaction(TransactionStatus transactionStatus) {
                return (List<User>) entityManager.createQuery("from User u WHERE u.userName =" + login.getUsername() + " AND u.password = " + login.getPassword()).getResultList();
            }
        }).get(0);
    }
}
