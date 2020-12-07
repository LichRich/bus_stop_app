package com.thinking.juicer.busstopapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GetOffNotificationActivity  extends AppCompatActivity {

    private Button getOff;
    long[] pattern = {100,300,100,500,700};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_getoff_notification);


        final Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        getOff = findViewById(R.id.btn_notificationOk);

        vibrator.vibrate(pattern,-0); //0: infinity loops, -1: no loop

        getOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                vibrator.cancel();
                finish();
            }
        });

    }
}
