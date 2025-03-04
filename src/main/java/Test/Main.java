package Test;

import DAO.AccompagnateurDAO;
import DAO.OffreEmploisDAO;
import Modeles.Accompagnateur;
import Modeles.OffreEmplois;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args)  {

        AccompagnateurDAO AcDAO = new AccompagnateurDAO();
        OffreEmploisDAO OffDAO = new OffreEmploisDAO();
        Accompagnateur ac = new Accompagnateur("test", "0000", "test@test.com", "gf", "url", "exp", "motiv", "[\"français\"]", "en attente", "12/02/2025");


        try {
            AcDAO.ajouter(ac);
            ac.setStatut("recruté");
            AcDAO.mettreAJourStatut(ac);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

