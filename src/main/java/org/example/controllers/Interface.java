package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import org.example.entities.MailSenderReservation;
import org.example.entities.Paiement;
import org.example.entities.Reservation;
import org.example.service.PaiementService;
import org.example.service.ReservationService;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Interface {

    @FXML
    private RadioButton f1;

    @FXML
    private RadioButton f2;

    @FXML
    private RadioButton f3;

    @FXML
    private Pane pn_pai;

    @FXML
    private GridPane gridpai;

    @FXML
    private GridPane gridres;

    @FXML
    private Label id;

    @FXML
    private Pane pn_adda;

    @FXML
    private Pane pn_addc;

    @FXML
    private Pane pn_addres;

    @FXML
    private Pane pn_addt;

    @FXML
    private Pane pn_home;

    @FXML
    private Pane pn_res;

    @FXML
    private Pane pn_signin;

    @FXML
    private RadioButton radioa;

    @FXML
    private RadioButton radioc;

    @FXML
    private RadioButton radiot;

    @FXML
    private TextField tf_accomodation;

    @FXML
    private DatePicker tf_checkin;

    @FXML
    private DatePicker tf_checkout;

    @FXML
    private DatePicker tf_date;

    @FXML
    private TextField tf_transport;

    @FXML
    private TextField tf_time;

    @FXML
    private TextField tf_clinic;

    @FXML
    private TextField tf_log;

    ReservationService rs = new ReservationService();
    PaiementService ps = new PaiementService();
    int idu = 1;

    @FXML
    void login(ActionEvent event) {
        pn_home.toFront();
    }

    @FXML
    void submita(ActionEvent event) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        if (tf_accomodation.getText().isEmpty() || tf_checkin.getValue()==null ||tf_checkout.getValue()==null ||tf_checkin.getValue().isBefore(currentDate)||tf_checkout.getValue().isBefore(currentDate)) {
            // Afficher un message d'alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs et entrer une date valide !");
            alert.showAndWait();
            return;
        }
        if (!tf_accomodation.getText().matches("\\d+")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Format invalide");
            alert.setHeaderText(null);
            alert.setContentText("Le champ hébergement doit être un nombre !");
            alert.showAndWait();
            return;
        }
        int accomodation =Integer.valueOf(tf_accomodation.getText());
        LocalDate localDateDebut = tf_checkin.getValue();
        LocalDate localDateFin = tf_checkout.getValue();
        // Conversion de LocalDate en java.sql.Date
        Date datedebut = Date.valueOf(localDateDebut);
        Date datefin = Date.valueOf(localDateFin);
        Reservation r = new Reservation(idu,accomodation,datedebut,datefin,"in progress");
        rs.ajouterEntite(r);
        MailSenderReservation ms = new MailSenderReservation();
        try {
            ms.sendMail("beyaabid876@gmail.com");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tf_accomodation.clear();
        tf_checkin.setValue(null);  // Clears the selected date in tf_checkin
        tf_checkout.setValue(null); // Clears the selected date in tf_checkout
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Valider");
        alert.setHeaderText(null);
        alert.setContentText("Reservation added succesfully !");
        alert.showAndWait();
        pn_res.toFront();
        gridres.getChildren().clear();
        displayg();
    }

    @FXML
    void submitc(ActionEvent event) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        if (tf_clinic.getText().isEmpty() ) {
            // Afficher un message d'alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.showAndWait();
            return;
        }
        if (!tf_clinic.getText().matches("\\d+")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Format invalide");
            alert.setHeaderText(null);
            alert.setContentText("Le champ hébergement doit être un nombre !");
            alert.showAndWait();
            return;
        }
        int clinic =Integer.valueOf(tf_clinic.getText());
        Reservation r = new Reservation(idu,clinic,"in progress");
        rs.ajouterEntite(r);
        MailSenderReservation ms = new MailSenderReservation();
        try {
            ms.sendMail("beyaabid876@gmail.com");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tf_clinic.clear();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Valider");
        alert.setHeaderText(null);
        alert.setContentText("Reservation added succesfully !");
        alert.showAndWait();
        pn_res.toFront();
        gridres.getChildren().clear();
        displayg();
    }

    @FXML
    void submitt(ActionEvent event) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        if (tf_transport.getText().isEmpty() || tf_time.getText().isEmpty() || tf_date.getValue()==null || tf_date.getValue().isBefore(currentDate)||    !Pattern.matches("([01]?[0-9]|2[0-3]):([0-5]?[0-9])", tf_time.getText())) {
            // Afficher un message d'alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs et entrer une date valide !");
            alert.showAndWait();
            return;
        }
        if (!tf_transport.getText().matches("\\d+")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Format invalide");
            alert.setHeaderText(null);
            alert.setContentText("Le champ hébergement doit être un nombre !");
            alert.showAndWait();
            return;
        }
        int transport =Integer.valueOf(tf_transport.getText());
        String time =tf_time.getText();
        LocalDate localDate = tf_date.getValue();
        // Conversion de LocalDate en java.sql.Date
        Date date = Date.valueOf(localDate);
        Reservation r = new Reservation(idu,transport,date,time,"in progress");
        rs.ajouterEntite(r);
        MailSenderReservation ms = new MailSenderReservation();
        try {
            ms.sendMail("beyaabid876@gmail.com");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tf_time.clear();
        tf_transport.clear();
        tf_date.setValue(null);  //
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Valider");
        alert.setHeaderText(null);
        alert.setContentText("Reservation added succesfully !");
        alert.showAndWait();
        pn_res.toFront();
        gridres.getChildren().clear();
        displayg();
    }

    @FXML
    void toHome(ActionEvent event) {
        pn_home.toFront();
    }

    @FXML
    void toSignup(ActionEvent event) {
        // Your toSignup logic
    }

    @FXML
    void toUpdate(ActionEvent event) {
        pn_pai.toFront();
        gridpai.getChildren().clear();
        displayp();
    }

    @FXML
    void toaddres(ActionEvent event) {
        pn_addres.toFront();
        // Make sure no radio buttons are selected initially
        radiot.setSelected(false);
        radioc.setSelected(false);
        radioa.setSelected(false);

        // Hide all panes
        pn_adda.setVisible(false);
        pn_addc.setVisible(false);
        pn_addt.setVisible(false);
    }

    @FXML
    void tores(ActionEvent event) {
        pn_res.toFront();
        gridres.getChildren().clear();
        displayg();
    }

    @FXML
    void tosignin(ActionEvent event) {
        pn_signin.toFront();
    }

    // This method is triggered when "Radioa" is selected
    @FXML
    void onRadioaSelected(ActionEvent event) {
        // Deselect other radio buttons
        radiot.setSelected(false);
        radioc.setSelected(false);

        // Show the relevant Pane and hide the others
        pn_adda.setVisible(true);
        pn_addc.setVisible(false);
        pn_addt.setVisible(false);
    }

    // This method is triggered when "Radioc" is selected
    @FXML
    void onRadiocSelected(ActionEvent event) {
        // Deselect other radio buttons
        radiot.setSelected(false);
        radioa.setSelected(false);

        // Show the relevant Pane and hide the others
        pn_adda.setVisible(false);
        pn_addc.setVisible(true);
        pn_addt.setVisible(false);
    }

    // This method is triggered when "Radiot" is selected
    @FXML
    void onRadiotSelected(ActionEvent event) {
        // Deselect other radio buttons
        radioa.setSelected(false);
        radioc.setSelected(false);

        // Show the relevant Pane and hide the others
        pn_adda.setVisible(false);
        pn_addc.setVisible(false);
        pn_addt.setVisible(true);
    }

    private void displayg() {
        ResultSet resultSet = rs.Getall();
        int column = 0;
        int row = 2;
        try {
            while (resultSet.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Reservation.fxml"));
                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    ReservationC itemController = fxmlLoader.getController();
                    int idReservation = resultSet.getInt("id_reservation");
                    int idPatient = resultSet.getInt("id_patient");
                    int idClinique = resultSet.getInt("id_clinique");
                    int idTransport = resultSet.getInt("id_transport");
                    Date dateDepart = resultSet.getDate("date_depart");
                    String heureDepart = resultSet.getString("heure_depart");
                    int idHebergement = resultSet.getInt("id_hebergement");
                    Date dateDebut = resultSet.getDate("date_debut");
                    Date dateFin = resultSet.getDate("date_fin");
                    String statut = resultSet.getString("statut");
                    Reservation reservation = new Reservation(
                            idReservation, idPatient, idClinique, idTransport,
                            dateDepart, heureDepart, idHebergement, dateDebut, dateFin, statut
                    );
                    itemController.setData(reservation);
                    if (column == 1) {
                        column = 0;
                        row++;
                    }
                    gridres.add(anchorPane, column++, row); //(child,column,row)
                    //set grid width
                    gridres.setMinWidth(Region.USE_COMPUTED_SIZE);
                    gridres.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    gridres.setMaxWidth(Region.USE_PREF_SIZE);
                    //set grid height
                    gridres.setMinHeight(Region.USE_COMPUTED_SIZE);
                    gridres.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    gridres.setMaxHeight(Region.USE_PREF_SIZE);
                    GridPane.setMargin(anchorPane, new Insets(10));
                } catch (IOException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void displayp() {
        ResultSet resultSet = ps.Getall();
        int column = 0;
        int row = 2;
        try {
            while (resultSet.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Paiement.fxml"));
                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    PaiementC itemController = fxmlLoader.getController();
                    int idPaiement = resultSet.getInt("id_paiement");
                    int idReservation = resultSet.getInt("id_reservation");
                    double montant = resultSet.getDouble("montant");
                    Timestamp datePaiement = resultSet.getTimestamp("date_paiement");
                    String methode = resultSet.getString("methode");
                    String type = resultSet.getString("type");
                    Paiement paiement = new Paiement(idPaiement, idReservation, montant, datePaiement, methode);
                    if(paiement.getMethode()!=null)
                    {
                        itemController.setData(paiement,type);
                        if (column == 1) {
                            column = 0;
                            row++;
                        }
                        gridpai.add(anchorPane, column++, row); //(child,column,row)
                        //set grid width
                        gridpai.setMinWidth(Region.USE_COMPUTED_SIZE);
                        gridpai.setPrefWidth(Region.USE_COMPUTED_SIZE);
                        gridpai.setMaxWidth(Region.USE_PREF_SIZE);
                        //set grid height
                        gridpai.setMinHeight(Region.USE_COMPUTED_SIZE);
                        gridpai.setPrefHeight(Region.USE_COMPUTED_SIZE);
                        gridpai.setMaxHeight(Region.USE_PREF_SIZE);
                        GridPane.setMargin(anchorPane, new Insets(10));
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void displaypm() {
        ResultSet resultSet = ps.Getallm();
        int column = 0;
        int row = 2;
        try {
            while (resultSet.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Paiement.fxml"));
                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    PaiementC itemController = fxmlLoader.getController();
                    int idPaiement = resultSet.getInt("id_paiement");
                    int idReservation = resultSet.getInt("id_reservation");
                    double montant = resultSet.getDouble("montant");
                    Timestamp datePaiement = resultSet.getTimestamp("date_paiement");
                    String methode = resultSet.getString("methode");
                    String type = resultSet.getString("type");
                    Paiement paiement = new Paiement(idPaiement, idReservation, montant, datePaiement, methode);
                    if(paiement.getMethode()!=null)
                    {
                        itemController.setData(paiement,type);
                        if (column == 1) {
                            column = 0;
                            row++;
                        }
                        gridpai.add(anchorPane, column++, row); //(child,column,row)
                        //set grid width
                        gridpai.setMinWidth(Region.USE_COMPUTED_SIZE);
                        gridpai.setPrefWidth(Region.USE_COMPUTED_SIZE);
                        gridpai.setMaxWidth(Region.USE_PREF_SIZE);
                        //set grid height
                        gridpai.setMinHeight(Region.USE_COMPUTED_SIZE);
                        gridpai.setPrefHeight(Region.USE_COMPUTED_SIZE);
                        gridpai.setMaxHeight(Region.USE_PREF_SIZE);
                        GridPane.setMargin(anchorPane, new Insets(10));
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void displaypd() {
        ResultSet resultSet = ps.Getalld();
        int column = 0;
        int row = 2;
        try {
            while (resultSet.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Paiement.fxml"));
                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    PaiementC itemController = fxmlLoader.getController();
                    int idPaiement = resultSet.getInt("id_paiement");
                    int idReservation = resultSet.getInt("id_reservation");
                    double montant = resultSet.getDouble("montant");
                    Timestamp datePaiement = resultSet.getTimestamp("date_paiement");
                    String methode = resultSet.getString("methode");
                    String type = resultSet.getString("type");
                    Paiement paiement = new Paiement(idPaiement, idReservation, montant, datePaiement, methode);
                    if(paiement.getMethode()!=null)
                    {
                        itemController.setData(paiement,type);
                        if (column == 1) {
                            column = 0;
                            row++;
                        }
                        gridpai.add(anchorPane, column++, row); //(child,column,row)
                        //set grid width
                        gridpai.setMinWidth(Region.USE_COMPUTED_SIZE);
                        gridpai.setPrefWidth(Region.USE_COMPUTED_SIZE);
                        gridpai.setMaxWidth(Region.USE_PREF_SIZE);
                        //set grid height
                        gridpai.setMinHeight(Region.USE_COMPUTED_SIZE);
                        gridpai.setPrefHeight(Region.USE_COMPUTED_SIZE);
                        gridpai.setMaxHeight(Region.USE_PREF_SIZE);
                        GridPane.setMargin(anchorPane, new Insets(10));
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    @FXML
    void f1(ActionEvent event) {
        f2.setSelected(false);
        f3.setSelected(false);
        gridres.getChildren().clear();
        filter("t");
    }

    @FXML
    void f2(ActionEvent event) {
        f1.setSelected(false);
        f3.setSelected(false);
        gridres.getChildren().clear();
        filter("a");
    }

    @FXML
    void f3(ActionEvent event) {
        f1.setSelected(false);
        f2.setSelected(false);
        gridres.getChildren().clear();
        filter("c");
    }
    @FXML
    void trim(ActionEvent event) {
        gridpai.getChildren().clear();
        displaypm();

    }
    @FXML
    void trid(ActionEvent event) {
        gridpai.getChildren().clear();
        displaypd();

    }

    private void filter(String s) {
        ResultSet resultSet = rs.GetallFilter(s);
        int column = 0;
        int row = 2;
        try {
            while (resultSet.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Reservation.fxml"));
                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    ReservationC itemController = fxmlLoader.getController();
                    int idReservation = resultSet.getInt("id_reservation");
                    int idPatient = resultSet.getInt("id_patient");
                    int idClinique = resultSet.getInt("id_clinique");
                    int idTransport = resultSet.getInt("id_transport");
                    Date dateDepart = resultSet.getDate("date_depart");
                    String heureDepart = resultSet.getString("heure_depart");
                    int idHebergement = resultSet.getInt("id_hebergement");
                    Date dateDebut = resultSet.getDate("date_debut");
                    Date dateFin = resultSet.getDate("date_fin");
                    String statut = resultSet.getString("statut");
                    Reservation reservation = new Reservation(
                            idReservation, idPatient, idClinique, idTransport,
                            dateDepart, heureDepart, idHebergement, dateDebut, dateFin, statut
                    );
                    itemController.setData(reservation);
                    if (column == 1) {
                        column = 0;
                        row++;
                    }
                    gridres.add(anchorPane, column++, row); //(child,column,row)
                    //set grid width
                    gridres.setMinWidth(Region.USE_COMPUTED_SIZE);
                    gridres.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    gridres.setMaxWidth(Region.USE_PREF_SIZE);
                    //set grid height
                    gridres.setMinHeight(Region.USE_COMPUTED_SIZE);
                    gridres.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    gridres.setMaxHeight(Region.USE_PREF_SIZE);
                    GridPane.setMargin(anchorPane, new Insets(10));
                } catch (IOException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//ajouter button dora
    /*public void butt(int id) {
        ReservationService reservationService = new ReservationService();
        Reservation r=new Reservation(1,id,"in progress");// 1 id user
        reservationService.ajouterEntite(r);



    }*/
    //ajout bout

}
