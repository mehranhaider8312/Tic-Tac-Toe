package com.example.tictactoe;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tictactoe.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ViewPager viewPager;
    private BottomNavigationView bottomNav;
    private MainViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding.ivContactDev.setOnClickListener(v->{
            ContactDialogUtils.setUpContactDialog(this);
        });

        setupViewPager();
        setupBottomNavigation();
    }


    private void setupViewPager() {
        viewPager = binding.viewPager;
        adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    binding.bottomNavigation.setSelectedItemId(R.id.navTickTacToe);
                    //updateBackgrounds(position);
                }
                else if(position == 1){
                    binding.bottomNavigation.setSelectedItemId(R.id.navGuessMaster);
                    //updateBackgrounds(position);
                }else{
                    binding.bottomNavigation.setSelectedItemId(R.id.navRockPaperScissor);
                    //updateBackgrounds(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupBottomNavigation() {
        bottomNav = binding.bottomNavigation;

        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navTickTacToe) {
                    viewPager.setCurrentItem(0, true);
                    //updateBackgrounds(0);
                    return true;
                } else if (itemId == R.id.navGuessMaster) {
                    viewPager.setCurrentItem(1, true);
                    //updateBackgrounds(1);
                    return true;
                }else if (itemId == R.id.navRockPaperScissor) {
                    viewPager.setCurrentItem(2, true);
                    //updateBackgrounds(1);
                    return true;
                }
                return false;
            }
        });
    }
    private void updateBackgrounds(int position){
        if (position == 0){
            binding.appbar.setBackgroundColor(R.drawable.bg_tick_tac_toe);
            binding.bottomNavigation.setBackgroundColor(R.drawable.bg_tick_tac_toe);
        }else{
            binding.appbar.setBackgroundColor(R.drawable.bg_guess_master);
            binding.bottomNavigation.setBackgroundColor(R.drawable.bg_guess_master);
        }
    }
}