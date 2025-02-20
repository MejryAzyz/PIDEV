package Models.Planning;

import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

public class planning_acc{
    private int id_planning;
    private int id_acc;
    private Date date_j;
    private Time h_deb;
    private Time h_fin;

    public planning_acc() {
    }

    public planning_acc(int id_acc, Date date_j, Time h_deb, Time h_fin) {
        this.id_acc = id_acc;
        this.date_j = date_j;
        this.h_deb = h_deb;
        this.h_fin = h_fin;
    }

    public int getId_acc() {
        return id_acc;
    }

    public Date getDate_j() {
        return date_j;
    }

    public Time getH_deb() {
        return h_deb;
    }

    public Time getH_fin() {
        return h_fin;
    }

    public void setId_acc(int id_acc) {
        this.id_acc = id_acc;
    }

    public void setDate_j(Date date_j) {
        this.date_j = date_j;
    }

    public void setH_deb(Time h_deb) {
        this.h_deb = h_deb;
    }

    public void setH_fin(Time h_fin) {
        this.h_fin = h_fin;
    }

    @Override
    public String toString() {
        return "planning_acc { " +
                "ID accompagnant = " + id_acc+
                ", date du jour = " + date_j +
                ", heure debut = " + h_deb +
                ", heure fin = " + h_fin +
                " }" ;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        planning_acc p = (planning_acc) o;
        return this.id_acc==p.id_acc && this.date_j.equals(p.date_j) && (this.h_deb.equals(p.h_deb) || this.h_fin.equals(p.h_fin));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_acc, date_j, h_deb, h_fin);
    }

}
