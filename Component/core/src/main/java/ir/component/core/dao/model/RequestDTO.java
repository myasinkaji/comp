package ir.component.core.dao.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Mohammad Yasin Kaji
 */
@Entity
@Table
public class RequestDTO extends BaseEntityInfo<Long>{


    private String title;
    private String employee;
    private String reason;
    private String performance;
    private String portletName;
    private String documentId;

    public RequestDTO() {
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "RequestDTO{" +
                "documentId='" + documentId + '\'' +
                ", employee='" + employee + '\'' +
                ", performance='" + performance + '\'' +
                ", portletName='" + portletName + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
