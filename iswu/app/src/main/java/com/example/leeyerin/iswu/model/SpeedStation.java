package com.example.leeyerin.iswu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Leeyerin on 2017. 11. 26..
 */

//json으로부터 데이터 list를 가져오는 객체

public class SpeedStation {


    @SerializedName("shortestRouteList")
    @Expose
    private ArrayList<SpeedStationInfo> infoList = null;



    public ArrayList<SpeedStationInfo> getInfoList() {
        return infoList;
    }

    public void setInfoList(ArrayList<SpeedStationInfo> infoList) {
        this.infoList = infoList;
    }


}
