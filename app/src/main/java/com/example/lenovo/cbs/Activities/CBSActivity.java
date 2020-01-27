package com.example.lenovo.cbs.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lenovo.cbs.Fragments.FeaturesFragment;
import com.example.lenovo.cbs.Fragments.SoonFragment;
import com.example.lenovo.cbs.Fragments.TheaterFragment;
import com.example.lenovo.cbs.Model.Film;
import com.example.lenovo.cbs.R;
import com.example.lenovo.cbs.ViewPager.FilmsPagerFragment;

import io.realm.Realm;
import io.realm.RealmResults;

public class CBSActivity extends AppCompatActivity {
    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        setContentView(R.layout.activity_cbs);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.reChooseTheater:
                    callFragment(new TheaterFragment(),"theaters");
                    break;
                case R.id.listItems:
                    callFragment(new FilmsPagerFragment(),"films");
                    break;
                case R.id.soon:
                    callFragment(new SoonFragment(),"soon");
                    break;
            }
            return true;
        });
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new TheaterFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cbs,menu);
       return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu:
                callFragment(new FeaturesFragment(),"feature");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callFragment(Fragment fragment,String back){
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putString("id",intent.getStringExtra("id"));
        fragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(back)
                .commit();
    }


}
