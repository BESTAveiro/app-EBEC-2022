package best.Aveiro.EBEC.Objects;

import java.util.ArrayList;

public class Day {
    private String day_designation;
    private String date;
    private ArrayList<Event> events;


    public String getDayDesignation() {
        return day_designation;
    }

    public void setDayDesignation(String day_designation) {
        this.day_designation = day_designation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }


    public Day(){
    }

}
