package ir.component.core.engine;

import ir.component.core.dao.model.Car;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Mohammad Yasin Kaji
 */
@Repository
@Transactional
public class CarDao {


    @PersistenceContext(name = "securityPersistentContext")
    private EntityManager entityManager;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Transactional
    public void persist(final Car car) {
//        EntityTransaction transaction = entityManager.getTransaction();
//        transaction.begin();
//        entityManager.persist(user);
//        transaction.commit();

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                entityManager.persist(car);
            }
        });
    }

    @Transactional
    public List<Car> allCars() {
        return transactionTemplate.execute(new TransactionCallback<List<Car>>() {
            public List<Car> doInTransaction(TransactionStatus transactionStatus) {
                return (List<Car>) entityManager.createQuery("from Car").getResultList();
            }
        });
    }
}
