package ensias.myteam.babytakingcare.dto;

public class LayerHistoryDto {

    private String date ;

    public LayerHistoryDto(String date) {
        this.date = date;
    }

    public LayerHistoryDto( ) {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
