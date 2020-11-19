package com.example.goeat;

import android.os.Bundle;

import com.example.goeat.Fragments.HomeFragment;
import com.example.goeat.Fragments.HistoryFragment;
import com.example.goeat.Fragments.NearbyFragment;
import com.example.goeat.Fragments.ProfileFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private HomeFragment homeFragment;
    private NearbyFragment nearbyFragment;
    private HistoryFragment historyFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);

        homeFragment = new HomeFragment();
        nearbyFragment = new NearbyFragment();
        historyFragment = new HistoryFragment();
        profileFragment = new ProfileFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(homeFragment, "");
        viewPagerAdapter.addFragment(nearbyFragment, "");
        viewPagerAdapter.addFragment(historyFragment, "");
        viewPagerAdapter.addFragment(profileFragment, "");
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_near_me_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_history_24);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_person_24);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
}