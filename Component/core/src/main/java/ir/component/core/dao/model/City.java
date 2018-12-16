package ir.component.core.dao.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Zahra Afsharinia
 */
@Entity
public class City extends BaseEntityInfo<Integer> {

    private String name;
    @ManyToOne
    private Province province;

    public City() {

    }

    public City(String name, Province province) {
        this.name = name;
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    @Override
    public String getTitle() {
        return name;
    }
}
