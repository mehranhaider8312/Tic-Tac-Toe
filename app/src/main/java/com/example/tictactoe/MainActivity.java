package com.example.tictactoe;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tictactoe.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
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

        binding.ivContactDev.setOnClickListener(v -> {
            ContactDialogUtils.setUpContactDialog(this);
        });

        setupViewPager();
        setupTabLayout();
        setUpCustomTab();
    }

    private void setUpCustomTab() {
        // Set custom views for all tabs
        for (int i = 0; i < adapter.getItemCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View customTabView = LayoutInflater.from(this)
                        .inflate(R.layout.custom_tab_layout, null, false);

                TextView tvTabTitle = customTabView.findViewById(R.id.tabText);
                tvTabTitle.setText(adapter.getPageTitle(i));
                tab.setCustomView(customTabView);

                // Set initial appearance
                if (i == 0) {
                    tvTabTitle.setBackgroundResource(R.drawable.bg_tab_active);
                    tvTabTitle.setTextColor(Color.WHITE);
                } else {
                    tvTabTitle.setBackgroundResource(R.drawable.bg_tab_inactive);
                    tvTabTitle.setTextColor(Color.BLACK);
                }
            }
        }
    }

    private void setupViewPager() {
        viewPager = binding.viewPager;
        adapter = new MainViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
    }

    private void setupTabLayout() {
        tabLayout = binding.tabLayout;

        // Connect TabLayout with ViewPager2 using TabLayoutMediator
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // We'll set text manually in setUpCustomTab
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateTabAppearance(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                updateTabAppearance(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void updateTabAppearance(TabLayout.Tab tab, boolean selected) {
        if (tab.getCustomView() != null) {
            TextView tabText = tab.getCustomView().findViewById(R.id.tabText);
            if (selected) {
                tabText.setBackgroundResource(R.drawable.bg_tab_active);
                tabText.setTextColor(Color.WHITE);
            } else {
                tabText.setBackgroundResource(R.drawable.bg_tab_inactive);
                tabText.setTextColor(Color.BLACK);
            }
        }
    }
}