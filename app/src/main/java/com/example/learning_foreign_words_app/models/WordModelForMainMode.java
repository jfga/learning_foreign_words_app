package com.example.learning_foreign_words_app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WordModelForMainMode implements Serializable {

    private String word;
    private String translation;
    public WordModelForMainMode(String word, String translation){
        this.word = word;
        this.translation = translation;
    }

    public String getWord() {
        return word;
    }

    public String getTranslation() {
        return translation;
    }

    public String toString(){
        return "Word: " + word + " translation: " + translation;
    }
}
