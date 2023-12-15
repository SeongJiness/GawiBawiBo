package com.example.gawibawibo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class WinngRate extends AppCompatActivity {
    TextView result, winning_rate, rate;
    SharedPreferences preferences;

    MediaPlayer mMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winng_rate);
        result = findViewById(R.id.result);
        rate = findViewById(R.id.rate);
        winning_rate = findViewById(R.id.winning_rate);

        preferences = getSharedPreferences("WinningRate_file", MODE_PRIVATE);

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WinngRate.this, ReGame.class);
                startActivity(intent);
            }
        });

        loadDataFromPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        updateAudioIcon(menu.findItem(R.id.audio));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int curId = item.getItemId();
        if (curId == R.id.audio) {
            MediaPlayer mMediaPlayer = ((MyApplication) getApplication()).getMediaPlayer();
            if (mMediaPlayer.isPlaying()) {
                item.setIcon(R.drawable.audiooff);
                mMediaPlayer.pause();
            } else {
                item.setIcon(R.drawable.audio);
                mMediaPlayer.start();
            }
        }

        if (curId == R.id.shutdown) {
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
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("앱을 완전히 종료하시겠습니까?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finishAffinity();
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
        loadDataFromPreferences();
    }


    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        loadDataFromPreferences();
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("뒤로 가기를 사용할 수 없습니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void loadDataFromPreferences() {
        if (preferences != null) {
            String name = preferences.getString("playerName", "");
            int playerWin = preferences.getInt("playerWin", 0);
            int playerLose = preferences.getInt("playerLose", 0);
            int draw = preferences.getInt("draw", 0);
            int match = preferences.getInt("match", 0);
            rate.setText(name + "님의 누적전적");
            result.setText(match + "전 " + playerWin + "승 " + draw + "무 " + playerLose + "패입니다.");
            double winRate = ((double) playerWin / match) * 100;
            String formattedWinRate = String.format("컴퓨터와의 승률은 %.2f%% 입니다.", winRate);
            winning_rate.setText(formattedWinRate);
        }
    }
}