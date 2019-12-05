package com.example.myapplication;

public interface  tokenService {
    @POST("/auth")
    Call<Token> login(@Body User login);
}