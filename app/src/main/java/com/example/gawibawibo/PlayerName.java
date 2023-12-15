package com.example.gawibawibo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PlayerName extends AppCompatActivity {
    EditText name;
    TextView text;
    SharedPreferences preferences;
    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_name);

        text = findViewById(R.id.text);
        name = findViewById(R.id.input_name);
        preferences = getSharedPreferences("WinningRate_file", MODE_PRIVATE);

        int match = preferences.getInt("match", 0);
        String matchString = Integer.toString(match);
        Log.d("match", matchString);



        if (preferences.getString("playerName", "").isEmpty()) {
            text.setText("당신의 이름은 무엇입니까?");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("playerName", name.getText().toString());
            editor.commit();
        } else {
            text.setText("당신의 이름이 맞습니까?");
            String playerName = preferences.getString("playerName", "");
            name.setText(playerName);
            name.setGravity(Gravity.CENTER);
        }
        findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("playerName", name.getText().toString());
                editor.commit();
                Intent intent = new Intent(PlayerName.this, Play.class);
                startActivity(intent);
            }
        });

        mMediaPlayer = ((MyApplication) getApplication()).getMediaPlayer();
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