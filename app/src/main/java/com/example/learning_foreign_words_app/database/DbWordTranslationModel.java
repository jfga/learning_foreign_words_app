package com.example.learning_foreign_words_app.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saved_word_translation_table")
public class DbWordTranslationModel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String word;
    private String translation;
    private String definition;
    private int shown;  // кількість відображень
    private int guessed;  // кількість вгадувань

//    private String original_language;
//    private String translation_language;

    public DbWordTranslationModel(String word, String translation, String definition, int shown, int guessed){
        this.word = word;
        this.translation = translation;
        this.definition = definition;
        this.shown = shown;
        this.guessed = guessed;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getTranslation() {
        return translation;
    }

    public String getDefinition() {
        return definition;
    }

    public int getShown() {
        return shown;
    }

    public int getGuessed() {
        return guessed;
    }

    public String toString(){
        return " id: " + getId()+ "Word: "+ getWord() + " transl: " + getTranslation()
                + "\nguessed: " + getGuessed() + " wasShowed: " + getShown();
    }
}
