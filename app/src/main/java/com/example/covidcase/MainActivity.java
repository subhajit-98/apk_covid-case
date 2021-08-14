package com.example.covidcase;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    // private ActivityMainBinding binding;
    // https://www.youtube.com/watch?v=tPV8xA7m-iw  ***************************

    FrameLayout fl_show_fragment;
    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // fl_show_fragment = findViewById(R.id.fl_show_fragment);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_show_fragment, new HomeFragment());
        ft.commit();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        ft.replace(R.id.fl_show_fragment, new HomeFragment());
                        // ft.addToBackStack("home");
                        ft.commit();
                        return true;

                    case R.id.navigation_global:
                        /*ft.replace(R.id.fl_show_fragment, new GlobalData());
                        // ft.addToBackStack("global");
                        ft.commit();
                        return true;*/
                        ft.replace(R.id.fl_show_fragment, new ShowGlobalData());
                        // ft.addToBackStack("global");
                        ft.commit();
                        return true;

                    case R.id.navigation_dashboard:
                        ft.replace(R.id.fl_show_fragment, new Dashboard());
                        ft.commit();
                        return true;

                    case R.id.navigation_tips:
                        ft.replace(R.id.fl_show_fragment, new Tips());
                        ft.commit();
                        return true;
                }
                return false;
            }
        });

        /*binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("List", String.valueOf(fm.getBackStackEntryCount()));
        // fm.popBackStack("home", 0);
        // fm.popBackStackImmediate()
        // finish();
    }
}