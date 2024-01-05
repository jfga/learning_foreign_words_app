package com.example.learning_foreign_words_app.repositories;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.learning_foreign_words_app.models.DictionaryWordResponse;
import com.example.learning_foreign_words_app.models.WordDictionaryDataResponse;
import com.example.learning_foreign_words_app.network.ApiClient;
import com.example.learning_foreign_words_app.network.DictionaryApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DictionaryWordRepository {

    private DictionaryApiService dictionaryApiService;

    public DictionaryWordRepository(){
        dictionaryApiService = ApiClient.getDictionaryRetrofit().create(DictionaryApiService.class);
    }


    public LiveData<WordDictionaryDataResponse> getDictionaryWordResponse(String word, Context context) {
        MutableLiveData<WordDictionaryDataResponse> wordData = new MutableLiveData<>();
        Log.i("REPOSITORY_WORK", "PROGRAM_TRY_TO_MAKE_CALL_FOR_WORD_DATA_WITH_WORD: " + word);

        dictionaryApiService.getDictionaryWordResponse(word).enqueue(new Callback<List<WordDictionaryDataResponse>>() {
            @Override
            public void onResponse(Call<List<WordDictionaryDataResponse>> call, Response<List<WordDictionaryDataResponse>> response) {
                if (response.body() == null) {
                    Log.i("REPOSITORY_WORK", "DATA_IS: " + "response.body() IS_NULL");
                    Toast.makeText(context, "Check the spelling of the word \nor selected language", Toast.LENGTH_SHORT).show();
                } else if (response.isSuccessful()) {
                    wordData.setValue(response.body().get(0));
                    Log.i("REPOSITORY_WORK", "PROGRAM_RECEIVE_response.isSuccessful()");
                    Log.i("REPOSITORY_WORK", "PROGRAM_RECEIVE " + response.body().get(0).getWord());
                } else {
                    Toast.makeText(context, "Problem in get response", Toast.LENGTH_SHORT).show();
                    Log.i("REPOSITORY_WORK", "PROGRAM_RECEIVE_SOME_DATA");
                    Log.i("REPOSITORY_WORK", "DATA_IS: " + response.body().get(1).getWord());
                }
            }

            @Override
            public void onFailure(Call<List<WordDictionaryDataResponse>> call, Throwable t) {

            }
        });
        return wordData;
    }
}




