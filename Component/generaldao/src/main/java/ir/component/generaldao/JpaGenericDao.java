package ir.component.generaldao;

import java.io.Serializable;

/**
 * @author Mohammad Yasin Kaji
 */
public interface JpaGenericDao<T, ID extends Serializable> {

    T find(ID id);

    void persist(T entity);
}
