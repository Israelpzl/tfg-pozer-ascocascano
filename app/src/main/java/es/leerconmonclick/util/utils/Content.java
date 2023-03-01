package es.leerconmonclick.util.utils;

public class Content {

    private String word;
    private String img;
    private String syllables;
    private String difficulty;
    private boolean isSyllable;

    public Content(String word, String img, String syllables, String difficulty,boolean isSyllable) {
        this.word = word;
        this.img = img;
        this.syllables = syllables;
        this.difficulty = difficulty;
        this.isSyllable = isSyllable;
    }

    public boolean isSyllable() {
        return isSyllable;
    }

    public void setSyllable(boolean syllable) {
        isSyllable = syllable;
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
