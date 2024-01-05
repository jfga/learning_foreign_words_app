package com.example.learning_foreign_words_app.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.learning_foreign_words_app.models.WordMinicardResponse;
import com.example.learning_foreign_words_app.models.WordTranslation;
import com.example.learning_foreign_words_app.repositories.WordTranslationMinicardRepository;

public class TranslatedWordViewModel extends ViewModel {
    private WordTranslationMinicardRepository wordTranslationMinicardRepository;
    private String authToApi;
    public TranslatedWordViewModel(){
        wordTranslationMinicardRepository = new WordTranslationMinicardRepository();
    }

    // метод працював із переданим із активності токеном
//    public LiveData<WordMinicardResponse> getWordTranslationMinicard(String token, String word, int scrLanguage, int destLenguage){
//        return wordTranslationMinicardRepository.getWordTranslationMinicard(token, word, scrLanguage, destLenguage);
//    }
    // метод зі зберіганням токена в даному класі
    public LiveData<WordMinicardResponse> getWordTranslationMinicard(String token, String word, int scrLanguage, int destLenguage, Context context){
        return wordTranslationMinicardRepository.getWordTranslationMinicard(token, word, scrLanguage, destLenguage, context);
    }

    public void setAuthToApi(String authToApi) {
        this.authToApi = "Bearer " + authToApi;
    }
    public String getAuthToApi(){
        return authToApi;
    }

    public LiveData<String> getAuthorizationToken(String apiKey){   // змінений вигляд метода
        // спроба передавати токен в ViewModel а не в Activity
        LiveData<String> response = wordTranslationMinicardRepository.getAuthorizationToken(apiKey);
        //setAuthToApi(response);
        //authToApi = "Bearer " + response.getValue();
        //
        return  response;
    }


}


// початковий вигляд метода
//    public LiveData<String> getAuthorizationToken(String apiKey){
//        return  wordTranslationMinicardRepository.getAuthorizationToken(apiKey);
//    }
