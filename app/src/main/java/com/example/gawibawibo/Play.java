package com.example.gawibawibo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

    MediaPlayer mMediaPlayer;
    boolean isMusicPlaying = false;

    SharedPreferences preferences;

    Handler computerHandler;
    Runnable computerRunnable;

    boolean isComputerUpdating = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlayout);

        preferences = getSharedPreferences("WinningRate_file", MODE_PRIVATE);
        saveDataToPreferences();

        if (!isMusicPlaying) {
            mMediaPlayer = MediaPlayer.create(Play.this, R.raw.play);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.start();
            isMusicPlaying = true;
        }

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
    public void onBackPressed() {
        if (isMusicPlaying) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAndSetPlayerName();
        saveDataToPreferences();
    }
    private void loadAndSetPlayerName() {
        name = preferences.getString("playerName", "");
        playerName.setText(name);
    }

    public void saveDataToPreferences() {
        Log.d("SaveData", "Saving data to preferences");
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("playerWin", playerWin);
        editor.putInt("draw", draw);
        editor.putInt("playerLose", playerLose);
        editor.putInt("match", match);
        editor.apply();
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
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}