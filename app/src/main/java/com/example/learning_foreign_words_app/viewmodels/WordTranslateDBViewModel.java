package com.example.learning_foreign_words_app.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.learning_foreign_words_app.database.DbWordTranslationModel;
import com.example.learning_foreign_words_app.repositories.WordTranslationMinicardRepository;

import java.util.List;

public class WordTranslateDBViewModel extends AndroidViewModel {
    private WordTranslationMinicardRepository repository;
    private LiveData<List<DbWordTranslationModel>> allWords;

    public WordTranslateDBViewModel(@NonNull Application application) { // не можна передавати контекст який швидко помирає
        super(application);
        repository = new WordTranslationMinicardRepository(application);
        allWords = repository.getAllWords();
    }

    public void saveWord(DbWordTranslationModel dbWordTranslationModel){
        repository.insertWord(dbWordTranslationModel);
    }

    public void deleteWord(DbWordTranslationModel dbWordTranslationModel){
        repository.delete(dbWordTranslationModel);
    }

    public void updateWord(DbWordTranslationModel dbWordTranslationModel){
        repository.updateWord(dbWordTranslationModel);
    }

    public void updateShown(DbWordTranslationModel dbWordTranslationModel){
        repository.updateWasShowed(dbWordTranslationModel);
    }

    public void updateGuessed(DbWordTranslationModel dbWordTranslationModel){
        repository.updateGuessed(dbWordTranslationModel);
    }

    public LiveData<List<DbWordTranslationModel>> getWordsListForMainMode(){
        return repository.getWordsListForMainMode();
    }

    public LiveData<List<DbWordTranslationModel>> getAllWords() {
        return allWords;
    }
}
