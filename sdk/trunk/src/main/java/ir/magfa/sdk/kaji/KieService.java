package ir.magfa.sdk.kaji;

import ir.magfa.sdk.model.MagfaTaskDefinition;
import ir.magfa.sdk.model.ProcessDefinitionProxy;
import ir.magfa.sdk.model.TaskInstanceProxy;
import ir.magfa.sdk.model.User;
import org.kie.server.api.model.KieServerInfo;
import org.kie.server.api.model.KieServerStateInfo;
import org.kie.server.api.model.definition.*;
import org.kie.server.api.model.instance.NodeInstance;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Mohammad Yasin Kaji
 */
public interface KieService {



    VariablesDefinition getProcessVariableDefinitions(String containerId, String processId);

    Map<String, Object> getProcessInstanceVariables(String containerId, Long processInstanceId);

    void setProcessVariable(String containerId, Long processInstanceId, String variableId, Object value);

    void setProcessVariables(String containerId, Long processInstanceId, Map<String, Object> variables);

    TaskOutputsDefinition taskOutputsDefinition(String containerId, String processId, String taskName);

    TaskInputsDefinition taskInputsDefinition(String containerId, String processId, String taskName);


    UserTaskDefinitionList userTaskDefinitionList(String containerId, String processId);

    ProcessDefinition processDefinition(String containerId, String processId);

    KieServerStateInfo kieServerStateInfo();

    NodeInstance activeNode(String containerId, Long processInstanceId);

    KieServerInfo about();

    void deployContainer(String containerId);

    void disposeContainer(String containerId);

    List<String> allContainers();

    List<ProcessDefinitionProxy> getProcessDefinition(User user);

    void startProcess(String processDefinitionId);

    void startProcess(String containerId, String processId) throws IOException;

    List<MagfaTaskDefinition> taskDefinitions(String containerId, String processId) throws IOException;

    MagfaTaskDefinition startTaskDefinitions(String containerId, String processId) throws IOException;

    List<ProcessDefinitionProxy> processDefinitions(String containerId, User user);

    TaskInstanceProxy startProcess(String containerId, String processId, Map<String, Object> variables) throws IOException;

    void closeTask(String containerId, String taskInstanceId, User user);

    List<TaskInstanceProxy> openTasks(User user);
//    List<KartableTaskDTO> stateMachine(String userName, Serializable serializable);

}
