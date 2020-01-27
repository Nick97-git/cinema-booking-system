package com.example.lenovo.cbs.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.lenovo.cbs.Model.Tickets;
import com.example.lenovo.cbs.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class MyTicketsFragment extends Fragment {

    private Realm realm  = Realm.getDefaultInstance();
    private RealmResults<Tickets> tickets = realm.where(Tickets.class).findAll();
    private String mTitle;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        realm.beginTransaction();
        tickets.deleteAllFromRealm();
        realm.commitTransaction();
        Tickets one = new Tickets();
        one.setTitle("Месники Війна Нескінченності");
        one.setId("9234567890");
        one.setDate("13.05.2018");
        one.setPrice("140");
        one.setRow("10");
        one.setTime("12:40");
        one.setSeat("2");
        Tickets two = new Tickets();
        two.setTitle("Месники Війна Нескінченності");
        two.setId("9234567891");
        two.setDate("13.05.2018");
        two.setPrice("140");
        two.setRow("10");
        two.setTime("12:40");
        two.setSeat("3");
        Tickets three = new Tickets();
        three.setTitle("Дедпул 2");
        three.setId("9324267891");
        three.setDate("20.05.2018");
        three.setPrice("75");
        three.setRow("2");
        three.setTime("21:20");
        three.setSeat("11");
        Tickets four = new Tickets();
        four.setTitle("Дедпул 2");
        four.setId("9924263891");
        four.setDate("20.05.2018");
        four.setPrice("75");
        four.setRow("2");
        four.setTime("21:20");
        four.setSeat("12");
        realm.beginTransaction();
        realm.insert(one);
        realm.insert(two);
        realm.insert(three);
        realm.insert(four);
        realm.commitTransaction();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager_tickets,container,false);
        Spinner spinnerTitles = view.findViewById(R.id.spinnerTitles);
        final ViewPager viewPager = view.findViewById(R.id.ticket_view_pager);
        ArrayAdapter<String> spinnerAdapterTheaters = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, getTitles());
        spinnerTitles.setAdapter(spinnerAdapterTheaters);
        spinnerTitles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                mTitle = adapterView.getItemAtPosition(pos).toString();
                MyTicketsPagerAdapter adapter = new MyTicketsPagerAdapter(getFragmentManager());
                viewPager.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    public ArrayList<String> getTitles(){
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < tickets.size() ; i++) {
            Tickets ticket = tickets.get(i);
            String title = ticket.getTitle();
            /*if (titles.size()!= 0){
                for (int j = 0; j < titles.size() ; j++) {
                    if (!(title.equals(titles.get(j)))){
                        titles.add(title);
                    }
                }
            } else if (titles.size()>0){
                titles.add(title);
            }*/
            titles.add(title);

        }
        return  titles;
    }


    private class MyTicketsPagerAdapter extends FragmentStatePagerAdapter{

        private MyTicketsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MyTicketsPagerFragment.newInstance(position,mTitle);
        }

        @Override
        public int getCount() {
            RealmResults<Tickets> ticketsByTitle = realm.where(Tickets.class).contains("title",mTitle).findAll();
            return ticketsByTitle.size();
        }
    }
}
