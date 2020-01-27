package com.example.lenovo.cbs.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovo.cbs.Model.Tickets;
import com.example.lenovo.cbs.R;

import butterknife.BindViews;
import io.realm.Realm;
import io.realm.RealmResults;


public class MyTicketsPagerFragment extends Fragment {

    private Tickets ticket;
    public static MyTicketsPagerFragment newInstance(int position,String title) {

        Bundle args = new Bundle();
        args.putInt("position",position);
        args.putString("title",title);
        MyTicketsPagerFragment fragment = new MyTicketsPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle!=null){
            int pos = bundle.getInt("position");
            String title = bundle.getString("title");
            Realm realm = Realm.getDefaultInstance();
            RealmResults<Tickets> tickets = realm.where(Tickets.class).contains("title",title).findAll();
            ticket = tickets.get(pos);

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_my_tickets,container,false);
        TextView title = view.findViewById(R.id.film_title_ticket);
        TextView id = view.findViewById(R.id.id_tickets);
        TextView row = view.findViewById(R.id.row);
        TextView seat = view.findViewById(R.id.seat);
        TextView price = view.findViewById(R.id.price);
        TextView date = view.findViewById(R.id.date);
        TextView time = view.findViewById(R.id.time_film);
        title.setText(ticket.getTitle());
        id.setText(ticket.getId());
        row.setText(ticket.getRow());
        seat.setText(ticket.getSeat());
        price.setText(ticket.getPrice());
        date.setText(ticket.getDate());
        time.setText(ticket.getTime());
        return view;
    }
}
