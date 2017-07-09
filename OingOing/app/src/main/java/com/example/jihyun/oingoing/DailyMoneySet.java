package com.example.jihyun.oingoing;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.jihyun.oingoing.R.id.end;
import static com.example.jihyun.oingoing.R.id.setButton;
import static com.example.jihyun.oingoing.R.id.setMoney;

/**
 * Created by jihyun on 2017-04-30.
 */

public class DailyMoneySet extends AppCompatActivity implements View.OnClickListener{

    private Button setbutton;
    private Realm myRealm;
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText startDate, endDate, setMoney;
    private AlertDialog.Builder subDialog;

    private static ArrayList<DailyDetailsModel> dailyDetailsModelArrayList = new ArrayList<>();
    private DailyDetailsAdapter dailyDetailsAdapter;
    private static int id=1;
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailymoneyset);
        myRealm = Realm.getInstance(DailyMoneySet.this);
        dailyDetailsAdapter = new DailyDetailsAdapter(DailyMoneySet.this, dailyDetailsModelArrayList);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        startDate = (EditText) findViewById(R.id.startdate);
        endDate = (EditText) findViewById(R.id.enddate);
        setMoney = (EditText) findViewById(R.id.setMoney);
        startDate.setInputType(InputType.TYPE_NULL);
        startDate.requestFocus();
        endDate.setInputType(InputType.TYPE_NULL);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();

        getAllUsers();


        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                endDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        //입력 다 안했을 때 뜨는 다이얼로그
        subDialog = new AlertDialog.Builder(DailyMoneySet.this)
                .setMessage("모두 입력해주세요")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg2, int which) {
                        dlg2.cancel();
                    }
                });

        setbutton=(Button) findViewById(setButton);
        setbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Utility.isBlankField(setMoney)) {
                    //금액 가져오기
                    String daily_money = setMoney.getText().toString();
                    int dailymoney= Integer.parseInt(daily_money);
                    String start_date=startDate.getText().toString();
                    String end_date=endDate.getText().toString();

                    //데이터베이스에 추가하기
                    myRealm.beginTransaction();
                    DailyDetailsModel dailyDetailsModel = myRealm.createObject(DailyDetailsModel.class);

                    dailyDetailsModel.setId(id+dailyDetailsModelArrayList.size());
                    dailyDetailsModel.setMoney_set(dailymoney);
                    dailyDetailsModel.setStartDate(start_date);
                    dailyDetailsModel.setEndDate(end_date);

                    dailyDetailsModelArrayList.add(dailyDetailsModel);
                    myRealm.commitTransaction();
                    dailyDetailsAdapter.notifyDataSetChanged();
                    id++;

                    //메인으로 돌아가기
                    Intent intent = new Intent(getApplicationContext(),//현재화면의
                            MainActivity.class);//다음 넘어갈 클래스 지정

                    startActivity(intent);//다음 화면으로 넘어간다
                }
                else{
                    subDialog.show();
                }
                //test//
                //Toast.makeText(UpdateSpend.this,selItem,Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public void onClick(View view) {
        if(view == startDate) {
            startDatePickerDialog.show();
        } else if(view == endDate) {
            endDatePickerDialog.show();
        }
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

    protected void onDestroy() {
        super.onDestroy();
        dailyDetailsModelArrayList.clear();
        myRealm.close();
    }


}
