package com.example.lenovo.cbs.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.lenovo.cbs.Loaders.FilmsLoader;

import com.example.lenovo.cbs.Model.Film;
import com.example.lenovo.cbs.R;

import io.realm.Realm;
import io.realm.RealmResults;


public class FilmsPagerFragment extends Fragment {


    private ProgressBar loadingIndicator;
    private static final int LOADER_FILMS = 0;
    @SuppressLint("SimpleDateFormat")
    private MyFragmentPagerAdapter pagerAdapter;
    private String mID;
    private RealmResults<Film> films;
    private ViewPager viewPager;
    private CardView cardView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Realm realm = Realm.getDefaultInstance();
        films = realm.where(Film.class).findAll();
        Bundle bundle = getArguments();
        int start = 1;
        if (bundle!=null){
            mID = bundle.getString("id");
            start = bundle.getInt("start");
        }

        if (start == 0){
            getLoaderManager().initLoader(LOADER_FILMS, null, loaderFilms);
        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_cbs, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        cardView = view.findViewById(R.id.cardWithViewPager);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        cardView.setVisibility(View.GONE);
        pagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getLoaderManager().destroyLoader(LOADER_FILMS);
    }


    private LoaderManager.LoaderCallbacks<String> loaderFilms = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            FilmsLoader filmsLoader = new FilmsLoader(getActivity(), mID,0);
            filmsLoader.forceLoad();
            return filmsLoader;
        }


        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            loadingIndicator.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            pagerAdapter.notifyDataSetChanged();
            viewPager.setAdapter(pagerAdapter);
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };



    private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {


        private MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FilmPageFragment.newInstance(position,mID);
        }

        @Override
        public int getCount() {
            return films.size();
        }

    }
}

