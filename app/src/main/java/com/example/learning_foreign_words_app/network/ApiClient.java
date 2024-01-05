package com.example.learning_foreign_words_app.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;
    private static Retrofit dictionaryRetrofit;
    public static Retrofit getRetrofit(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://developers.lingvolive.com/api/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // retrofit 2-d instance for Dictionary api
    public static  Retrofit getDictionaryRetrofit(){
        if (dictionaryRetrofit == null){
            dictionaryRetrofit = new Retrofit.Builder()
                    .baseUrl("https://api.dictionaryapi.dev/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return dictionaryRetrofit;
    }

}
