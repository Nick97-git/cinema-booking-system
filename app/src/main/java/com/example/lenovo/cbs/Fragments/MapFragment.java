package com.example.lenovo.cbs.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.cbs.Model.Theater;
import com.example.lenovo.cbs.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.realm.Realm;


public class MapFragment extends Fragment {

    public static MapFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Bundle bundle = getArguments();
        Realm realm = Realm.getDefaultInstance();
        final Theater theater = realm.where(Theater.class).contains("id", bundle.getString("id")).findFirst();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.fragment_container, mapFragment).commit();
        }

        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                LatLng currentTheater = new LatLng(theater.getLatitude(), theater.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currentTheater)
                        .zoom(17)
                        .bearing(45)
                        .tilt(20)
                        .build();
                googleMap.addMarker(new MarkerOptions().position(currentTheater).title(theater.toString()));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                googleMap.animateCamera(cameraUpdate);
            });

        }
        return view;
    }
}
