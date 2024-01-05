package com.example.learning_foreign_words_app.models;

import com.google.gson.annotations.SerializedName;

public class WordMinicardResponse {

    @SerializedName("SourceLanguage")
    private int sourceLanguage;

    @SerializedName("TargetLanguage")
    private int targetLanguage;

    @SerializedName("Heading")
    private String heading;

    @SerializedName("Translation")
    private WordTranslation wordTranslation;

    @SerializedName("SeeAlso")
    private String[] seeAlso;


    public int getSourceLanguage() {
        return sourceLanguage;
    }

    public int getTargetLanguage() {
        return targetLanguage;
    }

    public String getHeading() {
        return heading;
    }

    public WordTranslation getWordTranslation() {
        return wordTranslation;
    }

    public String[] getSeeAlso() {
        return seeAlso;
    }
}
