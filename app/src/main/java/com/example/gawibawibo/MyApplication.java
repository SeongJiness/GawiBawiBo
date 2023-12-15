package com.example.gawibawibo;

import android.app.Application;
import android.media.MediaPlayer;

public class MyApplication extends Application {
    private MediaPlayer mMediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = MediaPlayer.create(this, R.raw.play);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mMediaPlayer.start();
            }
        });
        mMediaPlayer.start();
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }
}