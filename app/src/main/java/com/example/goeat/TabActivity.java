package com.example.goeat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.Tasks;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class TabActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private HomeFragment homeFragment;
    private NearbyFragment nearbyFragment;
    private HistoryFragment historyFragment;
    private ProfileFragment profileFragment;
    private GeocodingAsync myGeocoding;
    private DatabaseReference mDatabase;
    private String mDistrict;
    public static ArrayList<Place> placesList;
    ViewPagerAdapter viewPagerAdapter;
    private ProgressBar loading_spinner;
    private Handler handler;

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }
    private FragmentRefreshListener fragmentRefreshListener;
    public interface FragmentRefreshListener{
        void onRefresh();
    }

    protected void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        placesList=new ArrayList<Place>();
        getNearby();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        loading_spinner=findViewById(R.id.loading_spinner);
        loading_spinner.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.alizarin_red,R.color.peter_blue);
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                viewPager = findViewById(R.id.viewpager);
                tabLayout = findViewById(R.id.tab_layout);
                homeFragment = new HomeFragment();
                nearbyFragment = new NearbyFragment();
                historyFragment = new HistoryFragment();
                profileFragment = new ProfileFragment();
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                viewPagerAdapter.addFragment(homeFragment, "");
                viewPagerAdapter.addFragment(nearbyFragment, "");
                viewPagerAdapter.addFragment(historyFragment, "");
                viewPagerAdapter.addFragment(profileFragment, "");
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);

                tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24);
                tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_near_me_24);
                tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_history_24);
                tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_person_24);

                loading_spinner.setVisibility(View.GONE);
            }
        }, 1500);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        myUpdateOperation();
                    }
                }
        );
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

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
    @Override
    protected void onResume(){
        super.onResume();
    }
    public void getNearby(){
        mDistrict="";
        myGeocoding=new GeocodingAsync(TabActivity.this);
        myGeocoding.execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                do{
                    SharedPreferences sharedPref = getSharedPreferences("GOeAT", Context.MODE_PRIVATE);
                    mDistrict=sharedPref.getString("curAddress","");
                }while (mDistrict=="");
                if (mDistrict.contains("Quận ")){
                    mDistrict=mDistrict.replace("Quận","District");
                }
                mDistrict=mDistrict.replace(" ","");
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Places").child("HoChiMinh").child(VNCharacterUtils.removeAccent(mDistrict)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        placesList.clear();
                        for (DataSnapshot data: snapshot.getChildren()) {
                            placesList.add(data.getValue(Place.class));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("test","failed");
                    }
                });
            }
        },1500);
    }
    public void myUpdateOperation(){
        int timer=2000;
        getNearby();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getFragmentRefreshListener()!=null){
                    getFragmentRefreshListener().onRefresh();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },timer);
    }
}