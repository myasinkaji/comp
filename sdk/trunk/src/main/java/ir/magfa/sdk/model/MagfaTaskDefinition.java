package ir.magfa.sdk.model;

import java.util.Map;

/**
 * @author Mohammad Yasin Kaji
 */
public class MagfaTaskDefinition {

    private String name;
    private Integer priority;
    private String comment;
    private String createdBy;
    private boolean skippable;

    private String[] associatedEntities;
    private Map<String, String> taskInputMappings;
    private Map<String, String> taskOutputMappings;


    public MagfaTaskDefinition() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isSkippable() {
        return skippable;
    }

    public void setSkippable(boolean skippable) {
        this.skippable = skippable;
    }

    public String[] getAssociatedEntities() {
        return associatedEntities;
    }

    public void setAssociatedEntities(String[] associatedEntities) {
        this.associatedEntities = associatedEntities;
    }

    public Map<String, String> getTaskInputMappings() {
        return taskInputMappings;
    }

    public void setTaskInputMappings(Map<String, String> taskInputMappings) {
        this.taskInputMappings = taskInputMappings;
    }

    public Map<String, String> getTaskOutputMappings() {
        return taskOutputMappings;
    }

    public void setTaskOutputMappings(Map<String, String> taskOutputMappings) {
        this.taskOutputMappings = taskOutputMappings;
    }
}
