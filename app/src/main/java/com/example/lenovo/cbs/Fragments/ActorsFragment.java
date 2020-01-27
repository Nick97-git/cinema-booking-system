package com.example.lenovo.cbs.Fragments;

import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lenovo.cbs.Loaders.ActorsLoader;
import com.example.lenovo.cbs.Model.Actor;
import com.example.lenovo.cbs.R;
import com.example.lenovo.cbs.RecyclerView.RecyclerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ActorsFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView rw;
    @BindView(R.id.actorsProgress)
    ProgressBar progress;
    @BindView(R.id.cardView_error)
    CardView cardView;
    public static final int LOADER_ACTORS = 3;
    private ArrayList<Actor> mActors;
    private String mID;
    private Unbinder unbinder;

    public static ActorsFragment newInstance(String id) {
        ActorsFragment actorsFragment = new ActorsFragment();
        Bundle arguments = new Bundle();
        arguments.putString("id", id);
        actorsFragment.setArguments(arguments);
        return actorsFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ACTORS,null,loaderActors);
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
        View view = inflater.inflate(R.layout.recycler_view_cbs,container,false);
         unbinder = ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private LoaderManager.LoaderCallbacks<ArrayList<Actor>> loaderActors = new LoaderManager.LoaderCallbacks<ArrayList<Actor>>() {
        @Override
        public Loader<ArrayList<Actor>> onCreateLoader(int id, Bundle args) {
            ActorsLoader actorsLoader = new ActorsLoader(getActivity(),mID,mActors);
            actorsLoader.forceLoad();
            return actorsLoader;
        }


        @Override
        public void onLoadFinished(Loader<ArrayList<Actor>> loader, ArrayList<Actor> actors) {
            if (actors.size() != 0) {
                rw.setLayoutManager(new LinearLayoutManager(getContext()));
                RecyclerAdapter adapter = new RecyclerAdapter(actors);
                rw.setAdapter(adapter);
            } else if (actors.size() == 0){
                cardView.setVisibility(View.VISIBLE);
            }
            progress.setVisibility(View.GONE);
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Actor>> loader) {
            mActors = null;
        }
    };
}
