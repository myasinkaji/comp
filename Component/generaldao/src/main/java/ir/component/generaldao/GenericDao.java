package ir.component.generaldao;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * @author Mohammad Yasin Kaji
 */
public abstract class GenericDao<T, ID extends Serializable> implements JpaGenericDao<T, ID> {

    private Class persistentClass;

    public GenericDao() {
        persistentClass = ReflectionUtil.getTypeArguments(GenericDao.class, this.getClass()).get(0);
    }

    public T find(ID id) {
        return (T) getEm().find(persistentClass, id);
    }

    public void persist(T entity) {
        getEm().persist(entity);
    }

    public abstract EntityManager getEm();

}
