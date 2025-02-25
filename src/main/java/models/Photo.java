package models;

public class Photo {
    private int id_photo;
    private int id_clinique;
    private String photoUrl;
    public Photo() {}
    public Photo(int id_clinique, String photoUrl) {
        this.id_clinique = id_clinique;
        this.photoUrl = photoUrl;
    }
    public Photo(int id_photo, int id_clinique, String url) {
        this.id_photo = id_photo;
        this.id_clinique = id_clinique;
        this.photoUrl = url;
    }

    public int getId_photo() {
        return id_photo;
    }

    public void setId_photo(int id_photo) {
        this.id_photo = id_photo;
    }

    public int getId_clinique() {
        return id_clinique;
    }

    public void setId_clinique(int id_clinique) {
        this.id_clinique = id_clinique;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String url) {
        this.photoUrl = url;
    }
    @Override
    public String toString() {
        return "Photo{" +
                "id_photo=" + id_photo +
                ", id_clinique=" + id_clinique +
                ", url='" + photoUrl + '\'' +
                '}';
    }
}
