package tests;
import Controllers.calendarController;
import Controllers.displayController;
import Controllers.displayRespController;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import com.calendarfx.view.*;

public class mainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/displayResp.fxml"));
        try
        {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
