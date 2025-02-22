package entities;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.Date;
import java.sql.Time;
import java.util.Objects;


public class planning {
    private SimpleIntegerProperty id_planning = new SimpleIntegerProperty();
    private SimpleIntegerProperty id_person = new SimpleIntegerProperty();
    private SimpleObjectProperty<Date> date_j = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Time> h_deb = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Time> h_fin = new SimpleObjectProperty<>();

    public planning() {
    }

    public planning(Date date_j, Time h_deb, Time h_fin) {
        this.date_j = new SimpleObjectProperty<>(date_j);
        this.h_deb = new SimpleObjectProperty<>(h_deb);
        this.h_fin = new SimpleObjectProperty<>(h_fin);
    }

    public int getId_person() {
        return id_person.get();
    }

    public void setId_person(int id_person) {
        this.id_person.set(id_person);
    }

    public SimpleIntegerProperty id_personProperty() {
        return id_person;
    }

    //*******************************************************************************************

    public int getId_planning() {
        return id_planning.get();
    }


    public SimpleIntegerProperty idPlanProperty() {
        return id_planning;
    }

    //***************************************************************************************************
    public Date getDate_j() {
        return date_j.get();
    }

    public SimpleObjectProperty<Date> dateProperty() {
        return date_j;
    }

//****************************************************************************************************

    public Time getH_deb() {
        return h_deb.get();
    }

    public SimpleObjectProperty<Time> h_debProperty() {
        return h_deb;
    }

//********************************************************************************************************

    public Time getH_fin() {
        return h_fin.get();
    }

    public SimpleObjectProperty<Time> h_finProperty() {
        return h_fin;
    }

//********************************************************************************************************


    public void setId_planning(int id_planning) {
        this.id_planning.set(id_planning);
    }

    public void setDate_j(Date date_j) {
        this.date_j.set(date_j);
    }

    public void setH_deb(Time h_deb) {
        this.h_deb.set(h_deb);
    }

    public void setH_fin(Time h_fin) {
        this.h_fin.set(h_fin);
    }

//***********************************************************************************************************

    @Override
    public String toString() {
        return "planning { " +
                "ID planning = " + getId_planning() +
                ", date du jour = " + date_j.get() +
                ", heure debut = " + h_deb.get() +
                ", heure fin = " + h_fin.get() +
                " }";
    }

}