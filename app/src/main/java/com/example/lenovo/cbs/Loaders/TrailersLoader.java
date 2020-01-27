package com.example.lenovo.cbs.Loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.lenovo.cbs.Model.HttpGetRequest;
import com.example.lenovo.cbs.Model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import io.realm.Realm;
import io.realm.RealmResults;


public class TrailersLoader extends AsyncTaskLoader<String> {
    private static final String LOG_TAG = "ERROR";
    private String mID;

    public TrailersLoader(Context context, String id){
        super(context);
        mID = id;
    }

    @Override
    public String loadInBackground() {
        String USGS_REQUEST_URL =
                "http://kino-teatr.ua:8081/services/api/film/"+mID+"/trailers?apiKey=pol1kh111&size=200";
        URL url = HttpGetRequest.createUrl(USGS_REQUEST_URL);

        String jsonResponse = "";
        try {
            jsonResponse = HttpGetRequest.makeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
        }
        return extractTrailersFromJson(jsonResponse);
    }

    private String extractTrailersFromJson(String jsonResponse) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Trailer> trailers = realm.where(Trailer.class).findAll();
        realm.beginTransaction();
        trailers.deleteAllFromRealm();
        realm.commitTransaction();
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray featureArray = baseJsonResponse.getJSONArray("content");
            for (int i = 0; i < featureArray.length(); i++) {
                JSONObject currentFilm = featureArray.getJSONObject(i);
                String title = currentFilm.getString("title");
                String language = currentFilm.getString("language");
                String url = currentFilm.getString("url");
                Trailer trailer = new Trailer();
                trailer.setTitle(title);
                trailer.setLanguage(language);
                trailer.setUrl(url);
                realm.beginTransaction();
                realm.insert(trailer);
                realm.commitTransaction();
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the trailers JSON results", e);
        }
        return "Completed";
    }
}
