package com.example.gawibawibo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class Play extends AppCompatActivity {
    ImageButton gawi, bawi, bo;
    ImageView player_play, computer_play;
    TextView vs, playerName;

    int player = 0;
    int randomNum = 0;

    String name = "";

    static int playerWin = 0;
    static int playerLose = 0;
    static int draw = 0;
    static int match = 0;

    private static final int DELAY_MILLISECONDS = 1500;
    private static final int COMPUTER_INTERVAL = 100;


    SharedPreferences preferences;

    MediaPlayer mMediaPlayer;

    Handler computerHandler;
    Runnable computerRunnable;

    boolean isComputerUpdating = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlayout);

        preferences = getSharedPreferences("WinningRate_file", MODE_PRIVATE);
        playerWin = preferences.getInt("playerWin",0);
        playerLose = preferences.getInt("playerLose" , 0);
        draw = preferences.getInt("draw" , 0);
        match = preferences.getInt("match" , 0);

        saveDataToPreferences();


        playerName = findViewById(R.id.name);
        gawi = findViewById(R.id.gawi);
        bawi = findViewById(R.id.bawi);
        bo = findViewById(R.id.bo);
        player_play = findViewById(R.id.player_play);
        computer_play = findViewById(R.id.computer_play);
        vs = findViewById(R.id.vs);



        name = preferences.getString("playerName" , "");

        playerName.setText(name);

        computerHandler = new Handler();

        gawi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player_play.setVisibility(View.VISIBLE);
                player_play.setImageResource(R.drawable.gawi);
                player = 1;
                stopComputerImageUpdate();
                updateComputerPlay();
                determineWinner(player, randomNum);
                gawi.setEnabled(false);
                bawi.setEnabled(false);
                bo.setEnabled(false);
                delayAndSwitchLayout();
            }
        });

        bawi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player_play.setVisibility(View.VISIBLE);
                player_play.setImageResource(R.drawable.bawi);
                player = 2;
                stopComputerImageUpdate();
                updateComputerPlay();
                determineWinner(player, randomNum);
                gawi.setEnabled(false);
                bawi.setEnabled(false);
                bo.setEnabled(false);
                delayAndSwitchLayout();
            }
        });

        bo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player_play.setVisibility(View.VISIBLE);
                player_play.setImageResource(R.drawable.bo);
                player = 3;
                stopComputerImageUpdate();
                updateComputerPlay();
                determineWinner(player, randomNum);
                gawi.setEnabled(false);
                bawi.setEnabled(false);
                bo.setEnabled(false);
                delayAndSwitchLayout();
            }
        });

        startComputerImageUpdateLoop();
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

    private void saveDataToSharedPreferences() {
        preferences.edit().commit();
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
                        saveDataToSharedPreferences();
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

    private void startComputerImageUpdateLoop() {
        isComputerUpdating = true;
        computerRunnable = new Runnable() {
            @Override
            public void run() {
                if (isComputerUpdating) {
                    updateComputerPlay();
                }
                computerHandler.postDelayed(computerRunnable, COMPUTER_INTERVAL);
            }
        };

        computerHandler.post(computerRunnable);
    }

    private void updateComputerPlay() {
        randomNum = new Random().nextInt(3) + 1;
        computer_play.setVisibility(View.VISIBLE);
        int computerImageResourceId;
        switch (randomNum) {
            case 1:
                computerImageResourceId = R.drawable.gawi;
                break;
            case 2:
                computerImageResourceId = R.drawable.bawi;
                break;
            case 3:
                computerImageResourceId = R.drawable.bo;
                break;
            default:
                computerImageResourceId = R.drawable.gawi;
                break;
        }

        computer_play.setImageResource(computerImageResourceId);
    }





    private void stopComputerImageUpdate() {
        isComputerUpdating = false;
    }
    private void determineWinner(int player, int randomNum) {
        match++;
        if (player == randomNum) {
            vs.setText("Computer와 비겼습니다.");
            draw++;
        } else if (player == 1 && randomNum == 3) {
            vs.setText(name +"님이 이겼습니다.");
            playerWin++;
        } else if (player == 1 && randomNum == 2) {
            vs.setText("Computer가 이겼습니다.");
            playerLose++;
        } else if (player == 2 && randomNum == 1) {
            vs.setText(name +"님이 이겼습니다.");
            playerWin++;
        } else if (player == 2 && randomNum == 3) {
            vs.setText("Computer가 이겼습니다.");
            playerLose++;
        } else if (player == 3 && randomNum == 1) {
            vs.setText("Computer가 이겼습니다.");
            playerLose++;
        } else if (player == 3 && randomNum == 2) {
            vs.setText(name +"님이 이겼습니다.");
            playerWin++;
        } else {
            vs.setText("잘못된 경우입니다.");
        }
    }

    private void delayAndSwitchLayout() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Play.this, ReGame.class);
                startActivity(intent);
                resetGame();
            }
        }, DELAY_MILLISECONDS); // 지연 시간 설정
    }

    private void resetGame() {
        player_play.setVisibility(View.INVISIBLE);
        vs.setText("");

        gawi.setEnabled(true);
        bawi.setEnabled(true);
        bo.setEnabled(true);

    }


    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        loadAndSetPlayerName();
        saveDataToPreferences();
    }
    private void loadAndSetPlayerName() {
        name = preferences.getString("playerName", "");
        playerName.setText(name);
    }

    public void saveDataToPreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("playerWin", playerWin);
        editor.putInt("draw", draw);
        editor.putInt("playerLose", playerLose);
        editor.putInt("match", match);
        editor.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        computer_play.setVisibility(View.VISIBLE);
        stopComputerImageUpdate();
        updateComputerPlay();
        startComputerImageUpdateLoop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isComputerUpdating = false;
        computer_play.setVisibility(View.INVISIBLE);
        computerHandler.removeCallbacks(computerRunnable);
        saveDataToPreferences();
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