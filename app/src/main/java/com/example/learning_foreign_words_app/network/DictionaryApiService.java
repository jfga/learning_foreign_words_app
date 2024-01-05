package com.example.learning_foreign_words_app.network;

import com.example.learning_foreign_words_app.models.WordDictionaryDataResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictionaryApiService {

    @GET("v2/entries/en/{word}")
    Call<List<WordDictionaryDataResponse>> getDictionaryWordResponse(@Path("word") String word);
}
