package ensias.myteam.babytakingcare.dto;

public class UserInfoDto {
    private String username ;
    private String imagePath ;
    private String phoneNumber ;
    private String createdAt ;
    private String updatedAt ;
    private long babiesNumber ;


    public UserInfoDto() {
    }

    public UserInfoDto(String username, String phoneNumber, String createdAt, String updatedAt, int babiesNumber) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.babiesNumber = babiesNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getBabiesNumber() {
        return babiesNumber;
    }

    public void setBabiesNumber(long babiesNumber) {
        this.babiesNumber = babiesNumber;
    }
}
