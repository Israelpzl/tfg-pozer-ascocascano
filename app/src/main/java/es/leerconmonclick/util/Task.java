package es.leerconmonclick.util;




public class Task {

    private int id;
    private String tittle;
    private String date;
    private String time;
    private String description;
    private String tagNoty;


    public Task(){}

    public Task(int id, String tittle, String date, String time, String description, String tagNoty) {
        this.id = id;
        this.tittle = tittle;
        this.date = date;
        this.time = time;
        this.description = description;
        this.tagNoty = tagNoty;
    }

    public String getTagNoty() {
        return tagNoty;
    }

    public void setTagNoty(String tagNoty) {
        this.tagNoty = tagNoty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
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
