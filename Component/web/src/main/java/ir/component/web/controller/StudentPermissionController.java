package ir.component.web.controller;

import ir.magfa.sdk.dto.PermRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * @author Mohammad Yasin Kaji
 */
@Controller
@Scope("view")
public class StudentPermissionController {

    private PermRequest request;

    @PostConstruct
    public void init() {
         request = new PermRequest();
    }

    public void register() {
        System.out.println(request);
    }

    public PermRequest getRequest() {
        return request;
    }

    public void setRequest(PermRequest request) {
        this.request = request;
    }
}
