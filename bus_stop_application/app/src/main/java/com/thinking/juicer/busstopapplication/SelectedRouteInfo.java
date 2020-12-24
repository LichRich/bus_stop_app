package com.thinking.juicer.busstopapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
    public static boolean firstup = true , secondup = true, firstdown = true, seconddown  =true ; //알람 확인용 플래그

    private static SelectedRouteInfo routeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_route_info);
        mContext = getApplicationContext();

        mTabLayout = (TabLayout) findViewById(R.id.layout_tab);
        intent = getIntent();

        task = new TimerTask() {
            @Override
            public void run() {
                //정류장에 따른 알림
                if ((!MainActivity.clickable_bus) && (!MainActivity.clickable_dest)) {
                    if(indexOfArray(MainActivity.checked_bus) > -1 && indexOfArray(MainActivity.checked_dest) > -1) { // 상행
                        if ((indexOfArray(MainActivity.checked_bus) == indexOfArray(MainActivity.checked_dest) - 1)&&firstup) {  // 한 정거장 전
                            Intent intent = new Intent(getApplicationContext(), GetOffNotificationActivity.class);
                            firstup = false;
                            startActivity(intent);
                        } else if (indexOfArray(MainActivity.checked_bus) > -1 &&
                                (indexOfArray(MainActivity.checked_bus) == indexOfArray(MainActivity.checked_dest))&&secondup) { // 도착
                            Intent intent = new Intent(getApplicationContext(), CheckNotificationActivity.class);
                            MainActivity.up_touchStart = false;
                            MainActivity.checked_bus[indexOfArray(MainActivity.checked_bus)] = false;
                            MainActivity.checked_dest[indexOfArray(MainActivity.checked_dest)] = false;
                            MainActivity.clickable_bus = true;
                            MainActivity.clickable_dest = true;
                            MainActivity.selectedID = null;
                            secondup = false;
                            startActivity(intent);
                        }
                    } else if(indexOfArray(MainActivity.down_checkedBus) > -1 && indexOfArray(MainActivity.down_checkedDest) > -1) { // 하행
                        if ((indexOfArray(MainActivity.down_checkedBus) == indexOfArray(MainActivity.down_checkedDest) - 1)&&firstdown) {  // 한 정거장 전
                            Intent intent = new Intent(getApplicationContext(), GetOffNotificationActivity.class);
                            firstdown = false;
                            startActivity(intent);
                        } else if (indexOfArray(MainActivity.down_checkedBus) > -1 &&
                                (indexOfArray(MainActivity.down_checkedBus) == indexOfArray(MainActivity.down_checkedDest))&&seconddown) { // 도착
                            Intent intent = new Intent(getApplicationContext(), CheckNotificationActivity.class);
                            MainActivity.down_touchStart = false;
                            MainActivity.down_checkedBus[indexOfArray(MainActivity.down_checkedBus)] = false;
                            MainActivity.down_checkedDest[indexOfArray(MainActivity.down_checkedDest)] = false;
                            MainActivity.clickable_bus = true;
                            MainActivity.clickable_dest = true;
                            MainActivity.selectedID = null;
                            seconddown = false;
                            startActivity(intent);
                        }
                    }

                }
            }

        };

        timer = new Timer();
        timer.schedule(task,0,100);

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

        routeInfo = SelectedRouteInfo.this;

    }

    public static void routeInfoFinish() {
        routeInfo.finish();
    }

    public static int indexOfArray(boolean[] checked){   // Searching index of TRUE
        for(int i=0; i<checked.length; i++) {
            if (checked[i]) return i;
        }
        return -1;
    }


}
