package com.thinking.juicer.busstopapplication.items;

public class BusRouteItem {

    private String busNum;

    public BusRouteItem(){}
    public BusRouteItem(String n) {
        this.busNum = n;
    }

    public String getBusNum() {
        return busNum;
    }

    public void setBusNum(String num) {
        this.busNum = num;
    }

}
