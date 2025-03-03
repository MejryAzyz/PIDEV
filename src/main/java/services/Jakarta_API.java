package services;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class Jakarta_API {
    private static final String SENDER_EMAIL = System.getenv("email");
    private static final String SENDER_PASSWORD = System.getenv("password");

    public static void sendEmail(String recipient, String subject, String message) throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");


        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });


        Message emailMessage = new MimeMessage(session);
        emailMessage.setFrom(new InternetAddress(SENDER_EMAIL));
        emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        emailMessage.setSubject(subject);
        emailMessage.setText(message);


        Transport.send(emailMessage);
    }
}

