package com.thinking.juicer.busstopapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.thinking.juicer.busstopapplication.Fragment.RouteInfoPagerAdapter;

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
    private ViewPager mViewPager;
    private RouteInfoPagerAdapter mAdapter;
/*
*
* Check Bus icon, Destination click
* If both of them are checked, turn on the notification.
*
* */
    private boolean checked_bus = false, checked_dest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_route_info);
        mContext = getApplicationContext();

        mTabLayout = (TabLayout) findViewById(R.id.layout_tab);

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

        mViewPager = (ViewPager) findViewById(R.id.pager_routeInfo);
        mAdapter = new RouteInfoPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}