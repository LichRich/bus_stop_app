package com.thinking.juicer.busstopapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.thinking.juicer.busstopapplication.items.BusRouteItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TextWatcher {

    ListView lv;
    ArrayList<String> routeIdList;
    EditText et_sch;
    ArrayAdapter<String> adapter;
    ArrayList<BusRouteItem> list;
    ArrayList<BusRouteItem> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView)findViewById(R.id.lv);
        routeIdList = new ArrayList<>();
        et_sch = (EditText) findViewById(R.id.et_search);

        list = xmlParser();
        String[] data = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i).getBusNum();
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, data);
        lv.setAdapter(adapter);
        lv.setTextFilterEnabled(true);
        et_sch.addTextChangedListener(this);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), SelectedRouteInfo.class);
                intent.putExtra("busRouteId", routeIdList.get(i));
                startActivity(intent);
            }
        });


    }

    private ArrayList<BusRouteItem> xmlParser() {
        arrayList = new ArrayList<BusRouteItem>();
        InputStream is = getResources().openRawResource(R.raw.bus_route_info);

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));
            int eventType = parser.getEventType();
            BusRouteItem busRouteItem = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                    String startTag = parser.getName();
                    if(startTag.equals("itemList")){
                        busRouteItem = new BusRouteItem();
                    }
                    if(startTag.equals("ROUTE_CD")){
                        routeIdList.add(parser.nextText());
                    }
                    if(startTag.equals("ROUTE_NO")){
                        busRouteItem.setBusNum(parser.nextText());
                    }

                    break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if(endTag.equals("itemList")){
                            arrayList.add(busRouteItem);
                        }
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String filterText = et_sch.getText().toString();



    if(filterText.length()>=0){
        //lv.setFilterText(filterText);
        ((ArrayAdapter<String>)lv.getAdapter()).getFilter().filter(filterText);
    }
        else{
        lv.clearTextFilter();

        }



    }


}