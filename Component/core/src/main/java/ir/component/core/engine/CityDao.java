package ir.component.core.engine;

import ir.component.core.dao.model.City;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.List;

/**
 * @author Zahra Afsharinia
 */
@Repository
@Transactional
public class CityDao extends AbstractDao {

    @Transactional
    public void persist(final City city) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                entityManager.persist(city);
            }
        });
    }

    @Transactional
    public List<City> allCities() {
        return transactionTemplate.execute(new TransactionCallback<List<City>>() {
            public List<City> doInTransaction(TransactionStatus transactionStatus) {
                return (List<City>) entityManager.createQuery("from City ").getResultList();
            }
        });
    }
}
