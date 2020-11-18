package com.thinking.juicer.busstopapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {




  TextView textView;
     EditText et_search;
     Button btn_search;
    RecyclerView recycler_routesAll;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        URL url;


        try {
            url = new URL("http://openapitraffic.daejeon.go.kr/api/rest/busRouteInfo/getRouteInfoAll?reqPage=1&serviceKey=cC0rVYquPDL%2Bu44mxQ0ds5EabhA44uysOYBPVwBa0%2FeoGxSfKQgQCP4eCys0OB6VU6LUc9Ty2e%2BaBw7w61QB4g%3D%3D");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("item");

            String s = "";

            for(int i = 0; i< nodeList.getLength(); i++) {

                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;

                NodeList ROUTE_NO = fstElmnt.getElementsByTagName("ROUTE_NO");
                s += "ROUTE_NO = " + ROUTE_NO.item(0).getChildNodes().item(0).getNodeValue() + "\n";


            }
            textView.setText(s);


        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
        }



    }


}