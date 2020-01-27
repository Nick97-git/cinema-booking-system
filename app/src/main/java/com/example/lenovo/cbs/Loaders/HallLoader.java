package com.example.lenovo.cbs.Loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.lenovo.cbs.Model.Column;
import com.example.lenovo.cbs.Model.HttpGetRequest;
import com.example.lenovo.cbs.Model.Seats;
import com.example.lenovo.cbs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class HallLoader extends AsyncTaskLoader<ArrayList<Seats>> {
    private String mID;
    private static final String LOG_TAG = "ERROR";

    public HallLoader(Context context, String id){
        super(context);
        mID = id;

    }

    @Override
    public ArrayList<Seats> loadInBackground() {
        String USGS_REQUEST_URL =
                "http://kino-teatr.ua:8081/services/api/showtime/"+mID+"/schema?apiKey=pol1kh111";
        URL url = HttpGetRequest.createUrl(USGS_REQUEST_URL);
        String jsonResponse = "";
        try {
            jsonResponse = HttpGetRequest.makeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
        }

        return extractSchema(jsonResponse);
    }

    @Override
    public void deliverResult(ArrayList<Seats> data) {
        super.deliverResult(data);
    }

    private ArrayList<Seats> extractSchema(String jsonResponse){
        ArrayList<Seats> seats = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray pricesArray = jsonObject.getJSONArray("prices");
            String[] prices = new String[pricesArray.length()];
            int[] colorsArray = getContext().getResources().getIntArray(R.array.pricesColors);
            int[] colors = new int[pricesArray.length()];
            for (int i = 0; i <pricesArray.length() ; i++) {
                JSONObject jsonObject1 = pricesArray.getJSONObject(i);
                prices[i] = jsonObject1.getString("price");
                colors[i] = colorsArray[i];
            }
            JSONArray sectorsArray = jsonObject.getJSONArray("sectors");
            JSONObject object = sectorsArray.getJSONObject(0);
            JSONArray rows = object.getJSONArray("rows");
            for (int i = 0; i <rows.length() ; i++) {
                JSONObject object1 = rows.getJSONObject(i);
                int rowId = object1.getInt("id");
                JSONArray places = object1.getJSONArray("places");
                ArrayList<Column> current = new ArrayList<>();
                for (int j = 0; j <places.length(); j++) {
                    JSONObject object2 = places.getJSONObject(j);
                    JSONArray ids = object2.getJSONArray("ids");
                    String id = ids.getString(0);
                    String seatNumber = object2.getString("number");
                    int status = object2.getInt("status");
                    int currentPrice = object2.getJSONArray("prices").getInt(0);
                    current.add(new Column(id,seatNumber,status,prices[currentPrice],colors[currentPrice]));
                }
                seats.add(new Seats(rowId,current));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem extracting hall", e);
        }
        return seats;
    }
}
