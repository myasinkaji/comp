package ir.component.core.engine;

import ir.component.core.dao.model.News;
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
public class NewsDao extends AbstractDao {

    @Transactional
    public void persist(final News news) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                entityManager.persist(news);
            }
        });
    }

    @Transactional
    public List<News> allNews() {
        return transactionTemplate.execute(new TransactionCallback<List<News>>() {
            public List<News> doInTransaction(TransactionStatus transactionStatus) {
                return (List<News>) entityManager.createQuery("from News").getResultList();
            }
        });
    }

}
