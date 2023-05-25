package ensias.myteam.babytakingcare.Models;

public class Message {

    private String email ;
    private String body ;
    private Long timestamp ;

    public Message() {
    }

    public Message(String email, String body,Long timestamp) {
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
