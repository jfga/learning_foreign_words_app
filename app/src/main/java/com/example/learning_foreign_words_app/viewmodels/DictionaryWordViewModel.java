package com.example.learning_foreign_words_app.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.learning_foreign_words_app.models.DictionaryWordResponse;
import com.example.learning_foreign_words_app.models.WordDictionaryDataResponse;
import com.example.learning_foreign_words_app.repositories.DictionaryWordRepository;

public class DictionaryWordViewModel extends ViewModel {

    private DictionaryWordRepository dictionaryWordRepository;

    public DictionaryWordViewModel(){
        dictionaryWordRepository = new DictionaryWordRepository();
    }

    public LiveData<WordDictionaryDataResponse> getDictionaryWordData(String word, Context context){
        LiveData<WordDictionaryDataResponse> dictionaryWordResponse = dictionaryWordRepository.getDictionaryWordResponse(word, context);
        return dictionaryWordResponse;
    }
}
