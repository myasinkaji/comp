package ir.component.web.controller;

import ir.component.core.dao.model.RequestDTO;
import ir.component.core.engine.RequestDTODao;
import ir.magfa.sdk.kaji.SimpleKieService;
import ir.magfa.sdk.model.Context;
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

        Context context = SimpleKieService.INSTANCE().getContext();
//SimpleKieService.INSTANCE().getProcessVariableDefinitions(context.getContainerId(), context.getProcessId());
        if (context != null && context.getProcessInstanceId() != null) {
            Map<String, Object> variables =
                    SimpleKieService.INSTANCE().getProcessInstanceVariables(context.getContainerId(), context.getProcessInstanceId());

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
            Context context = SimpleKieService.INSTANCE().getContext();
            SimpleKieService.INSTANCE().setProcessVariable(context.getContainerId(), context.getProcessInstanceId(), "request", requestDTO);
            SimpleKieService.INSTANCE().closeTask(requestDTO);
            UiUtil.redirect(UiUtil.cartable());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void register() {
        try {
            requestDTODao.persist(requestDTO);
            SimpleKieService.INSTANCE().startProcess(requestDTO);
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
