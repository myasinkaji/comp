package ir.magfa.sdk.service.impl;

import ir.magfa.sdk.model.*;
import ir.magfa.sdk.service.SDKUtils;
import ir.magfa.sdk.service.TaskStatus;
import org.apache.commons.lang3.Validate;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.definition.ProcessDefinition;
import org.kie.server.api.model.definition.QueryDefinition;
import org.kie.server.api.model.definition.UserTaskDefinition;
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
public class KieService {

    public static final String SERVICE_URL = "http://172.16.9.50:8230/kie-server/services/rest/server";
    private static final String TASK_QUERY_NAME = "readyTasksFilterByActualOwner";

    private static final KieService INSTANCE = new KieService();
    private KieServicesConfiguration configuration;

    private KieServicesClient kieServicesClient;

    private ProcessServicesClient processServicesClient;
    private QueryServicesClient queryServicesClient;
    private UserTaskServicesClient userTaskServicesClient;

    private ThreadLocal<Context> contextThreadLocal = new ThreadLocal<>();
    private Context context = new Context();

    private KieService() {
    }

    private KieService(String username, String password) {
        init(username, password);
    }

    public static KieService INSTANCE() {
        return INSTANCE;
    }

    private void init(String username, String password) {
        kieServicesClient = initConfiguration(SERVICE_URL, username, password);

        queryServicesClient = kieServicesClient.getServicesClient(QueryServicesClient.class);
        processServicesClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
        userTaskServicesClient = kieServicesClient.getServicesClient(UserTaskServicesClient.class);
    }

    public static void aware(User user, String containerId, String processId, Long processInstanceId, Long taskInstanceId) {

        KieService service = INSTANCE();

        service.contextThreadLocal.set(new Context(user, containerId, processId, processInstanceId, taskInstanceId));
        service.context = new Context(user, containerId, processId, processInstanceId, taskInstanceId);
    }

    public static <T> String[] startProcess(String containerId, String processId, T variable) {

        KieService service = INSTANCE();

        Map<String, Object> variables = SDKUtils.json(variable);
        Long processInstanceId = service.processServicesClient.startProcess(containerId, processId, variables);
        System.out.println("Started a task from processId: " + processInstanceId + " is started!");

        List<String> nus = service.nextUsersOf(service.context.getProcessInstanceId());
        if (nus == null)
            return null;

        return nus.toArray(new String[nus.size()]);
    }


    public static <T> String[] startProcess(T variables) throws IOException {

        KieService service = INSTANCE();

        Map<String, Object> params = SDKUtils.json(variables);
        Long processInstanceId = service.processServicesClient.startProcess(service.context.getContainerId(), service.context.getProcessId(), params);
        System.out.println("Started a task from processId: " + processInstanceId + " is started!");

        List<String> nus = service.nextUsersOf(processInstanceId);
        if (nus == null)
            return null;

        return nus.toArray(new String[nus.size()]);
    }

    public static <T> String[] closeTask(String containerId, String taskInstanceId, User user, T variables) {

        KieService service = INSTANCE();

        Map<String, Object> params = SDKUtils.json(variables);
        Long tid = Long.parseLong(taskInstanceId);
        service.userTaskServicesClient.startTask(containerId, tid, user.getUsername());
        service.userTaskServicesClient.completeTask(containerId, tid, user.getUsername(), params);

        List<String> nus = service.nextUsersOf(service.context.getProcessInstanceId());
        if (nus == null)
            return null;

        return nus.toArray(new String[nus.size()]);
    }


    public static <T> String[] closeTask(T variables) {

        KieService service = INSTANCE();

        Map<String, Object> params = SDKUtils.json(variables);
        Long tid = service.context.getTaskInstanceId();
        service.userTaskServicesClient.startTask(service.context.getContainerId(), tid, service.context.getUser().getUsername());
        service.userTaskServicesClient.completeTask(service.context.getContainerId(), tid, service.context.getUser().getUsername(), params);

        List<String> nus = service.nextUsersOf(service.context.getProcessInstanceId());
        if (nus == null)
            return null;

        return nus.toArray(new String[nus.size()]);
    }

    public static List<String> nextUsersOf(Long processInstanceId) {
        List<String> nus = null;

        List<String> status = new ArrayList();
        status.add(TaskStatus.RESERVED.toString());
        List<TaskSummary> nextTasks = INSTANCE().userTaskServicesClient.findTasksByStatusByProcessInstanceId(processInstanceId, status, 0, 1000);
        if (nextTasks != null) {
            nus = new ArrayList<>();
            for (TaskSummary nextTask: nextTasks) {
                if (nextTask != null)
                    nus.add(nextTask.getActualOwner());
            }
        }

        return nus;
    }
    public static Map<String, Object> getProcessInstanceVariables(String containerId, Long processInstanceId) {
        return INSTANCE().processServicesClient.getProcessInstanceVariables(containerId, processInstanceId);
    }

    public static void setProcessVariable(String containerId, Long processInstanceId, String variableId, Object value) {
        INSTANCE().processServicesClient.setProcessVariable(containerId, processInstanceId, variableId, value);
    }

    public static  void setProcessVariablesAsJson(String containerId, Long processInstanceId, Object value) {
        Map<String, Object> params = SDKUtils.json(value);
        INSTANCE().processServicesClient.setProcessVariables(containerId, processInstanceId, params);
    }

    public static void setProcessVariables(String containerId, Long processInstanceId, Map<String, Object> variables) {
        INSTANCE().processServicesClient.setProcessVariables(containerId, processInstanceId, variables);
    }

    public static List<String> allContainers(User user) {

        KieService service = INSTANCE();

        if (!service.initiated(user))
            service.init(user.getUsername(), user.getPassword());

        List<String> containers = new ArrayList<>();
        KieContainerResourceList containersResourceList = service.kieServicesClient.listContainers().getResult();
        if (containersResourceList != null) {
            for (KieContainerResource container : containersResourceList.getContainers()) {
                if (container != null) {
                    containers.add(container.getContainerId());
                }
            }
        }

        return containers;
    }

    public static List<MagfaProcessDefinition> processDefinitions(String containerId, User user) {

        KieService service = INSTANCE();

        if (!service.initiated(user))
            service.init(user.getUsername(), user.getPassword());

        List<ProcessDefinition> processDefinitions = service.queryServicesClient.findProcessesByContainerId(containerId, 0, 100);
        List<MagfaProcessDefinition> proxies = new ArrayList<>();

        for (ProcessDefinition definition : processDefinitions)
            proxies.add(service.proxy(definition));

        return proxies;
    }

    public static List<TaskInstance> openTasks(User user, Integer page, Integer pageSize) {

        KieService service = INSTANCE();

        if (!service.initiated(user))
            service.init(user.getUsername(), user.getPassword());

        List<TaskInstance> list = new ArrayList<>();

        try {
            service.queryServicesClient.unregisterQuery(TASK_QUERY_NAME);

        } catch (Exception e) {

        }

        service.queryServicesClient.registerQuery(service.userTaskQueryDefinition(user.getUsername()));


        List tasks = service.queryServicesClient.query(TASK_QUERY_NAME, QueryServicesClient.QUERY_MAP_RAW, page, pageSize, void.class);
        if (tasks.size() > 0) {
            List<ArrayList> tasksInf = tasks;
            for (ArrayList taskInformations : tasksInf) {
                TaskInstance instance = service.map(taskInformations);
                if (instance != null)
                    list.add(instance);
                else
                    throw new RuntimeException("An Exception was occurred when mapping task information.");
            }
        }
        return list;
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

    private QueryDefinition userTaskQueryDefinition(String username) {
        String q = "select t.id, t.formname, t.name, t.createdon, t.createdby_id, t.deploymentid, t.processid, " +
                "t.processinstanceid, t.status, t.actualowner_id from task t where t.actualowner_id = '" +
                username + "'  and t.status != 'Completed' order by t.id desc";

        QueryDefinition query = new QueryDefinition();
        query.setName(TASK_QUERY_NAME);
        query.setSource("java:jboss/datasources/ExampleDS");
        query.setExpression(q);
        query.setTarget("TASK");

        return query;
    }

    private boolean initiated(User user) {
        return kieServicesClient != null &&
                configuration.getUserName().equals(user.getUsername()) &&
                configuration.getPassword().equals(user.getPassword());
    }

    public static Context getContext() {
        return INSTANCE().context;
    }

    public static void setContext(Context context) {
        INSTANCE().contextThreadLocal.set(context);
        INSTANCE().context = context;
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
