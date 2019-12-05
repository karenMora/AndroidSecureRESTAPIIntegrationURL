package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.taskplanner.Entities.User;
import com.example.taskplanner.Entities.Token;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private final ExecutorService executorService = Executors.newFixedThreadPool( 1 );
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http:/10.0.2.2:8080") //localhost for emulator
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    IAuthService authService = retrofit.create(IAuthService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void checkData(View view){
        boolean flag=true;
        Intent intent = new Intent(this, LoginActivity.class);
        final EditText editTextName = (EditText) findViewById(R.id.editText2);
        String user = editTextName.getText().toString();
        final EditText editTextPassword=  (EditText) findViewById(R.id.editText3);
        String password=editTextPassword.getText().toString();
        if(user.equals("")){
            editTextName.setError("ingrese un usuario");
            flag=false;
        }
        if(password.equals("")){
            editTextPassword.setError("ingrese una contraseña");
            flag=false;
        }
        if(flag){

            executorService.execute( new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        Response<Token> response =
                                authService.login( new User( "test@mail.com", "password" ) ).execute();
                        Token token = response.body();
                        SharedPreferences.Editor sharedPref =
                                getSharedPreferences( getString( R.string.preference_file_key ), Context.MODE_PRIVATE ).edit();
                        sharedPref.putString("TOKEN_KEY",token.getAccessToken());
                        Intent i =new Intent(getBaseContext(),MainActivity.class);
                        startActivity(i);


                    }
                    catch ( IOException e )
                    {
                        e.printStackTrace();
                    }
                }
            } );


        }


    }
}