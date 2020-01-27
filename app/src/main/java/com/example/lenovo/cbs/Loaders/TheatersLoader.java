package com.example.lenovo.cbs.Loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.lenovo.cbs.Model.HttpGetRequest;
import com.example.lenovo.cbs.Model.Theater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import io.realm.Realm;
import io.realm.RealmResults;

public class TheatersLoader extends AsyncTaskLoader<String> {

    private static final String LOG_TAG = "ERROR";

    public TheatersLoader(Context context) {
        super(context);

    }
    @Override
    public String loadInBackground() {
        String sessions = "http://kino-teatr.ua:8081/services/api/city/1/cinemas?apiKey=pol1kh111&size=200";
        URL url = HttpGetRequest.createUrl(sessions);
        String jsonResponse = "";
        try {
            jsonResponse = HttpGetRequest.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with urlConnection", e);
        }

        return extractTheaters(jsonResponse);
    }

    private String extractTheaters(String jsonResponse) {
        String complete = "Completed";
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Theater> theaters = realm.where(Theater.class).findAll();
        theaters.deleteAllFromRealm();
        realm.commitTransaction();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray featureArray = jsonObject.getJSONArray("content");
            for (int i = 0; i <featureArray.length(); i++) {
                JSONObject currentTheater = featureArray.getJSONObject(i);
                String id = currentTheater.getString("id");
                String name = currentTheater.getString("name");
                String site = currentTheater.getString("site");
                double latitude = currentTheater.getDouble("latitude");
                double longitude = currentTheater.getDouble("longitude");
                String address = currentTheater.getString("address");
                Theater theater = new Theater();
                theater.setId(id);
                theater.setName(name);
                theater.setSite(site);
                theater.setLatitude(latitude);
                theater.setLongitude(longitude);
                theater.setAddress(address);
                realm.beginTransaction();
                realm.insert(theater);
                realm.commitTransaction();

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem extracting theaters", e);
        }
        return complete;

    }
}
