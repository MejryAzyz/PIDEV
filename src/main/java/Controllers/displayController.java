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
import javafx.scene.control.*;
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
import java.sql.Time;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class displayController {

    @FXML
    public AnchorPane Hbar;
    @FXML
    public VBox Vbar;


    @FXML
    private TableView<planning> tabview;

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

    @FXML
    private Button search_button;

    @FXML
    private TextField search_input;

    @FXML
    private  Button cancel_search;

    public boolean isDocteur;


    private ObservableList<planning> docteurs = FXCollections.observableArrayList();
    private ObservableList<planning> accompagnants = FXCollections.observableArrayList();



    @FXML
    public void initialize() {

        docteur.getStyleClass().add("selected");

        planning_docService ps = new planning_docService();

        delete.disableProperty().bind(Bindings.isNull(tabview.getSelectionModel().selectedItemProperty()));
        update.disableProperty().bind(Bindings.isNull(tabview.getSelectionModel().selectedItemProperty()));

        isDocteur=true;


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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setContentText("are you sure you want to delete?");
        alert.showAndWait().ifPresent(response -> {
            if(response == ButtonType.OK)
            {
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
        });

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

        isDocteur = false;

        /*tabview.getItems().clear();*/
        tabview.getColumns().clear();


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
                return null;
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
        /*tabview.getItems().clear();*/
        tabview.getColumns().clear();
        initialize();
    }

    private void toggleSelection(Button selected, Button other) {

        selected.getStyleClass().removeIf(s -> s.equals("selected"));
        other.getStyleClass().removeIf(s -> s.equals("selected"));

        selected.getStyleClass().add("selected");
    }

    @FXML
    private void searchAction(){

        String time = "^([01]?[0-9]|2[0-3]):([0-5]?[0-9])$";
        String date = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
        String number = "^\\d+$";

        Pattern timePattern = Pattern.compile(time);
        Pattern datePattern = Pattern.compile(date);
        Pattern numberPattern = Pattern.compile(number);

        String keyword = search_input.getText();

        Matcher timeMatcher = timePattern.matcher(keyword);
        Matcher dateMatcher = datePattern.matcher(keyword);
        Matcher numberMatcher = numberPattern.matcher(keyword);

        ObservableList<planning> results = FXCollections.observableArrayList();


        if(tabview.getColumns().isEmpty())
        {
            TableColumn<planning,Integer> id_planning = new TableColumn<>("ID Planning");
            id_planning.setCellValueFactory(cellData -> cellData.getValue().idPlanProperty().asObject());

            tabview.getColumns().add(id_planning);

            if(isDocteur) {
                TableColumn<planning,Integer> id_docteur = new TableColumn<>("ID Docteur");
                id_docteur.setCellValueFactory(cellData -> {
                    planning p = cellData.getValue();
                    if (p instanceof planning_doc) {
                        return ((planning_doc) p).idDocProperty().asObject();
                    }
                    return null;
                });
                tabview.getColumns().add(id_docteur);
            }
            else {
                TableColumn<planning, Integer> id_accompagnant = new TableColumn<>("ID Accompagnant");
                id_accompagnant.setCellValueFactory(cellData -> {
                    planning p = cellData.getValue();
                    if (p instanceof planning_acc) {
                        return ((planning_acc) p).idAccProperty().asObject();
                    }
                    return null; // Return null for other subclasses
                });
                tabview.getColumns().add(id_accompagnant);
            }

            TableColumn<planning,String> date_jour = new TableColumn<>("Date Jour");
            date_jour.setCellValueFactory(cellData -> cellData.getValue().dateProperty().asString());

            TableColumn<planning,String> heure_debut = new TableColumn<>("Heure Debut");
            heure_debut.setCellValueFactory(cellData -> cellData.getValue().h_debProperty().asString());

            TableColumn<planning,String> heure_fin = new TableColumn<>("Heure Fin");
            heure_fin.setCellValueFactory(cellData -> cellData.getValue().h_finProperty().asString());

            tabview.getColumns().addAll(date_jour,heure_debut,heure_fin);
            tabview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }


        if(isDocteur)
        {
            if(timeMatcher.matches())
            {
                    results = docteurs.filtered(planning ->{
                    planning_doc p = (planning_doc) planning;
                    int l = p.getH_deb().toString().length();
                    return (p.getH_deb().toString().substring(0, l-3)
                            .equals(keyword)) || (p.getH_fin().toString().substring(0, l-3)
                            .equals(keyword));
                    });
            }
            else if(dateMatcher.matches())
                {
                    results = docteurs.filtered(planning ->{
                        planning_doc p = (planning_doc) planning;
                        return p.getDate_j().toString().equals(keyword);
                    });
                }
                else if(numberMatcher.matches())
                    {
                        results = docteurs.filtered(planning ->{
                            planning_doc p = (planning_doc) planning;
                            return p.getId_doc() == Integer.parseInt(keyword);
                        });
                    }
                else
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("FAIL");
                        alert.setContentText("Enter valid keyword");
                        alert.showAndWait();
                        return;
                    }

        }
        else
        {

            if(timeMatcher.matches())
            {
                results = accompagnants.filtered(planning ->{
                    planning_acc p = (planning_acc) planning;
                    int l = p.getH_deb().toString().length();
                    return (p.getH_deb().toString().substring(0, l-3)
                            .equals(keyword)) || (p.getH_fin().toString().substring(0, l-3)
                            .equals(keyword));
                });
            }
            else if(dateMatcher.matches())
            {
                results = accompagnants.filtered(planning ->{
                    planning_acc p = (planning_acc) planning;
                    return p.getDate_j().toString().equals(keyword);
                });
            }
            else if(numberMatcher.matches())
            {
                results = accompagnants.filtered(planning ->{
                    planning_acc p = (planning_acc) planning;
                    return p.getId_acc() == Integer.parseInt(keyword);
                });
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("FAIL");
                alert.setContentText("Enter valid keyword");
                alert.showAndWait();
                return;
            }

        }
        tabview.setItems(results);
    }

    @FXML
    private void cancelSearchAction(){
        search_input.clear();
        if(isDocteur)
        {
            showDocteur(new ActionEvent());
        }
        else
        {
            showAcc(new ActionEvent());
        }
    }

}

