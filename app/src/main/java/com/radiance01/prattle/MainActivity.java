package com.radiance01.prattle;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.TimerTask;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {

    TextView prattle;
    TextView under_text;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        prattle = findViewById(R.id.prattle);
        under_text = findViewById(R.id.under_text);
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                prattle.animate().alphaBy(1).setDuration(2000);
                under_text.animate().alphaBy(1).setDuration(2000);
            }

            @Override
            public void onFinish() {

                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                CustomIntent.customType(MainActivity.this,"bottom-to-up");
            }
        }.start();

    }
}
