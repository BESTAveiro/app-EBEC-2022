package best.Aveiro.EBEC.Objects;
public class Event {

    private String time;
    private String description;
    private String event_designation;
    public Event(){

    }
    public String getDesignation(){
        return event_designation;
    }

    public void setDesignation(String designation){
        this.event_designation=designation;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int order;

    public void setOrder(Integer order) {
        this.order= order;
    }
}