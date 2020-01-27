package com.example.lenovo.cbs.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.example.lenovo.cbs.Fragments.MapFragment;
import com.example.lenovo.cbs.Fragments.SiteFragment;
import com.example.lenovo.cbs.Model.Theater;
import com.example.lenovo.cbs.PagerTabs;
import com.example.lenovo.cbs.R;

import io.realm.Realm;

public class InfoTheaterActivity extends FragmentActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_film_info);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Realm realm = Realm.getDefaultInstance();
        Theater theater = realm.where(Theater.class).contains("id", id).findFirst();
        ViewPager vp = findViewById(R.id.viewPagerTab);
        TabLayout tab = findViewById(R.id.tabs);
        PagerTabs pagerTabs = new PagerTabs(getSupportFragmentManager());
        pagerTabs.addFragment(MapFragment.newInstance(id),getResources().getString(R.string.map));
        pagerTabs.addFragment(SiteFragment.newInstance(theater.getSite()),getResources().getString(R.string.official_site));
        vp.setAdapter(pagerTabs);
        tab.setupWithViewPager(vp);
    }


}
