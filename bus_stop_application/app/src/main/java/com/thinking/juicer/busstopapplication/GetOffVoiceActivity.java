package com.thinking.juicer.busstopapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GetOffVoiceActivity  extends AppCompatActivity {

    Button yes, no;
    TextView text;
    MediaPlayer m;
    boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_getoff_voice);

        yes = findViewById(R.id.btn_please);
        no = findViewById(R.id.btn_nothanks);
        text = findViewById(R.id.requestdown);
        m = MediaPlayer.create(GetOffVoiceActivity.this, R.raw.getoffvoice);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setText("재생 중 입니다...");
                yes.setText("다시 재생");
                no.setText("취소");
                if(m.isPlaying()){
                    m.seekTo(0);
                }
                m.start();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.stop();
                m.release();

                finish();
            }
        });


        m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                text.setText("재생 완료되었습니다");
                m.seekTo(0);
            }
        });


    }
    @Override
    public void onBackPressed() {
        m.stop();
        m.release();

        finish();
    }

}
