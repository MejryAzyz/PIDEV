package Controllers;
import entities.planning_doc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import services.planning_docService;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class addController {

    @FXML
    private DatePicker date_input;

    @FXML
    private TextField hd_input;

    @FXML
    private TextField hf_input;

    @FXML
    private ChoiceBox<Integer> id_input;

    @FXML
    private Button save;

    private Controllers.displayController displayController;

    public void setDisplayController(displayController displayController) {
        this.displayController = displayController;
    }

    @FXML
    public void initialize() {
        planning_docService ps = new planning_docService();
        try
        {
            ObservableList<Integer> observableList = FXCollections.observableArrayList(ps.retrieveIdDoc());
            id_input.setItems(observableList);
            id_input.setValue(observableList.get(0));
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
    }


    @FXML
    void saveAction(ActionEvent event) {

        planning_docService ps = new planning_docService();

        int id = id_input.getSelectionModel().getSelectedItem();
        Date date = Date.valueOf(date_input.getValue());
        String h_deb = hd_input.getText();
        String h_fin = hf_input.getText();

        String timePattern = "^([01]?[0-9]|2[0-3]):([0-5]?[0-9])$";
        Pattern pattern = Pattern.compile(timePattern);
        Matcher matcher1 = pattern.matcher(h_deb);
        Matcher matcher2 = pattern.matcher(h_fin);


        if(matcher1.matches() && matcher2.matches() && !(date_input.getValue().isBefore(LocalDate.now())))
        {
            planning_doc p = new planning_doc(id,date, Time.valueOf(h_deb+":00"),Time.valueOf(h_fin+":00"));

            if(ps.checkExistence(p)==0)
            {
                try {ps.add(p);}
                catch (SQLException e){ System.err.println(e.getMessage());}

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("SUCCESS");
                alert.setContentText("planning added with success");
                alert.showAndWait();

                displayController.refresh();

                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("FAIL");
                alert.setContentText("planning not added,time already taken");
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FAIL");
            alert.setContentText("enter a valid Time or Date");
            alert.showAndWait();
        }

    }

}