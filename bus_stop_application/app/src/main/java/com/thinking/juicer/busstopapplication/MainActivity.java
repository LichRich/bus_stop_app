package com.thinking.juicer.busstopapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    /**
     *
     * 임시로 실행하기 위한 코드
     *
     * */
    EditText et_search;
    Button btn_search;
    RecyclerView recycler_routeAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_search = (EditText) findViewById(R.id.et_search);
        btn_search = (Button) findViewById(R.id.btn_search);
        recycler_routeAll = (RecyclerView) findViewById(R.id.recycler_routesAll);

        btn_search.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SelectedRouteInfo.class);
            startActivity(intent);
        });

    }
}