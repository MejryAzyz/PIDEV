package services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import java.io.IOException;
import java.util.UUID;

public class ServiceSendEmail {
    private static final String API_KEY = System.getenv("SENDGRID_API_KEY"); // Never share API keysss

    // Helper method to send emails
    private static void sendEmailWithTemplate(String toEmail,
                                              String subject,
                                              String templateId,
                                              String dynamicDataKey1,
                                              String dynamicDataValue1,
                                              String dynamicDataKey2,
                                              String dynamicDataValue2) throws IOException {
        Email from = new Email("med.azyz.mejry@gmail.com");  // Your verified email address
        Email to = new Email(toEmail);

        // Create the personalization object and add dynamic data
        Personalization personalization = new Personalization();
        personalization.addTo(to);
        personalization.setSubject(subject);

        // Add both dynamic data to the template
        personalization.addDynamicTemplateData(dynamicDataKey1, dynamicDataValue1);
        personalization.addDynamicTemplateData(dynamicDataKey2, dynamicDataValue2);

        // Create the Mail object
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.addPersonalization(personalization);
        mail.setTemplateId(templateId); // Use the Template ID from SendGrid

        // Send the email
        sendMail(mail);
    }


    // Send the email via SendGrid
    private static void sendMail(Mail mail) throws IOException {
        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);

        // Log the response from SendGrid API
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Body: " + response.getBody());
        System.out.println("Headers: " + response.getHeaders());
    }

    // Method to send a generic email
    public static void sendEmail(String toEmail, String subject, String body) throws IOException {
        Email from = new Email("med.azyz.mejry@gmail.com");  // Your verified email address
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        // Send the email
        sendMail(mail);
    }

    // Method to send a verification email
    public static void sendVerificationEmail(String toEmail, String name, String verificationLink) throws IOException {
        // Create the email content using a template
        sendEmailWithTemplate(toEmail,"Verify Your Email Address","d-c9f35eba31194d908becfc3831325f8f","name", name,"verification_link", verificationLink);
    }

    // Method to generate a unique verification link (this could be a simple token or JWT)
    public static String generateVerificationLink(String email) {
        // Here, we generate a unique verification token. This could be a JWT or a random string.
        String token = UUID.randomUUID().toString();  // Example token (UUID)
        String verificationLink = "http://localhost:8000/verify?token=" + token;

        // You can store this token in your database against the user's record for verification later
        return verificationLink;
    }
}