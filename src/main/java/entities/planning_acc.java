package entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

public class planning_acc extends planning{
    private SimpleIntegerProperty id_planning = new SimpleIntegerProperty();
    private SimpleIntegerProperty id_acc = new SimpleIntegerProperty();
    private SimpleObjectProperty<Date> date_j = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Time> h_deb = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Time> h_fin = new SimpleObjectProperty<>();

    public planning_acc() {
    }

    public planning_acc(int id_acc, Date date_j, Time h_deb, Time h_fin) {
        this.id_acc = new SimpleIntegerProperty(id_acc);
        this.date_j = new SimpleObjectProperty<>(date_j);
        this.h_deb = new SimpleObjectProperty<>(h_deb);
        this.h_fin  = new SimpleObjectProperty<>(h_fin);
    }

    public int getId_planning() {
        return id_planning.get();
    }

    public int getId_acc() {
        return id_acc.get();
    }

    public Date getDate_j() {
        return date_j.get();
    }

    public Time getH_deb() {
        return h_deb.get();
    }

    public Time getH_fin() {
        return h_fin.get();
    }

    public void setId_planning(int id_planning) { this.id_planning.set(id_planning); }

    public void setId_acc(int id_acc) {
        this.id_acc.set(id_acc);
    }

    public void setDate_j(Date date_j) {
        this.date_j.set(date_j);
    }

    public void setH_deb(Time h_deb) { this.h_deb.set(h_deb); }

    public void setH_fin(Time h_fin) {
        this.h_fin.set(h_fin);
    }
    //*************************************************************************************
    public SimpleIntegerProperty idPlanProperty() { return id_planning; }
    public SimpleIntegerProperty idAccProperty() { return id_acc; }
    public SimpleObjectProperty<Date> dateProperty() {return date_j;}
    public SimpleObjectProperty<Time> h_debProperty() {return h_deb;}
    public SimpleObjectProperty<Time> h_finProperty() {return h_fin;}
    //*************************************************************************************

    @Override
    public String toString() {
        return "planning_acc { " +
                "ID planning" + id_planning.get() +
                "ID accompagnant = " + id_acc.get() +
                ", date du jour = " + date_j.get() +
                ", heure debut = " + h_deb.get() +
                ", heure fin = " + h_fin.get() +
                " }" ;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        planning_acc p = (planning_acc) o;
        return this.id_acc.get()==p.id_acc.get() && this.date_j.get().equals(p.date_j.get()) && (this.h_deb.get().equals(p.h_deb.get()) || this.h_fin.get().equals(p.h_fin.get()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_acc.get(), date_j.get(), h_deb.get(), h_fin.get());
    }
}


