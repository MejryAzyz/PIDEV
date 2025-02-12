package entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Accompagnateur {
    private int id_accompagnateur;
    private String fichierCv;
    private String experience;
    private String langues;
    private String certifications;
    private String statut;
    private String dateRecrutement;
    public Accompagnateur() {}
    public Accompagnateur(int id_accompagnateur, String fichierCv, String certifications, String experience, String langues, String statut, String dateRecrutement ) {
        this.id_accompagnateur = id_accompagnateur;
        this.fichierCv = fichierCv;
        this.certifications = certifications;
        this.experience = experience;
        this.langues = langues;
        this.statut = statut;
        this.dateRecrutement = dateRecrutement;
    }
    public Accompagnateur(String fichierCv, String experience, String langues, String certifications, String statut, String dateRecrutement){
        this.fichierCv=fichierCv;
        this.experience=experience;
        this.langues=langues;
        this.certifications=certifications;
        this.statut=statut;
        this.dateRecrutement=convertDateFormat(dateRecrutement);
    }
    private String convertDateFormat(String dateStr) {
        try {
            SimpleDateFormat fromFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fromFormat.parse(dateStr);
            return toFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Gestion des erreurs
        }
    }

    public int getId_accompagnateur() {
        return id_accompagnateur;
    }

    public void setId_accompagnateur(int id_accompagnateur) {
        this.id_accompagnateur = id_accompagnateur;
    }

    public String getFichierCv() {
        return fichierCv;
    }
    public void setFichierCv(String fichierCv) {
        this.fichierCv = fichierCv;
    }
    public String getExperience() {
        return experience;
    }
    public void setExperience(String experience) {
        this.experience = experience;
    }
    public String getLangues() {
        return langues;
    }
    public void setLangues(String langues) {
        this.langues = langues;
    }
    public String getCertifications() {
        return certifications;
    }
    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }
    public String getStatut() {
        return statut;
    }
    public void setStatut(String statut) {
        this.statut = statut;
    }
    public String getDateRecrutement() {
        return dateRecrutement;
    }
    public void setDateRecrutement(String dateRecrutement) {
        this.dateRecrutement = dateRecrutement;
    }

    @Override
    public String toString() {
        return "Accompagnateur{" +
                "id_accompagnateur=" + id_accompagnateur +
                "fichierCv='" + fichierCv + '\'' +
                ", experience='" + experience + '\'' +
                ", langues='" + langues + '\'' +
                ", certifications='" + certifications + '\'' +
                ", statut='" + statut + '\'' +
                ", dateRecrutement='" + dateRecrutement + '\'' +
                '}';
    }
}
