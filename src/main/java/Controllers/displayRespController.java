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
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import services.fileChooser;
import services.planning_accService;
import services.planning_docService;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class displayRespController {

    @FXML
    public AnchorPane Hbar;

    @FXML
    private TableView<planning> tabview;

    @FXML
    private Button delete;

    @FXML
    private Button update;

    @FXML
    private Button add;

    @FXML
    private Button search_button;

    @FXML
    private TextField search_input;

    @FXML
    private Button cancel_search;

    @FXML
    private Label nb_doc;

    @FXML
    private Label nb_plan;


    private ObservableList<planning> docteurs = FXCollections.observableArrayList();


    public void initialize() {

        planning_docService ps = new planning_docService();

        delete.disableProperty().bind(Bindings.isNull(tabview.getSelectionModel().selectedItemProperty()));
        update.disableProperty().bind(Bindings.isNull(tabview.getSelectionModel().selectedItemProperty()));


        TableColumn<planning, Integer> id_planning = new TableColumn<>("ID Planning");
        id_planning.setCellValueFactory(cellData -> cellData.getValue().idPlanProperty().asObject());

        TableColumn<planning, Integer> id_docteur = new TableColumn<>("ID Docteur");
        id_docteur.setCellValueFactory(cellData -> {
            planning p = cellData.getValue();
            if (p instanceof planning_doc) {
                return ((planning_doc) p).idDocProperty().asObject();
            }
            return null;
        });

        TableColumn<planning, String> date_jour = new TableColumn<>("Date Jour");
        date_jour.setCellValueFactory(cellData -> cellData.getValue().dateProperty().asString());

        TableColumn<planning, String> heure_debut = new TableColumn<>("Heure Debut");
        heure_debut.setCellValueFactory(cellData -> cellData.getValue().h_debProperty().asString());

        TableColumn<planning, String> heure_fin = new TableColumn<>("Heure Fin");
        heure_fin.setCellValueFactory(cellData -> cellData.getValue().h_finProperty().asString());

        tabview.getColumns().addAll(id_planning, id_docteur, date_jour, heure_debut, heure_fin);
        tabview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        try {
            List<planning_doc> list = ps.retrieve();
            docteurs.clear();
            docteurs.addAll(list);
            tabview.setItems(docteurs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        nb_doc.setText(String.valueOf(numberOfDoctors()));
        nb_plan.setText(String.valueOf(numberOfPlannings()));
    }


    @FXML
    private void deleteAction(ActionEvent event) {

        planning selectedItem = tabview.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setContentText("are you sure you want to delete?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

                    planning_docService ps = new planning_docService();
                    try {
                        ps.delete(selectedItem.getId_planning());
                        tabview.getItems().remove(selectedItem);
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                    }
            }
        });
        refresh();
    }

    @FXML
    private void addAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/RespAdd.fxml"));
            Parent root = loader.load();

            addControllerResp addControllerResp = loader.getController();
            addControllerResp.setDisplayRespController(this);

            addControllerResp.initializeChoiceBox();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("add");
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void updateAction(ActionEvent event) {
        planning selectedItem = tabview.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/RespUpdate.fxml"));
            Parent root = loader.load();

            updateControllerResp updateControllerResp = loader.getController();


            updateControllerResp.setDisplayRespController(this);

            updateControllerResp.setSelectedItem(selectedItem);

            updateControllerResp.fillFields();


            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("update");
            stage.show();

        } catch (IOException e) {
            //throw new RuntimeException(e);
            System.err.println("update loading error ");

        }

    }

    public void refresh() {

        try {
                planning_docService ps = new planning_docService();
                docteurs.clear();
                List<planning_doc> list = ps.retrieve();
                docteurs.addAll(list);
                tabview.refresh();
                nb_doc.setText(String.valueOf(numberOfDoctors()));
                nb_plan.setText(String.valueOf(numberOfPlannings()));

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
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


                TableColumn<planning,Integer> id_docteur = new TableColumn<>("ID Docteur");
                id_docteur.setCellValueFactory(cellData -> {
                    planning p = cellData.getValue();
                    if (p instanceof planning_doc) {
                        return ((planning_doc) p).idDocProperty().asObject();
                    }
                    return null;
                });
                tabview.getColumns().add(id_docteur);

            TableColumn<planning,String> date_jour = new TableColumn<>("Date Jour");
            date_jour.setCellValueFactory(cellData -> cellData.getValue().dateProperty().asString());

            TableColumn<planning,String> heure_debut = new TableColumn<>("Heure Debut");
            heure_debut.setCellValueFactory(cellData -> cellData.getValue().h_debProperty().asString());

            TableColumn<planning,String> heure_fin = new TableColumn<>("Heure Fin");
            heure_fin.setCellValueFactory(cellData -> cellData.getValue().h_finProperty().asString());

            tabview.getColumns().addAll(date_jour,heure_debut,heure_fin);
            tabview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }


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

        tabview.setItems(results);
    }

    @FXML
    private void cancelSearchAction(){
        search_input.clear();
        tabview.getColumns().clear();
        initialize();

    }

    public int numberOfDoctors(){

        Set<Integer> set = new HashSet<>();
        for(planning i : docteurs)
        {
            if(i instanceof planning_doc)
            {
                planning_doc doc = (planning_doc) i;
                set.add(doc.getId_doc());
            }
        }
        return set.size();
    }

    public int numberOfPlannings(){
        int nb = 0;
        for(planning i : docteurs)
        {
            nb++;
        }
        return nb;
    }

    @FXML
    private void export(){
        fileChooser.chooseSaveLocationAndExport(tabview);
    }
}