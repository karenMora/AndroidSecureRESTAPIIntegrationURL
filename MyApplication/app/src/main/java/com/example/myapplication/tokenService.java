package com.example.myapplication;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface  tokenService {
    @POST("/auth")
    Call<Token> login(@Body User login);
}