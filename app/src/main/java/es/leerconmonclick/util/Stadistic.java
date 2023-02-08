package es.leerconmonclick.util;

import java.util.List;

public class Stadistic {

   private List<Game> joinWords,letters,syllables;

    public Stadistic(List<Game> joinWords, List<Game> letters, List<Game> syllables) {
        this.joinWords = joinWords;
        this.letters = letters;
        this.syllables = syllables;
    }

    public List<Game> getJoinWords() {
        return joinWords;
    }

    public void setJoinWords(List<Game> joinWords) {
        this.joinWords = joinWords;
    }

    public List<Game> getLetters() {
        return letters;
    }

    public void setLetters(List<Game> letters) {
        this.letters = letters;
    }

    public List<Game> getSyllables() {
        return syllables;
    }

    public void setSyllables(List<Game> syllables) {
        this.syllables = syllables;
    }
}
