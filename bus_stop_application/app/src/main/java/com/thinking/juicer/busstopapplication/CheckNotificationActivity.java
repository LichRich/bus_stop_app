package com.thinking.juicer.busstopapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class CheckNotificationActivity extends AppCompatActivity {

    private Button yes, no;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_notification);
        this.setFinishOnTouchOutside(false);

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        yes = findViewById(R.id.btn_yes);
        no = findViewById(R.id.btn_no);

        vibrator.vibrate(1000);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(getApplicationContext(), GetOffVoiceActivity.class);
                startActivity(myintent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SelectedRouteInfo.routeInfoFinish();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        vibrator.cancel();
        finish();
    }

}