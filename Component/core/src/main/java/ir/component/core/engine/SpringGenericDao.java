package ir.component.core.engine;

import ir.component.generaldao.GenericDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 * @author Mohammad Yasin Kaji
 */
public abstract class SpringGenericDao<T, ID extends Serializable> extends GenericDao<T, ID>{

    @PersistenceContext(name = "securityPersistentContext")
    private EntityManager entityManager;
    
    public EntityManager getEm() {
        return entityManager;
    }

}
