package com.example.lenovo.cbs.Loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.lenovo.cbs.Model.FilmSoon;
import com.example.lenovo.cbs.Model.HttpGetRequest;
import com.example.lenovo.cbs.Model.Film;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;


public class FilmsLoader extends AsyncTaskLoader<String>{
    private static final String LOG_TAG = "ERROR";
    private static final String GET_STUDIOS = "http://kino-teatr.ua:8081/services/api/studios?apiKey=pol1kh111&size=2000";
    private static final String GET_GENRES = "http://kino-teatr.ua:8081/services/api/genres?apiKey=pol1kh111&size=2000";
    private static final String GET_COUNTRIES = "http://kino-teatr.ua:8081/services/api/countries?apiKey=pol1kh111&size=2000";
    private String mTheaterID;
    private int mID;

    public FilmsLoader(Context context, String theaterId, int filmId){
        super(context);
        mTheaterID = theaterId;
        mID = filmId;

    }


    @Override
    public String  loadInBackground() {
        String USGS_REQUEST_URL = "";
        if (mID == 0){
            USGS_REQUEST_URL =
                    "http://kino-teatr.ua:8081/services/api/cinema/" + mTheaterID + "/shows?apiKey=pol1kh111&size=30&detalization=FULL";
        } else if (mID == 1){
            USGS_REQUEST_URL = "http://kino-teatr.ua:8081/services/api/films/premieres?apiKey=pol1kh111&size=200";
        }
        URL url = HttpGetRequest.createUrl(USGS_REQUEST_URL);

        String jsonResponse = "";
        try {
            jsonResponse = HttpGetRequest.makeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
        }
        String c = "";
        if (mID == 0){
            c = extractFeatureFromJson(jsonResponse);
        } else if (mID == 1){
        c = extractFilmsSoonFromJson(jsonResponse);
    }
            return c;
    }

    @Override
    public void deliverResult(String data) {
        super.deliverResult(data);
    }



    private String extractFeatureFromJson(String filmJSON) {
        String complete = "Completed";
        Realm realm = Realm.getDefaultInstance();

        try {
            JSONObject baseJsonResponse = new JSONObject(filmJSON);
            JSONArray featureArray = baseJsonResponse.getJSONArray("films");

            for (int i = 0; i < featureArray.length(); i++) {
                JSONObject currentFilm = featureArray.getJSONObject(i);

                String id = currentFilm.getString("id");
                String title = currentFilm.getString("title");
                String titleOrig = currentFilm.getString("title_orig");
                String date = currentFilm.getString("premiere_ukraine");
                String time = currentFilm.getString("duration")+" минут";
                String rating = currentFilm.getString("age_limit")+"+";
                JSONArray studio_ids = currentFilm.getJSONArray("studio_ids");
                String[] studios = extractArray(studio_ids);
                JSONArray genre_id = currentFilm.getJSONArray("genre_ids");
                String[]genres = extractArray(genre_id);
                JSONArray country_ids = currentFilm.getJSONArray("country_ids");
                String[] countries = extractArray(country_ids);
                String synopsis = android.text.Html.fromHtml(getDescription(id)).toString();
                Bitmap image = getBitmap(id);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Film film = new Film();
                film.setId(id);
                film.setTitleEng(titleOrig);
                film.setStudios(getNamesByIDs(GET_STUDIOS,studios));
                film.setGenres(getNamesByIDs(GET_GENRES,genres));
                film.setCountries(getNamesByIDs(GET_COUNTRIES,countries));
                film.setTitle(title);
                film.setDate(formatDate(date));
                film.setTime(time);
                film.setRating(rating);
                film.setSynopsis(synopsis);
                film.setImageID(byteArray);
                realm.beginTransaction();
                realm.insert(film);
                realm.commitTransaction();
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the films JSON results", e);
        }
        return complete;
    }

    private String extractFilmsSoonFromJson(String filmJSON) {
        String complete = "Completed";
        deleteFilmsFromRealm();
        Realm realm = Realm.getDefaultInstance();
        try {
            JSONObject baseJsonResponse = new JSONObject(filmJSON);
            JSONArray featureArray = baseJsonResponse.getJSONArray("content");

            for (int i = 0; i < featureArray.length(); i++) {
                JSONObject currentFilm = featureArray.getJSONObject(i);
                String id = currentFilm.getString("id");
                String titleUA = currentFilm.getString("title");
                String titleENG = currentFilm.getString("title_orig");
                String date = currentFilm.getString("premiere_ukraine");
                String newDate = formatDate(date);
                String time = currentFilm.getString("duration");
                String age = currentFilm.getString("age_limit");
                JSONArray studio_ids = currentFilm.getJSONArray("studio_ids");
                String[] studios = extractArray(studio_ids);
                JSONArray genre_id = currentFilm.getJSONArray("genre_ids");
                String[]genres = extractArray(genre_id);
                JSONArray country_ids = currentFilm.getJSONArray("country_ids");
                String[] countries = extractArray(country_ids);
                String description = android.text.Html.fromHtml(getDescription(id)).toString();
                Bitmap image = getBitmap(id);
                byte[] byteArray;
                if (image == null){
                    byteArray = null;
                } else{
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();
                }

                FilmSoon filmSoon = new FilmSoon();
                filmSoon.setId(id);
                filmSoon.setRating(age);
                filmSoon.setTime(time);
                filmSoon.setSynopsis(description);
                filmSoon.setCountries(getNamesByIDs(GET_COUNTRIES,countries));
                filmSoon.setDate(stringToDate(newDate));
                filmSoon.setImage(byteArray);
                filmSoon.setGenres(getNamesByIDs(GET_GENRES,genres));
                filmSoon.setStudios(getNamesByIDs(GET_STUDIOS,studios));
                filmSoon.setTitleENG(titleENG);
                filmSoon.setTitleUA(titleUA);
                realm.beginTransaction();
                realm.insert(filmSoon);
                realm.commitTransaction();
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the filmsSoon JSON results", e);
        }
        return complete;
    }

    private Bitmap getBitmap(String id) {
        String posterByID = "http://kino-teatr.ua:8081/services/api/film/" + id + "/poster?apiKey=pol1kh111&width=1000&height=1000&ratio=1";
        URL url = HttpGetRequest.createUrl(posterByID);
        return extractImage(url);

    }

    private String getDescription(String id){
        String descriptionById = "http://kino-teatr.ua:8081/services/api/film/" + id + "?apiKey=pol1kh111";
        URL url = HttpGetRequest.createUrl(descriptionById);
        String jsonResponse = "";
        try {
            jsonResponse = HttpGetRequest.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with urlConnection", e);
        }

        return extractDescription(jsonResponse);
    }

    private Bitmap extractImage(URL url) {
        Bitmap mIcon = null;
        try {
            mIcon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem extracting image", e);
        }
        return mIcon;

    }

    private String extractDescription(String jsonResponse) {
        String description = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            description = jsonObject.getString("description");
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem extracting description", e);
        }
        return description;
    }

    private String getNamesByIDs(String byIds,String [] studiosID){
        URL url = HttpGetRequest.createUrl(byIds);
        String jsonResponse = "";
        try {
            jsonResponse = HttpGetRequest.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with urlConnection", e);
        }

        return extractNames(jsonResponse,studiosID);
    }


    private String extractNames(String jsonResponse, String[] ids){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <ids.length ; i++) {
            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray content = jsonObject.getJSONArray("content");
                for (int j = 0; j <content.length() ; j++) {
                    JSONObject currentGenre = content.getJSONObject(j);
                    String id = currentGenre.getString("id");
                    if (ids[i].equals(id)){
                        String name = currentGenre.getString("name");;
                        stringBuilder.append(name).append(",");
                    }
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem extracting description", e);
            }
        }

        return stringBuilder.toString();
    }

    private String[] extractArray(JSONArray array){
        String[] strings = new String[array.length()];
        for (int j = 0; j <array.length() ; j++) {
            try {
                strings[j] = array.getString(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return strings ;
    }
    private String formatDate(String date) {
        if (!date.equals("null")) {
            StringBuilder stringBuilder = new StringBuilder(date);
            stringBuilder.delete(10, 19);
            String s = new String(stringBuilder);
            String[] subStr;
            String delimeter = "-";
            subStr = s.split(delimeter);
            return subStr[2] + "." + subStr[1] + "." + subStr[0];
        }else {
            return "Неизвестно";
        }
    }

    private Date stringToDate(String newDate){
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd.MM.yyyy");
        Date date = null;
        try {
            date= format.parse(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private void deleteFilmsFromRealm(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<FilmSoon> films = realm.where(FilmSoon.class).findAll();
        realm.commitTransaction();
        films.deleteAllFromRealm();
        realm.beginTransaction();
    }
}