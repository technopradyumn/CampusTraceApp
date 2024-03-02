package com.technopradyumn.campustrace;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        replaceFragment(new HomeFragment());

        bottomNav = findViewById(R.id.bottomNavView);

        bottomNav.setOnItemSelectedListener((NavigationBarView.OnItemSelectedListener) item -> {
            int menuItemId = item.getItemId();

            if (menuItemId == R.id.nav_home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (menuItemId == R.id.nav_found) {
                replaceFragment(new FoundFragment());
                return true;
            }else if(menuItemId == R.id.nav_notifications){
                replaceFragment(new NotificationFragment());
            }else {
                replaceFragment(new HomeFragment());
                return true;
            }

            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

}