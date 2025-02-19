package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import Models.Transport;
import service.ServiceTransport;

import java.sql.SQLException;
import java.util.List;

public class ClientTransport {

    @FXML
    private VBox transportContainer;

    public void initialize() throws SQLException {
        afficherTransports();
    }

    private void afficherTransports() throws SQLException {
        ServiceTransport serviceTransport = new ServiceTransport();
        List<Transport> transports = serviceTransport.recuperer();

        for (Transport t : transports) {
            HBox card = createTransportCard(t);
            transportContainer.getChildren().add(card);
        }
    }

    private HBox createTransportCard(Transport transport) {
        HBox card = new HBox(10);
        card.setStyle("-fx-padding: 10px; -fx-border-color: #ddd; -fx-border-radius: 8px; -fx-background-color: #fff;");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(150);
        imageView.setImage(new Image(transport.getPhotoUrl())); // 🔥 Image correcte selon le type de transport


        VBox details = new VBox(5);
        details.setStyle("-fx-padding: 10px; -fx-background-color: #fff; -fx-border-radius: 5px;");

        Label typeLabel = new Label(transport.getType() != null ? transport.getType() : "Type inconnu");
        typeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #333; -fx-font-weight: bold;");

        Label capaciteLabel = new Label("🚗 Capacité : " + transport.getCapacite());
        capaciteLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333; -fx-font-weight: bold;");

        Label tarifLabel = new Label("💰 Tarif : " + transport.getTarif() + " €");
        tarifLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333; -fx-font-weight: bold;");



        details.getChildren().addAll(typeLabel, capaciteLabel, tarifLabel);
        card.getChildren().addAll(imageView, details);

        return card;
    }

}
