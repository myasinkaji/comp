package ir.component.core.engine;

import ir.component.core.dao.model.Province;
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
public class ProvinceDao extends AbstractDao {

    @Transactional
    public void persist(final Province province) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                entityManager.persist(province);
            }
        });
    }

    @Transactional
    public List<Province> allProvinces() {
        return transactionTemplate.execute(new TransactionCallback<List<Province>>() {
            public List<Province> doInTransaction(TransactionStatus transactionStatus) {
                return (List<Province>) entityManager.createQuery("from Province ").getResultList();
            }
        });
    }
}
