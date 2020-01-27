package com.example.lenovo.cbs.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.lenovo.cbs.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;


public class PlayerActivity extends AppCompatActivity implements View.OnTouchListener {

    @BindView(R.id.trailer)
    VideoView videoView;
    @BindView(R.id.player_state)
    ImageView playerState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String mUrl = intent.getStringExtra("url");
        try {
            Uri uri = Uri.parse(mUrl);
            videoView.setVideoURI(uri);
            videoView.requestFocus();
            videoView.start();
        } catch (Exception ex){
           Log.d("VideoPlayer","ERROR");
        }



}

    @Override
    @OnTouch(R.id.trailer)
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Runnable hide = () -> playerState.setVisibility(View.GONE);
        if (motionEvent.getAction()== MotionEvent.ACTION_DOWN){
            if (videoView.isPlaying()){
                videoView.pause();
                playerState.setVisibility(View.VISIBLE);
                playerState.setImageResource(R.drawable.ic_pause);
            } else {
                playerState.setImageResource(R.drawable.ic_play_button_1);
                playerState.postDelayed(hide,1000);
                videoView.start();
            }
        }
        return true;
    }
}
