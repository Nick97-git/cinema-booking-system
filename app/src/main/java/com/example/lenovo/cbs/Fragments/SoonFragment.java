package com.example.lenovo.cbs.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.lenovo.cbs.Activities.InfoActivity;
import com.example.lenovo.cbs.Loaders.FilmsLoader;
import com.example.lenovo.cbs.Model.FilmSoon;
import com.example.lenovo.cbs.R;
import com.example.lenovo.cbs.RecyclerView.SoonAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;


public class SoonFragment extends Fragment  {

    private static final int LOADER_SOON = 7;
    private RecyclerView rw;
    private ProgressBar progressBar;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Realm realm = Realm.getDefaultInstance();
        FilmSoon filmSoon = realm.where(FilmSoon.class).findFirst();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        if (filmSoon.getId() == null){
            getLoaderManager().initLoader(LOADER_SOON,null,loaderSoon);
        } else if (!dateFormat.format(date).equals(dateFormat.format(filmSoon.getDate())) ){
            setSoonAdapter();
        } else if (dateFormat.format(date).equals(dateFormat.format(filmSoon.getDate()))){
            getLoaderManager().initLoader(LOADER_SOON,null,loaderSoon);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_view_soon,container,false);
        rw = v.findViewById(R.id.recyclerView_soon);
        progressBar = v.findViewById(R.id.recycler_progress);
        return v;
    }

    private void setSoonAdapter(){
        rw.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar.setVisibility(View.GONE);
        SoonAdapter soonAdapter = new SoonAdapter((position, id, code) -> {
            Bundle bundle = new Bundle();
            bundle.putInt("num",position);
            bundle.putString("id",id);
            bundle.putInt("code",code);
            Intent intent = new Intent(getContext(), InfoActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        rw.setAdapter(soonAdapter);
    }

    private LoaderManager.LoaderCallbacks<String> loaderSoon = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            FilmsLoader filmsLoader = new FilmsLoader(getActivity(), "",1);
            filmsLoader.forceLoad();
            return filmsLoader;
        }



        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            setSoonAdapter();


        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };
}
