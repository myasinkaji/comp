package ir.magfa.sdk.kaji;

import ir.magfa.sdk.model.MagfaTaskDefinition;
import ir.magfa.sdk.model.ProcessDefinitionProxy;
import ir.magfa.sdk.model.TaskInstanceProxy;
import ir.magfa.sdk.model.User;
import ir.magfa.sdk.utils.KieClientConstant;
import org.apache.commons.lang3.Validate;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.KieServerInfo;
import org.kie.server.api.model.KieServerStateInfo;
import org.kie.server.api.model.definition.*;
import org.kie.server.api.model.instance.NodeInstance;
import org.kie.server.api.model.instance.TaskInstance;
import org.kie.server.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mohammad Yasin Kaji
 */
public class SimpleKieService implements KieService {


    private static final SimpleKieService INSTANCE = new SimpleKieService();

    private KieServicesConfiguration configuration;

    private KieServicesClient kieServicesClient;

    private ProcessServicesClient processServicesClient;
    private QueryServicesClient queryServicesClient;
    private UserTaskServicesClient userTaskServicesClient;


    //    private String TASK_QUERY_NAME = "magfaTaskQuery";
    private String TASK_QUERY_NAME = "readyTasksFilterByActualOwner";

    private SimpleKieService() {
    }

    private SimpleKieService(String username, String password) {
        init(username, password);
    }

    public static SimpleKieService INSTANCE() {
        return INSTANCE;
    }

    private void init(String username, String password) {
        kieServicesClient = initConfiguration(KieClientConstant.SERVICE_URL, username, password);

        queryServicesClient = kieServicesClient.getServicesClient(QueryServicesClient.class);
        processServicesClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
        userTaskServicesClient = kieServicesClient.getServicesClient(UserTaskServicesClient.class);
//        queryServicesClient.unregisterQuery(TASK_QUERY_NAME);
//        registerQueries();
    }

    private void registerQueries() {
       /* QueryDefinition query = new QueryDefinition();
        query.setName(TASK_QUERY_NAME);
        query.setSource("java:jboss/datasources/ExampleDS");
        query.setExpression("select * from task t ");
        query.setTarget("TASK");
//queryClient.unregisterQuery(magfaQueryName);
        queryServicesClient.registerQuery(query);
*/

/*
        String q = "select * from task t where t.actualowner_id = 'hadi'";
        String readyTasksFilterByActualOwner = "readyTasksFilterByActualOwner";
        query = new QueryDefinition();
        query.setName(readyTasksFilterByActualOwner);
        query.setSource("java:jboss/datasources/ExampleDS");
        query.setExpression(q);
        query.setTarget("TASK");
        queryServicesClient.unregisterQuery(readyTasksFilterByActualOwner);
        queryServicesClient.registerQuery(query);
*/


    }


    @Override
    public VariablesDefinition getProcessVariableDefinitions(String containerId, String processId) {
        return processServicesClient.getProcessVariableDefinitions(containerId, processId);
    }

    @Override
    public Map<String, Object> getProcessInstanceVariables(String containerId, Long processInstanceId) {
        return processServicesClient.getProcessInstanceVariables(containerId, processInstanceId);
    }

    @Override
    public void setProcessVariable(String containerId, Long processInstanceId, String variableId, Object value) {
        processServicesClient.setProcessVariable(containerId, processInstanceId, variableId, value);
    }

    @Override
    public void setProcessVariables(String containerId, Long processInstanceId, Map<String, Object> variables) {
        processServicesClient.setProcessVariables(containerId, processInstanceId, variables);
    }

    @Override
    public TaskOutputsDefinition taskOutputsDefinition(String containerId, String processId, String taskName) {
        return processServicesClient.getUserTaskOutputDefinitions(containerId, processId, taskName);
    }

    @Override
    public TaskInputsDefinition taskInputsDefinition(String containerId, String processId, String taskName) {
        return processServicesClient.getUserTaskInputDefinitions(containerId, processId, taskName);
    }

    @Override
    public UserTaskDefinitionList userTaskDefinitionList(String containerId, String processId) {
        return processServicesClient.getUserTaskDefinitions(containerId, processId);
    }

    @Override
    public ProcessDefinition processDefinition(String containerId, String processId) {
        return processServicesClient.getProcessDefinition(containerId, processId);
    }

    @Override
    public KieServerStateInfo kieServerStateInfo() {
        return kieServicesClient.getServerState().getResult();
    }

    @Override
    public NodeInstance activeNode(String containerId, Long processInstanceId) {
        List<NodeInstance> nodeInstances = processServicesClient.findActiveNodeInstances(containerId, processInstanceId, 0, 100);
        if (nodeInstances != null && nodeInstances.size() > 0)
            return nodeInstances.get(0);

        return null;
    }

    @Override
    public KieServerInfo about() {
        KieServerInfo info = kieServicesClient.getServerInfo().getResult();

        return info;
    }

    @Override
    public void deployContainer(String containerId) {
        kieServicesClient.createContainer(containerId, null);
    }

    @Override
    public void disposeContainer(String containerId) {
        kieServicesClient.disposeContainer(containerId);
    }

    @Override
    public List<String> allContainers() {
        List<String> containers = new ArrayList<>();
        KieContainerResourceList containersResourceList = kieServicesClient.listContainers().getResult();
        if (containersResourceList != null) {
            for (KieContainerResource container : containersResourceList.getContainers()) {
                if (container != null) {
                    containers.add(container.getContainerId());
                }
            }
        }

        return containers;
    }

    @Override
    public List<ProcessDefinitionProxy> getProcessDefinition(User user) {
        if (!initiated(user))
            init(user.getUsername(), user.getPassword());
        //Todo This must be refactored
        /*KieContainerResourceList containers = kieServicesClient.listContainers().getResult();
        String containerId = "";
        String processId = "";
        processServicesClient.getProcessDefinition(containerId, processId);
        */

        List<ProcessDefinition> processDefinitions = queryServicesClient.findProcesses(0, 10);
        List<ProcessDefinitionProxy> proxies = new ArrayList<>();

        for (ProcessDefinition definition : processDefinitions)
            proxies.add(proxy(definition));

        return proxies;
    }

    private boolean initiated(User user) {
        return kieServicesClient != null &&
                configuration.getUserName().equals(user.getUsername()) &&
                configuration.getPassword().equals(user.getPassword());
    }

    private ProcessDefinitionProxy proxy(ProcessDefinition processDefinition) {
        Validate.notNull(processDefinition, "ProcessDefinition must not be null!");

        ProcessDefinitionProxy proxy = new ProcessDefinitionProxy();
        proxy.setId(processDefinition.getId());
        proxy.setContainerId(processDefinition.getContainerId());
        proxy.setName(processDefinition.getName());
        proxy.setPackageName(processDefinition.getPackageName());
        proxy.setVersion(processDefinition.getVersion());

        return proxy;
    }

    private TaskInstanceProxy proxy(TaskInstance taskInstance) {
        Validate.notNull(taskInstance, "TaskInstance must not be null!");

        TaskInstanceProxy proxy = new TaskInstanceProxy();

        proxy.setId(taskInstance.getId());
        proxy.setContainerId(taskInstance.getContainerId());
        proxy.setName(taskInstance.getName());
        proxy.setCreatedBy(taskInstance.getCreatedBy());
        proxy.setCreatedOn(taskInstance.getCreatedOn());
        proxy.setActivationTime(taskInstance.getActivationTime());
        proxy.setExpirationDate(taskInstance.getExpirationDate());
        proxy.setActualOwner(taskInstance.getActualOwner());
        proxy.setDescription(taskInstance.getDescription());
        proxy.setFormName(taskInstance.getFormName());
        proxy.setInputData(taskInstance.getInputData());
        proxy.setSubject(taskInstance.getSubject());
        proxy.setStatus(taskInstance.getStatus());
        proxy.setProcessInstanceId(taskInstance.getProcessInstanceId());
        proxy.setProcessId(taskInstance.getProcessId());
        proxy.setPriority(taskInstance.getPriority());
        proxy.setParentId(taskInstance.getParentId());
        proxy.setOutputData(taskInstance.getOutputData());

        return proxy;
    }

    @Override
    public void startProcess(String processDefinitionId) {
        ProcessDefinition processDefinition = queryServicesClient.findProcessesById(processDefinitionId).get(0);
        Long processInstanceId = processServicesClient.startProcess(processDefinition.getContainerId(), processDefinition.getId());
        List<NodeInstance> completedNodes = queryServicesClient.findCompletedNodeInstances(processInstanceId, 0, 10);
    }

    @Override
    public void startProcess(String containerId, String processId) throws IOException {
        Long processInstanceId = processServicesClient.startProcess(containerId, processId);
        List<NodeInstance> completedNodes = queryServicesClient.findCompletedNodeInstances(processInstanceId, 0, 10);
    }

    @Override
    public List<MagfaTaskDefinition> taskDefinitions(String containerId, String processId) throws IOException {
        UserTaskDefinitionList list = processServicesClient.getUserTaskDefinitions(containerId, processId);
        List<MagfaTaskDefinition> magfaTaskDefinitions = new ArrayList<>();
        if (list != null) {
            for (UserTaskDefinition definition : list.getItems()) {
                if (definition != null)
                    magfaTaskDefinitions.add(proxy(definition));
            }
        }
        return magfaTaskDefinitions;
    }

    @Override
    public MagfaTaskDefinition startTaskDefinitions(String containerId, String processId) throws IOException {
        UserTaskDefinitionList list = processServicesClient.getUserTaskDefinitions(containerId, processId);
        MagfaTaskDefinition startTask = null;
        if (list != null) {
            UserTaskDefinition definition = list.getItems().get(0);
            if (definition != null)
                startTask = proxy(definition);
        }
        return startTask;
    }

    @Override
    public List<ProcessDefinitionProxy> processDefinitions(String containerId, User user) {
        if (!initiated(user))
            init(user.getUsername(), user.getPassword());

        List<ProcessDefinition> processDefinitions = queryServicesClient.findProcessesByContainerId(containerId, 0, 100);
        List<ProcessDefinitionProxy> proxies = new ArrayList<>();

        for (ProcessDefinition definition : processDefinitions)
            proxies.add(proxy(definition));

        return proxies;
    }

    private MagfaTaskDefinition proxy(UserTaskDefinition definition) {
        MagfaTaskDefinition task = null;
        if (definition != null) {
            task = new MagfaTaskDefinition();
            task.setName(definition.getName());
            task.setPriority(definition.getPriority());
            task.setComment(definition.getComment());
            task.setCreatedBy(definition.getCreatedBy());
            task.setSkippable(definition.isSkippable());
            task.setAssociatedEntities(definition.getAssociatedEntities());
            task.setTaskInputMappings(definition.getTaskInputMappings());
            task.setTaskOutputMappings(definition.getTaskOutputMappings());
        }

        return task;
    }

    @Override
    public TaskInstanceProxy startProcess(String containerId, String processId, Map<String, Object> variables) throws IOException {
        return null;
    }


    @Override
    public void closeTask(String containerId, String taskInstanceId, User user) {
        userTaskServicesClient.startTask(containerId, Long.parseLong(taskInstanceId), user.getUsername());
    }

    private QueryDefinition userTaskQueryDefinition(String username) {
        String q = "select * from task t where t.actualowner_id = '" + username + "'";
        String readyTasksFilterByActualOwner = "readyTasksFilterByActualOwner";
        QueryDefinition query = new QueryDefinition();
        query.setName(readyTasksFilterByActualOwner);
        query.setSource("java:jboss/datasources/ExampleDS");
        query.setExpression(q);
        query.setTarget("TASK");
//        queryServicesClient.unregisterQuery(readyTasksFilterByActualOwner);
//        queryServicesClient.registerQuery(query);

        return query;
    }

    @Override
    public List<TaskInstanceProxy> openTasks(User user) {

        if (!initiated(user))
            init(user.getUsername(), user.getPassword());

        List<TaskInstanceProxy> list = new ArrayList<>();

        try {
            queryServicesClient.unregisterQuery(TASK_QUERY_NAME);

        } catch (Exception e) {

        }

        queryServicesClient.registerQuery(userTaskQueryDefinition(user.getUsername()));


        List<TaskInstance> tasks = queryServicesClient.query(TASK_QUERY_NAME, QueryServicesClient.QUERY_MAP_TASK, 0, 10, TaskInstance.class);
        if (tasks != null)
            for (TaskInstance instance : tasks)
                if (instance != null)
                    list.add(proxy(instance));

        return list;
    }

    private void setProcessServicesClient(ProcessServicesClient processServicesClient) {
        this.processServicesClient = processServicesClient;
    }

    private void setQueryServicesClient(QueryServicesClient queryServicesClient) {
        this.queryServicesClient = queryServicesClient;
    }

    private void setUserTaskServicesClient(UserTaskServicesClient userTaskServicesClient) {
        this.userTaskServicesClient = userTaskServicesClient;
    }

    private KieServicesClient initConfiguration(String serviceURL, String userName, String password) {
        configuration = getKieServicesConfiguration(serviceURL, userName, password);

        configuration.setMarshallingFormat(MarshallingFormat.JAXB);
        KieServicesClient kieServicesClient = getKieServicesClient(configuration);
        return kieServicesClient;

    }

    private KieServicesConfiguration getKieServicesConfiguration(String serviceURL, String userName, String password) {
        return KieServicesFactory.newRestConfiguration(serviceURL, userName, password);
    }

    private KieServicesClient getKieServicesClient(KieServicesConfiguration configuration) {
        return KieServicesFactory.newKieServicesClient(configuration);
    }
}
