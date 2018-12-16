package ir.magfa.sdk.dto;

import java.util.Date;

/**
 * Data model of Permission that is registered by student
 *
 * @author Mohammad Yasin Kaji
 *
 */
public class PermRequest {
    private long id;
    private String subject;
    private String description;
    private Date permissionFrom;
    private Date permissionTo;

    private String student;

    public PermRequest() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
