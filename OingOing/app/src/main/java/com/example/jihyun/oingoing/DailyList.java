package com.example.jihyun.oingoing;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jihyun on 2017-07-09.
 */

public class DailyList extends AppCompatActivity {
    private DailyDetailsAdapter dailyDetailsAdapter;
    private static int id = 1;
    private static ArrayList<DailyDetailsModel> dailyDetailsModelArrayList = new ArrayList<>();
    private Realm myRealm;
    private ListView lvList;
    private static DailyList instance;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main2);
        lvList = (ListView) findViewById(R.id.lvList);
        myRealm = Realm.getInstance(DailyList.this);
        instance=this;
        setDailyDetailsAdapter();
        getAllUsers();
    }
    public static DailyList getInstance() {

        return instance;
    }
    private void setDailyDetailsAdapter() {
        dailyDetailsAdapter = new DailyDetailsAdapter(DailyList.this, dailyDetailsModelArrayList);
        lvList.setAdapter(dailyDetailsAdapter);
    }
    private void getAllUsers() {
        RealmResults<DailyDetailsModel> results = myRealm.where(DailyDetailsModel.class).findAll();
        myRealm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            dailyDetailsModelArrayList.add(results.get(i));
        }
        if(results.size()>0)
            id = myRealm.where(DailyDetailsModel.class).max("id").intValue() + 1;
        myRealm.commitTransaction();
        dailyDetailsAdapter.notifyDataSetChanged();
    }
    public void deleteData(int personId, int position) {
        RealmResults<DailyDetailsModel> results = myRealm.where(DailyDetailsModel.class).equalTo("id", personId).findAll();
        myRealm.beginTransaction();
        results.remove(0);
        myRealm.commitTransaction();
        dailyDetailsModelArrayList.remove(position);
        dailyDetailsAdapter.notifyDataSetChanged();
    }
    public DailyDetailsModel searchData(int personId) {
        RealmResults<DailyDetailsModel> results = myRealm.where(DailyDetailsModel.class).equalTo("id", personId).findAll();
        myRealm.beginTransaction();
        myRealm.commitTransaction();
        return results.get(0);
    }

    private void addDataToRealm(DailyDetailsModel model) {
        myRealm.beginTransaction();

        DailyDetailsModel dailyDetailsModel = myRealm.createObject(DailyDetailsModel.class);
        dailyDetailsModel.setId(id+dailyDetailsModelArrayList.size()); //id+남아있는리스트개수를 해줘야해
        dailyDetailsModel.setMoney_set(model.getMoney_set());
        dailyDetailsModelArrayList.add(dailyDetailsModel);
        myRealm.commitTransaction();
        dailyDetailsAdapter.notifyDataSetChanged();
        id++;
    }

    protected void onDestroy() {
        super.onDestroy();
        dailyDetailsModelArrayList.clear();
        myRealm.close();
    }







}
