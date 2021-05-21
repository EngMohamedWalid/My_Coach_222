package com.example.my_coach;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    //view
    private FragmentContainerView nav;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private TextView titel;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //findview
        nav=findViewById(R.id.nav_host_fragment);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        titel=findViewById(R.id.titel_home);
        navController= Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
        Menu menu =bottomNavigationView.getMenu();
        boolean isCoach = getSharedPreferences("user",MODE_PRIVATE)
                .getBoolean("isCoach",false);

if (isCoach){
    menu.findItem(R.id.nave_favorite).setVisible(false);
    menu.findItem(R.id.nav_search).setVisible(false);
}

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {


            switch (item.getItemId()){
                case R.id.nav_home:
                    titel.setText(R.string.Home);
                    navController.navigate(R.id.nav_home);

                    return true;
                    case R.id.nav_search:
                        titel.setText(R.string.Search);
                        navController.navigate(R.id.nav_search);

                    return true;
                    case R.id.nave_favorite:
                        titel.setText(R.string.Favorite);
                        navController.navigate(R.id.nave_favorite);

                    return true;
                    case R.id.nav_person:
                        titel.setText(R.string.Profile);
                        navController.navigate(R.id.nav_person);

                    return true;
                    case R.id.nav_chat:
                        titel.setText(R.string.Chat);
                        navController.navigate(R.id.nav_chat);

                    return true;

            }

            return false;
        });
    }
}