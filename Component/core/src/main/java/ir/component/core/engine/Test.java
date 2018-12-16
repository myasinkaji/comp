package ir.component.core.engine;

import ir.component.core.dao.model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Mohammad Yasin Kaji
 */
public class Test {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("META-INF/dao-context.xml");

        User user = new User();
        user.setId(12);
        user.setFirstName("Kaji");

        UserDao userDao = (UserDao) applicationContext.getBean("userDao");
        userDao.persist(user);
    }
}
