package com.whywhom.soft.huarongdao.utils;

/**
 * Created by wuhaoyong on 2019-09-08-09-2019.
 */
public class GameHRD {
    private int hId;
    private String hName;
    private boolean hLocked;
    public GameHRD(int id, String name, boolean locked){
        hId = id;
        hName = name;
        hLocked = locked;
    }

    public int gethId() {
        return hId;
    }

    public void sethId(int hId) {
        this.hId = hId;
    }

    public String gethName() {
        return hName;
    }

    public void sethName(String hName) {
        this.hName = hName;
    }

    public boolean ishLocked() {
        return hLocked;
    }

    public void sethLocked(boolean hLocked) {
        this.hLocked = hLocked;
    }
}
