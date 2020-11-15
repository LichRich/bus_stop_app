package com.thinking.juicer.busstopapplication.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/*
*
* Adapter for show the information of selected route on the view pager.
*
* */

public class RouteInfoPagerAdapter extends FragmentPagerAdapter {

    private int pageCount;

    public RouteInfoPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.pageCount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                UpLineFragment frag_upLine = new UpLineFragment();
                return frag_upLine;
            case 1:
                DownLineFragment frag_downLine = new DownLineFragment();
                return frag_downLine;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return pageCount;
    }
}
