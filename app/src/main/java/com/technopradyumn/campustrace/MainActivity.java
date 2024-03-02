package com.technopradyumn.campustrace;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.technopradyumn.campustrace.data.LostItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    private LostItemViewModel lostItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        replaceFragment(new HomeFragment());

        bottomNav = findViewById(R.id.bottomNavView);
        lostItemViewModel = new ViewModelProvider(this).get(LostItemViewModel.class);

        // Observe the LiveData
        lostItemViewModel.getLostItems().observe(this, new Observer<List<LostItem>>() {
            @Override
            public void onChanged(List<LostItem> lostItems) {
                // Update the UI with the new list of LostItems
            }
        });

        bottomNav.setOnItemSelectedListener((NavigationBarView.OnItemSelectedListener) item -> {
            int menuItemId = item.getItemId();

            if (menuItemId == R.id.nav_home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (menuItemId == R.id.nav_found) {
                replaceFragment(new AddItemFragment());
                return true;
            }
//            else if(menuItemId == R.id.nav_notifications){
//                replaceFragment(new NotificationFragment());
//            }else {
//                replaceFragment(new HomeFragment());
//                return true;
//            }

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