package com.example.learning_foreign_words_app.models;

import com.google.gson.annotations.SerializedName;

public class WordTranslation {

    @SerializedName("Heading")
    private String heading;

    @SerializedName("Translation")
    private String translation;

    @SerializedName("DictionaryName")
    private String dictionaryName;

    @SerializedName("SoundName")
    private String soundName;

    @SerializedName("Type")
    private int type;

    @SerializedName("OriginalWord")
    private String originalWord;


    public String getHeading() {
        return heading;
    }

    public String getTranslation() {
        return translation;
    }

    public String getDictionaryName() {
        return dictionaryName;
    }

    public String getSoundName() {
        return soundName;
    }

    public int getType() {
        return type;
    }

    public String getOriginalWord() {
        return originalWord;
    }
}
