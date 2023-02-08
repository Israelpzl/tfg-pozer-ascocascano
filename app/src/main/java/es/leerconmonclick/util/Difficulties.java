package es.leerconmonclick.util;

public class Difficulties {

    int failed,succes,timesPlayed;

    public Difficulties(int failed, int succes, int timesPlayed) {
        this.failed = failed;
        this.succes = succes;
        this.timesPlayed = timesPlayed;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getSucces() {
        return succes;
    }

    public void setSucces(int succes) {
        this.succes = succes;
    }

    public int getTimesPlayed() {
        return timesPlayed;
    }

    public void setTimesPlayed(int timesPlayed) {
        this.timesPlayed = timesPlayed;
    }
}
