package tests;
import Controllers.displayRespController;
import entities.planning_doc;
import jakarta.mail.MessagingException;
import services.Jakarta_API;
import services.SMS_API;
import services.planning_docService;

import java.sql.Date;
import java.sql.Time;

public class Main {
    public static void main(String[] args) {

        planning_docService ps = new planning_docService();

        planning_doc p = new planning_doc(1, Date.valueOf("2025-02-19"), Time.valueOf("8:00:00"), Time.valueOf("9:00:00"));
        try {
            Jakarta_API.sendEmail("dhia1925@yahoo.fr", "test", "this is a test");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}