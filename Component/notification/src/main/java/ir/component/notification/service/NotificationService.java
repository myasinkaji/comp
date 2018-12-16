package ir.component.notification.service;

/**
 * @author Mohammad Yasin Kaji
 */
public interface NotificationService {

    void sendMassage(String subject, String message, String to);

}
