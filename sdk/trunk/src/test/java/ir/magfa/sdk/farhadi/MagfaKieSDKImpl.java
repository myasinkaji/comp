package ir.magfa.sdk.farhadi;


import ir.magfa.sdk.dto.KartableTaskDTO;
//import ir.magfa.sdk.dto.PermRequest;
import ir.magfa.sdk.model.MagfaProcessDefinition;
import ir.magfa.sdk.model.MagfaTaskInstance;
import ir.magfa.sdk.model.User;
import org.apache.commons.lang3.Validate;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.definition.ProcessDefinition;
import org.kie.server.api.model.instance.*;
import org.kie.server.client.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Mostafa.Farhadi on 11/13/2017.
 */
public class MagfaKieSDKImpl implements MagfaKieSDK {

    private KieServicesClient kieServicesClient;
    private ProcessServicesClient processServicesClient;
    private QueryServicesClient queryServicesClient;
    private UserTaskServicesClient userTaskServicesClient;


    public MagfaKieSDKImpl() {

    }

    public MagfaKieSDKImpl(String username, String password) {
        KieServicesClient client= initConfiguration(KieClientConstant.SERVICE_URL, username, password);
        QueryServicesClient queryClient = getQueryClient(client);
        ProcessServicesClient processClient = getProcessServicesClient(client);
        UserTaskServicesClient taskClient = getUserTaskServicesClient(client);


        setQueryServicesClient(queryClient);
        setProcessServicesClient(processClient);
        setUserTaskServicesClient(taskClient);
    }



    public KieServicesClient getKieServicesClient() {
        return kieServicesClient;
    }

    public void setKieServicesClient(KieServicesClient kieServicesClient) {
        this.kieServicesClient = kieServicesClient;
    }

    public ProcessServicesClient getProcessServicesClient() {
        return processServicesClient;
    }

    public void setProcessServicesClient(ProcessServicesClient processServicesClient) {
        this.processServicesClient = processServicesClient;
    }

    public QueryServicesClient getQueryServicesClient() {
        return queryServicesClient;
    }

    public void setQueryServicesClient(QueryServicesClient queryServicesClient) {
        this.queryServicesClient = queryServicesClient;
    }

    public UserTaskServicesClient getUserTaskServicesClient() {
        return userTaskServicesClient;
    }

    public void setUserTaskServicesClient(UserTaskServicesClient userTaskServicesClient) {
        this.userTaskServicesClient = userTaskServicesClient;
    }

    @Override
    public List<MagfaProcessDefinition> getProcessDefinition(User user) {
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
    public MagfaTaskInstance startProcess(String containerId, String processId, Map<String, Object> variables) throws IOException {
        Long processInstanceId = processServicesClient.startProcess(containerId, processId, variables);
        System.out.println("\t######### Process instance id: " + processInstanceId);
        List<NodeInstance> completedNodes = queryServicesClient.findCompletedNodeInstances(processInstanceId, 0, 10);
        System.out.println("\t######### Completed nodes: " + completedNodes);
        String fileName = "C:\\prs\\prs.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(processInstanceId+"");
        writer.close();

        return null;
    }

    @Override
    public void closeTask(String containerId, String taskInstanceId, User user) {
        userTaskServicesClient.startTask(containerId, Long.parseLong(taskInstanceId), user.getUsername());
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


    public List<KartableTaskDTO> stateMachine(int state, String userName, KieServicesClient kieServicesClient,
                                              UserTaskServicesClient taskClient, QueryServicesClient queryClient,
                                              ProcessServicesClient processClient, KieContainerResourceList containers,
                                              Object permRequest) throws IOException, IllegalAccessException {
        Long processInstanceId= 0l;
        Long taskId = 0l;

        List<KartableTaskDTO> result =null;

//        MagfaKieSDKImpl magfaKieSDK = new MagfaKieSDKImpl();

        if (1==state){
            Map<String, Object> params = JbpmClientUtils.json(permRequest);
            startProcessInstance(queryClient, processClient, params, KieClientConstant.CONTAINER_ID, KieClientConstant.PROCESS_ID);
            processInstanceId = JbpmClientUtils.readProccessIDFromFile();
            result=JbpmClientUtils.printNextUserInfo(kieServicesClient, processInstanceId,state);
        }else if (2==state) {
            List<TaskSummary> taskList = findAvailableTasks(taskClient, userName);
            taskId = taskList.get(0).getId();
            Map<String, Object> params = JbpmClientUtils.fillParam(permRequest);
            startTask(taskClient, KieClientConstant.CONTAINER_ID,taskId,userName);

            completeTask(taskClient,KieClientConstant.CONTAINER_ID,taskId,userName,params);
            processInstanceId = JbpmClientUtils.readProccessIDFromFile();
            result=JbpmClientUtils.printNextUserInfo(kieServicesClient, processInstanceId,state);
        }else if (3==state) {
            List<TaskSummary> taskList = findAvailableTasks(taskClient, userName);
            taskId = taskList.get(0).getId();
            Map<String, Object> params = JbpmClientUtils.fillParam(permRequest);
            startTask(taskClient, KieClientConstant.CONTAINER_ID,taskId,userName);

            completeTask(taskClient,KieClientConstant.CONTAINER_ID,taskId,userName,params);
            processInstanceId = JbpmClientUtils.readProccessIDFromFile();
            result=JbpmClientUtils.printNextUserInfo(kieServicesClient, processInstanceId,state);

        }
        return result;
    }

    public KieServicesConfiguration getKieServicesConfiguration(String serviceURL, String userName, String password) {
        return KieServicesFactory.newRestConfiguration(serviceURL, userName, password);
    }

    public KieServicesClient getKieServicesClient(KieServicesConfiguration configuration) {
        return KieServicesFactory.newKieServicesClient(configuration);
    }

    public KieContainerResourceList getContainers(KieServicesClient kieServicesClient) {
        return kieServicesClient.listContainers().getResult();
    }

    public QueryServicesClient getQueryClient(KieServicesClient kieServicesClient) {
        return kieServicesClient.getServicesClient(QueryServicesClient.class);
    }

    public ProcessServicesClient getProcessServicesClient(KieServicesClient kieServicesClient) {
        return kieServicesClient.getServicesClient(ProcessServicesClient.class);
    }
    public  void getAllAvailableProcessByProcessName(KieServicesClient kieServicesClient,String processName) {
        QueryServicesClient queryClient = kieServicesClient.getServicesClient(QueryServicesClient.class);
        List<ProcessDefinition> processes = queryClient.findProcesses(processName,0, 10);

        System.out.println("\t######### Available processes" + processes);
    }
    public void getProcessesByProjectName(KieServicesClient kieServicesClient, String projectName){
        QueryServicesClient queryClient = kieServicesClient.getServicesClient(QueryServicesClient.class);
        List<ProcessDefinition> processes = queryClient.findProcessesByContainerId(projectName,0,10);
        System.out.println("\t######### Available processes" + processes);
    }
    public ProcessDefinition getProcessDefinition(KieServicesClient kieServicesClient,String containerId, String processId) {
        ProcessServicesClient processClient = getProcessServicesClient(kieServicesClient);
        return processClient.getProcessDefinition(containerId,processId);
    }

    public UserTaskServicesClient getUserTaskServicesClient(KieServicesClient kieServicesClient) {
        return kieServicesClient.getServicesClient(UserTaskServicesClient.class);
    }

    public  void foundContainer(KieContainerResourceList containers, String containerId){
        boolean deployContainer = true;
        if (containers != null) {
            for (KieContainerResource kieContainerResource : containers.getContainers()) {
                if (kieContainerResource.getContainerId().equals(containerId)) {
                    System.out.println("\t######### Found container " + containerId + " skipping deployment...");
                    deployContainer = false;
                    break;
                }
            }
        }
    }

    public  void getAllAvailableProcess(KieServicesClient kieServicesClient) {
        QueryServicesClient queryClient = kieServicesClient.getServicesClient(QueryServicesClient.class);
        List<ProcessDefinition> processes = queryClient.findProcesses(0, 10);
        System.out.println("\t######### Available processes" + processes);
    }

    public  void getDetailsOfProcessDefinition(ProcessServicesClient processClient, String containerId, String processId){
        ProcessDefinition definition = processClient.getProcessDefinition(containerId, processId);
        System.out.println("\t######### Definition details: " + definition);
    }

    public   void startProcessInstance(QueryServicesClient queryClient, ProcessServicesClient processClient, Map<String, Object> params, String containerId, String processId) throws IOException {
        Long processInstanceId = processClient.startProcess(containerId, processId, params);
        System.out.println("\t######### Process instance id: " + processInstanceId);
        List<NodeInstance> completedNodes = queryClient.findCompletedNodeInstances(processInstanceId, 0, 10);
        System.out.println("\t######### Completed nodes: " + completedNodes);
        String fileName = "C:\\prs\\prs.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(processInstanceId+"");
        writer.close();
    }

    public  List<TaskSummary>  findAvailableTasks(UserTaskServicesClient taskClient, String user){
        List<TaskSummary> tasks = taskClient.findTasksAssignedAsPotentialOwner(user, 0, 10);
        System.out.println("\t######### Tasks: " +tasks);
        Long taskId = tasks.get(0).getId();
        System.out.println("\t######### Last Task ID : " +taskId);
        return tasks;
    }

    public   void startTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String user){
        taskClient.startTask(containerId, taskId, user);
    }

    public  void completeTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String user, Map<String, Object> params){
        taskClient.completeTask(containerId,taskId,user,params);
    }


    public  void claimTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String user){
        taskClient.claimTask(containerId,taskId,user);
    }

    @Override
    public void activateTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId) {
        taskClient.activateTask(containerId,taskId,userId);
    }



    @Override
    public void exitTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId) {
        taskClient.activateTask(containerId,taskId,userId);
    }

    @Override
    public void failTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId, Map<String, Object> params) {
        taskClient.failTask(containerId,taskId,userId,params);
    }

    @Override
    public void forwardTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId, String targetEntityId) {
        taskClient.forwardTask(containerId,taskId,userId,targetEntityId);
    }

    @Override
    public void releaseTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId) {
        taskClient.releaseTask(containerId,taskId,userId);
    }

    @Override
    public void resumeTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId) {
        taskClient.resumeTask(containerId,taskId,userId);
    }

    @Override
    public void skipTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId) {
        taskClient.skipTask(containerId,taskId,userId);
    }

    @Override
    public void stopTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId) {
        taskClient.stopTask(containerId,taskId,userId);
    }

    @Override
    public void suspendTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId) {
        taskClient.suspendTask(containerId,taskId,userId);
    }

    @Override
    public void setTaskPriority(UserTaskServicesClient taskClient, String containerId, Long taskId, int priority) {
        taskClient.setTaskPriority(containerId, taskId, priority);
    }

    @Override
    public void setTaskExpirationDate(UserTaskServicesClient taskClient, String containerId, Long taskId, Date date) {
        taskClient.setTaskExpirationDate(containerId, taskId, date);
    }

    @Override
    public void setTaskSkipable(UserTaskServicesClient taskClient, String containerId, Long taskId, boolean skipable) {
        taskClient.setTaskSkipable(containerId,taskId,skipable);
    }

    @Override
    public void setTaskName(UserTaskServicesClient taskClient, String containerId, Long taskId, String name) {
        taskClient.setTaskName(containerId,taskId,name);
    }

    @Override
    public void setTaskDescription(UserTaskServicesClient taskClient, String containerId, Long taskId, String description) {
        taskClient.setTaskDescription(containerId,taskId,description);
    }

    @Override
    public Long saveTaskContent(UserTaskServicesClient taskClient, String containerId, Long taskId, Map<String, Object> values) {
        return taskClient.saveTaskContent(containerId,taskId,values);
    }

    @Override
    public Map<String, Object> getTaskOutputContentByTaskId(UserTaskServicesClient taskClient, String containerId, Long taskId) {
        return taskClient.getTaskOutputContentByTaskId(containerId,taskId);
    }

    @Override
    public Map<String, Object> getTaskInputContentByTaskId(UserTaskServicesClient taskClient, String containerId, Long taskId) {
        return taskClient.getTaskInputContentByTaskId(containerId,taskId);
    }

    @Override
    public void deleteTaskContent(UserTaskServicesClient taskClient, String containerId, Long taskId, Long contentId) {
        taskClient.deleteTaskContent(containerId, taskId, contentId);
    }

    @Override
    public Long addTaskComment(UserTaskServicesClient taskClient, String containerId, Long taskId, String text, String addedBy, Date addedOn) {
        return taskClient.addTaskComment(containerId,taskId,text,addedBy,addedOn);
    }

    @Override
    public void deleteTaskComment(UserTaskServicesClient taskClient, String containerId, Long taskId, Long commentId) {
        taskClient.deleteTaskComment(containerId, taskId, commentId);
    }

    @Override
    public List<TaskComment> getTaskCommentsByTaskId(UserTaskServicesClient taskClient, String containerId, Long taskId) {
        return taskClient.getTaskCommentsByTaskId(containerId, taskId);
    }

    @Override
    public TaskComment getTaskCommentById(UserTaskServicesClient taskClient, String containerId, Long taskId, Long commentId) {
        return taskClient.getTaskCommentById(containerId, taskId, commentId);
    }

    @Override
    public Long addTaskAttachment(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId, String name, Object attachment) {
        return taskClient.addTaskAttachment(containerId, taskId, userId, name, attachment);
    }

    @Override
    public void deleteTaskAttachment(UserTaskServicesClient taskClient, String containerId, Long taskId, Long attachmentId) {
        taskClient.deleteTaskAttachment(containerId, taskId, attachmentId);
    }

    @Override
    public TaskAttachment getTaskAttachmentById(UserTaskServicesClient taskClient, String containerId, Long taskId, Long attachmentId) {
        return taskClient.getTaskAttachmentById(containerId, taskId, attachmentId);
    }

    @Override
    public Object getTaskAttachmentContentById(UserTaskServicesClient taskClient, String containerId, Long taskId, Long attachmentId) {
        return taskClient.getTaskAttachmentContentById(containerId, taskId, attachmentId);
    }

    @Override
    public List<TaskAttachment> getTaskAttachmentsByTaskId(UserTaskServicesClient taskClient, String containerId, Long taskId) {
        return taskClient.getTaskAttachmentsByTaskId(containerId, taskId);
    }

    @Override
    public TaskInstance getTaskInstance(UserTaskServicesClient taskClient, String containerId, Long taskId) {
        return taskClient.getTaskInstance(containerId, taskId);
    }

    @Override
    public TaskInstance getTaskInstance(UserTaskServicesClient taskClient, String containerId, Long taskId, boolean withInputs, boolean withOutputs, boolean withAssignments) {
        return taskClient.getTaskInstance(containerId,taskId,withInputs,withOutputs,withAssignments);
    }

    @Override
    public List<TaskEventInstance> findTaskEvents(UserTaskServicesClient taskClient, String containerId, Long taskId, Integer page, Integer pageSize) {
        return taskClient.findTaskEvents(containerId,taskId,page,pageSize);
    }

    @Override
    public List<TaskEventInstance> findTaskEvents(UserTaskServicesClient taskClient, String containerId, Long taskId, Integer page, Integer pageSize, String sort, boolean sortOrder) {
        return taskClient.findTaskEvents(containerId, taskId, page, pageSize, sort, sortOrder);
    }

    @Override
    public TaskInstance findTaskByWorkItemId(UserTaskServicesClient taskClient, Long workItemId) {
        return taskClient.findTaskByWorkItemId(workItemId);
    }

    @Override
    public TaskInstance findTaskById(UserTaskServicesClient taskClient, Long taskId) {
        return taskClient.findTaskById(taskId);
    }

    @Override
    public List<TaskSummary> findTasksAssignedAsBusinessAdministrator(UserTaskServicesClient taskClient, String userId, Integer page, Integer pageSize) {
        return taskClient.findTasksAssignedAsBusinessAdministrator(userId, page, pageSize);
    }

    @Override
    public List<TaskSummary> findTasksAssignedAsBusinessAdministrator(UserTaskServicesClient taskClient, String userId, List<String> status, Integer page, Integer pageSize) {
        return taskClient.findTasksAssignedAsBusinessAdministrator(userId, status, page, pageSize);
    }

    @Override
    public List<TaskSummary> findTasksAssignedAsPotentialOwner(UserTaskServicesClient taskClient, String userId, Integer page, Integer pageSize) {
        return taskClient.findTasksAssignedAsPotentialOwner(userId, page, pageSize);
    }

    @Override
    public List<TaskSummary> findTasksAssignedAsPotentialOwner(UserTaskServicesClient taskClient, String userId, List<String> status, Integer page, Integer pageSize) {
        return taskClient.findTasksAssignedAsPotentialOwner(userId, status, page, pageSize);
    }

    @Override
    public List<TaskSummary> findTasksAssignedAsPotentialOwner(UserTaskServicesClient taskClient, String userId, String filter, List<String> status, Integer page, Integer pageSize) {
        return taskClient.findTasksAssignedAsPotentialOwner(userId, filter, status, page, pageSize);
    }

    @Override
    public List<TaskSummary> findTasksAssignedAsPotentialOwner(UserTaskServicesClient taskClient, String userId, List<String> groups, List<String> status, Integer page, Integer pageSize) {
        return taskClient.findTasksAssignedAsPotentialOwner(userId, groups, status, page, pageSize);
    }

    @Override
    public List<TaskSummary> findTasksOwned(UserTaskServicesClient taskClient, String userId, Integer page, Integer pageSize) {
        return taskClient.findTasksOwned(userId, page, pageSize);
    }

    @Override
    public List<TaskSummary> findTasksOwned(UserTaskServicesClient taskClient, String userId, List<String> status, Integer page, Integer pageSize) {
        return taskClient.findTasksOwned(userId, status, page, pageSize);
    }

    @Override
    public List<TaskSummary> findTasksByStatusByProcessInstanceId(UserTaskServicesClient taskClient, Long processInstanceId, List<String> status, Integer page, Integer pageSize) {
        return taskClient.findTasksByStatusByProcessInstanceId(processInstanceId, status, page, pageSize);
    }

    @Override
    public List<TaskSummary> findTasks(UserTaskServicesClient taskClient, String userId, Integer page, Integer pageSize) {
        return taskClient.findTasks(userId, page, pageSize);
    }

    @Override
    public List<TaskEventInstance> findTaskEvents(UserTaskServicesClient taskClient, Long taskId, Integer page, Integer pageSize) {
        return taskClient.findTaskEvents(taskId, page, pageSize);
    }

    @Override
    public List<TaskSummary> findTasksByVariable(UserTaskServicesClient taskClient, String userId, String variableName, List<String> status, Integer page, Integer pageSize) {
        return taskClient.findTasksByVariable(userId, variableName, status, page, pageSize);
    }

    @Override
    public List<TaskSummary> findTasksByVariableAndValue(UserTaskServicesClient taskClient, String userId, String variableName, String variableValue, List<String> status, Integer page, Integer pageSize) {
        return taskClient.findTasksByVariableAndValue(userId,variableName,variableValue,status,page,pageSize);
    }

    @Override
    public List<TaskSummary> findTasksAssignedAsBusinessAdministrator(UserTaskServicesClient taskClient, String userId, Integer page, Integer pageSize, String sort, boolean sortOrder) {
        return taskClient.findTasksAssignedAsBusinessAdministrator(userId, page, pageSize, sort, sortOrder);
    }

    @Override
    public List<TaskSummary> findTasksAssignedAsBusinessAdministrator(UserTaskServicesClient taskClient, String userId, List<String> status, Integer page, Integer pageSize, String sort, boolean sortOrder) {
        return taskClient.findTasksAssignedAsBusinessAdministrator(userId, status, page, pageSize, sort, sortOrder);
    }

    @Override
    public List<TaskSummary> findTasksAssignedAsPotentialOwner(UserTaskServicesClient taskClient, String userId, Integer page, Integer pageSize, String sort, boolean sortOrder) {
        return taskClient.findTasksAssignedAsPotentialOwner(userId, page, pageSize, sort, sortOrder);
    }

    @Override
    public List<TaskSummary> findTasksAssignedAsPotentialOwner(UserTaskServicesClient taskClient, String userId, List<String> status, Integer page, Integer pageSize, String sort, boolean sortOrder) {
        return taskClient.findTasksAssignedAsPotentialOwner(userId, status, page, pageSize, sort, sortOrder);
    }

    @Override
    public List<TaskSummary> findTasksAssignedAsPotentialOwner(UserTaskServicesClient taskClient, String userId, String filter, List<String> status, Integer page, Integer pageSize, String sort, boolean sortOrder) {
        return taskClient.findTasksAssignedAsPotentialOwner(userId, filter, status, page, pageSize, sort, sortOrder);

    }

    @Override
    public List<TaskSummary> findTasksAssignedAsPotentialOwner(UserTaskServicesClient taskClient, String userId, List<String> groups, List<String> status, Integer page, Integer pageSize, String sort, boolean sortOrder) {
        return taskClient.findTasksAssignedAsPotentialOwner(userId, groups, status, page, pageSize, sort, sortOrder);
    }

    @Override
    public List<TaskSummary> findTasksOwned(UserTaskServicesClient taskClient, String userId, Integer page, Integer pageSize, String sort, boolean sortOrder) {
        return taskClient.findTasksOwned(userId, page, pageSize, sort, sortOrder);
    }

    @Override
    public List<TaskSummary> findTasksOwned(UserTaskServicesClient taskClient, String userId, List<String> status, Integer page, Integer pageSize, String sort, boolean sortOrder) {
        return taskClient.findTasksOwned(userId, status, page, pageSize, sort, sortOrder);
    }

    @Override
    public List<TaskSummary> findTasksByStatusByProcessInstanceId(UserTaskServicesClient taskClient, Long processInstanceId, List<String> status, Integer page, Integer pageSize, String sort, boolean sortOrder) {
        return taskClient.findTasksByStatusByProcessInstanceId(processInstanceId, status, page, pageSize, sort, sortOrder);
    }

    @Override
    public List<TaskSummary> findTasks(UserTaskServicesClient taskClient, String userId, Integer page, Integer pageSize, String sort, boolean sortOrder) {
        return taskClient.findTasks(userId, page, pageSize, sort, sortOrder);
    }

    @Override
    public List<TaskEventInstance> findTaskEvents(UserTaskServicesClient taskClient, Long taskId, Integer page, Integer pageSize, String sort, boolean sortOrder) {
        return taskClient.findTaskEvents(taskId, page, pageSize, sort, sortOrder);
    }

    @Override
    public List<TaskSummary> findTasksByVariable(UserTaskServicesClient taskClient, String userId, String variableName, List<String> status, Integer page, Integer pageSize, String sort, boolean sortOrder) {
        return taskClient.findTasksByVariable(userId, variableName, status, page, pageSize, sort, sortOrder);
    }

    @Override
    public List<TaskSummary> findTasksByVariableAndValue(UserTaskServicesClient taskClient, String userId, String variableName, String variableValue, List<String> status, Integer page, Integer pageSize, String sort, boolean sortOrder) {
        return taskClient.findTasksByVariableAndValue(userId, variableName, variableValue, status, page, pageSize);
    }

    public  KieServicesClient initConfiguration(String serviceURL,String userName,String password){
        KieServicesConfiguration configuration = getKieServicesConfiguration(serviceURL, userName, password);
        configuration.setMarshallingFormat(MarshallingFormat.JAXB);
        KieServicesClient kieServicesClient = getKieServicesClient(configuration);
        return kieServicesClient;

    }



//    public List<KartableTaskDTO> findAllTask(int state, String serviceURL, Object object, String userName, String password) throws IOException, IllegalAccessException {
//
//
//        KieServicesConfiguration configuration = getKieServicesConfiguration(serviceURL, userName, password);
//        configuration.setMarshallingFormat(MarshallingFormat.JAXB);
//        KieServicesClient kieServicesClient = getKieServicesClient(configuration);
//        ProcessServicesClient processClient = getProcessServicesClient(kieServicesClient);
//        QueryServicesClient queryClient = getQueryClient(kieServicesClient);
//        UserTaskServicesClient taskClient = getUserTaskServicesClient(kieServicesClient);
//        KieContainerResourceList containers = (KieContainerResourceList)kieServicesClient.listContainers().getResult();
//        getAllAvailableProcess(kieServicesClient);
//        return stateMachine(state,userName,kieServicesClient,taskClient,queryClient,processClient,containers,object);
//
//    }
//

}
