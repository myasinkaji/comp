package ir.component.core.dao.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * @author Mohammad Yasin Kaji
 */
@MappedSuperclass
public abstract class BaseEntityInfo<ID> extends BaseEntity<ID> implements HasTitle {

    private Boolean active;

    private Integer creator;
    private Date created;

    private Integer modifier;
    private Date modified;


    public BaseEntityInfo() {
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getModifier() {
        return modifier;
    }

    public void setModifier(Integer modifier) {
        this.modifier = modifier;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return String.format("%s[id:%s, title:%s]", this.getClass().getSimpleName(), getId(), getTitle());
    }

    @Override
    public String entityLog() {
        return String.format("%s[active=%s, title=%s]", getClass().getSimpleName(), active, StringUtils.left(getTitle(), 150));
    }


}
