package com.example.gawibawibo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.app.AlertDialog;
public class ReGame extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.regame);





        findViewById(R.id.regame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReGame.this, Play.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();


            }
        });


        findViewById(R.id.winning_rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReGame.this, WinngRate.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitDialog();

            }
        });
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

}