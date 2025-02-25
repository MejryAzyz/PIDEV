package services;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapService {
    public static void afficherCarte(ImageView mapView, double lat, double lon) {
        String mapUrl = "https://static-maps.yandex.ru/1.x/?ll=" + lon + "," + lat +
                "&size=600,400&z=12&l=map"
                 +"&pt=" + lon + "," + lat + ",pm2rdm"+
                "&lang=fr_FR";

       /* String mapUrl = "https://static-maps.yandex.ru/1.x/?ll=" + lon + "," + lat +
                "&size=600,400&z=12&l=map" +
                "&pt=" + lon + "," + lat + ",pm2rdm";*/



        Image mapImage = new Image(mapUrl, true); // true pour charger de manière asynchrone.

        // Vérifiez si l'image a une erreur.
        if (mapImage.isError()) {
            System.out.println("Erreur lors du chargement de l'image de la carte.");
            System.out.println("Détails de l'erreur: " + mapImage.getException());
        } else {
            mapView.setImage(mapImage);  // Assurez-vous de mettre l'image dans l'ImageView.
            mapView.setPreserveRatio(true);  // Conservez le ratio de l'image.
            mapView.setFitWidth(600);  // Définissez la largeur de l'ImageView.
            mapView.setFitHeight(400); // Définissez la hauteur de l'ImageView.
        }
    }
}
