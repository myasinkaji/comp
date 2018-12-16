package ir.magfa.sdk.dto;

import java.io.Serializable;

/**
 * Created by Mostafa on 9/4/2018.
 */
public  class KartableTaskDTO implements Serializable{

    public KartableTaskDTO() {}
    private String processId;
    private String processInstanceId;
    private String taskId;
    private String taskName;
    private String actualOwnerId;
    private String taskInitiatorId;
    private String status;
    private String documentId;
    private String portletName;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getActualOwnerId() {
        return actualOwnerId;
    }

    public void setActualOwnerId(String actualOwnerId) {
        this.actualOwnerId = actualOwnerId;
    }

    public String getTaskInitiatorId() {
        return taskInitiatorId;
    }

    public void setTaskInitiatorId(String taskInitiatorId) {
        this.taskInitiatorId = taskInitiatorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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
        if (!(o instanceof KartableTaskDTO)) return false;

        KartableTaskDTO that = (KartableTaskDTO) o;

        if (getProcessId() != null ? !getProcessId().equals(that.getProcessId()) : that.getProcessId() != null)
            return false;
        if (getProcessInstanceId() != null ? !getProcessInstanceId().equals(that.getProcessInstanceId()) : that.getProcessInstanceId() != null)
            return false;
        if (getTaskId() != null ? !getTaskId().equals(that.getTaskId()) : that.getTaskId() != null) return false;
        if (getTaskName() != null ? !getTaskName().equals(that.getTaskName()) : that.getTaskName() != null)
            return false;
        if (getActualOwnerId() != null ? !getActualOwnerId().equals(that.getActualOwnerId()) : that.getActualOwnerId() != null)
            return false;
        if (getTaskInitiatorId() != null ? !getTaskInitiatorId().equals(that.getTaskInitiatorId()) : that.getTaskInitiatorId() != null)
            return false;
        if (getStatus() != null ? !getStatus().equals(that.getStatus()) : that.getStatus() != null) return false;
        if (getDocumentId() != null ? !getDocumentId().equals(that.getDocumentId()) : that.getDocumentId() != null)
            return false;
        return getPortletName() != null ? getPortletName().equals(that.getPortletName()) : that.getPortletName() == null;
    }

    @Override
    public int hashCode() {
        int result = getProcessId() != null ? getProcessId().hashCode() : 0;
        result = 31 * result + (getProcessInstanceId() != null ? getProcessInstanceId().hashCode() : 0);
        result = 31 * result + (getTaskId() != null ? getTaskId().hashCode() : 0);
        result = 31 * result + (getTaskName() != null ? getTaskName().hashCode() : 0);
        result = 31 * result + (getActualOwnerId() != null ? getActualOwnerId().hashCode() : 0);
        result = 31 * result + (getTaskInitiatorId() != null ? getTaskInitiatorId().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getDocumentId() != null ? getDocumentId().hashCode() : 0);
        result = 31 * result + (getPortletName() != null ? getPortletName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "KartableTaskDTO{" +
                "processId='" + processId + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", actualOwnerId='" + actualOwnerId + '\'' +
                ", taskInitiatorId='" + taskInitiatorId + '\'' +
                ", status='" + status + '\'' +
                ", documentId='" + documentId + '\'' +
                ", portletName='" + portletName + '\'' +
                '}';
    }
}
