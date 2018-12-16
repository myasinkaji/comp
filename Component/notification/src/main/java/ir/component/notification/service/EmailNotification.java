package ir.component.notification.service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * @author Mohammad Yasin Kaji
 */
public class EmailNotification implements NotificationService {

    public void sendMassage(String subject, String text, String to) {

        final String username = new String("mosayeb.smk@gmail.com");
        final String password = new String("mohammad yasin kaji678");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("Mohammad_Yasin_Kaji"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject);
            message.setHeader("header1", "header2");
            message.setContent(text, "text/html");

            Transport.send(message);

            System.out.println("Email has been sent to " + to);

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
