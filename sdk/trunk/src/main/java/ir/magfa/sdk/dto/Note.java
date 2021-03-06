package ir.magfa.sdk.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mostafa on 9/4/2018.
 */
@XmlRootElement()
public class Note implements Serializable{
    private Long id;
    private Long creatorUser;
    private Date createDate;
    private String createDateJalali;
    private Long modifierUser;
    private Date modifyDate;
    private String modifyDateJalali;
    private String employee;
    private String reason;
    private String performance;
    private String portletName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(Long creatorUser) {
        this.creatorUser = creatorUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateDateJalali() {
        return createDateJalali;
    }

    public void setCreateDateJalali(String createDateJalali) {
        this.createDateJalali = createDateJalali;
    }

    public Long getModifierUser() {
        return modifierUser;
    }

    public void setModifierUser(Long modifierUser) {
        this.modifierUser = modifierUser;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyDateJalali() {
        return modifyDateJalali;
    }

    public void setModifyDateJalali(String modifyDateJalali) {
        this.modifyDateJalali = modifyDateJalali;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getPortletName() {
        return portletName;
    }

    public void setPortletName(String portletName) {
        this.portletName = portletName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;

        Note note = (Note) o;

        if (getId() != null ? !getId().equals(note.getId()) : note.getId() != null) return false;
        if (getCreatorUser() != null ? !getCreatorUser().equals(note.getCreatorUser()) : note.getCreatorUser() != null)
            return false;
        if (getCreateDate() != null ? !getCreateDate().equals(note.getCreateDate()) : note.getCreateDate() != null)
            return false;
        if (getCreateDateJalali() != null ? !getCreateDateJalali().equals(note.getCreateDateJalali()) : note.getCreateDateJalali() != null)
            return false;
        if (getModifierUser() != null ? !getModifierUser().equals(note.getModifierUser()) : note.getModifierUser() != null)
            return false;
        if (getModifyDate() != null ? !getModifyDate().equals(note.getModifyDate()) : note.getModifyDate() != null)
            return false;
        if (getModifyDateJalali() != null ? !getModifyDateJalali().equals(note.getModifyDateJalali()) : note.getModifyDateJalali() != null)
            return false;
        if (getEmployee() != null ? !getEmployee().equals(note.getEmployee()) : note.getEmployee() != null)
            return false;
        if (getReason() != null ? !getReason().equals(note.getReason()) : note.getReason() != null) return false;
        if (getPerformance() != null ? !getPerformance().equals(note.getPerformance()) : note.getPerformance() != null)
            return false;
        return getPortletName() != null ? getPortletName().equals(note.getPortletName()) : note.getPortletName() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getCreatorUser() != null ? getCreatorUser().hashCode() : 0);
        result = 31 * result + (getCreateDate() != null ? getCreateDate().hashCode() : 0);
        result = 31 * result + (getCreateDateJalali() != null ? getCreateDateJalali().hashCode() : 0);
        result = 31 * result + (getModifierUser() != null ? getModifierUser().hashCode() : 0);
        result = 31 * result + (getModifyDate() != null ? getModifyDate().hashCode() : 0);
        result = 31 * result + (getModifyDateJalali() != null ? getModifyDateJalali().hashCode() : 0);
        result = 31 * result + (getEmployee() != null ? getEmployee().hashCode() : 0);
        result = 31 * result + (getReason() != null ? getReason().hashCode() : 0);
        result = 31 * result + (getPerformance() != null ? getPerformance().hashCode() : 0);
        result = 31 * result + (getPortletName() != null ? getPortletName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", creatorUser=" + creatorUser +
                ", createDate=" + createDate +
                ", createDateJalali='" + createDateJalali + '\'' +
                ", modifierUser=" + modifierUser +
                ", modifyDate=" + modifyDate +
                ", modifyDateJalali='" + modifyDateJalali + '\'' +
                ", employee='" + employee + '\'' +
                ", reason='" + reason + '\'' +
                ", performance='" + performance + '\'' +
                ", portletName='" + portletName + '\'' +
                '}';
    }
}
