package com.example.lenovo.cbs.ViewPager;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lenovo.cbs.Activities.BuyActivity;
import com.example.lenovo.cbs.Activities.InfoActivity;
import com.example.lenovo.cbs.DialogSessions;
import com.example.lenovo.cbs.Loaders.SessionsLoader;
import com.example.lenovo.cbs.Model.Film;
import com.example.lenovo.cbs.Model.Session;
import com.example.lenovo.cbs.Model.Theater;
import com.example.lenovo.cbs.R;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class FilmPageFragment extends Fragment implements View.OnClickListener {

    private int mPage;
    private String mTheaterID;
    private Film film;

    static FilmPageFragment newInstance(int page, String theaterID) {
        FilmPageFragment pageFragment = new FilmPageFragment();
        Bundle arguments = new Bundle();
        arguments.putString("theater", theaterID);
        arguments.putInt("num", page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPage = bundle.getInt("num");
            mTheaterID = bundle.getString("theater");
            Realm realm = Realm.getDefaultInstance();
            RealmResults<Film> films = realm.where(Film.class).findAll();
            films.addChangeListener((films1, changeSet) -> changeSet.getInsertions());
            film = films.get(mPage);
        }


    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cardview_film_item, container, false);
        Button info = view.findViewById(R.id.infoButton);
        ImageButton description = view.findViewById(R.id.descriptionButton);
        Button buy = view.findViewById(R.id.buyButton);
        buy.setOnClickListener(this);
        info.setOnClickListener(this);
        description.setOnClickListener(this);
        ImageView poster = view.findViewById(R.id.poster);
        byte[]byteArray = film.getImageID();
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        poster.setImageBitmap(bitmap);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.infoButton:
                callIntent();
                break;
            case R.id.descriptionButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.description)
                        .setMessage(film.getSynopsis())
                        .setIcon(R.drawable.ic_info_black_24dp)
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.buyButton:
                DialogSessions dialog = new DialogSessions(getContext(),mTheaterID,film.getId(),getLoaderManager());
                dialog.createDialog();
                break;

        }
    }



    public void callIntent() {
        Bundle bundle = new Bundle();
        bundle.putInt("num",mPage);
        bundle.putString("id",mTheaterID);
        bundle.putInt("code",0);
        Intent intent = new Intent(getContext(), InfoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }



}
