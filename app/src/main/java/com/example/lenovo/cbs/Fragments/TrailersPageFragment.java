package com.example.lenovo.cbs.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lenovo.cbs.Activities.PlayerActivity;
import com.example.lenovo.cbs.Model.Trailer;
import com.example.lenovo.cbs.R;

import io.realm.Realm;


public class TrailersPageFragment extends Fragment implements View.OnClickListener {

    private Trailer trailer;

    public static TrailersPageFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position",position);
        TrailersPageFragment fragment = new TrailersPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        Realm realm = Realm.getDefaultInstance();
        trailer = realm.where(Trailer.class).findAll().get(args.getInt("position"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trailers,container,false);
        TextView title = view.findViewById(R.id.trailer_title);
        ImageButton playTrailer = view.findViewById(R.id.play_trailer);
        title.setText(trailer.toString());
        playTrailer.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), PlayerActivity.class);
        intent.putExtra("url",trailer.getUrl());
        startActivity(intent);
    }
}
