package tests;

import models.Clinique;
import models.Docteur;
import models.Specialite;
import services.ServiceClinique;
import services.ServiceDocteur;
import services.ServiceSpecialite;
import tools.MyDataBase;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        //MyDataBase md = MyDataBase.getInstance();
        ServiceClinique sv = new ServiceClinique();
        ServiceDocteur sd = new ServiceDocteur();
        ServiceSpecialite ss = new ServiceSpecialite();
        Clinique c = new Clinique(3,"Clinique Cairo Health","27 Nile Corniche, Zamalek, Cairo, Égypte","+81 3 1234 5678","cairo@gmail.com","Clinique moderne au Caire, offrant une gamme complète de soins médicaux et un service client exceptionnel.", 7800);
        Docteur d = new Docteur(1,1,"Rossi","Luca","luca@gmail.com","42887495");
        Specialite s = new Specialite("ORL");
        try {
            sv.ajouter(c);
            //sv.supprimer(4);
            //sv.modifier(1,"MediCare International Hospital");

            //sd.ajouter(d);
            //sd.supprimer(6);
            sd.modifier(5,"Sami");


            //ss.ajouter(s);
            //ss.supprimer(4);
            ss.modifier(5,"Hématologie");

            System.out.println(sv.recuperer());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
}
