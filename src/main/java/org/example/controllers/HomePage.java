package org.example.controllers;

import atlantafx.base.theme.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage stg;

    @Override
    public void start(Stage primaryStage) {
        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
        try {
            this.stg = primaryStage;
            Parent root = FXMLLoader.load(getClass().getResource("/Interface.fxml")); //mod
            primaryStage.setTitle("Gestion_Reservation_Paiement");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}