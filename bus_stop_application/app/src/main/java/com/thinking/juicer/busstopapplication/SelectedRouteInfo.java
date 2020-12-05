package com.thinking.juicer.busstopapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.thinking.juicer.busstopapplication.Fragment.RouteInfoPagerAdapter;
import com.thinking.juicer.busstopapplication.Fragment.UpLineFragment;

import java.util.Timer;
import java.util.TimerTask;


public class SelectedRouteInfo extends AppCompatActivity {

/*
*
* TAG
*
* */
    private final String TAG = "Selected Route Info";
/*
*
* Layout Components
*
* */
    private Context mContext;
    private TabLayout mTabLayout;
    private RouteInfoViewPager mViewPager;
    private RouteInfoPagerAdapter mAdapter;
/*
*
* Check Bus icon, Destination click
* If both of them are checked, turn on the notification.
*
* */
    public static boolean[] checked_bus = new boolean[60], checked_dest = new boolean[60];
    public static boolean clickable_bus = true, clickable_dest = true;
//    private boolean checked_bus = false, checked_dest = false;
    /*
     *
     * get current position of the tab
     *
     * */
    private static int currentPosition = 0;
    public static int getCurrentPosition() {return currentPosition;}
/*
*
* getIntent from MainActivity
*
* */
    private static Intent intent;
    public static Intent getSRIntent() {
        return intent;
    }

    private TimerTask task;
    private Timer timer;
    private BusThread bt = new BusThread();
    private Thread bthread = new Thread(bt);
    public static boolean firstA = true, secondA = true; //알람 확인용 플래그

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_route_info);
        mContext = getApplicationContext();

        mTabLayout = (TabLayout) findViewById(R.id.layout_tab);
        intent = getIntent();

//        TimerTask task2 = new TimerTask() {
//            @Override
//            public void run() {
//                Intent mIntest = new Intent(getApplicationContext(), GetOffNotificationActivity.class);
//
//                startActivity(mIntest);
//            }
//        };
//
//        Timer timer2 = new Timer();
//        timer2.schedule(task2, 1000);


        bthread.start();

        task = new TimerTask() {
            @Override
            public void run() {
                //정류장에 따른 알림
                if ((!clickable_bus) && (!clickable_dest) && firstA && secondA) {
                    if (indexOfArray(checked_bus) == indexOfArray(checked_dest) - 1) {  //한 정거장 전
                        Intent intent = new Intent(getApplicationContext(), GetOffNotificationActivity.class);
                        startActivity(intent);
                        firstA = false;
                    } else if (indexOfArray(checked_bus) == indexOfArray(checked_dest)) { //도착
                        Intent intent = new Intent(getApplicationContext(), CheckNotificationActivity.class);
                        startActivity(intent);
                        secondA = false;
                    }
                }
            }

        };


        timer = new Timer();
        timer.schedule(task,0,100);
/*
*
* dir 값을 받아오면서 상행, 하행에 걸맞는 종점을 tab에 출력
*
* */
        mTabLayout.addTab(mTabLayout.newTab().setText("상행"));
        mTabLayout.addTab(mTabLayout.newTab().setText("하행"));

/*
*
* use the adapter on view pager.
*
* */

        mViewPager = (RouteInfoViewPager) findViewById(R.id.pager_routeInfo);
        mAdapter = new RouteInfoPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                currentPosition = tab.getPosition();
                firstA=true;
                secondA=true;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public int indexOfArray(boolean[] checked){   // Searchng index of TRUE
        for(int i=0; i<checked.length; i++) {
            if (checked[i] == true) return i;
        }
        return -1;
    }

    class BusThread extends Thread {


        @Override
        public void run() {


        }
    }



}

class RouteInfoViewPager extends ViewPager {

    public RouteInfoViewPager(@NonNull Context context) {
        super(context);
    }
    public RouteInfoViewPager(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if(h > height) {
                height = h;
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}