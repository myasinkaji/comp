package ir.component.web.service.model;

import ir.magfa.sdk.model.MagfaProcessDefinition;

import java.util.List;

/**
 * @author Mohammad Yasin Kaji
 */
public class Container {
    private List<MagfaProcessDefinition> processDefinitionList;

    public Container(List<MagfaProcessDefinition> processDefinitionList) {
        this.processDefinitionList = processDefinitionList;
    }

    public List<MagfaProcessDefinition> getProcessDefinitionList() {
        return processDefinitionList;
    }

    public void setProcessDefinitionList(List<MagfaProcessDefinition> processDefinitionList) {
        this.processDefinitionList = processDefinitionList;
    }
}
