package com.example.jihyun.oingoing;

import android.app.TabActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost=getTabHost() ;
        TabHost.TabSpec tabSpecMonth=tabHost.newTabSpec("tabMonth").setIndicator("월별");
        tabSpecMonth.setContent(R.id.tabMonth);
        tabHost.addTab(tabSpecMonth);


        TabHost.TabSpec tabSpecWeek=tabHost.newTabSpec("tabWeek").setIndicator("주별");
        tabSpecWeek.setContent(R.id.tabWeek);
        tabHost.addTab(tabSpecWeek);

        TabHost.TabSpec tabSpecDay=tabHost.newTabSpec("tabDay").setIndicator("일별");
        tabSpecDay.setContent(R.id.tabDay);
        tabHost.addTab(tabSpecDay);



        tabHost.setCurrentTab(0);



    }



}
