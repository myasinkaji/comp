package ir.component.notification.service;

/**
 * @author Mohammad Yasin Kaji
 */
public class SMSNotification implements NotificationService {

    public void sendMassage(String subject, String message, String to) {
        System.out.println("SMS has been sent.");
    }
}
