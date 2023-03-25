package es.leerconmonclick.util.utils;

public class Syllable {
    private String syllable,img;

    public Syllable(String syllable, String img) {
        this.syllable = syllable;
        this.img = img;
    }

    public String getSyllable() {
        return syllable;
    }

    public void setSyllable(String syllable) {
        this.syllable = syllable;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
