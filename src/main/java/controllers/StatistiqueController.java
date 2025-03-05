package controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;
import models.Clinique;
import services.ServiceClinique;

import java.sql.SQLException;
import java.util.List;

public class StatistiqueController {
    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

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
}
