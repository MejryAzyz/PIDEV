package controllers;

import API.ExchangeRateService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CurrencyController {
    @FXML
    private ComboBox<String> baseCurrencyComboBox;
    @FXML
    private ComboBox<String> targetCurrencyComboBox;
    @FXML
    private Button convertButton;
    @FXML
    private Label resultLabel;

    @FXML
    public void initialize() {
        // Liste des devises populaires
        List<String> currencies = Arrays.asList("USD", "EUR", "TND", "GBP", "CAD", "JPY");

        // Ajouter les devises aux ComboBox
        baseCurrencyComboBox.getItems().addAll(currencies);
        targetCurrencyComboBox.getItems().addAll(currencies);

        // Définir les valeurs par défaut
        baseCurrencyComboBox.setValue("USD");
        targetCurrencyComboBox.setValue("EUR");

        // Gérer l'événement du bouton
        convertButton.setOnAction(event -> convertCurrency());
    }

    private void convertCurrency() {
        String baseCurrency = baseCurrencyComboBox.getValue();
        String targetCurrency = targetCurrencyComboBox.getValue();

        if (baseCurrency != null && targetCurrency != null) {
            try {
                double rate = ExchangeRateService.getExchangeRate(baseCurrency, targetCurrency);
                resultLabel.setText(String.format("1 %s = %.2f %s", baseCurrency, rate, targetCurrency));
            } catch (IOException e) {
                resultLabel.setText("Erreur lors de la récupération des taux.");
                e.printStackTrace();
            }
        } else {
            resultLabel.setText("Sélectionnez les devises.");
        }
    }
}
