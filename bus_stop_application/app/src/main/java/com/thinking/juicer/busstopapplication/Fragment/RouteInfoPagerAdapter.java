package com.thinking.juicer.busstopapplication.Fragment;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.thinking.juicer.busstopapplication.RouteInfoViewPager;

/*
*
* Adapter for show the information of selected route on the view pager.
*
* */

public class RouteInfoPagerAdapter extends FragmentPagerAdapter {

    private int pageCount;
    private int mCurrentPosition = -1;

    public RouteInfoPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.pageCount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UpLineFragment();
            case 1:
                return new DownLineFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        if(position != mCurrentPosition) {
            Fragment fragment = (Fragment) object;
            RouteInfoViewPager pager = (RouteInfoViewPager) container;
            if(fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }
}
