package com.example.lenovo.cbs.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lenovo.cbs.Loaders.TheatersLoader;
import com.example.lenovo.cbs.Model.Film;
import com.example.lenovo.cbs.Model.Theater;
import com.example.lenovo.cbs.R;
import com.example.lenovo.cbs.ViewPager.FilmsPagerFragment;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.view.View.GONE;


public class TheaterFragment extends Fragment {
    private Unbinder unbinder;
    private FragmentManager fm;
    private Spinner spinner;
    private static final int LOADER_THEATERS = 1;
    private RealmResults<Theater> mTheaters ;
    private String selected;
    private TextView textView;
    private Button button;
    private ProgressBar progress;
    private CardView card;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Realm realm = Realm.getDefaultInstance();
        mTheaters = realm.where(Theater.class).findAll();
        if (mTheaters.size() == 0){
            getLoaderManager().initLoader(LOADER_THEATERS, null, loaderTheaters);
            textView.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            card.setVisibility(GONE);
        } else {
            spinnerWithTheaters();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_theater,container,false);
        spinner = view.findViewById(R.id.spinnerTheater);
        textView = view.findViewById(R.id.chooseTheater);
        card = view.findViewById(R.id.cardWithSpinner);
        button = view.findViewById(R.id.nextFragment);
        progress = view.findViewById(R.id.loading);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getLoaderManager().destroyLoader(LOADER_THEATERS);
    }


    @Optional
    @OnClick(R.id.nextFragment)
    public void onNext(){
        deleteFilms();
        Bundle bundle = new Bundle();
        bundle.putString("id",getTheaterID(selected));
        bundle.putInt("start",0);
        Fragment fragment = new FilmsPagerFragment();
        fragment.setArguments(bundle);
        callFragment(fragment);

    }

    private void deleteFilms(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Film> films = realm.where(Film.class).findAll();
        realm.beginTransaction();
        films.deleteAllFromRealm();
        realm.commitTransaction();
    }

    private void callFragment(Fragment fragment){
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment,"Pager")
                .commit();
    }

    private LoaderManager.LoaderCallbacks<String> loaderTheaters = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            TheatersLoader theatersLoader = new TheatersLoader(getActivity());
            theatersLoader.forceLoad();
            return theatersLoader;
        }


        @Override
        public void onLoadFinished(Loader<String> loader, String theaters) {
            spinnerWithTheaters();
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
            mTheaters = null;
        }
    };

    private String[] getListTheaters() {
        String[] theater = new String[mTheaters.size()];
        for (int i = 0; i < mTheaters.size(); i++) {
            Theater t = mTheaters.get(i);
            theater[i] = t.getName();
        }

        Arrays.sort(theater);
        return theater;
    }

    public String getTheaterID(String theater) {
        String id = "";
        for (int i = 0; i < mTheaters.size(); i++) {
            Theater t = mTheaters.get(i);
            String name = t.getName();
            if (name.equals(theater)) {
                id = t.getId();
            }
        }
        return id;
    }


    private void spinnerWithTheaters(){
        ArrayAdapter<String> spinnerAdapterTheaters = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, getListTheaters());
        spinner.setAdapter(spinnerAdapterTheaters);
        card.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                selected = adapterView.getItemAtPosition(pos).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
