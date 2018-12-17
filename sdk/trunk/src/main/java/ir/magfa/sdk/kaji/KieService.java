package ir.magfa.sdk.kaji;

import ir.magfa.sdk.model.*;
import org.kie.server.api.model.KieServerInfo;
import org.kie.server.api.model.KieServerStateInfo;
import org.kie.server.api.model.definition.*;
import org.kie.server.api.model.instance.NodeInstance;
import org.kie.server.api.model.instance.TaskInstance;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Mohammad Yasin Kaji
 */
public interface KieService {


    void setContext(Context context);

    void aware(User user, String containerId, String processId, Long processInstanceId, Long taskInstanceId);

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

    List<String> allContainers(User user);

    List<ProcessDefinitionProxy> getProcessDefinition(User user);


    List<MagfaTaskDefinition> taskDefinitions(String containerId, String processId) throws IOException;

    MagfaTaskDefinition startTaskDefinitions(String containerId, String processId) throws IOException;

    List<ProcessDefinitionProxy> processDefinitions(String containerId, User user);

    void startProcess(String processDefinitionId);

    void startProcess(String containerId, String processId) throws IOException;

    TaskInstanceProxy startProcess(String containerId, String processId, Map<String, Object> variables) throws IOException;

    <T> String startProcess(String containerId, String processId, T variables) throws IOException;

    <T> String startProcess(T variables) throws IOException;

    <T> String closeTask(String containerId, String taskInstanceId, User user, T variables);

    <T> String closeTask(T variables);

    List<TaskInstanceProxy> assignedTasks(User user, Integer page, Integer pageSize);

    List<TaskInstance> openTasks(User user, Integer page, Integer pageSize);
//    List<KartableTaskDTO> stateMachine(String userName, Serializable serializable);

}
