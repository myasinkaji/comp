package ir.component.core.dao.model;

import javax.persistence.Entity;

/**
 * @author Mohammad Yasin Kaji
 */
@Entity
public class Role extends BaseEntityInfo<Short> {

    private String title;
    private String permissions;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

}
