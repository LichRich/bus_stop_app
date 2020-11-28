package com.thinking.juicer.busstopapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

public class CheckNotificationActivity extends AppCompatActivity {

    private Button yes, no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_notification);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        yes = findViewById(R.id.btn_yes);
        no = findViewById(R.id.btn_no);
    }


}