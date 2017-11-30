package com.example.leeyerin.iswu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Leeyerin on 2017. 11. 26..
 */

public class SpeedStationInfo {



    @SerializedName("shtTransferMsg")
    @Expose
    private String shtTransferMsg;


    public String getShtTransferMsg() {
        return shtTransferMsg;
    }

    public void setShtTransferMsg(String shtTransferMsg) {
        this.shtTransferMsg = shtTransferMsg;
    }

}





