package com.example.jihyun.oingoing;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jihyun on 2017-07-09.
 */

public class DailyDetailsModel extends RealmObject{
    @PrimaryKey
    private int id;
    private int money_set = 0; //일일설정액
    private String startDate;
    private String endDate;

    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getstartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getMoney_set() { return money_set; }
    public void setMoney_set(int money_set) { this.money_set = money_set; }

    public int getId() {return id;}
    public void setId(int id) {
        this.id = id;
    }

}
