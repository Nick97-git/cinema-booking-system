package com.example.lenovo.cbs.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.lenovo.cbs.Model.News;
import com.example.lenovo.cbs.R;

import butterknife.ButterKnife;
import butterknife.OnTouch;
import butterknife.Optional;
import butterknife.Unbinder;
import io.realm.Realm;

/**
 * Created by LenoVo on 07.05.2018.
 */

public class NewsPageFragment extends Fragment implements View.OnTouchListener {

    private News mNew;
    private TextView text;
    private FrameLayout frame;
    private TextView titleFull;
    private String mTitle;
    private Unbinder unbinder;

    public static NewsPageFragment newInstance(int position,String title) {
        Bundle args = new Bundle();
        args.putInt("position",position);
        args.putString("title",title);
        NewsPageFragment fragment = new NewsPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        Realm realm = Realm.getDefaultInstance();
        mNew = realm.where(News.class).findAll().get(args.getInt("position"));
        mTitle = args.getString("title");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,container,false);
        TextView title = view.findViewById(R.id.news_title);
        TextView filmTitle = view.findViewById(R.id.film_title_news);
        TextView date = view.findViewById(R.id.date_title_news);
        frame = view.findViewById(R.id.news_frame);
        text = view.findViewById(R.id.news_text);
        titleFull = view.findViewById(R.id.news_title_full);
        title.setText(mNew.getTitle());
        date.setText(mNew.getDate());
        filmTitle.setText(mTitle);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Optional
    @OnTouch({R.id.news_text,R.id.news_title})
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.news_title:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    frame.setVisibility(View.VISIBLE);
                    titleFull.setText(mNew.getTitle());
                    text.setText(mNew.getText());
                }
                break;
            case R.id.news_text:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    frame.setVisibility(View.GONE);
                }
                break;
        }
        return true;
    }
}
