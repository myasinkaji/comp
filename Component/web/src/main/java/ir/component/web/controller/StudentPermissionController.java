package ir.component.web.controller;

import ir.component.core.dao.model.PermRequest;
import ir.component.core.dao.model.RequestDTO;
import ir.component.core.engine.PermRequestDao;
import ir.magfa.sdk.kaji.SimpleKieService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author Mohammad Yasin Kaji
 */
@Controller
@Scope("view")
public class StudentPermissionController {

    private PermRequest request;

    @Resource
    private SessionBean sessionBean;

    @Resource
    private PermRequestDao permRequestDao;


    @PostConstruct
    public void init() {
         request = new PermRequest();
    }

    public void register() {
        request.setStudent(sessionBean.getUser().getUsername());
        permRequestDao.persist(request);
    }

    public PermRequest getRequest() {
        return request;
    }

    public void setRequest(PermRequest request) {
        this.request = request;
    }
}
