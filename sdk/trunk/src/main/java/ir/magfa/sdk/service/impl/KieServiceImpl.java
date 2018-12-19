package ir.magfa.sdk.service.impl;

import ir.magfa.sdk.model.*;
import ir.magfa.sdk.service.SDKUtils;
import ir.magfa.sdk.service.TaskStatus;
import org.apache.commons.lang3.Validate;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.KieServerInfo;
import org.kie.server.api.model.KieServerStateInfo;
import org.kie.server.api.model.definition.*;
import org.kie.server.api.model.instance.NodeInstance;
import org.kie.server.api.model.instance.TaskInstance;
import org.kie.server.api.model.instance.TaskSummary;
import org.kie.server.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Mohammad Yasin Kaji
 */
public class KieServiceImpl implements ir.magfa.sdk.service.KieService {


    private static final KieServiceImpl INSTANCE = new KieServiceImpl();
    public static final String SERVICE_URL = "http://172.16.9.50:8230/kie-server/services/rest/server";

    private KieServicesConfiguration configuration;

    private KieServicesClient kieServicesClient;

    private ProcessServicesClient processServicesClient;
    private QueryServicesClient queryServicesClient;
    private UserTaskServicesClient userTaskServicesClient;


    private String TASK_QUERY_NAME = "readyTasksFilterByActualOwner";

    private ThreadLocal<Context> contextThreadLocal = new ThreadLocal<>();
    private Context context = new Context();

    private KieServiceImpl() {
    }

    private KieServiceImpl(String username, String password) {
        init(username, password);
    }

    public static KieServiceImpl INSTANCE() {
        return INSTANCE;
    }

    private void init(String username, String password) {
        kieServicesClient = initConfiguration(SERVICE_URL, username, password);

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
    public void aware(User user, String containerId, String processId, Long processInstanceId, Long taskInstanceId) {
        this.contextThreadLocal.set(new Context(user, containerId, processId, processInstanceId, taskInstanceId));
        this.context = new Context(user, containerId, processId, processInstanceId, taskInstanceId);
    }

    @Override
    public void setContext(Context context) {
        this.contextThreadLocal.set(context);
        this.context = context;
    }

    public Context getContext() {
        return context;
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
    public void setProcessVariablesAsJson(String containerId, Long processInstanceId, Object value) {
        Map<String, Object> params = SDKUtils.json(value);
        processServicesClient.setProcessVariables(containerId, processInstanceId, params);
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
    public List<String> allContainers(User user) {
        if (!initiated(user))
            init(user.getUsername(), user.getPassword());

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
    public List<MagfaProcessDefinition> getProcessDefinition(User user) {
        if (!initiated(user))
            init(user.getUsername(), user.getPassword());
        //Todo This must be refactored
        /*KieContainerResourceList containers = kieServicesClient.listContainers().getResult();
        String containerId = "";
        String processId = "";
        processServicesClient.getProcessDefinition(containerId, processId);
        */

        List<ProcessDefinition> processDefinitions = queryServicesClient.findProcesses(0, 10);
        List<MagfaProcessDefinition> proxies = new ArrayList<>();

        for (ProcessDefinition definition : processDefinitions)
            proxies.add(proxy(definition));

        return proxies;
    }

    private boolean initiated(User user) {
        return kieServicesClient != null &&
                configuration.getUserName().equals(user.getUsername()) &&
                configuration.getPassword().equals(user.getPassword());
    }

    private MagfaProcessDefinition proxy(ProcessDefinition processDefinition) {
        Validate.notNull(processDefinition, "ProcessDefinition must not be null!");

        MagfaProcessDefinition proxy = new MagfaProcessDefinition();
        proxy.setId(processDefinition.getId());
        proxy.setContainerId(processDefinition.getContainerId());
        proxy.setName(processDefinition.getName());
        proxy.setPackageName(processDefinition.getPackageName());
        proxy.setVersion(processDefinition.getVersion());

        return proxy;
    }

    private MagfaTaskInstance proxy(TaskSummary task) {
        Validate.notNull(task, "TaskInstance must not be null!");

        MagfaTaskInstance proxy = new MagfaTaskInstance();

        proxy.setId(task.getId());
        proxy.setContainerId(task.getContainerId());
        proxy.setName(task.getName());
        proxy.setCreatedBy(task.getCreatedBy());
        proxy.setCreatedOn(task.getCreatedOn());
        proxy.setActivationTime(task.getActivationTime());
        proxy.setActualOwner(task.getActualOwner());
        proxy.setDescription(task.getDescription());
        proxy.setSubject(task.getSubject());
        proxy.setStatus(task.getStatus());
        proxy.setProcessInstanceId(task.getProcessInstanceId());
        proxy.setProcessId(task.getProcessId());
        proxy.setPriority(task.getPriority());
        proxy.setParentId(task.getParentId());

        return proxy;
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
    public List<MagfaProcessDefinition> processDefinitions(String containerId, User user) {
        if (!initiated(user))
            init(user.getUsername(), user.getPassword());

        List<ProcessDefinition> processDefinitions = queryServicesClient.findProcessesByContainerId(containerId, 0, 100);
        List<MagfaProcessDefinition> proxies = new ArrayList<>();

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
    public MagfaTaskInstance startProcess(String containerId, String processId, Map<String, Object> variables) throws IOException {
        return null;
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
    public <T> String[] startProcess(String containerId, String processId, T variable) {
        Map<String, Object> variables = SDKUtils.json(variable);
        Long processInstanceId = processServicesClient.startProcess(containerId, processId, variables);
        System.out.println("Started a task from processId: " + processInstanceId + " is started!");

        List<String> nus = nextUsersOf(context.getProcessInstanceId());
        if (nus == null)
            return null;

        return nus.toArray(new String[nus.size()]);
    }

    @Override
    public <T> String[] startProcess(T variables) throws IOException {
        Map<String, Object> params = SDKUtils.json(variables);
        Long processInstanceId = processServicesClient.startProcess(context.getContainerId(), context.getProcessId(), params);
        System.out.println("Started a task from processId: " + processInstanceId + " is started!");

        List<String> nus = nextUsersOf(processInstanceId);
        if (nus == null)
            return null;

        return nus.toArray(new String[nus.size()]);
    }

    @Override
    public <T> String[] closeTask(String containerId, String taskInstanceId, User user, T variables) {
        Map<String, Object> params = SDKUtils.json(variables);
        Long tid = Long.parseLong(taskInstanceId);
        userTaskServicesClient.startTask(containerId, tid, user.getUsername());
        userTaskServicesClient.completeTask(containerId, tid, user.getUsername(), params);

        List<String> nus = nextUsersOf(context.getProcessInstanceId());
        if (nus == null)
            return null;

        return nus.toArray(new String[nus.size()]);
    }

    @Override
    public <T> String[] closeTask(T variables) {
        Map<String, Object> params = SDKUtils.json(variables);
        Long tid = context.getTaskInstanceId();
        userTaskServicesClient.startTask(context.getContainerId(), tid, context.getUser().getUsername());
        userTaskServicesClient.completeTask(context.getContainerId(), tid, context.getUser().getUsername(), params);

        List<String> nus = nextUsersOf(context.getProcessInstanceId());
        if (nus == null)
            return null;

        return nus.toArray(new String[nus.size()]);
    }

    public List<String> nextUsersOf(Long processInstanceId) {
        List<String> nus = null;

        List<String> status = new ArrayList();
        status.add(TaskStatus.RESERVED.toString());
        List<TaskSummary> nextTasks = userTaskServicesClient.findTasksByStatusByProcessInstanceId(processInstanceId, status, 0, 1000);
        if (nextTasks != null) {
            nus = new ArrayList<>();
            for (TaskSummary nextTask: nextTasks) {
                if (nextTask != null)
                    nus.add(nextTask.getActualOwner());
            }
        }

        return nus;
    }

    private QueryDefinition userTaskQueryDefinition(String username) {
        String q = "select t.id, t.formname, t.name, t.createdon, t.createdby_id, t.deploymentid, t.processid, t.processinstanceid, t.status, t.actualowner_id from task t where t.actualowner_id = '" + username + "'  and t.status != 'Completed' order by t.id desc";
        String readyTasksFilterByActualOwner = "readyTasksFilterByActualOwner";
        QueryDefinition query = new QueryDefinition();
        query.setName(readyTasksFilterByActualOwner);
        query.setSource("java:jboss/datasources/ExampleDS");
        query.setExpression(q);
        query.setTarget("TASK");

        return query;
    }


    @Override
    public List<MagfaTaskInstance> assignedTasks(User user, Integer page, Integer pageSize) {
        if (!initiated(user))
            init(user.getUsername(), user.getPassword());

        List<MagfaTaskInstance> list = new ArrayList<>();
        List<TaskSummary> taskSummaryList = userTaskServicesClient.findTasksAssignedAsPotentialOwner(user.getUsername(), page, pageSize);
        if (taskSummaryList != null)
            for (TaskSummary instance : taskSummaryList)
                if (instance != null)
                    list.add(proxy(instance));

        return list;
    }

        @Override
    public List<TaskInstance> openTasks(User user, Integer page, Integer pageSize) {
        if (!initiated(user))
            init(user.getUsername(), user.getPassword());

        List<TaskInstance> list = new ArrayList<>();

        try {
            queryServicesClient.unregisterQuery(TASK_QUERY_NAME);

        } catch (Exception e) {

        }

        queryServicesClient.registerQuery(userTaskQueryDefinition(user.getUsername()));


        List tasks = queryServicesClient.query(TASK_QUERY_NAME, QueryServicesClient.QUERY_MAP_RAW, page, pageSize, void.class);
        if (tasks.size() > 0) {
            List<ArrayList> tasksInf = tasks;
            for (ArrayList taskInformations : tasksInf) {
                TaskInstance instance = map(taskInformations);
                if (instance != null)
                    list.add(instance);
                else
                    throw new RuntimeException("An Exception was occurred when mapping task information.");
            }
        }
        return list;
    }

    private TaskInstance map(ArrayList taskInformations) {
        if (taskInformations != null && taskInformations.size() == 10) {
            TaskInstance instance = new TaskInstance();

            instance.setId(((Double)taskInformations.get(0)).longValue());
            instance.setFormName((String) taskInformations.get(1));
            instance.setName((String) taskInformations.get(2));
            instance.setCreatedOn((Date) taskInformations.get(3));
            instance.setCreatedBy((String) taskInformations.get(4));
            instance.setContainerId((String) taskInformations.get(5));
            instance.setProcessId((String) taskInformations.get(6));
            instance.setProcessInstanceId(((Double)taskInformations.get(7)).longValue());
            instance.setStatus((String) taskInformations.get(8));
            instance.setActualOwner((String) taskInformations.get(9));
            return instance;
        }
        return null;
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
