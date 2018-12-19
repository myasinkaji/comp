package ir.magfa.sdk.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Mostafa on 5/26/2018.
 */

@XmlRootElement
public class RequestDTO implements Serializable{

	
    public RequestDTO() {}

    private String employee;
    private String reason;
    private String performance;
    private String portletName;
    private String documentId;

    public RequestDTO(String employee, String reason, String performance, String portletName, String documentId) {
        this.employee = employee;
        this.reason = reason;
        this.performance = performance;
        this.portletName = portletName;
        this.documentId = documentId;
    }

    public RequestDTO(String employee, String reason, String performance) {
        this.employee = employee;
        this.reason = reason;
        this.performance = performance;
    }

    public RequestDTO(String employee, String reason) {
        this.employee = employee;
        this.reason = reason;
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

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestDTO)) return false;

        RequestDTO that = (RequestDTO) o;

        if (!getEmployee().equals(that.getEmployee())) return false;
        if (!getReason().equals(that.getReason())) return false;
        if (!getPerformance().equals(that.getPerformance())) return false;
        if (!getPortletName().equals(that.getPortletName())) return false;
        return getDocumentId().equals(that.getDocumentId());
    }

    @Override
    public int hashCode() {
        int result = getEmployee().hashCode();
        result = 31 * result + getReason().hashCode();
        result = 31 * result + getPerformance().hashCode();
        result = 31 * result + getPortletName().hashCode();
        result = 31 * result + getDocumentId().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RequestDTO{" +
                "employee='" + employee + '\'' +
                ", reason='" + reason + '\'' +
                ", performance='" + performance + '\'' +
                ", portletName='" + portletName + '\'' +
                ", documentId='" + documentId + '\'' +
                '}';
    }
}
