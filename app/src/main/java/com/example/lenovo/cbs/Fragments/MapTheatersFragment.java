package com.example.lenovo.cbs.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.cbs.Activities.InfoTheaterActivity;
import com.example.lenovo.cbs.R;
import com.example.lenovo.cbs.RecyclerView.TheatersAdapter;


public class MapTheatersFragment extends Fragment implements TheatersAdapter.OnTheaterClickListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_view_cbs,container,false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TheatersAdapter adapter = new TheatersAdapter(this);
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onTheaterClick(String id) {
        Intent intent = new Intent(getContext(), InfoTheaterActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}
