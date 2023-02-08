package es.leerconmonclick.util;

import java.util.List;
import java.util.Map;

public class Game {

    private int timesPlayed;
    private Map<String,Categories> categories;
    private Map<String,Difficulties> difficulties;

    public Game(int timesPlayed, Map<String, Categories> categories, Map<String, Difficulties> difficulties) {
        this.timesPlayed = timesPlayed;
        this.categories = categories;
        this.difficulties = difficulties;
    }

    public int getTimesPlayed() {
        return timesPlayed;
    }

    public void setTimesPlayed(int timesPlayed) {
        this.timesPlayed = timesPlayed;
    }

    public Map<String, Categories> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Categories> categories) {
        this.categories = categories;
    }

    public Map<String, Difficulties> getDifficulties() {
        return difficulties;
    }

    public void setDifficulties(Map<String, Difficulties> difficulties) {
        this.difficulties = difficulties;
    }
}
