package com.example.learning_foreign_words_app.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WordDictionaryDataResponse {
    @SerializedName("word")
    private String word;
    @SerializedName("phonetics")
    private List<Phonetic> phonetics;
    @SerializedName("meanings")
    private List<Meaning> meanings;

    public String getWord() {
        return word;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public List<Phonetic> getPhonetics(){return phonetics;}

    public static class Meaning {
        @SerializedName("partOfSpeech")
        private String partOfSpeech;

        @SerializedName("definitions")
        private List<Definition> definitions;

        public String getPartOfSpeech() {
            return partOfSpeech;
        }

        public List<Definition> getDefinitions() {
            return definitions;
        }
    }

    public static class Definition {
        @SerializedName("definition")
        private String definition;

        @SerializedName("synonyms")
        private List<String> synonyms;

        public String getDefinition() {
            return definition;
        }

        public List<String> getSynonyms() {
            return synonyms;
        }
    }

    public static class Phonetic{
        @SerializedName("text")
        private String text;
        @SerializedName("audio")
        private  String audio;

        public String getTextPhonetic() {
            return text;
        }

        public String getAudio() {
            return audio;
        }
    }
}
