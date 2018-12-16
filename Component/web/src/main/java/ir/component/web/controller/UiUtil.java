package ir.component.web.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohammad Yasin Kaji
 */
@Service
@Scope("session")
public class UiUtil {

    @Resource
    private SessionBean sessionBean;


    public void checkUser() {
        if (sessionBean.getUser().getUsername() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Null Session", "User must be logged in"));
//            FacesContext.getCurrentInstance().renderResponse();
            login();
            throw new RuntimeException();
        }
    }

    public void login() {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
        String outcome = "/page/login.xhtml";
        try {
            redirect(outcome);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, outcome);

    }

    public static void refresh() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public static void redirect(String address) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(address);
    }



    public List<SelectItem> asSelectItem(String bundleKey, int from, int count) {
        return asFilterItem(null, bundleKey, from, count);
    }

    public List<SelectItem> asFilterItem(String firstItem, String bundleKey, int from, int count) {
        List<SelectItem> l = new ArrayList<SelectItem>();
        if (firstItem != null) {
            l.add(new SelectItem("", firstItem));
        }
        for (int i = from; i < from + count; i++) {
            l.add(new SelectItem(i, "Admin" + i));
        }
        return l;
    }
}
