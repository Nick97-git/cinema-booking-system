package com.example.lenovo.cbs.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lenovo.cbs.Loaders.TrailersLoader;
import com.example.lenovo.cbs.Model.Trailer;
import com.example.lenovo.cbs.R;

import io.realm.Realm;
import io.realm.RealmResults;


public class TrailersFragment extends Fragment  {

    private String mID;
    private static final int LOADER_TRAILERS = 6;
    private ViewPager vp;
    private ProgressBar progress;
    private CardView cardView;


    public static TrailersFragment newInstance(String id) {
        TrailersFragment trailersFragment = new TrailersFragment();
        Bundle arguments = new Bundle();
        arguments.putString("id", id);
        trailersFragment.setArguments(arguments);
        return trailersFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_TRAILERS,null,loaderTrailers);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle!=null){
            mID = bundle.getString("id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_trailers,container,false);
        vp = view.findViewById(R.id.trailers_viewPager);
        progress = view.findViewById(R.id.trailersProgress);
        cardView = view.findViewById(R.id.cardView_error);
        return view;
    }

    private LoaderManager.LoaderCallbacks<String> loaderTrailers = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            TrailersLoader trailersLoader = new TrailersLoader(getActivity(),mID);
            trailersLoader.forceLoad();
            return trailersLoader;
        }


        @Override
        public void onLoadFinished(Loader<String> loader, String trailers) {
            TrailersPagerAdapter adapter = new TrailersPagerAdapter(getFragmentManager());
            vp.setAdapter(adapter);
            progress.setVisibility(View.GONE);
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };

    private class TrailersPagerAdapter extends FragmentStatePagerAdapter{

        private TrailersPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TrailersPageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<Trailer> mTrailers = realm.where(Trailer.class).findAll();
            if (mTrailers.size() == 0){
               cardView.setVisibility(View.VISIBLE);
            }
            return mTrailers.size();
        }
    }

}
