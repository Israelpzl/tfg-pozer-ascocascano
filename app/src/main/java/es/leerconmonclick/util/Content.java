package es.leerconmonclick.util;

import java.util.List;

public class Content {

    private String word;
    private String img;
    private String syllables;
    private String determinant;

    public Content(String word, String img, String syllables, String determinant) {
        this.word = word;
        this.img = img;
        this.syllables = syllables;
        this.determinant = determinant;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSyllables() {
        return syllables;
    }

    public void setSyllables(String syllables) {
        this.syllables = syllables;
    }

    public String getDeterminant() {
        return determinant;
    }

    public void setDeterminant(String determinant) {
        this.determinant = determinant;
    }
}
