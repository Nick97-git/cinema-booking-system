package com.example.lenovo.cbs.Loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.lenovo.cbs.Model.HttpGetRequest;
import com.example.lenovo.cbs.Model.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by LenoVo on 14.04.2018.
 */

public class SessionsLoader extends AsyncTaskLoader<ArrayList<Session>> {

    private static final String LOG_TAG = "ERROR";
    private String mID;
    private String mTheaterID;
    private String mDate;

    public SessionsLoader(Context context, String theaterID, String id, String date) {
        super(context);
        mID = id;
        mTheaterID = theaterID;
        mDate = date;

    }

    @Override
    public ArrayList<Session> loadInBackground() {
        String USGS_REQUEST_URL =
                "http://kino-teatr.ua:8081/services/api/cinema/" + mTheaterID + "/film/" + mID + "/shows?apiKey=pol1kh111&size=2000&date=" + mDate + "&detalization=FULL";
        Log.d("Session",USGS_REQUEST_URL);
        URL url = HttpGetRequest.createUrl(USGS_REQUEST_URL);

        String jsonResponse = "";
        try {
            jsonResponse = HttpGetRequest.makeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
        }

        return extractSessions(jsonResponse);
    }

    @Override
    public void deliverResult(ArrayList<Session> data) {
        super.deliverResult(data);
    }

    private ArrayList<Session> extractSessions(String jsonResponse) {
        ArrayList<Session> sessions = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray featureArray = jsonObject.getJSONArray("content");
            for (int i = 0; i <featureArray.length() ; i++) {
                JSONObject jsonObject1 = featureArray.getJSONObject(i);
                JSONArray times = jsonObject1.getJSONArray("times");
                for (int j = 0; j <times.length(); j++) {
                    JSONObject currentSession =times.getJSONObject(j);
                    String id = currentSession.getString("id");
                    String session = currentSession.getString("time");
                    boolean dimension = currentSession.getBoolean("3d");
                    if (dimension){
                        sessions.add(new Session(id,session.substring(0,session.length()-3),"3D"));
                    } else {
                        sessions.add(new Session(id,session.substring(0,session.length()-3),"2D"));
                    }

                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem extracting sessions", e);
        }
        return sessions;

    }


}
