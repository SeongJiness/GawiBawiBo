package com.example.gawibawibo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.AlertDialog;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mMediaPlayer;
    boolean isMusicPlaying = false;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("WinningRate_file" , MODE_PRIVATE);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preferences.getString("playerName" , "").isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, PlayerName.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, RePlayerName.class);
                    startActivity(intent);
                }
            }
        });

        findViewById(R.id.rule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, Rule.class);
                startActivity(intent2);
            }
        });

        findViewById(R.id.end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("앱을 종료하시겠습니까?");
                builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();


        if (!isMusicPlaying) {
            mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.main);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.start();
            isMusicPlaying = true;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isMusicPlaying) {
            mMediaPlayer.pause();
        }

    }
}