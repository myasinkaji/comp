package ir.component.core.dao.model;

import javax.persistence.Entity;

/**
 * @author Zahra Afsharinia
 */
@Entity
public class Province extends BaseEntityInfo<Integer> {

    private String name;

    public Province() {
    }

    public Province(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTitle() {
        return name;
    }
}
