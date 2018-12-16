package ir.component.notification.consumer;

import ir.component.notification.service.NotificationService;

/**
 * @author Mohammad Yasin Kaji
 */
public class NotificationConsumer {

    private NotificationService notificationService = null;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void sendNotification(String subject, String message, String to) {
        notificationService.sendMassage(subject, message, to);
    }
}
