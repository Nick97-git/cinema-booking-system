package com.example.lenovo.cbs.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.view.*;
import android.widget.*;

import com.example.lenovo.cbs.DialogSessions;
import com.example.lenovo.cbs.Model.Film;
import com.example.lenovo.cbs.Model.FilmSoon;
import com.example.lenovo.cbs.R;

import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmResults;

public class FilmInfoFragment extends Fragment implements View.OnClickListener {
    private Film film;
    private String mTheaterID;
    private int code;
    private int mPage;

    public static FilmInfoFragment newInstance(int page, String theaterId,int code ) {
        FilmInfoFragment pageFragment = new FilmInfoFragment();
        Bundle arguments = new Bundle();
        arguments.putInt("num", page);
        arguments.putInt("code",code);
        arguments.putString("id",theaterId);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle!=null){
            code = bundle.getInt("code");
            mTheaterID = bundle.getString("id");
            mPage = bundle.getInt("num");
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_film_info,container,false);
        Realm realm = Realm.getDefaultInstance();
            TextView dateFilm = v.findViewById(R.id.premiere_ua_film_info);
            TextView titleUA = v.findViewById(R.id.title_ua_film_info);
            TextView titleENG = v.findViewById(R.id.title_eng_film_info);
            TextView studios = v.findViewById(R.id.studios_film_info);
            TextView genres = v.findViewById(R.id.genres_film_info);
            TextView countries = v.findViewById(R.id.countries_film_info);
            TextView filmTime = v.findViewById(R.id.duration_film_info);
            ImageView imageView = v.findViewById(R.id.poster);
            TextView ratingFilm = v.findViewById(R.id.fim_rating_film_info);
            TextView synopsis = v.findViewById(R.id.synopsis);
            Button buyButton = v.findViewById(R.id.buyButton_film_info);
            CardView cardView = v.findViewById(R.id.descriptionCard);
            TextView premiere = v.findViewById(R.id.premiere_film_info);
            TextView time = v.findViewById(R.id.time_film_info);
            TextView rating = v.findViewById(R.id.rating_film_info);
            if (code == 1){
                RealmResults<FilmSoon> filmsSoon = realm.where(FilmSoon.class).sort("date").findAll();
                FilmSoon filmSoon = filmsSoon.get(mPage);
                titleUA.setText(filmSoon.getTitleUA());
                titleENG.setText(filmSoon.getTitleENG());
                studios.setText(filmSoon.getStudios());
                if (studios.getText() == null){
                    studios.setVisibility(View.GONE);
                }
                genres.setText(filmSoon.getGenres());
                countries.setText(filmSoon.getCountries());
                if (filmSoon.getSynopsis().equals("null")){
                    cardView.setVisibility(View.GONE);
                } else {
                    synopsis.setMovementMethod(new ScrollingMovementMethod());
                    synopsis.setText(filmSoon.getSynopsis());
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                dateFilm.setText(dateFormat.format(filmSoon.getDate()));
                checkNull(premiere,dateFilm);
                filmTime.setText(filmSoon.getTime());
                checkNull(time,filmTime);
                byte[]byteArray = filmSoon.getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
                roundedBitmapDrawable.setCornerRadius(50);
                imageView.setImageDrawable(roundedBitmapDrawable);
                ratingFilm.setText(filmSoon.getRating());
                checkNull(rating,ratingFilm);
                buyButton.setVisibility(View.GONE);
            } else if (code == 0){
                RealmResults<Film> films = realm.where(Film.class).findAll();
                film = films.get(mPage);
                titleUA.setText(film.getTitle());
                titleENG.setText(film.getTitleEng());
                studios.setText(film.getStudios());
                genres.setText(film.getGenres());
                countries.setText(film.getCountries());
                synopsis.setMovementMethod(new ScrollingMovementMethod());
                dateFilm.setText(film.getDate());
                filmTime.setText(film.getTime());
                byte[]byteArray = film.getImageID();
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
                roundedBitmapDrawable.setCornerRadius(50);
                imageView.setImageDrawable(roundedBitmapDrawable);
                ratingFilm.setText(film.getRating());
                checkNull(rating,ratingFilm);
                synopsis.setText(film.getSynopsis());
                buyButton.setOnClickListener(this);
            }

        return v;
    }

    private  void checkNull(TextView first,TextView second){
        if (second.getText().equals("null")){
            first.setVisibility(View.GONE);
            second.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View view) {
        DialogSessions dialog = new DialogSessions(getContext(),mTheaterID,film.getId(),getLoaderManager());
        dialog.createDialog();
    }
}
