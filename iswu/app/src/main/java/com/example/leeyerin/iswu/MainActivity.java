package com.example.leeyerin.iswu;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOBeaconRegionState;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECOMonitoringListener;
import com.perples.recosdk.RECOServiceConnectListener;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity
        implements Button.OnClickListener{

    private Button subwayBtn;
    private Button locationBtn;

    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        subwayBtn = (Button)findViewById(R.id.subwayBtn);
        subwayBtn.setOnClickListener(this);
        locationBtn = (Button)findViewById(R.id.locationBtn);
        locationBtn.setOnClickListener(this);

        // 블루트스 권한 승인 요청
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // 블루트스가 지원되는 기기
        if(mBluetoothAdapter != null)
        {
            // 블루트스가 꺼져있을때
            if(!mBluetoothAdapter.isEnabled()) {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent, 1);
            }else {

            }
        }else {
            finish();
        }



    }


    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()){

            case R.id.subwayBtn:
                intent = new Intent(getApplicationContext(),SubwayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                break;

            case R.id.locationBtn:
                intent = new Intent(getApplicationContext(),LocationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                break;


        }
    }
}
