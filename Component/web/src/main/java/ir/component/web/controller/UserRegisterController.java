package ir.component.web.controller;

import ir.component.core.dao.model.AdvertiserState;
import ir.component.core.dao.model.User;
import ir.component.core.dao.model.UserType;
import ir.component.core.engine.UserDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mohammad Yasin Kaji
 */

@Controller("userRegisterController")
@Scope("view")
public class UserRegisterController implements Serializable {

    private User user = new User();
    @Resource
    private UserDao userDao;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void save() {
        userDao.persist(user);
    }

    public List<UserType> getUserTypes() {
        return Arrays.asList(UserType.values());
    }

    public AdvertiserState[] getAdvertiserStates() {
        return AdvertiserState.values();
    }

    public String getName(UserType userType) {
        return userType.name();
    }

}
