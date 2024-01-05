package com.example.learning_foreign_words_app.models;

import com.google.gson.annotations.SerializedName;

public class AuthorizationToken {

    @SerializedName("token")
    private String authorizationToken;

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    public String getToken(){
        return authorizationToken;
    }

}
