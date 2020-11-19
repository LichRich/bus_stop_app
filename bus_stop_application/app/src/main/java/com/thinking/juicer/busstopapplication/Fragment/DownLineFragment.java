package com.thinking.juicer.busstopapplication.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thinking.juicer.busstopapplication.R;
import com.thinking.juicer.busstopapplication.items.SelectedRouteItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DownLineFragment extends Fragment {

    /*
     *
     * TAG
     *
     * */
    private final String TAG = "Down Line Fragment";
    /*
     *
     * Layout Components
     *
     * */
    private View downLineLayout;
    private RecyclerView rv_down;
    /*
     *
     * Setting API url
     *
     * */
    private final String url_main = "http://openapitraffic.daejeon.go.kr/api/rest";
    //    url_operations[0] = 노선 위치(버스 위치 나열)
    //    url_operations[1] = 노선 정보(정류장 목록 나열)
    private final int num_posInfo = 0;
    private final int num_routeInfo = 1;
    private final String url_key = "?serviceKey=cC0rVYquPDL%2Bu44mxQ0ds5EabhA44uysOYBPVwBa0%2FeoGxSfKQgQCP4eCys0OB6VU6LUc9Ty2e%2BaBw7w61QB4g%3D%3D&busRouteId=";
    /*
     *
     * Get ROUTE_NO from intent.
     *
     * */
    private String route_no;
    private String busRouteId;
    private DownLineAdapter downLineAdapter;

    public DownLineFragment() {}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        downLineLayout =  inflater.inflate(R.layout.fragment_down_line, container, false);

        StrictMode.enableDefaults();

        /**
         *
         * These values are just temp values.
         * These must get ROUTE_NO from intent.
         *
         * */
        route_no = "1";
        busRouteId = "30300001";


        rv_down = (RecyclerView) downLineLayout.findViewById(R.id.recycler_downLine);
        rv_down.setLayoutManager(new LinearLayoutManager(getContext()));

        return downLineLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        ArrayList<SelectedRouteItem> itemList = getInfoFromAPI(url_main, num_posInfo, num_routeInfo, url_key, busRouteId);
        downLineAdapter = new DownLineAdapter(itemList);
        rv_down.setAdapter(downLineAdapter);
    }

    /*
     *
     * function: get information from API.
     *
     *  */
    private ArrayList<SelectedRouteItem> getInfoFromAPI(String mUrl, int op1, int op2, String k, String id) {
        ArrayList<Boolean> bus_isHere = new ArrayList<>();
        ArrayList<String> station_id = new ArrayList<>();
        ArrayList<String> station_name = new ArrayList<>();

        String[] url_operations = {"/busRouteInfo/getRouteInfo", "/busposinfo/getBusPosByRtid"};
        String url_busPos = mUrl + url_operations[op1] + k + id;
        String url_busRoute = mUrl + url_operations[op2] + k + id;

        /*
         * builders to get information from API
         * */
        DocumentBuilderFactory pos_factory = DocumentBuilderFactory.newInstance();
        DocumentBuilderFactory rt_factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder pos_builder = null;
        DocumentBuilder rt_builder = null;


        /*
         *
         * get Bus Position Information
         *
         * */
        try {
            pos_builder = pos_factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document pos_doc = null;
        try {
            pos_doc = pos_builder.parse(url_busPos);
        } catch(IOException | SAXException e) {
            e.printStackTrace();
        }
//        parsing tag = itemList
        pos_doc.getDocumentElement().normalize();
        NodeList itemList1 = pos_doc.getElementsByTagName("itemList");

//        get station id from XML to station_id
        for(int i = 0 ; i < itemList1.getLength() ; i++) {
            Node item = itemList1.item(i);
            if(item.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) item;
//                하행선 버스의 위치만 저장
                if(getTagValue("DIR", element).equals("1")) {
                    station_id.add(getTagValue("BUS_STOP_ID", element));
                }
            }
        }

        /*
         *
         * get Bus Route Information
         *
         * */
        try {
            rt_builder = rt_factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document rt_doc = null;
        try {
            rt_doc = rt_builder.parse(url_busRoute);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
        rt_doc.getDocumentElement().normalize();
        NodeList itemList2 = rt_doc.getElementsByTagName("itemList");

//        get station name from XML to station_name and check bus_isHere
        int j = 0;
        Element element = null;
        boolean downLine = false;
        do {
            Node item = itemList2.item(j);
            if(item.getNodeType() == Node.ELEMENT_NODE) {
                element = (Element) item;
//                하행선만 계산
                if(getTagValue("BUSSTOP_TP", element).equals("2")) downLine = true;
                if(downLine) {
                    station_name.add(getTagValue("BUSSTOP_NM", element));
                    bus_isHere.add(station_id.contains(getTagValue("BUS_STOP_ID", element)));
                }
            }
            j++;
        } while(!getTagValue("BUSSTOP_TP", element).equals("3"));

        ArrayList<SelectedRouteItem> res = new ArrayList<>();

        for(int index = 0 ; index < station_name.size() ; index++) {
            SelectedRouteItem item = new SelectedRouteItem(bus_isHere.get(index), station_name.get(index));
            res.add(item);
        }

        return res;
    }

    private String getTagValue(String tag, Element e) {
        NodeList list = e.getElementsByTagName(tag).item(0).getChildNodes();
        Node v = (Node) list.item(0);
        if(v == null) return null;
        return v.getNodeValue();
    }

}

class DownLineAdapter extends RecyclerView.Adapter<DownLineAdapter.ViewHolder> {

    private ArrayList<SelectedRouteItem> busStops = null;

    @NonNull
    @Override
    public DownLineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_selected_route, parent, false);
        DownLineAdapter.ViewHolder viewHolder = new DownLineAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DownLineAdapter.ViewHolder holder, int position) {
        String station_name = busStops.get(position).getBusStopName();
        holder.tv_busStop.setText(station_name);

        if(busStops.get(position).isBusIsHere()) {
            holder.iv_busIcon.setImageResource(R.drawable.bus);
        } else if (!busStops.get(position).isBusIsHere()) {
            holder.iv_busIcon.setImageResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return busStops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_busIcon;
        TextView tv_busStop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_busIcon = itemView.findViewById(R.id.iv_busIcon);
            tv_busStop = itemView.findViewById(R.id.tv_busStop);
        }
    }

    public DownLineAdapter(ArrayList<SelectedRouteItem> list) {
        busStops = list;
    }

}
