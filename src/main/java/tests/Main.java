package tests;

import Models.HT.Hebergement;
import Models.HT.Transport;
import service.HT.ServiceHebergement;
import service.HT.ServiceTransport;

public class Main {
    public static void main(String[] args) {
        ServiceHebergement sh = new ServiceHebergement();
        ServiceTransport st = new ServiceTransport();
        Hebergement hebergement = new Hebergement ( "nom2", "adresse2", 4514, "email2", 20,  120);
        Transport transport = new Transport("van",2500,20);
        try {
            //st.ajouter(transport);
            //st.supprimer(2);
            //st.modifier(1,"taxi");
            //sh.ajouter(hebergement);
            //sh.supprimer(3);
            //sh.modifier(1,"ahmed");
            //System.out.println(sh.recuperer());
            //System.out.println(st.recuperer());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

}
