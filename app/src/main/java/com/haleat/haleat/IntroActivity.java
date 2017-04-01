package com.haleat.haleat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class IntroActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 1;

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    private Button skipButton;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("initdetails",MODE_PRIVATE);
        preferences.edit().putBoolean("init",true).commit();
        setContentView(R.layout.activity_intro);
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        skipButton = (Button) findViewById(R.id.btn_skip);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCameraActivity();
            }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new IntroFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private void launchCameraActivity(){
        startActivity(new Intent(this, CameraActivity.class));
        finish();
    }

    public static class IntroFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.intro_slide1, container, false);

            return rootView;
        }
    }

    private void checkIfFirstRun(){
        if(preferences.getBoolean("init",true)){
            preferences.edit().putBoolean("init",false).commit();
        }
        else{
            launchCameraActivity();
        }
        return;
    }
    protected void onResume(){
        super.onResume();
        checkIfFirstRun();
    }
    protected void onDestroy(){
        if(preferences.getBoolean("init",false)){
            preferences.edit().putBoolean("init",true).commit();
        }
        super.onDestroy();
    }
}
