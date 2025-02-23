package test;

import Server.LocalHttpServer;
import models.Utilisateur;
import services.ServiceSendEmail;
import services.ServiceUtilisateur;
import tools.MyDataBase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static services.ServiceSendEmail.generateVerificationLink;
import static services.ServiceSendEmail.sendVerificationEmail;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        MyDataBase md=  MyDataBase.getInstance();
       ServiceUtilisateur service = new ServiceUtilisateur();
        ServiceSendEmail se = new ServiceSendEmail();
        LocalHttpServer Lh = new LocalHttpServer();

        Lh.startServer();
        try {
            // Example: Generate the verification link
            String email = "fihit18472@codverts.com";  // Replace with recipient's email
            String name = "John Doee";  // Replace with recipient's name
            String verificationLink = generateVerificationLink(email);

            // Send the verification email
            sendVerificationEmail(email, name, verificationLink);
        } catch (IOException e) {
            e.printStackTrace();
        }
          // Debugging line
    }


/*/String templateid =  "d-230b850d02cb4bfb85298b91ed20e70f";
        se.sendEmailUsingTemplate("msehli.elyes@esprit.tn","test",templateid,"aya behy l email khtaf");

*/
     /*   Utilisateur nouvelUtilisateur = new Utilisateur("Mejri","islem","islem@example.com","123", "12345678",new Date(),"Tunis","https://ibb.co/r25WJZ9");
service.login("islem@example.com","123");
        try {
          List U = new ArrayList<>(  service.recuperer());
            System.out.println(U);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
*/
    }


