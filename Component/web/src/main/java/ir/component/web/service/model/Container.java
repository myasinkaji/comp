package ir.component.web.service.model;

import ir.magfa.sdk.model.ProcessDefinitionProxy;

import java.util.List;

/**
 * @author Mohammad Yasin Kaji
 */
public class Container {
    private List<ProcessDefinitionProxy> processDefinitionList;

    public Container(List<ProcessDefinitionProxy> processDefinitionList) {
        this.processDefinitionList = processDefinitionList;
    }

    public List<ProcessDefinitionProxy> getProcessDefinitionList() {
        return processDefinitionList;
    }

    public void setProcessDefinitionList(List<ProcessDefinitionProxy> processDefinitionList) {
        this.processDefinitionList = processDefinitionList;
    }
}
