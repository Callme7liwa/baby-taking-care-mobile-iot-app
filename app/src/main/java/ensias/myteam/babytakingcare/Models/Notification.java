package ensias.myteam.babytakingcare.Models;

public class Notification {

    private String id ;
    private String date ;
    private String time ;
    private String description ;


    public Notification() {
    }

    public Notification(String id , String date, String text) {
        this.id = id ;
        this.date = date;
        this.description = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
