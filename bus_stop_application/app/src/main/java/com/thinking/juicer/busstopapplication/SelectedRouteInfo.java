package com.thinking.juicer.busstopapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.thinking.juicer.busstopapplication.Fragment.RouteInfoPagerAdapter;

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

    private Timer timer;
    private TimerTask task;
    boolean preFlag=false, postFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_route_info);
        mContext = getApplicationContext();

        mTabLayout = (TabLayout) findViewById(R.id.layout_tab);
        intent = getIntent();

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                int busIndex = getIndex(checked_bus);
                int destIndex = getIndex(checked_dest);

                if((busIndex>=0) && (destIndex>=0)){
                    preFlag=true;
                    postFlag=true;
                }
                if((busIndex==destIndex) && preFlag){
                    Intent mint = new Intent(getApplicationContext(),CheckNotificationActivity.class);
                    startActivity(mint);
                    preFlag = false;
                }
                else if((busIndex==destIndex-1) && postFlag){
                    Intent mint = new Intent(getApplicationContext(),GetOffNotificationActivity.class);
                    startActivity(mint);
                    postFlag = false;
                }
            }
        };

        timer.schedule(task,0,500);

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
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    int getIndex(boolean[] arr){
        for(int i=0; i<arr.length; i++){
            if(arr[i]==true) return i;
        }
        return -1;
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