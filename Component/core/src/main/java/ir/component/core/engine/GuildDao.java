package ir.component.core.engine;

import ir.component.core.dao.model.Guild;
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
public class GuildDao extends AbstractDao {

    @Transactional
    public void persist(final Guild guild) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                entityManager.persist(guild);
            }
        });
    }

    @Transactional
    public List<Guild> allGuilds() {
        return transactionTemplate.execute(new TransactionCallback<List<Guild>>() {
            public List<Guild> doInTransaction(TransactionStatus transactionStatus) {
                return (List<Guild>) entityManager.createQuery("from Guild ").getResultList();
            }
        });
    }
}
