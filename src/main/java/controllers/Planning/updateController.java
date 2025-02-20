package controllers.Planning;

import Models.Planning.planning_doc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.Planning.planning_docService;
import java.sql.Date;

import java.sql.SQLException;
import java.sql.Time;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class updateController {

    @FXML
    private DatePicker date_input_update;

    @FXML
    private TextField hd_input_update;

    @FXML
    private TextField hf_input_update;

    @FXML
    private ChoiceBox<Integer> id_input_update;

    @FXML
    private Button save_update;

    private planning_doc selectedItem;

    private displayController displayController;

    public void setDisplayController(displayController displayController) {
        this.displayController = displayController;
    }

    public void setSelectedItem(planning_doc selectedItem) {
        this.selectedItem = selectedItem;
    }

    public planning_doc getSelectedItem()
    {
        return selectedItem;
    }

    @FXML
    public void fillFields() {
        planning_docService ps = new planning_docService();
        try
        {
            ObservableList<Integer> observableList = FXCollections.observableArrayList(ps.retrieveIdDoc());
            id_input_update.setItems(observableList);

            id_input_update.setValue(selectedItem.getId_doc());
            date_input_update.setValue(selectedItem.getDate_j().toLocalDate());

            String h_deb = selectedItem.getH_deb().toString();
            h_deb = h_deb.substring(0,h_deb.length()-3);

            String h_fin = selectedItem.getH_fin().toString();
            h_fin = h_fin.substring(0,h_fin.length()-3);

            hd_input_update.setText(h_deb);
            hf_input_update.setText(h_fin);

        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }



    @FXML
    void saveUpdateAction(ActionEvent event) {

        planning_docService ps = new planning_docService();

        int id_planning = selectedItem.getId_planning();

        Integer id = id_input_update.getSelectionModel().getSelectedItem();
        Date date = Date.valueOf(date_input_update.getValue());

        String h_deb = hd_input_update.getText();
        String h_fin = hf_input_update.getText();


        String timePattern = "^([01]?[0-9]|2[0-3]):([0-5]?[0-9])$";
        Pattern pattern = Pattern.compile(timePattern);
        Matcher matcher1 = pattern.matcher(h_deb);
        Matcher matcher2 = pattern.matcher(h_fin);


        if(matcher1.matches()&&matcher2.matches())
        {
            planning_doc p = new planning_doc(id,date, Time.valueOf(h_deb+":00"),Time.valueOf(h_fin+":00"));
            if(ps.checkExistence(p)==0 || (ps.checkExistence(p)== selectedItem.getId_planning()))
            {
                try {ps.modifier(id_planning,p);}
                catch (SQLException e){ System.err.println(e.getMessage());}

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("SUCCESS");
                alert.setContentText("planning updated successfully");
                alert.showAndWait();

                displayController.refresh();

                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("FAIL");
                alert.setContentText("failed to update planning,time already taken");
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FAIL");
            alert.setContentText("enter a valid time format HH:mm");
            alert.showAndWait();
        }

    }

}