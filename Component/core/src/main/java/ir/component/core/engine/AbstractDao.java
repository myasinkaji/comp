package ir.component.core.engine;

import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Mohammad Yasin Kaji
 */
public abstract class AbstractDao {

    @PersistenceContext(name = "securityPersistentContext")
    protected EntityManager entityManager;

    @Resource
    protected TransactionTemplate transactionTemplate;
}
