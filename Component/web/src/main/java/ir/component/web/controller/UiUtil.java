package ir.component.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.kie.server.api.model.instance.TaskInstance;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

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

    public static final String TASK_FORMS_PREFIX = "/page/task/";
    public static final String TASK_FORMS_POSTFIX = ".xhtml";
    public static final String CARTABLE_FORMS_POSTFIX = "cartable";
    @Resource
    private SessionBean sessionBean;

    public static void refresh() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public static void redirect(String address) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(address);
    }

    public static String formOf(TaskInstance taskInstance) {
        String formName = taskInstance.getFormName().toLowerCase();
        if (StringUtils.isNotBlank(formName)) {
            StringBuilder sb = new StringBuilder();
            sb.append(TASK_FORMS_PREFIX).append(formName).append(TASK_FORMS_POSTFIX);
            return sb.toString();
        }
        return null;
    }
    public static String processForm(String processName) {
        if (StringUtils.isNotBlank(processName)) {
            StringBuilder sb = new StringBuilder();
            sb.append(TASK_FORMS_PREFIX).append(processName.toLowerCase()).append(TASK_FORMS_POSTFIX);
            return sb.toString();
        }
        return null;
    }

    public static String cartable() {
        StringBuilder sb = new StringBuilder();
        sb.append("/page/").append(CARTABLE_FORMS_POSTFIX).append(TASK_FORMS_POSTFIX);
        return sb.toString();
    }

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
