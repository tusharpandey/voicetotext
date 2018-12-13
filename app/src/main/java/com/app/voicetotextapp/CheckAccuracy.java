package com.app.voicetotextapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.app.voicetotextapp.util.Constants;
import com.app.voicetotextapp.viewpager.MyPageAdapter;
import com.app.voicetotextapp.viewpager.VoiceAccuracyFragment;

import java.util.ArrayList;
import java.util.List;

public class CheckAccuracy extends AppCompatActivity {

    private MyPageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
        getSupportActionBar().setTitle(getString(R.string.check_accuracy));
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<>();
        VoiceAccuracyFragment fragment = new VoiceAccuracyFragment();
        fragment.setData(getIntent().getStringExtra(Constants.QUESTION),getIntent().getStringExtra(Constants.TOKEN));
        fList.add(fragment);
        return fList;
    }
}
