package controllers.Planning;

import Models.Planning.planning_doc;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import service.Planning.planning_docService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class displayController {

    @FXML
    private TableView<planning_doc> tabview;

    @FXML
    private TableColumn<planning_doc, Integer> id_planning;

    @FXML
    private TableColumn<planning_doc, Integer> id_docteur;

    @FXML
    private TableColumn<planning_doc, String> date_jour;

    @FXML
    private TableColumn<planning_doc, String> heure_debut;

    @FXML
    private TableColumn<planning_doc, String> heure_fin;

    @FXML
    private Button delete;

    @FXML
    private Button update;

    @FXML
    private Button add;




    private ObservableList<planning_doc> data = FXCollections.observableArrayList(); // Observable list for TableView


    @FXML
    public void initialize() {
        // Call your existing method to retrieve data from the database
        planning_docService ps = new planning_docService();
        delete.disableProperty().bind(Bindings.isNull(tabview.getSelectionModel().selectedItemProperty()));
        update.disableProperty().bind(Bindings.isNull(tabview.getSelectionModel().selectedItemProperty()));


        try
        {
            List<planning_doc> list = ps.recuperer();

            // Add all retrieved data to the ObservableList
            data.addAll(list);


            // Bind columns to the Person properties
            id_planning.setCellValueFactory(cellData -> cellData.getValue().idPlanProperty().asObject());
            id_docteur.setCellValueFactory(cellData -> cellData.getValue().idDocProperty().asObject());
            date_jour.setCellValueFactory(cellData -> cellData.getValue().dateProperty().asString());
            heure_debut.setCellValueFactory(cellData -> cellData.getValue().h_debProperty().asString());
            heure_fin.setCellValueFactory(cellData -> cellData.getValue().h_finProperty().asString());


            // Set the ObservableList as the TableView's data source
            tabview.setItems(data);

        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void deleteAction(ActionEvent event)
    {
        // Get the selected item
        planning_doc selectedItem = tabview.getSelectionModel().getSelectedItem();
        planning_docService ps = new planning_docService();


        if (selectedItem != null)
        {
            try
            {
                // Remove from database (assuming delete() is implemented in your service)
                ps.supprimer(selectedItem.getId_planning());
                // Remove from TableView
                tabview.getItems().remove(selectedItem);
            }

            catch (SQLException e)
            {
                System.err.println(e.getMessage());
            }
        }


    }

    @FXML
    private void addAction(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add.fxml"));
            Parent root = loader.load();

            addController addController = loader.getController();
            addController.setDisplayController(this);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("add");
            stage.show();

        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void updateAction(ActionEvent event)
    {
        planning_doc selectedItem = tabview.getSelectionModel().getSelectedItem();

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/update.fxml"));
            Parent root = loader.load();

            updateController updateController = loader.getController();


            updateController.setDisplayController(this);

            updateController.setSelectedItem(selectedItem);

            updateController.fillFields();


            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("update");
            stage.show();

        }
        catch (IOException e)
        {
            //throw new RuntimeException(e);
            System.err.println("update loading error ");

        }

    }

    public void refresh()
    {
        planning_docService ps = new planning_docService();
        try
        {
            data.clear();
            List<planning_doc> list = ps.recuperer();
            data.addAll(list);
            tabview.refresh();
        }
        catch (SQLException e){
            System.err.println(e.getMessage());}
    }
    @FXML

    public void navUser(ActionEvent actionEvent) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUser.fxml"));
            Parent root = loader.load();

            System.out.println("FXML chargé avec succès.");

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Navigation réussie !");
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du FXML : " + e.getMessage());
            e.printStackTrace();
        }

    }
    @FXML

    public void NavSpec(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherSpecialite.fxml"));
            Parent root = loader.load();

            System.out.println("FXML chargé avec succès.");

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Navigation réussie !");
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du FXML : " + e.getMessage());
            e.printStackTrace();
        }

    }
    @FXML

    public void NavTransport(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeTransport.fxml"));
            Parent root = loader.load();

            System.out.println("FXML chargé avec succès.");

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Navigation réussie !");
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du FXML : " + e.getMessage());
            e.printStackTrace();
        }

    }
    @FXML
    void NavHeb(ActionEvent event) {
        try {
            System.out.println("Navigation vers ListeHebergement.fxml...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeHebegement.fxml"));
            Parent root = loader.load();

            System.out.println("FXML chargé avec succès.");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Navigation réussie !");
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du FXML : " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML

    public void NavRes(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation.fxml"));
            Parent root = loader.load();

            System.out.println("FXML chargé avec succès.");

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Navigation réussie !");
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du FXML : " + e.getMessage());
            e.printStackTrace();
        }

    }
    @FXML

    public void NavPlanning(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/display.fxml"));
            Parent root = loader.load();

            System.out.println("FXML chargé avec succès.");

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Navigation réussie !");
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du FXML : " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void NavClinique(ActionEvent actionEvent) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherClinique.fxml"));
            Parent root = loader.load();

            System.out.println("FXML chargé avec succès.");

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Navigation réussie !");
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du FXML : " + e.getMessage());
            e.printStackTrace();
        }

    }

}

