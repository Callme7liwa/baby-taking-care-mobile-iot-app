package ensias.myteam.babytakingcare.Models;

public class Baby {
    private String id ;
    private String name ;
    private String image ;
    private String birthday ;
    private String weight ;
    private String temperature ;
    private String created_on, last_updated_on;


    public Baby() {
    }

    public Baby(String name , String weight , String temperature,String birthday) {
        this.name=name;
        this.weight=weight;
        this.temperature=temperature;
        this.birthday=birthday;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCreated_on()
    {
        return  this.created_on;
    }

    public String getLast_updated_on()
    {
        return this.last_updated_on;
    }

    public void setCreated_on(String created_on)
    {
        this.created_on = created_on ;
    }

    public void setLast_updated_on(String last_updated_on)
    {
        this.last_updated_on = last_updated_on ;
    }

}
