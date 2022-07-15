package es.leerconmonclick.util;




public class Task {

    private String date;
    private String time;
    private String description;


    public Task(){}

    public Task(String date, String description, String time) {
        this.date = date;
        this.description = description;
        this.time = time;
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
