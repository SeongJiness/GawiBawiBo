package com.example.gawibawibo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WinngRate extends AppCompatActivity {
    TextView result, winning_rate, rate;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winng_rate);
        result = findViewById(R.id.result);
        rate = findViewById(R.id.rate);
        winning_rate = findViewById(R.id.winning_rate);

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
    protected void onResume() {
        super.onResume();
        loadDataFromPreferences();
    }

    private void loadDataFromPreferences() {
        preferences = getSharedPreferences("WinningRate_file", MODE_PRIVATE);
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