package com.thinking.juicer.busstopapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thinking.juicer.busstopapplication.MainActivity;
import com.thinking.juicer.busstopapplication.R;
import com.thinking.juicer.busstopapplication.SelectedRouteInfo;
import com.thinking.juicer.busstopapplication.items.SelectedRouteItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class UpLineFragment extends Fragment {

    /*
     *
     * TAG
     *
     * */
    private final String TAG = "Up Line Fragment";
    /*
     *
     * Layout Components
     *
     * */
    private RecyclerView rv_up;
        /*
        *
        * Handler for using Network
        *
        * */
        private Handler mHandler;
        private static final int THREAD_ID = 10000;
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
    private final String url_key = "?serviceKey=HntIzh0TwfhWUCDfBN6E5chJKpNN3LQwIORIX85PkejkQPwTx%2BpUpsKnRBMzK2XHnrpdMNJJnxQFF6HcSu53DQ%3D%3D&busRouteId=";
    /*
     *
     * Get ROUTE_NO from intent.
     *
     * */
    private static String busRouteId;
    public static String getBusRouteId() {return busRouteId;}

    private TimerTask task;
    private Timer timer;

    public UpLineFragment() {}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View upLineLayout =  inflater.inflate(R.layout.fragment_up_line, container, false);
        TrafficStats.setThreadStatsTag(THREAD_ID);

        Intent intent = SelectedRouteInfo.getSRIntent();
        busRouteId = intent.getStringExtra("busRouteId");

        rv_up = (RecyclerView) upLineLayout.findViewById(R.id.recycler_upLine);
        rv_up.setLayoutManager(new LinearLayoutManager(getContext()));

        return upLineLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                ArrayList<SelectedRouteItem> itemList = (ArrayList<SelectedRouteItem>) msg.obj;
                UpLineAdapter upLineAdapter = new UpLineAdapter(itemList);
                rv_up.setAdapter(upLineAdapter);



            }
        };


        class UpThread extends Thread {

            final Handler handler = mHandler;

            @Override
            public void run() {

                Message message = handler.obtainMessage();
                message.obj = getInfoFromAPI(url_main, num_posInfo, num_routeInfo, url_key, busRouteId);

                handler.sendMessage(message);
                task = new TimerTask() {
                    @Override
                    public void run() {
                        Message message = handler.obtainMessage();
                        message.obj = getInfoFromAPI(url_main, num_posInfo, num_routeInfo, url_key, busRouteId);

                        handler.sendMessage(message);
                    }
                };

                timer = new Timer();
                timer.schedule(task,100,30000);


            }
        }

        UpThread ut = new UpThread();
        Thread utr = new Thread(ut);
        utr.start();

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

        String[] url_operations = {"/busposinfo/getBusPosByRtid", "/busRouteInfo/getStaionByRoute"};
        String url_busPos = mUrl + url_operations[op1] + k + id;
        String url_busRoute = mUrl + url_operations[op2] + "?serviceKey=N9x0ED%2BuCBJqyok37iImcDr0gUaIdjzZSSReUuciozLoPPfPGRx0pJsAiBmMwst6%2FOxuM3yYLkFAE0Q4Zp8hbQ%3D%3D&busRouteId=" + id;

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
//                상행선 버스의 위치만 저장
                if(getTagValue("DIR", element).equals("0")) {
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
        do {
            Node item = itemList2.item(j);
            if(item.getNodeType() == Node.ELEMENT_NODE) {
                element = (Element) item;
                station_name.add(getTagValue("BUSSTOP_NM", element));
                bus_isHere.add(station_id.contains(getTagValue("BUS_STOP_ID", element)));
            }
            j++;
        } while(!getTagValue("BUSSTOP_TP", element).equals("2"));

        ArrayList<SelectedRouteItem> res = new ArrayList<>();

        for(int index = 0 ; index < station_name.size() ; index++) {
            SelectedRouteItem item = new SelectedRouteItem(bus_isHere.get(index), station_name.get(index));
            res.add(item);
        }

        /**
         *
         * static variation in SelectedRouteInfo init
         * array length = res.size()
         *
         * */
//        if(SelectedRouteInfo.checked_bus == null && SelectedRouteInfo.checked_dest == null) {
//            SelectedRouteInfo.checked_bus = new boolean[res.size()];
//            SelectedRouteInfo.checked_dest = new boolean[res.size()];
//        }

        return res;
    }

    private String getTagValue(String tag, Element e) {
        NodeList list = e.getElementsByTagName(tag).item(0).getChildNodes();
        Node v = (Node) list.item(0);
        if(v == null) return null;
        return v.getNodeValue();
    }

}

class UpLineAdapter extends RecyclerView.Adapter<UpLineAdapter.ViewHolder> {

    private final ArrayList<SelectedRouteItem> busStops;

    @NonNull
    @Override
    public UpLineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_selected_route, parent, false);

        return new UpLineAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpLineAdapter.ViewHolder holder, int position) {
        String station_name = busStops.get(position).getBusStopName();
        holder.tv_busStop.setText(station_name);

        if(MainActivity.selectedID == null || MainActivity.selectedID.equals(UpLineFragment.getBusRouteId())) {
            //버스 혹은 정류장이 선택된 노선이 없거나, 버스 혹은 정류장이 선택된 노선이 현재 노선인 경우

            if(MainActivity.checked_dest[position]){ //새로고침 할 때 정류장 클릭배경색 유지
                holder.tv_busStop.setBackgroundColor(Color.rgb(178,204,255));
            }

            if(busStops.get(position).isBusIsHere()) {  //  버스가 여기에 있다면
                holder.iv_busIcon.setVisibility(View.VISIBLE);
                holder.iv_clickedBusIcon.setVisibility(View.GONE);
                holder.blank.setVisibility(View.GONE);

                //  직전 정류장에 있던 버스가 선택된 버스였던 경우
                if(position > 0) {  // 첫 정류장에 있는 버스에서 발생하는 오류 방지
                    if(MainActivity.checked_bus[position-1]) {
                        MainActivity.checked_bus[position] = true;
                        holder.iv_clickedBusIcon.setVisibility(View.VISIBLE);
                        holder.iv_busIcon.setVisibility(View.GONE);
                        holder.blank.setVisibility(View.GONE);
                        MainActivity.checked_bus[position-1] = false;
                    }
                }

                if(MainActivity.checked_bus[position]) { //새로고침할 때 버스 클릭아이콘 유지
                    holder.iv_busIcon.setVisibility(View.GONE);
                    holder.blank.setVisibility(View.GONE);
                    holder.iv_clickedBusIcon.setVisibility(View.VISIBLE);
                }

            } else if (!busStops.get(position).isBusIsHere()) { //  버스가 여기에 없다면
                holder.blank.setVisibility(View.VISIBLE);
                holder.iv_busIcon.setVisibility(View.GONE);
                holder.iv_clickedBusIcon.setVisibility(View.GONE);
            }

        } else {    // 특정 노선에서 버스 혹은 정류장을 선택했으며, 현재 화면에 보이는 노선이 해당 노선이 아닌 경우
            if(busStops.get(position).isBusIsHere()) {  //  버스가 여기에 있다면
                holder.iv_busIcon.setVisibility(View.VISIBLE);
                holder.iv_clickedBusIcon.setVisibility(View.GONE);
                holder.blank.setVisibility(View.GONE);

            } else if (!busStops.get(position).isBusIsHere()) { //  버스가 여기에 없다면
                holder.blank.setVisibility(View.VISIBLE);
                holder.iv_busIcon.setVisibility(View.GONE);
                holder.iv_clickedBusIcon.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return busStops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView iv_busIcon, iv_clickedBusIcon;
        TextView tv_busStop;
        View blank;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_busIcon = itemView.findViewById(R.id.iv_busIcon);
            iv_clickedBusIcon = itemView.findViewById(R.id.iv_clickedBusIcon);
            tv_busStop = itemView.findViewById(R.id.tv_busStop);
            blank = itemView.findViewById(R.id.blank);

            iv_busIcon.setOnClickListener(this);
            iv_clickedBusIcon.setOnClickListener(this);
            tv_busStop.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            if(!MainActivity.down_touchStart) { //  하행선에서 아무것도 선택하지 않은 경우 에만 onClick 동작
                if(view.getId() == R.id.iv_busIcon) {   // 버스 아이콘 클릭 시
                    if(MainActivity.selectedID == null || MainActivity.selectedID.equals(UpLineFragment.getBusRouteId())){
                        // 선택된 노선이 없거나, 선택된 노선이 현재 노선인 경우에만 동작
                        MainActivity.selectedID = UpLineFragment.getBusRouteId();
                        if(MainActivity.clickable_bus && !MainActivity.checked_bus[getAdapterPosition()]) {
                            view.setVisibility(View.GONE);
                            iv_clickedBusIcon.setVisibility(View.VISIBLE);
                            MainActivity.up_touchStart = !MainActivity.up_touchStart;
                            MainActivity.clickable_bus = false;
                            MainActivity.checked_bus[getAdapterPosition()] = true;
                            SelectedRouteInfo.firstup=true;
                            SelectedRouteInfo.secondup=true;
                        }
                    }
                } else if(view.getId() == R.id.iv_clickedBusIcon) { // 이미 선택된 버스 아이콘 클릭 시
                    if(MainActivity.selectedID.equals(UpLineFragment.getBusRouteId()) && MainActivity.clickable_dest) MainActivity.selectedID = null;
                    // 선택된 노선이 현재 노선과 동일하고, 목적지가 선택되지 않은 경우, 선택된 노선을 초기화.
                    if(!MainActivity.clickable_bus && MainActivity.checked_bus[getAdapterPosition()]) {
                        view.setVisibility(View.GONE);
                        iv_busIcon.setVisibility(View.VISIBLE);
                        MainActivity.up_touchStart = !MainActivity.up_touchStart;
                        MainActivity.clickable_bus = true;
                        MainActivity.checked_bus[getAdapterPosition()] = false;
                        SelectedRouteInfo.firstup=true;
                        SelectedRouteInfo.secondup=true;
                    }
                }

                if(view.getId() == R.id.tv_busStop) {  //  버스 정류장 부분 클릭 시
                    if(MainActivity.selectedID == null || MainActivity.selectedID.equals(UpLineFragment.getBusRouteId())) {
                        MainActivity.selectedID = UpLineFragment.getBusRouteId();
                        if(MainActivity.clickable_dest && !MainActivity.checked_dest[getAdapterPosition()]) {
                            tv_busStop.setBackgroundColor(Color.rgb(178,204,255));
                            MainActivity.up_touchStart = !MainActivity.up_touchStart;
                            MainActivity.clickable_dest = false;
                            MainActivity.checked_dest[getAdapterPosition()] = true;
                            SelectedRouteInfo.firstup=true;
                            SelectedRouteInfo.secondup=true;

                        } else if(!MainActivity.clickable_dest && MainActivity.checked_dest[getAdapterPosition()]) { //  이미 선택된 정류장을 눌렀을 때
                            if(MainActivity.selectedID.equals(UpLineFragment.getBusRouteId()) && MainActivity.clickable_bus) MainActivity.selectedID = null;
                            tv_busStop.setBackgroundColor(Color.WHITE);
                            MainActivity.up_touchStart = !MainActivity.up_touchStart;
                            MainActivity.clickable_dest = true;
                            MainActivity.checked_dest[getAdapterPosition()] = false;
                            SelectedRouteInfo.firstup=true;
                            SelectedRouteInfo.secondup=true;
                        }
                    }
                }

            }

        }
    }

    public UpLineAdapter(ArrayList<SelectedRouteItem> list) {
        busStops = list;
    }

}
