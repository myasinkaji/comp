package ir.component.web.controller;

import ir.component.core.dao.model.RequestDTO;
import ir.component.core.engine.RequestDTODao;
import ir.magfa.sdk.model.Context;
import ir.magfa.sdk.service.impl.KieService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @author Mohammad Yasin Kaji
 */
@Controller
@Scope("view")
public class EvaluationController {

    private RequestDTO requestDTO;
    @Resource
    private RequestDTODao requestDTODao;

    @Resource
    private SessionBean sessionBean;

    @PostConstruct
    public void init() {
        requestDTO = new RequestDTO();

        Context context = KieService.getContext();
//SimpleKieService.INSTANCE().getProcessVariableDefinitions(context.getContainerId(), context.getProcessId());
        if (context != null && context.getProcessInstanceId() != null) {
            Map<String, Object> variables =
                    KieService.getProcessInstanceVariables(context.getContainerId(), context.getProcessInstanceId());

            requestDTO.setReason((String) variables.get("reason"));
            requestDTO.setPerformance((String) variables.get("performance"));
            requestDTO.setDocumentId((String) variables.get("documentId"));
            requestDTO.setEmployee((String) variables.get("employee"));
            Double id = Double.parseDouble(variables.get("id").toString());
            requestDTO.setId(id.longValue());
            requestDTO.setTitle((String) variables.get("title"));
            requestDTO.setPortletName((String) variables.get("portletName"));
//requestDTO.set((String) variables.get("initiator"));
        }
    }

    public void closeTask() {
        try {
            requestDTODao.merge(requestDTO);
            Context context = KieService.getContext();
            KieService.setProcessVariablesAsJson(context.getContainerId(), context.getProcessInstanceId(), requestDTO);
            String[] nextUsers = KieService.closeTask(requestDTO);
            System.out.println("Next Users for Task with id: " + context.getTaskInstanceId() + " are: " + nextUsers);
            KieService.setContext(new Context());
            UiUtil.redirect(UiUtil.cartable());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void register() {
        try {
            requestDTODao.persist(requestDTO);
            String[] nextUsers = KieService.startProcess(requestDTO);
            System.out.println("Next Users are: " + nextUsers);
            UiUtil.redirect(UiUtil.cartable());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RequestDTO getRequestDTO() {
        return requestDTO;
    }

    public void setRequestDTO(RequestDTO requestDTO) {
        this.requestDTO = requestDTO;
    }
}
