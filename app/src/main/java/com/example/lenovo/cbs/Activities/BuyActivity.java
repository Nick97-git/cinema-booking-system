package com.example.lenovo.cbs.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.cbs.Loaders.HallLoader;
import com.example.lenovo.cbs.Model.Column;
import com.example.lenovo.cbs.Model.Seats;
import com.example.lenovo.cbs.R;

import java.util.ArrayList;

public class BuyActivity extends AppCompatActivity {

    private static final int LOADER_HALL = 4;
    private String mID;
    private HorizontalScrollView hsw;
    private LinearLayout linearLayout;
    private Button payButton;
    private TextView sumText;
    private TextView sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        Intent intent = getIntent();
        mID = intent.getStringExtra("id");
        getSupportLoaderManager().initLoader(LOADER_HALL,null,loaderHalls);
        hsw = findViewById(R.id.scroll);
        linearLayout = findViewById(R.id.pickedTickets);
        payButton = findViewById(R.id.payButton);
        sumText = findViewById(R.id.sum);
        sum = findViewById(R.id.current_sum);


    }

    private LoaderManager.LoaderCallbacks<ArrayList<Seats>> loaderHalls = new LoaderManager.LoaderCallbacks<ArrayList<Seats>>() {
        @Override
        public Loader<ArrayList<Seats>> onCreateLoader(int id, Bundle args) {
            HallLoader hallLoader = new HallLoader(BuyActivity.this,mID);
            hallLoader.forceLoad();
            return hallLoader;
        }


        @Override
        public void onLoadFinished(Loader<ArrayList<Seats>> loader, ArrayList<Seats> seats) {
            GridLayout layout = new GridLayout(BuyActivity.this);
            layout.setOrientation(GridLayout.VERTICAL);
            layout.setRowCount(seats.size());
            for (int i = 0; i <seats.size() ; i++) {
                Seats s = seats.get(i);
                GridLayout gridLayout = new GridLayout(BuyActivity.this);
                gridLayout.setOrientation(GridLayout.HORIZONTAL);

                for (int j = 0; j <s.getSeats().size() ; j++) {
                    final Column column = s.getSeats().get(j);
                    gridLayout.setColumnCount(s.getSeats().size());
                    final Button button = new Button(BuyActivity.this);
                    final TextView textView = new TextView(BuyActivity.this);
                    button.setText(column.getSeat());
                    final String id = i+""+j;
                    button.setId(Integer.parseInt(id));
                    button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    textView.setId(Integer.parseInt(id));
                    if (column.getStatus()>0){
                        button.setBackgroundResource(R.drawable.red_seat);
                    } else {
                        button.setBackgroundResource(R.drawable.green_seat);
                    }
                    final int finalI = i;
                    final int finalJ = j;
                    button.setOnLongClickListener(view -> {
                        button.setBackgroundResource(R.drawable.green_seat);
                        linearLayout.removeView(textView);
                        return true;
                    });
                    button.setOnClickListener(view -> {

                        Drawable drawable = getResources().getDrawable(R.drawable.red_seat);
                        double together = 0;
                        if (!(button.getBackground() == drawable)){
                            if (column.getStatus()<2){
                                button.setBackgroundResource(R.drawable.blue_seat);
                                String pickedSeat = getResources().getString(R.string.row) + String.valueOf(finalI + 1) + getResources().getString(R.string.seat) + String.valueOf(finalJ + 1) + getResources().getString(R.string.price) + column.getPrice();
                                textView.setText(pickedSeat);
                                textView.setTextSize(18);
                                textView.setTextColor(getResources().getColor(R.color.colorBlack));
                                textView.setTypeface(Typeface.SERIF);
                                linearLayout.removeView(textView);
                                linearLayout.addView(textView);
                                linearLayout.setVisibility(View.VISIBLE);
                                double seatSum = Double.valueOf(column.getPrice());
                                together += seatSum;
                                sum.setText(String.valueOf(together));
                                sum.setVisibility(View.VISIBLE);
                                sumText.setVisibility(View.VISIBLE);
                                payButton.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(BuyActivity.this,R.string.seat_not_available,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    gridLayout.addView(button);
                }
                layout.addView(gridLayout);
            }
            hsw.addView(layout);


        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Seats>> loader) {
        }
    };

}
