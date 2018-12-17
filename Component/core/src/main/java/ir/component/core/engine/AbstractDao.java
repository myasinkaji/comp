package ir.component.core.engine;

import ir.component.generaldao.GenericDao;
import ir.component.generaldao.ReflectionUtil;
import org.springframework.lang.Nullable;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 * @author Mohammad Yasin Kaji
 */
public abstract class AbstractDao<T> {

    @PersistenceContext(name = "securityPersistentContext")
    protected EntityManager entityManager;
    @Resource
    protected TransactionTemplate transactionTemplate;

    private Class<T> persistentClass;

    public AbstractDao() {
        persistentClass = (Class<T>) ReflectionUtil.getTypeArguments(AbstractDao.class, this.getClass()).get(0);
    }

    public void persist(T entity) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                entityManager.persist(entity);
            }
        });
    }

    public void merge(T entity) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                entityManager.merge(entity);
            }
        });
    }

    public T find(Serializable id) {
        return transactionTemplate.execute(new TransactionCallback<T>() {
            @Nullable
            @Override
            public T doInTransaction(TransactionStatus transactionStatus) {
                T t = entityManager.find(persistentClass, id);
                return t;
            }
        });
    }
}
