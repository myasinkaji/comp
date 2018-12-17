package ir.magfa.sdk.model;

/**
 * @author Mohammad Yasin Kaji
 */
public class Context {

    private User user;
    private String containerId;
    private String processId;
    private Long processInstanceId;
    private Long taskInstanceId;

    public Context() {
    }

    public Context(User user, String containerId, String processId, Long processInstanceId, Long taskInstanceId) {
        this.user = user;
        this.containerId = containerId;
        this.processId = processId;
        this.processInstanceId = processInstanceId;
        this.taskInstanceId = taskInstanceId;
    }

    public Context(User user, String containerId, String processId) {
        this.user = user;
        this.containerId = containerId;
        this.processId = processId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Long getTaskInstanceId() {
        return taskInstanceId;
    }

    public void setTaskInstanceId(Long taskInstanceId) {
        this.taskInstanceId = taskInstanceId;
    }
}
