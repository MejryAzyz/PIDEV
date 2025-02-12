package org.example;

import dao.AccompagnateurDAO;
import entities.Accompagnateur;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        AccompagnateurDAO accompagnateurDAO = new AccompagnateurDAO();
        Accompagnateur ac = new Accompagnateur("cv","0ex","fretang","5cert","recruté","12/02/2025");
        try {
            //accompagnateurDAO.ajoutreAccompagnateur(ac);
            System.out.println(accompagnateurDAO.recupererAccompagnateur());
            //accompagnateurDAO.supprimerAccompagnateur(2);
            accompagnateurDAO.modifierAccompagnateur(ac,3);

        } catch (SQLException e) {
           System.out.println(e.getMessage());
        }
        }
    }
