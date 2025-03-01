package entities;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Objects;


public class planning_doc extends planning{
    private SimpleIntegerProperty id_planning = new SimpleIntegerProperty();
    private SimpleIntegerProperty id_doc = new SimpleIntegerProperty();
    private SimpleObjectProperty<Date> date_j = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Time> h_deb = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Time> h_fin = new SimpleObjectProperty<>();

    public planning_doc() {
    }

    public planning_doc(int id_doc, Date date_j, Time h_deb, Time h_fin) {
        this.id_doc = new SimpleIntegerProperty(id_doc);
        this.date_j = new SimpleObjectProperty<>(date_j);
        this.h_deb = new SimpleObjectProperty<>(h_deb);
        this.h_fin  = new SimpleObjectProperty<>(h_fin);
    }
//*******************************************************************************************

    public int getId_planning() { return id_planning.get(); }

    public SimpleIntegerProperty idPlanProperty() { return id_planning; }

//************************************************************************************************

    public int getId_doc() {
        return id_doc.get();
    }

    public SimpleIntegerProperty idDocProperty() { return id_doc; }

//***************************************************************************************************
    public Date getDate_j() {
        return date_j.get();
    }

    public SimpleObjectProperty<Date> dateProperty() {return date_j;}

//****************************************************************************************************

    public Time getH_deb() {
        return h_deb.get();
    }

    public SimpleObjectProperty<Time> h_debProperty() {return h_deb;}

//********************************************************************************************************

    public Time getH_fin() {
        return h_fin.get();
    }

    public SimpleObjectProperty<Time> h_finProperty() {return h_fin;}

//********************************************************************************************************


    public void setId_planning(int id_planning) { this.id_planning.set(id_planning); }

    public void setId_doc(int id_doc) {
        this.id_doc.set(id_doc);
    }

    public void setDate_j(Date date_j) {
        this.date_j.set(date_j);
    }

    public void setH_deb(Time h_deb) { this.h_deb.set(h_deb); }

    public void setH_fin(Time h_fin) {
        this.h_fin.set(h_fin);
    }

//***********************************************************************************************************

    @Override
    public String toString() {
        return "planning_doc { " +
                "ID planning = " + getId_planning() +
                "ID docteur = " + id_doc.get() +
                ", date du jour = " + date_j.get() +
                ", heure debut = " + h_deb.get() +
                ", heure fin = " + h_fin.get() +
                " }" ;
    }

        public boolean timeOverlap(planning_doc p)
        {
            LocalTime start1 = this.getH_deb().toLocalTime();
            LocalTime end1 = this.getH_fin().toLocalTime();
            LocalTime start2 = p.getH_deb().toLocalTime();
            LocalTime end2 = p.getH_fin().toLocalTime();

            return start1.isBefore(end2) && start2.isBefore(end1);
        }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        planning_doc p = (planning_doc) o;
        return this.id_doc.get()==p.id_doc.get() && this.date_j.get().equals(p.date_j.get()) && timeOverlap(p);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_doc.get(), date_j.get(), h_deb.get(), h_fin.get());
    }
}
