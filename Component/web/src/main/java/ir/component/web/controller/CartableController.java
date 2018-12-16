package ir.component.web.controller;

import ir.component.web.service.model.Container;
import ir.magfa.sdk.kaji.SimpleKieService;
import ir.magfa.sdk.model.ProcessDefinitionProxy;
import ir.magfa.sdk.model.TaskInstanceProxy;
import ir.magfa.sdk.model.User;
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

    @Resource
    private SessionBean sessionBean;

    @Resource
    private UiUtil uiUtil;

    private MenuModel model;

    private boolean renderForm = false;

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


                List<ProcessDefinitionProxy> processDefinitionsProxies = getProcessDefinitionList(container);
                Container container1Obj = new Container(processDefinitionsProxies);
                this.containers.add(container1Obj);
                if (processDefinitionsProxies != null) {
                    for (ProcessDefinitionProxy proxy : processDefinitionsProxies) {
                        item = new DefaultMenuItem(proxy.getName());
                        item.setIcon("fa fa-tasks");
//            item.setCommand("#{PF('carDialog').show()}");
                        item.setCommand("#{cartableController.startProcess('" + proxy.getContainerId() + "%$#" + proxy.getId() + "')}");
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
        return SimpleKieService.INSTANCE().allContainers();
    }


    public MenuModel getModel() {
        return model;
    }

    public List<ProcessDefinitionProxy> getProcessDefinitionList(String containerId) throws IOException, IllegalAccessException {
        return SimpleKieService.INSTANCE().processDefinitions(containerId, sessionBean.getUser());
    }

    public List<TaskInstanceProxy> getOpenTasks() {
        try {
            uiUtil.checkUser();
            return SimpleKieService.INSTANCE().openTasks(sessionBean.getUser());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void startProcess(ProcessDefinitionProxy process) {
        System.out.println(process);
    }

    public void startProcess(String process) {
        System.out.println(process);
    }
    public void startProcess(User user) {
        System.out.println(user);
    }

    public boolean isRenderForm() {
        return renderForm;
    }

    public void setRenderForm(boolean renderForm) {
        this.renderForm = renderForm;
    }
}
