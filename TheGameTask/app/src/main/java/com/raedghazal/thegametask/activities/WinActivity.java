package com.raedghazal.thegametask.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.raedghazal.thegametask.R;

public class WinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        TextView tv = findViewById(R.id.tv_winner);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int winnerNumber = bundle.getInt("winner", 0);
            tv.setText(String.format(getResources().getString(R.string.winningMessage), winnerNumber));
        }
    }

    @Override
    public void onBackPressed() {
        clickBackHome(null);
    }

    public void clickBackHome(View view) {
        finish();
    }

    public void clickNewGame(View view) {
        startActivity(new Intent(this, GameActivity.class));
        finish();
    }
}