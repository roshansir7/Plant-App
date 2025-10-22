package com.example.plantapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;

    private final Handler handler = new Handler();
    private final int REFRESH_INTERVAL = 2 * 60 * 1000; // 2 minutes
    private Runnable updateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Default fragment = Home
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment selectedFragment = null;
            String title = "";

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                title = "Home";
            } else if (id == R.id.nav_graphs) {
                selectedFragment = new GraphsFragment();
                title = "Graphs";
            } else if (id == R.id.nav_history) {
                selectedFragment = new HistoryFragment();
                title = "History";
            } else if (id == R.id.nav_logout) {
                Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show();

                // ✅ Go back to LoginActivity safely
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                toolbar.setTitle(title);
            }

            drawerLayout.closeDrawers();
            return true;
        });

        // 🔁 Set up automatic Home update every 2 minutes
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (fragment instanceof HomeFragment) {
                    //((HomeFragment) fragment).updatePlantData(false); // auto update
                }
                handler.postDelayed(this, REFRESH_INTERVAL);
            }
        };

        // Start first automatic update after 2 minutes
        handler.postDelayed(updateRunnable, REFRESH_INTERVAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateRunnable);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "PlantAlertChannel";
            String description = "Channel for Plant Moisture Alerts";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("plant_alerts", name, importance);
            channel.setDescription(description);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
