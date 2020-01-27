package com.example.lenovo.cbs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lenovo.cbs.Activities.BuyActivity;
import com.example.lenovo.cbs.Loaders.SessionsLoader;
import com.example.lenovo.cbs.Model.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class DialogSessions extends android.app.Dialog implements View.OnClickListener {

    private DatePickerDialog.OnDateSetListener mDateListener;
    private String mDate;
    private TextView textDate;
    private Context mContext;
    private  Spinner spinnerSession;
    private String pickedSession;
    private ArrayList<Session> mSessions;
    private ProgressBar progressBar;
    private String mTheaterID;
    private TextView chooseSession;
    private String mId;
    private LoaderManager mLoader;
    private CardView cardView;
    private static final int LOADER_SESSIONS = 2;

    public DialogSessions(@NonNull Context context, String theaterId, String id,LoaderManager loaderManager) {
        super(context);
        mContext = context;
        mTheaterID = theaterId;
        mId = id;
        mLoader = loaderManager;
    }

    public void createDialog(){
        setContentView(R.layout.dialog_buy_layout);
        Button next = findViewById(R.id.nextButton);
        progressBar = findViewById(R.id.progressSession);
        Button cancel = findViewById(R.id.cancelButton);
        textDate = findViewById(R.id.editDate);
        cardView = findViewById(R.id.cardView_spinnerSession);
        chooseSession = findViewById(R.id.chooseSession);
        spinnerSession = findViewById(R.id.spinnerSession);
        cardView.setVisibility(View.GONE);
        chooseSession.setVisibility(View.GONE);
        ImageButton calendar = findViewById(R.id.calendarButton);
        sessionsOfToday();
        calendar.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), mDateListener, year, month, day);
            mDateListener = (datePicker, year1, month1, day1) -> {
                month1 += 1;
                String date = day1 + "/" + month1 + "/" + year1;
                if (month1 > 9 && day1 >9) {
                    mDate = year1 + "-" + month1 + "-" + day1;
                } else if (month1 <=9 && day1 <=9) {
                    mDate = year1 + "-0" + month1 + "-0" + day1;
                } else if (month1 >9 && day1 <=9){
                    mDate = year1 + "-" + month1 + "-0" + day1;
                } else if (month1 <=9 && day1 >9){
                    mDate = year1 + "-0" + month1 + "-" + day1;
                }
                if (textDate.getText()!=null && mDate!=textDate.getText()) {
                   sessionsOfPickedDate(date);
                }
            };
            datePickerDialog.show();
        });
        cancel.setOnClickListener(this);
        next.setOnClickListener(this);
        show();
    }



    private void intentToBuyActivity(){
        Intent intent = new Intent(mContext, BuyActivity.class);
        intent.putExtra("id",getSessionID(pickedSession));
        mContext.startActivity(intent);
    }

    private void sessionsOfPickedDate(String date){
        textDate.setText(date);
        cardView.setVisibility(View.GONE);
        chooseSession.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mLoader.restartLoader(LOADER_SESSIONS, null, loaderSessions);
        progressBar.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);
        chooseSession.setVisibility(View.VISIBLE);
    }
    private void sessionsOfToday(){
        progressBar.setVisibility(View.VISIBLE);
        Date date = new Date();
        SimpleDateFormat dateFormatLoader = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        mDate = dateFormatLoader.format(date);
        textDate.setText(dateFormat.format(date));
        mLoader.initLoader(LOADER_SESSIONS,null,loaderSessions);
        progressBar.setVisibility(View.GONE);
       cardView.setVisibility(View.VISIBLE);
        chooseSession.setVisibility(View.VISIBLE);
    }

    private LoaderManager.LoaderCallbacks<ArrayList<Session>> loaderSessions = new LoaderManager.LoaderCallbacks<ArrayList<Session>>() {
        @Override
        public Loader<ArrayList<Session>> onCreateLoader(int id, Bundle args) {
            SessionsLoader sessionsLoader = new SessionsLoader(mContext, mTheaterID, mId, mDate);
            sessionsLoader.forceLoad();
            return sessionsLoader;
        }


        @Override
        public void onLoadFinished(Loader<ArrayList<Session>> loader, ArrayList<Session> sessions) {
            mSessions = sessions;
            if (mSessions.size()!=0) {
                ArrayAdapter<String> spinnerAdapterSessions = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, getSessions(mSessions));
                spinnerSession.setAdapter(spinnerAdapterSessions);
                spinnerSession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                        pickedSession = adapterView.getItemAtPosition(pos).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                cardView.setVisibility(View.VISIBLE);
                chooseSession.setVisibility(View.VISIBLE);
                findViewById(R.id.errorSessions).setVisibility(View.GONE);
            } else if (mSessions.size() == 0){
                cardView.setVisibility(View.GONE);
                chooseSession.setVisibility(View.GONE);
                findViewById(R.id.errorSessions).setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);

        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Session>> loader) {
        }
    };

    private String[] getSessions(ArrayList<Session> sessions){
        String[] mSessions = new String[sessions.size()];
        for (int i = 0; i <sessions.size() ; i++) {
            mSessions[i] = sessions.get(i).toString();
        }
        Arrays.sort(mSessions);
        return mSessions;
    }


    private String getSessionID(String session) {
        String id = "";
        for (int i = 0; i < mSessions.size(); i++) {
            Session s = mSessions.get(i);
            String time = s.toString();
            if (time.equals(session)) {
                id = s.getId();
                break;
            }
        }
        return id;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancelButton:
                dismiss();
                break;
            case R.id.nextButton:
                intentToBuyActivity();
                break;
        }
    }
}
