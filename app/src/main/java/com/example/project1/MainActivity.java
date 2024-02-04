package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.project1.databinding.ActivityMainBinding;
import com.example.project1.fragments.assignmentsFragment;
import com.example.project1.fragments.classFragment;
import com.example.project1.fragments.examFragment;
import com.example.project1.fragments.todoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Bottom Navigation Bar Code
        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frameLayout);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                if (itemID == R.id.classes) {
                    loadFragment(new classFragment(), false);
                } else if (itemID == R.id.assignments) {
                    loadFragment(new assignmentsFragment(), false);
                } else if (itemID == R.id.exams) {
                    loadFragment(new examFragment(), false);
                } else if (itemID == R.id.todo){
                    loadFragment(new todoFragment(), false);
                }
                return true;
            }
        });
        loadFragment(new classFragment(), true);
    }

    //Helper method to load fragment when button on nav bar is clicked.
    private void loadFragment(Fragment fragment, boolean isAppIntialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isAppIntialized) {
            fragmentTransaction.add(R.id.frameLayout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }
        fragmentTransaction.commit();
    }
}