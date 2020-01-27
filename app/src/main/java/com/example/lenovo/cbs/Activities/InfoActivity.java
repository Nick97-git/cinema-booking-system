package com.example.lenovo.cbs.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.lenovo.cbs.Fragments.ActorsFragment;
import com.example.lenovo.cbs.Fragments.FilmInfoFragment;
import com.example.lenovo.cbs.Fragments.NewsFragment;
import com.example.lenovo.cbs.Fragments.TrailersFragment;
import com.example.lenovo.cbs.Model.Film;
import com.example.lenovo.cbs.Model.FilmSoon;
import com.example.lenovo.cbs.PagerTabs;
import com.example.lenovo.cbs.R;

import io.realm.Realm;
import io.realm.RealmResults;


public class InfoActivity extends AppCompatActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_film_info);
        Realm realm = Realm.getDefaultInstance();
        ViewPager viewPager = findViewById(R.id.viewPagerTab);
        final TabLayout tabLayout = findViewById(R.id.tabs);
        PagerTabs adapter = new PagerTabs(getSupportFragmentManager());
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            String mTheaterID = extras.getString("id");
            int mPage = extras.getInt("num");
            if (extras.getInt("code") == 0) {
                RealmResults<Film> films = realm.where(Film.class).findAll();
                Film film = films.get(mPage);
                adapter.addFragment(FilmInfoFragment.newInstance(mPage, mTheaterID,0),getResources().getString(R.string.about_film));
                adapter.addFragment(ActorsFragment.newInstance(film.getId()),getResources().getString(R.string.actors));
                adapter.addFragment(TrailersFragment.newInstance(film.getId()),getResources().getString(R.string.trailers));
                adapter.addFragment(NewsFragment.newInstance(film.getId(), film.getTitle()),getResources().getString(R.string.news));
            } else if (extras.getInt("code") == 1){
                RealmResults<FilmSoon> filmsSoon = realm.where(FilmSoon.class).sort("date").findAll();
                FilmSoon filmSoon = filmsSoon.get(mPage);
                adapter.addFragment(FilmInfoFragment.newInstance(mPage, mTheaterID,1),getResources().getString(R.string.about_film));
                adapter.addFragment(ActorsFragment.newInstance(filmSoon.getId()),getResources().getString(R.string.actors));
                adapter.addFragment(TrailersFragment.newInstance(filmSoon.getId()),getResources().getString(R.string.trailers));
                adapter.addFragment(NewsFragment.newInstance(filmSoon.getId(), filmSoon.getTitleUA()),getResources().getString(R.string.news));

            }
        }
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }


}
