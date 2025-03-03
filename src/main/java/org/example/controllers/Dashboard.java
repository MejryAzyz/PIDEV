package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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

public class Dashboard {

    @FXML
    private Pane pn_pai;

    @FXML
    private GridPane gridpai;

    @FXML
    private GridPane gridres;


    @FXML
    private Pane pn_home;

    @FXML
    private Pane pn_res;

    @FXML
    private Pane pn_signin;

    @FXML
    private Label revenue;

    ReservationService rs = new ReservationService();
    PaiementService ps = new PaiementService();
    int idu = 1;

    @FXML
    void login(ActionEvent event) {
        pn_home.toFront();
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
        revenue.setText("Total revenue : "+ps.getTotalMontant() + " DT");
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


    private void displayg() {
        ResultSet resultSet = rs.Getall();
        int column = 0;
        int row = 2;
        try {
            while (resultSet.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ReservationPaiment.fxml"));
                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    ReservationPaimentC itemController = fxmlLoader.getController();
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

    public void toReservations(MouseEvent mouseEvent) {
        pn_res.toFront();
        gridres.getChildren().clear();
        displayg();
    }

    public void topaiment(MouseEvent mouseEvent) {
        pn_pai.toFront();
        gridpai.getChildren().clear();
        displayp();
    }

    public void tologin(MouseEvent mouseEvent) {
        pn_signin.toFront();
    }
}
