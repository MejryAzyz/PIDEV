package Models;

public class Photo {
    private int idPhoto;
    private int hebergementId;
    private String photoUrl;

    public Photo() {
    }

    public Photo(int idPhoto, int hebergementId, String photoUrl) {
        this.idPhoto = idPhoto;
        this.hebergementId = hebergementId;
        this.photoUrl = photoUrl;
    }

    public Photo(int hebergementId, String photoUrl) {
        this.hebergementId = hebergementId;
        this.photoUrl = photoUrl;
    }

    public int getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(int idPhoto) {
        this.idPhoto = idPhoto;
    }

    public int getHebergementId() {
        return hebergementId;
    }

    public void setHebergementId(int hebergementId) {
        this.hebergementId = hebergementId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "idPhoto=" + idPhoto +
                ", hebergementId=" + hebergementId +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
