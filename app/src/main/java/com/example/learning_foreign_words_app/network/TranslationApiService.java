package com.example.learning_foreign_words_app.network;

import com.example.learning_foreign_words_app.models.AuthorizationToken;
import com.example.learning_foreign_words_app.models.WordMinicardResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TranslationApiService {

   // @Headers("Authorization: Bearer " + auth)
    @GET("v1/Minicard")
    Call<WordMinicardResponse> getWordTranslationMinicard(@Header("Authorization") String auth, @Query("text") String text, @Query("srcLang") int srcLang, @Query("dstLang") int dstLang );

    // static header // Call<WordMinicardResponse> getWordTranslationMinicard(@Query("text") String text, @Query("srcLang") int srcLang, @Query("dstLang") int dstLang );


    //@Headers("Authorization: Basic " + translateApiKey)
    @POST("v1.1/authenticate")
    Call<String> getAuthorizationToken(@Header("Authorization") String apiKey, @Body AuthorizationToken token);
    //(@Header("Authorization") String apiKey, @Body AuthorizationToken token);
    //Call<String> getAuthorizationToken(@Body AuthorizationToken token); // static header
}
