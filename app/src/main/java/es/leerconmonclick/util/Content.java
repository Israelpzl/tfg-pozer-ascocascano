package es.leerconmonclick.util;

public class Content {

    private String word;
    private String img;
    private String syllables;
    private String difficulty;

    public Content(String word, String img, String syllables, String difficulty) {
        this.word = word;
        this.img = img;
        this.syllables = syllables;
        this.difficulty = difficulty;
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

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
