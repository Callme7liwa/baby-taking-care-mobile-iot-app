package ensias.myteam.babytakingcare.Models;

public class Message {

    private String email ;
    private String body ;
    private String timestamp ;

    public Message() {
    }

    public Message(String email, String body,String timestamp) {
        this.email = email;
        this.body = body;
        this.timestamp = timestamp ;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
