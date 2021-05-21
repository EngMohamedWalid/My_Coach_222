package com.example.my_coach.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my_coach.R;
import com.example.my_coach.ui.auth.Login;

import java.util.Locale;

public class Splash extends AppCompatActivity {
    private String currentlanguage, language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to mack activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


       String CurrentLang = Locale.getDefault().getDisplayLanguage();
        String lang = getSharedPreferences("language",MODE_PRIVATE)
                .getString("lang",CurrentLang);
        //language
        Locale l =new Locale(lang);
        Locale.setDefault(l);
        Configuration configuration=new Configuration();
        configuration.locale=l;
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());
        new Handler().postDelayed(() -> {

            startActivity(new Intent(Splash.this, Login.class));
            finish();
        }, 3000);

    }


}