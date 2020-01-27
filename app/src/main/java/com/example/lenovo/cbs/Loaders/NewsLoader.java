package com.example.lenovo.cbs.Loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.lenovo.cbs.Model.HttpGetRequest;
import com.example.lenovo.cbs.Model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import io.realm.Realm;
import io.realm.RealmResults;


public class NewsLoader extends AsyncTaskLoader<String> {
    private static final String LOG_TAG = "ERROR";
    private String mID;

    public NewsLoader(Context context,String id){
        super(context);
        mID = id;
    }
    @Override
    public String loadInBackground() {
        String USGS_REQUEST_URL =
                "http://kino-teatr.ua:8081/services/api/film/"+mID+"/news?apiKey=pol1kh111&size=2000";
        URL url = HttpGetRequest.createUrl(USGS_REQUEST_URL);

        String jsonResponse = "";
        try {
            jsonResponse = HttpGetRequest.makeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
        }
        return extractNewsFromJson(jsonResponse);
    }

    @Override
    public void deliverResult(String data) {
        super.deliverResult(data);
    }

    private String extractNewsFromJson(String filmJSON) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<News> news = realm.where(News.class).findAll();
        realm.beginTransaction();
        news.deleteAllFromRealm();
        realm.commitTransaction();
        try {
            JSONObject baseJsonResponse = new JSONObject(filmJSON);
            JSONArray featureArray = baseJsonResponse.getJSONArray("content");

            for (int i = 0; i < featureArray.length(); i++) {
                JSONObject currentFilm = featureArray.getJSONObject(i);
                String title = currentFilm.getString("title");
                String text = currentFilm.getString("text");
                String date = currentFilm.getString("date");
                News newOne = new News();
                newOne.setTitle(title);
                newOne.setText(android.text.Html.fromHtml(text).toString());
                newOne.setDate(formatDate(date));
                realm.beginTransaction();
                realm.insert(newOne);
                realm.commitTransaction();
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        return "Completed";
    }

    private String formatDate(String date) {
        StringBuilder stringBuilder = new StringBuilder(date);
        stringBuilder.delete(10,19);
        String s = new String(stringBuilder);
        String[] subStr;
        String delimeter = "-";
        subStr = s.split(delimeter);
        return "Дата: " + subStr[2] + "." + subStr[1] + "." + subStr[0];
    }


}
