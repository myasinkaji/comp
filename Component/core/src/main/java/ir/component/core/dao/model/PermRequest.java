package ir.component.core.dao.model;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Data model of Permission that is registered by student
 *
 * @author Mohammad Yasin Kaji
 *
 */
@Entity
public class PermRequest extends BaseEntityInfo<Long>{

    private String subject;
    private String description;
    private Date permissionFrom;
    private Date permissionTo;

    private String student;

    public PermRequest() {
    }

    @Override
    public String getTitle() {
        return getClass().getSimpleName() + "#" + subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getPermissionFrom() {
        return permissionFrom;
    }

    public void setPermissionFrom(Date permissionFrom) {
        this.permissionFrom = permissionFrom;
    }

    public Date getPermissionTo() {
        return permissionTo;
    }

    public void setPermissionTo(Date permissionTo) {
        this.permissionTo = permissionTo;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }
}
