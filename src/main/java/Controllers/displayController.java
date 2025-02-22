package Controllers;

import entities.planning;
import entities.planning_acc;
import entities.planning_doc;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.planning_accService;
import services.planning_docService;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class displayController {

    @FXML
    public AnchorPane Hbar;
    @FXML
    public VBox Vbar;


    @FXML
    private TableView<planning> tabview;

   /* @FXML
    private TableColumn<planning, Integer> id_planning;

    @FXML
    private TableColumn<planning, String> date_jour;

    @FXML
    private TableColumn<planning, String> heure_debut;

    @FXML
    private TableColumn<planning, String> heure_fin;*/

    @FXML
    private Button delete;

    @FXML
    private Button update;

    @FXML
    private Button add;

    @FXML
    private Button docteur;

    @FXML
    private Button accompagnant;

    public boolean isDocteur;


    private ObservableList<planning> docteurs = FXCollections.observableArrayList();// Observable list for TableView
    private ObservableList<planning> accompagnants = FXCollections.observableArrayList();



    @FXML
    public void initialize() {

        docteur.getStyleClass().add("selected");
        // Call your existing method to retrieve data from the database
        planning_docService ps = new planning_docService();

        delete.disableProperty().bind(Bindings.isNull(tabview.getSelectionModel().selectedItemProperty()));
        update.disableProperty().bind(Bindings.isNull(tabview.getSelectionModel().selectedItemProperty()));

        isDocteur=true;

        // Bind columns to the Person properties
        TableColumn<planning,Integer> id_planning = new TableColumn<>("ID Planning");
        id_planning.setCellValueFactory(cellData -> cellData.getValue().idPlanProperty().asObject());

        TableColumn<planning,Integer> id_docteur = new TableColumn<>("ID Docteur");
        id_docteur.setCellValueFactory(cellData -> {
            planning p = cellData.getValue();
            if (p instanceof planning_doc) {
                return ((planning_doc) p).idDocProperty().asObject();
            }
            return null;
        });

        TableColumn<planning,String> date_jour = new TableColumn<>("Date Jour");
        date_jour.setCellValueFactory(cellData -> cellData.getValue().dateProperty().asString());

        TableColumn<planning,String> heure_debut = new TableColumn<>("Heure Debut");
        heure_debut.setCellValueFactory(cellData -> cellData.getValue().h_debProperty().asString());

        TableColumn<planning,String> heure_fin = new TableColumn<>("Heure Fin");
        heure_fin.setCellValueFactory(cellData -> cellData.getValue().h_finProperty().asString());

        tabview.getColumns().addAll(id_planning,id_docteur,date_jour,heure_debut,heure_fin);
        tabview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        try
        {
            List<planning_doc> list = ps.retrieve();
            docteurs.clear();
            docteurs.addAll(list);
            tabview.setItems(docteurs);
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }


    @FXML
    private void deleteAction(ActionEvent event)
    {
        planning selectedItem = tabview.getSelectionModel().getSelectedItem();

        if (isDocteur)
        {
            planning_docService ps = new planning_docService();
            try
            {
                ps.delete(selectedItem.getId_planning());
                tabview.getItems().remove(selectedItem);
            }

            catch (SQLException e)
            {
                System.err.println(e.getMessage());
            }
        }
        else
        {
            planning_accService ps = new planning_accService();
            try
            {
                ps.delete(selectedItem.getId_planning());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/add.fxml"));
            Parent root = loader.load();

            addController addController = loader.getController();
            addController.setDisplayController(this);

            addController.initializeChoiceBox();

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
        planning selectedItem = tabview.getSelectionModel().getSelectedItem();

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/update.fxml"));
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

        try
        {
            if(isDocteur){
                planning_docService ps = new planning_docService();
                docteurs.clear();
                List<planning_doc> list = ps.retrieve();
                docteurs.addAll(list);
                tabview.refresh();
            }
            else
            {
                planning_accService ps = new planning_accService();
                accompagnants.clear();
                List<planning_acc> list = ps.retrieve();
                accompagnants.addAll(list);
                tabview.refresh();
            }
        }
        catch (SQLException e){
            System.err.println(e.getMessage());}
    }

    @FXML
    void showAcc(ActionEvent event) {
        toggleSelection(accompagnant,docteur);

        planning_accService ps = new planning_accService();

        isDocteur = false; // Assuming this flag is defined elsewhere

        // Clear the TableView and its columns
        tabview.getItems().clear();
        tabview.getColumns().clear();

        // Add common columns (using the superclass type `planning`)
        TableColumn<planning, Integer> idPlanningColumn = new TableColumn<>("ID Planning");
        idPlanningColumn.setCellValueFactory(cellData -> cellData.getValue().idPlanProperty().asObject());

        TableColumn<planning, String> dateJourColumn = new TableColumn<>("Date Jour");
        dateJourColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty().asString());

        TableColumn<planning, String> heureDebutColumn = new TableColumn<>("Heure Début");
        heureDebutColumn.setCellValueFactory(cellData -> cellData.getValue().h_debProperty().asString());

        TableColumn<planning, String> heureFinColumn = new TableColumn<>("Heure Fin");
        heureFinColumn.setCellValueFactory(cellData -> cellData.getValue().h_finProperty().asString());


            TableColumn<planning, Integer> idAccompagnantColumn = new TableColumn<>("ID Accompagnant");
            idAccompagnantColumn.setCellValueFactory(cellData -> {
                planning p = cellData.getValue();
                if (p instanceof planning_acc) {
                    return ((planning_acc) p).idAccProperty().asObject();
                }
                return null; // Return null for other subclasses
            });

        tabview.getColumns().addAll(idPlanningColumn,idAccompagnantColumn,dateJourColumn, heureDebutColumn, heureFinColumn);
        tabview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        try {
            List<planning_acc> list = ps.retrieve();
            accompagnants.clear();
            accompagnants.addAll(list);
            tabview.setItems(accompagnants);
        } catch (SQLException e) {
            System.out.println("Error retrieving data: " + e.getMessage());
        }
    }

    @FXML
    void showDocteur(ActionEvent event) {
        toggleSelection(docteur,accompagnant);
        isDocteur = true;
        tabview.getItems().clear();
        tabview.getColumns().clear();
        initialize();
    }

    private void toggleSelection(Button selected, Button other) {

        selected.getStyleClass().removeIf(s -> s.equals("selected"));
        other.getStyleClass().removeIf(s -> s.equals("selected"));

        selected.getStyleClass().add("selected");
    }


}

