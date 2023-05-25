package ensias.myteam.babytakingcare.Models;

public class Conversation {
    private String fullName ;
    private String image ;
    private String conversationId ;
    private String message ;
    private String lastMessage ;
    private Long lastMessageTimestamp ;
    private Integer numberMessageNotViewed ;

    public Conversation() {
    }

    public Conversation(String userId, String username, String message, String lastMessage , Long lastMessageTimestamp) {
        this.message = message;
        this.lastMessage = lastMessage;
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Long getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Long timestamp) {
        this.lastMessageTimestamp = timestamp;
    }

    public void setInfoParent(String firstName , String secondName , String image )
    {
        this.fullName = firstName + " " + secondName ;
        this.image = image;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getNumberMessageNotViewed() {
        return numberMessageNotViewed;
    }

    public void setNumberMessageNotViewed(Integer numberMessageNotViewed) {
        this.numberMessageNotViewed = numberMessageNotViewed;
    }
}
