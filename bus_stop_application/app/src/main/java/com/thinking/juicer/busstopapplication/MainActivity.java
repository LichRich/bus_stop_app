package com.thinking.juicer.busstopapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.thinking.juicer.busstopapplication.R;
import com.thinking.juicer.busstopapplication.items.BusRouteItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

   ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView)findViewById(R.id.lv);

        ArrayList<BusRouteItem> list = xmlParser();
        String[] data = new String[list.size()];
        for(int i=0; i<list.size(); i++){
            data[i] = list.get(i).getBusNum();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,data);
        lv.setAdapter(adapter);

    }

    private ArrayList<BusRouteItem> xmlParser(){
        ArrayList<BusRouteItem> arrayList = new ArrayList<BusRouteItem>();
        InputStream is = getResources().openRawResource(R.raw.bus_route_info);

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));
            int eventType = parser.getEventType();
            BusRouteItem busRouteItem = null;

            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                    String startTag = parser.getName();
                    if(startTag.equals("itemList")){
                        busRouteItem = new BusRouteItem();
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

}