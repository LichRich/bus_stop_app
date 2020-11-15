package com.thinking.juicer.busstopapplication.items;

public class SelectedRouteItem {

    private boolean busIsHere;
    private String busStopName;

    public SelectedRouteItem() {}
    public SelectedRouteItem(String n) {
        this.busStopName = n;
    }
    public SelectedRouteItem(boolean b, String n){
        this.busIsHere = b;
        this.busStopName = n;
    }

    public boolean isBusIsHere() {
        return busIsHere;
    }

    public void setBusIsHere(boolean busIsHere) {
        this.busIsHere = busIsHere;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public void setBusStopName(String busStopName) {
        this.busStopName = busStopName;
    }

}
