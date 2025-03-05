package controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Clinique;
import services.ServiceClinique;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class StatistiqueController {
    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Button retourButton;


    private final ServiceClinique sc = new ServiceClinique();

    public void initialize() {
        try {
            mettreAJourGraphique();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mettreAJourGraphique() throws SQLException {
        List<Clinique> cliniques = sc.recuperer();

        if (!cliniques.isEmpty()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Prix des Cliniques");

            for (Clinique clinique : cliniques) {
                series.getData().add(new XYChart.Data<>(clinique.getNom(), clinique.getPrix()));
            }

            barChart.getData().clear();
            barChart.getData().add(series);

            for (XYChart.Data<String, Number> data : series.getData()) {
                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        newNode.setStyle("-fx-bar-fill: #3498db;"); // Bleu
                        newNode.setOpacity(0);

                        FadeTransition ft = new FadeTransition(Duration.seconds(1), newNode);
                        ft.setFromValue(0);
                        ft.setToValue(1);
                        ft.play();
                    }
                });
            }

        }
    }

    @FXML
    private void retourAction() {
        try {
            // Charger la scène précédente (par exemple, la vue principale)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherClinique.fxml"));
            Parent root = loader.load();

            // Obtenez la scène actuelle (celle des statistiques)
            Stage stage = (Stage) retourButton.getScene().getWindow();

            // Définir la scène précédente
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
