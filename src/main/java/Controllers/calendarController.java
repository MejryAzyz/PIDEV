package Controllers;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import entities.planning_acc;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.planning_accService;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class calendarController {

    List<planning_acc> data = new ArrayList<>();
    List<planning_acc> data2 = new ArrayList<>();

    public void start(int id_acc){

        try
        {
            planning_accService ps = new planning_accService();
            data = ps.retrieve();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(planning_acc i : data)
        {
            if(i.getId_acc()==id_acc)
            {
                data2.add(i);
            }
        }

        CalendarView CalendarView = new CalendarView();

        Scene scene = new Scene(CalendarView, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/Styles/calendarStyle.css").toExternalForm());

        Calendar calendar = new Calendar("My Calendar");
        calendar.setStyle(Calendar.Style.STYLE2);

        for(planning_acc i : data2)
        {
            LocalDate eventDate = i.getDate_j().toLocalDate();
            LocalTime startTime = i.getH_deb().toLocalTime();
            LocalTime endTime = i.getH_fin().toLocalTime();

            Entry<String> event = new Entry<>("accompagnement");
            event.setInterval(eventDate, startTime, eventDate, endTime);

            calendar.addEntry(event);
        }

        CalendarSource calendarSource = new CalendarSource("My Calendars");
        calendarSource.getCalendars().add(calendar);

        CalendarView.getCalendarSources().add(calendarSource);

        Stage Stage = new Stage();

        Stage.setScene(scene);
        Stage.setTitle("display");
        Stage.show();

    }
}
