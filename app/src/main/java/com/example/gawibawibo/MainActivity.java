package com.example.gawibawibo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.AlertDialog;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mMediaPlayer;

    SharedPreferences preferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        updateAudioIcon(menu.findItem(R.id.audio));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int curId = item.getItemId();
        if(curId == R.id.audio) {
            if (mMediaPlayer.isPlaying()) {
                item.setIcon(R.drawable.audiooff);
                mMediaPlayer.pause();
            } else {
                item.setIcon(R.drawable.audio);
                mMediaPlayer.start();
            }
        }

        if(curId == R.id.shutdown) {
           showExitDialog();
        }

        if(curId == R.id.reset) {
            showResetConfirmationDialog();
        }

        return super.onOptionsItemSelected(item);
    }


    private void updateAudioIcon(MenuItem audioMenuItem) {
        if (audioMenuItem != null) {
            MediaPlayer mMediaPlayer = ((MyApplication) getApplication()).getMediaPlayer();
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                audioMenuItem.setIcon(R.drawable.audio);
            } else {
                audioMenuItem.setIcon(R.drawable.audiooff);
            }
        }
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("앱을 완전히 종료하시겠습니까?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                }
        );

        builder.setNegativeButton(
                "취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }
        );

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showResetConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("전적을 초기화하시겠습니까?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetStatistics();
                        dialog.dismiss();
                    }
                }
        );

        builder.setNegativeButton(
                "취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }
        );

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void resetStatistics() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("playerWin", 0);
        editor.putInt("draw", 0);
        editor.putInt("playerLose", 0);
        editor.putInt("match", 0);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("WinningRate_file" , MODE_PRIVATE);
        MyApplication myApplication = (MyApplication) getApplication();
        mMediaPlayer = myApplication.getMediaPlayer();
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, PlayerName.class);
                    startActivity(intent);
            }
        });

        findViewById(R.id.rule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, Rule.class);
                startActivity(intent2);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        super.onDestroy();
    }
}