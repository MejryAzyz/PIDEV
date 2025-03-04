package Modeles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Accompagnateur {
    private int idAccompagnateur;
    private String username;
    private String passwordHash;
    private String email;
    private String fichierCv;
    private String photoProfil;
    private String experience;
    private String motivation;
    private String langues;
    private String statut;
    private LocalDate dateRecrutement;

    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Accompagnateur() {
        // Constructeur vide
    }

    // ✅ Constructeur avec LocalDate directement pour éviter les conversions inutiles
    public Accompagnateur(String username, String passwordHash, String email, String fichierCv, String photoProfil,
                          String experience, String motivation, String langues, String statut, LocalDate dateRecrutement) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.fichierCv = fichierCv;
        this.photoProfil = photoProfil;
        this.experience = experience;
        this.motivation = motivation;
        this.langues = langues;
        this.statut = statut;
        this.dateRecrutement = dateRecrutement;
    }

    // ✅ Alternative : Constructeur prenant une date en String
    public Accompagnateur(String username, String passwordHash, String email, String fichierCv, String photoProfil,
                          String experience, String motivation, String langues, String statut, String dateRecrutement) {
        this(username, passwordHash, email, fichierCv, photoProfil, experience, motivation, langues, statut, parseDate(dateRecrutement));
    }

    // ✅ Méthode améliorée pour convertir une date String -> LocalDate
    private static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return LocalDate.parse(dateStr, INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            System.err.println("⚠️ Erreur de format de date : " + dateStr + " (Format attendu : JJ/MM/AAAA)");
            return null;
        }
    }

    public String getFormattedDateRecrutement() {
        return (dateRecrutement != null) ? dateRecrutement.format(OUTPUT_FORMAT) : "Non spécifiée";
    }

    // ✅ Setter pour une LocalDate
    public void setDateRecrutement(LocalDate dateRecrutement) {
        this.dateRecrutement = dateRecrutement;
    }

    // ✅ Setter pour une String (convertie en LocalDate)
    public void setDateRecrutement(String dateRecrutement) {
        this.dateRecrutement = parseDate(dateRecrutement);
    }

    // Getters et Setters
    public int getIdAccompagnateur() { return idAccompagnateur; }
    public void setIdAccompagnateur(int idAccompagnateur) { this.idAccompagnateur = idAccompagnateur; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFichierCv() { return fichierCv; }
    public void setFichierCv(String fichierCv) { this.fichierCv = fichierCv; }
    public String getPhotoProfil() { return photoProfil; }
    public void setPhotoProfil(String photoProfil) { this.photoProfil = photoProfil; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public String getMotivation() { return motivation; }
    public void setMotivation(String motivation) { this.motivation = motivation; }
    public String getLangues() { return langues; }
    public void setLangues(String langues) { this.langues = langues; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public LocalDate getDateRecrutement() { return dateRecrutement; }

    @Override
    public String toString() {
        return "Accompagnateur{" +
                "idAccompagnateur=" + idAccompagnateur +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fichierCv='" + fichierCv + '\'' +
                ", photoProfil='" + photoProfil + '\'' +
                ", experience='" + experience + '\'' +
                ", motivation='" + motivation + '\'' +
                ", langues='" + langues + '\'' +
                ", statut='" + statut + '\'' +
                ", dateRecrutement='" + getFormattedDateRecrutement() + '\'' +
                '}';
    }
}
