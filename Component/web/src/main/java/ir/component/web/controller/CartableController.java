package ir.component.web.controller;

import ir.component.web.service.model.Container;
import ir.magfa.sdk.model.MagfaProcessDefinition;
import ir.magfa.sdk.model.User;
import ir.magfa.sdk.service.impl.KieService;
import org.kie.server.api.model.instance.TaskInstance;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zahra Afsharinia
 */
@Controller
@Scope("view")
public class CartableController {

    private static final String CONTAINER_PROCESS_DELIMITER = "###";

    @Resource
    private SessionBean sessionBean;

    @Resource
    private UiUtil uiUtil;

    private MenuModel model;

    private List<Container> containers = new ArrayList<>();


    @PostConstruct
    public void init() throws IOException, IllegalAccessException {
        try {
            uiUtil.checkUser();
            initMenu();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    void initMenu() throws IOException, IllegalAccessException {
        model = new DefaultMenuModel();

        DefaultMenuItem item;

        //First submenu
        DefaultSubMenu firstSubmenu = new DefaultSubMenu("Recent Containers");
        firstSubmenu.setExpanded(true);

        List<String> containers = allContainers();

        if (containers != null) {
            for (String container : containers) {

                DefaultSubMenu subMenu = new DefaultSubMenu(container);
//                subMenu.setIcon("fa fa-arrow-circle-down");


                List<MagfaProcessDefinition> processDefinitionsProxies = getProcessDefinitionList(container);
                Container container1Obj = new Container(processDefinitionsProxies);
                this.containers.add(container1Obj);
                if (processDefinitionsProxies != null) {
                    for (MagfaProcessDefinition proxy : processDefinitionsProxies) {
                        item = new DefaultMenuItem(proxy.getName());
                        item.setIcon("fa fa-tasks");
//            item.setCommand("#{PF('carDialog').show()}");
                        item.setCommand("#{cartableController.openProcessForm('" + proxy.getContainerId() + CONTAINER_PROCESS_DELIMITER +
                                proxy.getId() + CONTAINER_PROCESS_DELIMITER + proxy.getName() + "')}");
//            item.setOnclick("PF('dlg2').show();");
                        item.setUpdate("messages");
                        subMenu.addElement(item);
//            processManagementSubmenu.addElement(item);
                    }

                }


                firstSubmenu.addElement(subMenu);
            }

        }

        model.addElement(firstSubmenu);


        //Second submenu
        DefaultSubMenu secondSubmenu = new DefaultSubMenu("Old Containers");

        item = new DefaultMenuItem("something1");
        item.setUrl("#");
        item.setIcon("fa fa-home");
        secondSubmenu.addElement(item);

        item = new DefaultMenuItem("something2");
        item.setUrl("#");
        item.setIcon("fa fa-home");
        secondSubmenu.addElement(item);

        model.addElement(secondSubmenu);

        DefaultSubMenu thirdSubmenu = new DefaultSubMenu("Review Containers");

        item = new DefaultMenuItem("something1");
        item.setUrl("#");
        item.setIcon("fa fa-home");
        thirdSubmenu.addElement(item);

        item = new DefaultMenuItem("something2");
        item.setUrl("#");
        item.setIcon("fa fa-home");
        thirdSubmenu.addElement(item);

        model.addElement(thirdSubmenu);
//        panelMenu.setModel(model);
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setPassword("Pass" + i);
            user.setUsername("User" + i);
            users.add(user);
        }

        return users;
    }

    private List<String> allContainers() {
        return KieService.allContainers(sessionBean.getUser());
    }


    public MenuModel getModel() {
        return model;
    }

    public List<MagfaProcessDefinition> getProcessDefinitionList(String containerId) throws IOException, IllegalAccessException {
        return KieService.processDefinitions(containerId, sessionBean.getUser());
    }

    public List<TaskInstance> getOpenTasks() {
        try {
            uiUtil.checkUser();
            return KieService.openTasks(sessionBean.getUser(), 0, 100);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void openTaskForm(TaskInstance taskInstance) {
        try {
            KieService.getContext().setContainerId(taskInstance.getContainerId());
            KieService.getContext().setProcessId(taskInstance.getProcessId());
            KieService.getContext().setProcessInstanceId(taskInstance.getProcessInstanceId());
            KieService.getContext().setTaskInstanceId(taskInstance.getId());
            KieService.getContext().setUser(sessionBean.getUser());
            UiUtil.redirect(UiUtil.formOf(taskInstance));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openProcessForm(String process) {
        try {
            String[] strings = process.split(CONTAINER_PROCESS_DELIMITER);
            if (strings.length != 3)
                return;

            String containerId = strings[0];
            String processName = strings[2];
            String processId = strings[1];
            KieService.aware(sessionBean.getUser(), containerId, processId, null, null);
            UiUtil.redirect(UiUtil.processForm(processName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
