package com.example.lenovo.cbs.Loaders;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.lenovo.cbs.Model.Actor;
import com.example.lenovo.cbs.Model.HttpGetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class ActorsLoader extends AsyncTaskLoader<ArrayList<Actor>> {
    private static final String LOG_TAG = "ERROR";
    private ArrayList<Actor> mData;
    private String mID;

    public ActorsLoader(Context context, String id, ArrayList<Actor> actors) {
        super(context);
        mID = id;
        mData = actors;
    }

    @Override
    public ArrayList<Actor> loadInBackground() {
        String USGS_REQUEST_URL =
                "http://kino-teatr.ua:8081/services/api/film/"+mID+"/persons?apiKey=pol1kh111&size=100&detalization=FULL";
        URL url = HttpGetRequest.createUrl(USGS_REQUEST_URL);

        String jsonResponse = "";
        try {
            jsonResponse = HttpGetRequest.makeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
        }
        mData = extractFeatureFromJson(jsonResponse);
        return mData;
    }

    private ArrayList<Actor> extractFeatureFromJson(String actorsJSON) {
        ArrayList<Actor> data = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(actorsJSON);
            JSONArray featureArray = baseJsonResponse.getJSONArray("content");
            JSONArray personsArray  = baseJsonResponse.getJSONArray("persons");
            for (int i = 0; i <featureArray.length() ; i++) {
                JSONObject currentFilm = featureArray.getJSONObject(i);
                String personID = currentFilm.getString("person_id");
                String role = currentFilm.getString("role");
                String professionID = currentFilm.getString("profession_id");
                if (role.equals("null")){
                    role = "Неизвестно";
                }
                if (professionID.equals("2")) {
                    for (int j = 0; j < personsArray.length(); j++) {
                        JSONObject currentActor = personsArray.getJSONObject(j);
                        String id = currentActor.getString("id");
                        if (personID.equals(id)) {
                            String lastName = currentActor.getString("last_name");
                            String firstName = currentActor.getString("first_name");
                            Bitmap image = getBitmap(id);
                            String fullName = firstName + " " + lastName;
                            data.add(new Actor(id, fullName, image, role));

                        }
                    }
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return data;
    }

    @Override
    public void deliverResult(ArrayList<Actor> data) {
        super.deliverResult(data);
    }

    private Bitmap getBitmap(String id) {
        String posterByID = "http://kino-teatr.ua:8081/services/api/person/"+id+"/photo?apiKey=pol1kh111&width=300&height=400&ratio=1";
        URL url = HttpGetRequest.createUrl(posterByID);
        return extractImage(url);

    }

    private Bitmap extractImage(URL url) {
        Bitmap mIcon = null;
        try {
            mIcon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (FileNotFoundException fex){
            mIcon = null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem extracting image", e);
        }
        return mIcon;

    }
}
