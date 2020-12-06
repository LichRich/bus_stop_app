package com.thinking.juicer.busstopapplication;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GetOffNotificationActivity  extends AppCompatActivity {

    private Button getOff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_getoff_notification);

        getOff = findViewById(R.id.btn_notificationOk);

        getOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
