package ir.component.core.engine.factory;

import ir.component.core.dao.model.NotificationType;
import ir.component.notification.service.EmailNotification;
import ir.component.notification.service.NotificationService;
import ir.component.notification.service.SMSNotification;

/**
 * @author Zahra Afsharinia
 */
public class NotificationServiceFactory {

    private NotificationServiceFactory() {}

    private static final NotificationServiceFactory INSTANCE = new NotificationServiceFactory();

    private NotificationService emailService = new EmailNotification();
    private NotificationService smsService = new SMSNotification();

    public static NotificationServiceFactory instance() {
        return INSTANCE;
    }
    public NotificationService notificationService(NotificationType type) {

        if (type == null)
            return null;

        if (type.equals(NotificationType.EMAIL))
            return emailService;
        else if (type.equals(NotificationType.SMS))
            return smsService;

        else
            return null;
    }
}
