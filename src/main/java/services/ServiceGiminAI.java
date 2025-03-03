package services;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Clinique;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import tools.MyDataBase;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServiceGiminAI {
    private static final String API_KEY = System.getenv("api_gemini");
    private static final String API_URL = System.getenv("api_url");// Remplacer par l'URL exacte de l'API

    Connection cnx;
    public ServiceGiminAI() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @FXML
    private Label messageLabel;

    /*public JSONArray rechercherCliniquesParSpecialite(double budgetMax, String specialite) throws IOException {
        // Construction de l'URL avec les paramètres
        String url = String.format("%s?specialite=%s&budgetMax=%f", API_URL, specialite, budgetMax);

        // Création de la requête
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", "Bearer " + API_KEY);

        // Exécution de la requête
        try (CloseableHttpResponse response = client.execute(request)) {
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getJSONArray("cliniques");
        } finally {
            client.close();
        }
    }*/

    public JSONArray rechercherCliniquesParPrix(double budget) throws IOException {
        // Créez l'URL selon le budget
        String url = String.format("%s?prixMax=%f", API_URL, budget);

        // Créez et envoyez la requête
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", "Bearer " + API_KEY);

        // Exécution de la requête
        try (CloseableHttpResponse response = client.execute(request)) {
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getJSONArray("cliniques");
        } finally {
            client.close();
        }
    }
    public JSONArray rechercherCliniquesParBesoins(String question) {
        String requeteSQL = construireRequeteSQL(question);
        if (requeteSQL == null) {
            return new JSONArray(); // Retourne une liste vide si la question n'est pas reconnue
        }

        List<Clinique> cliniques = new ArrayList<>();
        try (PreparedStatement preparedStatement = cnx.prepareStatement(requeteSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Clinique clinique = new Clinique(
                        resultSet.getInt("id_clinique"),
                        resultSet.getString("nom"),
                        resultSet.getString("adresse"),
                        resultSet.getString("description"),
                        resultSet.getString("telephone"),
                        resultSet.getString("email"),
                        resultSet.getDouble("prix")
                );
                cliniques.add(clinique);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertirListeEnJSON(cliniques);
    }

    private String construireRequeteSQL(String question) {
        // Analyser la question et construire la requête SQL correspondante
        if (question.matches(".*prix.*inférieur.*(\\d+).*")) {
            // Si la question parle de prix inférieur
            int prix = Integer.parseInt(question.replaceAll("\\D+", ""));
            return "SELECT * FROM clinique WHERE prix < " + prix;
        } else if (question.matches(".*prix.*supérieur.*(\\d+).*")) {
            // Si la question parle de prix supérieur
            int prix = Integer.parseInt(question.replaceAll("\\D+", ""));
            return "SELECT * FROM clinique WHERE prix > " + prix;
        } else if (question.matches(".*adresse.*(\\w+).*")) {
            // Si la question parle de l'adresse (ville)
            String ville = question.split(" ")[question.split(" ").length - 1];
            return "SELECT * FROM clinique WHERE adresse LIKE '%" + ville + "%'";
        }
        //messageLabel.setText("Désolé, je peux vous aider uniquement avec les questions concernant le prix ou l'adresse des cliniques.");
        return null;
          // Si la question ne correspond pas à un format connu
    }

    private JSONArray convertirListeEnJSON(List<Clinique> cliniques) {
        JSONArray jsonArray = new JSONArray();
        for (Clinique clinique : cliniques) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_clinique", clinique.getIdClinique());
            jsonObject.put("nom", clinique.getNom());
            jsonObject.put("adresse", clinique.getAdresse());
            jsonObject.put("description", clinique.getDescription());
            jsonObject.put("telephone", clinique.getTelephone());
            jsonObject.put("email", clinique.getEmail());
            jsonObject.put("prix", clinique.getPrix());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
}
