package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Clinique;

public class AfficherClinique2Controller {
    @FXML
    private TableView<Clinique> table_clinique;

    @FXML
    private TableColumn<Clinique, String> col_adesse;

    @FXML
    private TableColumn<Clinique, String> col_desc;

    @FXML
    private TableColumn<Clinique, String> col_email;

    @FXML
    private TableColumn<Clinique, Integer> col_id;

    @FXML
    private TableColumn<Clinique, String> col_nom;

    @FXML
    private TableColumn<Clinique, Double> col_prix;

    @FXML
    private TableColumn<Clinique, Integer> col_rate;

    @FXML
    private TableColumn<Clinique, String> col_tel;

    @FXML
    private Pane sidebar;
    @FXML
    private ImageView logo;
    @FXML
    private VBox sidebarButtons;  // VBox holding the buttons
    @FXML
    private ToggleButton toggleSidebarBtn;
    @FXML
    private StackPane sidebarBackground;

    // List of all the label elements
    @FXML
    private Label labelDashboard;
    @FXML
    private Label labelUserManagement;
    @FXML
    private Label labelClinicalManagement;
    @FXML
    private Label labelSpecialtyManagement;
    @FXML
    private Label labelTransportManagement;
    @FXML
    private Label labelAccommodationManagement;
    @FXML
    private Label labelReservationManagement;
    @FXML
    private Label labelPaymentManagement;
    @FXML
    private Label labelSupportManagement;
    @FXML
    private Label labelLogout;
    @FXML
    private TableColumn<Clinique, String> colActions;  // Add this line

    // List of all the icon elements
    @FXML
    private Text iconDashboard;
    @FXML
    private Text iconUserManagement;
    @FXML
    private Text iconClinicalManagement;
    @FXML
    private Text iconSpecialtyManagement;
    @FXML
    private Text iconTransportManagement;
    @FXML
    private Text iconAccommodationManagement;
    @FXML
    private Text iconReservationManagement;
    @FXML
    private Text iconPaymentManagement;
    @FXML
    private Text iconSupportManagement;
    @FXML
    private Text iconLogout;
    @FXML
    private VBox contentBox;

}
