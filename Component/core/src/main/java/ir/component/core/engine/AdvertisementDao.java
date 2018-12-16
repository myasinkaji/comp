package ir.component.core.engine;

import ir.component.core.dao.model.AbstractAdvertisement;
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
public class AdvertisementDao extends AbstractDao {

    @Transactional
    public void persist(final AbstractAdvertisement advertisement) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                entityManager.persist(advertisement);
            }
        });
    }

    @Transactional
    public void remove(final AbstractAdvertisement advertisement) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                entityManager.remove(advertisement);
            }
        });
    }

    @Transactional
    public void removeDetached(final AbstractAdvertisement advertisement) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                AbstractAdvertisement adv = entityManager.merge(advertisement);
                entityManager.remove(adv);
            }
        });
    }

    @Transactional
    public List<AbstractAdvertisement> allAdvertisements(final String entity) {
        return transactionTemplate.execute(new TransactionCallback<List<AbstractAdvertisement>>() {
            public List<AbstractAdvertisement> doInTransaction(TransactionStatus transactionStatus) {
                return (List<AbstractAdvertisement>) entityManager.createQuery("from " + entity).getResultList();
            }
        });
    }

}
