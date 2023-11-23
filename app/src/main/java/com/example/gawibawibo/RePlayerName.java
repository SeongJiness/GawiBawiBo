package com.example.gawibawibo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

public class RePlayerName extends AppCompatActivity {
    EditText name;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_player_name);


                name = findViewById(R.id.input_name);
                preferences = getSharedPreferences("WinningRate_file" , MODE_PRIVATE);

                String playerName = preferences.getString("playerName", "");
                name.setText(playerName);
                name.setGravity(Gravity.CENTER);
                findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("playerName", name.getText().toString());
                        editor.commit();
                        Intent intent = new Intent(RePlayerName.this, Play.class);
                        startActivity(intent);
                    }
                });


            }
        }