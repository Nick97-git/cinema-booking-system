package com.example.lenovo.cbs.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lenovo.cbs.Model.Tickets;
import com.example.lenovo.cbs.R;
import com.example.lenovo.cbs.RecyclerView.TicketsAdapter;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class TicketsBackFragment extends Fragment implements View.OnClickListener {

    private Realm realm = Realm.getDefaultInstance();
    private RealmResults<Tickets> tickets = realm.where(Tickets.class).findAll();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets_back,container,false);
        Button ticketsBack = view.findViewById(R.id.tickets_back);
        ticketsBack.setOnClickListener(this);
        RecyclerView recyclerView = view.findViewById(R.id.tickets_back_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TicketsAdapter adapter = new TicketsAdapter(getContext(), ids -> {
            for (int i = 0; i < ids.size() ; i++) {
                for (int j = 0; j <tickets.size() ; j++) {
                    Tickets ticket = tickets.get(j);
                    if (ids.get(i).equals(ticket.getId()) ){
                        realm.beginTransaction();
                        tickets.deleteFromRealm(j);
                        realm.commitTransaction();
                    }
                }
            }
        });
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        return view;
    }



    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.warning)
                .setMessage(R.string.tickets_back)
                .setIcon(R.drawable.ic_error_black_24dp)
                .setCancelable(false)
                .setNegativeButton(R.string.no, (dialog, i) -> dialog.dismiss())
                .setPositiveButton(R.string.yes, (dialog, i) -> {
                    dialog.dismiss();
                    Toast.makeText(getContext(),R.string.money,Toast.LENGTH_LONG).show();
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
