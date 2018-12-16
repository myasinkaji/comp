package ir.component.core.dao.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author Mohammad Yasin Kaji
 */
@MappedSuperclass
public abstract class BaseEntity<ID> implements Identifiable<ID>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ID id;

    public BaseEntity() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null)
            return false;

        Class<?> objClass = obj.getClass();
        while (objClass.getSimpleName().contains("$")) {
            objClass = objClass.getSuperclass();
        }

        Class<?> thisClass = this.getClass();
        while (thisClass.getSimpleName().contains("$")) {
            thisClass = thisClass.getSuperclass();
        }

        if (thisClass.equals(objClass)) {
            BaseEntity<ID> entity = (BaseEntity<ID>) obj;
            return entity.getId() != null && getId() != null && getId().equals(entity.getId());
        } else {
            return false;
        }
    }


    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public String entityLog() {
        return toString();
    }
}
