package ir.magfa.sdk.farhadi;

import ir.magfa.sdk.dto.KartableTaskDTO;
//import ir.magfa.sdk.dto.PermRequest;
import ir.magfa.sdk.model.MagfaProcessDefinition;
import ir.magfa.sdk.model.MagfaTaskInstance;
import ir.magfa.sdk.model.User;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.definition.ProcessDefinition;
import org.kie.server.api.model.instance.*;
import org.kie.server.client.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Mostafa.Farhadi on 11/13/2017.
 */

public interface MagfaKieSDK {


    List<MagfaProcessDefinition> getProcessDefinition(User user);

    void startProcess(String processDefinitionId);

    void startProcess(String containerId, String processId) throws IOException;

    MagfaTaskInstance startProcess(String containerId, String processId, Map<String, Object> variables) throws IOException;

    void closeTask(String containerId, String taskInstanceId, User user);



    void startProcessInstance(QueryServicesClient queryClient, ProcessServicesClient processClient, Map<String, Object> params, String containerId, String processId) throws IOException;



    KieServicesConfiguration getKieServicesConfiguration(String serviceURL, String userName, String password);

    KieServicesClient getKieServicesClient(KieServicesConfiguration configuration);

    KieContainerResourceList getContainers(KieServicesClient kieServicesClient);

    QueryServicesClient getQueryClient(KieServicesClient kieServicesClient);

    ProcessServicesClient getProcessServicesClient(KieServicesClient kieServicesClient);
    void getAllAvailableProcessByProcessName(KieServicesClient kieServicesClient,String processName);

    void getProcessesByProjectName(KieServicesClient kieServicesClient, String processName);
    ProcessDefinition getProcessDefinition(KieServicesClient kieServicesClient,String containerId, String processId );

    UserTaskServicesClient getUserTaskServicesClient(KieServicesClient kieServicesClient);

     void foundContainer(KieContainerResourceList containers, String containerId);

    void getAllAvailableProcess(KieServicesClient kieServicesClient);

    void getDetailsOfProcessDefinition(ProcessServicesClient processClient, String containerId, String processId);


    List<TaskSummary> findAvailableTasks(UserTaskServicesClient taskClient, String user);

    void startTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String user);

     void completeTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String user, Map<String, Object> params);

    void claimTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String user);

    void activateTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId);

    void exitTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId);

    void failTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId, Map<String, Object> params);

    void forwardTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId, String targetEntityId);

    void releaseTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId);

    void resumeTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId);

    void skipTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId);

    void stopTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId);

    void suspendTask(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId);

    void setTaskPriority(UserTaskServicesClient taskClient, String containerId, Long taskId, int priority);

    void setTaskExpirationDate(UserTaskServicesClient taskClient, String containerId, Long taskId, Date date);

    void setTaskSkipable(UserTaskServicesClient taskClient, String containerId, Long taskId, boolean skipable);

    void setTaskName(UserTaskServicesClient taskClient, String containerId, Long taskId, String name);

    void setTaskDescription(UserTaskServicesClient taskClient, String containerId, Long taskId, String description);

    Long saveTaskContent(UserTaskServicesClient taskClient, String containerId, Long taskId, Map<String, Object> values);

    Map<String, Object> getTaskOutputContentByTaskId(UserTaskServicesClient taskClient, String containerId, Long taskId);

    Map<String, Object> getTaskInputContentByTaskId(UserTaskServicesClient taskClient, String containerId, Long taskId);

    void deleteTaskContent(UserTaskServicesClient taskClient, String containerId, Long taskId, Long contentId);

    Long addTaskComment(UserTaskServicesClient taskClient, String containerId, Long taskId, String text, String addedBy, Date addedOn);

    void deleteTaskComment(UserTaskServicesClient taskClient, String containerId, Long taskId, Long commentId);

    List<TaskComment> getTaskCommentsByTaskId(UserTaskServicesClient taskClient, String containerId, Long taskId);

    TaskComment getTaskCommentById(UserTaskServicesClient taskClient, String containerId, Long taskId, Long commentId);

    Long addTaskAttachment(UserTaskServicesClient taskClient, String containerId, Long taskId, String userId, String name, Object attachment);

    void deleteTaskAttachment(UserTaskServicesClient taskClient, String containerId, Long taskId, Long attachmentId);

    TaskAttachment getTaskAttachmentById(UserTaskServicesClient taskClient, String containerId, Long taskId, Long attachmentId);

    Object getTaskAttachmentContentById(UserTaskServicesClient taskClient, String containerId, Long taskId, Long attachmentId);

    List<TaskAttachment> getTaskAttachmentsByTaskId(UserTaskServicesClient taskClient, String containerId, Long taskId);

    TaskInstance getTaskInstance(UserTaskServicesClient taskClient, String containerId, Long taskId);

    TaskInstance getTaskInstance(UserTaskServicesClient taskClient, String containerId, Long taskId, boolean withInputs, boolean withOutputs, boolean withAssignments);

    List<TaskEventInstance> findTaskEvents(UserTaskServicesClient taskClient, String containerId, Long taskId, Integer page, Integer pageSize);

    List<TaskEventInstance> findTaskEvents(UserTaskServicesClient taskClient, String containerId, Long taskId, Integer page, Integer pageSize, String sort, boolean sortOrder);

    // task searches
    TaskInstance findTaskByWorkItemId(UserTaskServicesClient taskClient, Long workItemId);

    TaskInstance findTaskById(UserTaskServicesClient taskClient, Long taskId);

    List<TaskSummary> findTasksAssignedAsBusinessAdministrator(UserTaskServicesClient taskClient, String userId, Integer page, Integer pageSize);

    List<TaskSummary> findTasksAssignedAsBusinessAdministrator(UserTaskServicesClient taskClient, String userId, List<String> status, Integer page, Integer pageSize);

    List<TaskSummary> findTasksAssignedAsPotentialOwner(UserTaskServicesClient taskClient, String userId, Integer page, Integer pageSize);

    List<TaskSummary> findTasksAssignedAsPotentialOwner(UserTaskServicesClient taskClient, String userId, List<String> status, Integer page, Integer pageSize);

    List<TaskSummary> findTasksAssignedAsPotentialOwner(UserTaskServicesClient taskClient, String userId, String filter, List<String> status, Integer page, Integer pageSize);

    List<TaskSummary> findTasksAssignedAsPotentialOwner(UserTaskServicesClient taskClient, String userId, List<String> groups, List<String> status, Integer page, Integer pageSize);

    List<TaskSummary> findTasksOwned(UserTaskServicesClient taskClient, String userId, Integer page, Integer pageSize);

    List<TaskSummary> findTasksOwned(UserTaskServicesClient taskClient, String userId, List<String> status, Integer page, Integer pageSize);

    List<TaskSummary> findTasksByStatusByProcessInstanceId(UserTaskServicesClient taskClient, Long processInstanceId, List<String> status, Integer page, Integer pageSize);

    List<TaskSummary> findTasks(UserTaskServicesClient taskClient, String userId, Integer page, Integer pageSize);

    List<TaskEventInstance> findTaskEvents(UserTaskServicesClient taskClient, Long taskId, Integer page, Integer pageSize);

    List<TaskSummary> findTasksByVariable(UserTaskServicesClient taskClient, String userId, String variableName, List<String> status, Integer page, Integer pageSize);

    List<TaskSummary> findTasksByVariableAndValue(UserTaskServicesClient taskClient, String userId, String variableName, String variableValue, List<String> status, Integer page, Integer pageSize);

    List<TaskSummary> findTasksAssignedAsBusinessAdministrator(UserTaskServicesClient taskClient, String userId, Integer page, Integer pageSize, String sort, boolean sortOrder);

    List<TaskSummary> findTasksAssignedAsBusinessAdministrator(UserTaskServicesClient taskClient, String userId, List<String> status, Integer page, Integer pageSize, String sort, boolean sortOrder);

    List<TaskSummary> findTasksAssignedAsPotentialOwner(UserTaskServicesClient taskClient, String userId, Integer page, Integer pageSize, String sort, boolean sortOrder);

    List<TaskSummary> findTasksAssignedAsPotentialOwner(UserTaskServicesClient taskClient, String userId, List<String> status, Integer page, Integer pageSize, String sort, boolean sortOrder);

    List<TaskSummary> findTasksAssignedAsPotentialOwner(UserTaskServicesClient taskClient, String userId, String filter, List<String> status, Integer page, Integer pageSize, String sort, boolean sortOrder);

    List<TaskSummary> findTasksAssignedAsPotentialOwner(UserTaskServicesClient taskClient, String userId, List<String> groups, List<String> status, Integer page, Integer pageSize, String sort, boolean sortOrder);

    List<TaskSummary> findTasksOwned(UserTaskServicesClient taskClient, String userId, Integer page, Integer pageSize, String sort, boolean sortOrder);

    List<TaskSummary> findTasksOwned(UserTaskServicesClient taskClient, String userId, List<String> status, Integer page, Integer pageSize, String sort, boolean sortOrder);

    List<TaskSummary> findTasksByStatusByProcessInstanceId(UserTaskServicesClient taskClient, Long processInstanceId, List<String> status, Integer page, Integer pageSize, String sort, boolean sortOrder);

    List<TaskSummary> findTasks(UserTaskServicesClient taskClient, String userId, Integer page, Integer pageSize, String sort, boolean sortOrder);

    List<TaskEventInstance> findTaskEvents(UserTaskServicesClient taskClient, Long taskId, Integer page, Integer pageSize, String sort, boolean sortOrder);

    List<TaskSummary> findTasksByVariable(UserTaskServicesClient taskClient, String userId, String variableName, List<String> status, Integer page, Integer pageSize, String sort, boolean sortOrder);

    List<TaskSummary> findTasksByVariableAndValue(UserTaskServicesClient taskClient, String userId, String variableName, String variableValue, List<String> status, Integer page, Integer pageSize, String sort, boolean sortOrder);
     KieServicesClient initConfiguration(String serviceURL,String userName,String password);

    public List<KartableTaskDTO> stateMachine(int state, String userName, KieServicesClient kieServicesClient, UserTaskServicesClient taskClient, QueryServicesClient queryClient, ProcessServicesClient processClient, KieContainerResourceList containers, Object object) throws IOException, IllegalAccessException;
//    public List<KartableTaskDTO> findAllTask(int state, String serviceURL, Object object, String userName, String password) throws IOException, IllegalAccessException;

}
