package ir.component.core.dao.model;

import javax.persistence.Entity;

/**
 * @author Zahra Afsharinia
 */

@Entity
public class Guild extends BaseEntityInfo<Integer> {

    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


}
