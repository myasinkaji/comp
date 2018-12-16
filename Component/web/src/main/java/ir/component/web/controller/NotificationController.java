package ir.component.web.controller;

import ir.component.core.dao.model.NotificationType;
import ir.component.core.engine.factory.NotificationServiceFactory;
import ir.component.notification.service.NotificationService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mohammad Yasin Kaji
 */

@Controller
@Scope("request")
public class NotificationController {

    private NotificationType selectedNotification;
    private String subject;
    private String recipient;
    private String message;

    private NotificationService notificationService;

    public List<NotificationType> getNotificationTypes() {
        return Arrays.asList(NotificationType.values());
    }


    public String getName(NotificationType notificationType) {
        return notificationType.name();
    }

    public void send() {
        notificationService = NotificationServiceFactory.instance().notificationService(selectedNotification);

        notificationService.sendMassage(subject, message, recipient);
    }

    public NotificationType getSelectedNotification() {
        return selectedNotification;
    }

    public void setSelectedNotification(NotificationType selectedNotification) {
        this.selectedNotification = selectedNotification;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
