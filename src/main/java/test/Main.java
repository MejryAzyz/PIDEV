package test;

import models.Utilisateur;
import services.ServiceUtilisateur;
import tools.MyDataBase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        MyDataBase md=  MyDataBase.getInstance();
       ServiceUtilisateur service = new ServiceUtilisateur();

        //Utilisateur nouvelUtilisateur = new Utilisateur("Mejri","islem","islem@example.com","password123", "12345678",new Date(),"Tunis");
int count = service.countConnectedUsers();
        System.out.println("conneted users : " + count);
        try {
          List U = new ArrayList<>(  service.recuperer());
            System.out.println(U);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
