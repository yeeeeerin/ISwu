package com.example.leeyerin.iswu;

import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOBeaconRegionState;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECOMonitoringListener;
import com.perples.recosdk.RECOServiceConnectListener;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity implements
        RECOServiceConnectListener,
        RECOMonitoringListener{

    RECOBeaconManager recoManager;
    boolean mScanRecoOnly = true;
    boolean mEnableBackgroundTimeout = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        recoManager = RECOBeaconManager.getInstance(this, mScanRecoOnly, mEnableBackgroundTimeout);

        recoManager.bind(this);

        recoManager.setMonitoringListener(this);

        //scan 시간을 설정할 수 있습니다. 기본 값은 1초 입니다.
        recoManager.setScanPeriod(TIME_IN_MILLISECOND);

//scan 후, 다음 scan 시작 전까지의 시간을 설정할 수 있습니다. 기본 값은 10초 입니다.
        recoManager.setSleepPeriod(TIME_IN_MILLISECOND);

        ArrayList<RECOBeaconRegion> monitoringRegions = new ArrayList<RECOBeaconRegion>();
        monitoringRegions.add(new RECOBeaconRegion(YOUR_REGION_UUID, YOU_REGION_UNIQUE_IDENTIFIER));
        monitoringRegions.add(new RECOBeaconRegion(YOUR_REGION_UUID, YOUR_REGION_MAJOR,
                YOU_REGION_UNIQUE_IDENTIFIER));

        for(RECOBeaconRegion region : monitoringRegions) {
            try {
                recoManager.startMonitoringForRegion(region);
            } catch (RemoteException e) {
                //RemoteException 발생 시 작성 코드
            } catch (NullPointerException e) {
                //NullPointerException 발생 시 작성 코드
            }

            
        }
    }

    @Override
    public void onServiceConnect() {
        //RECOBeaconService와 연결 시 코드 작성
    }

    @Override
    public void onServiceFail(RECOErrorCode recoErrorCode) {
        //RECOBeaconService와 연결 되지 않았을 시 코드 작성
    }

    @Override
    public void didEnterRegion(RECOBeaconRegion recoBeaconRegion, Collection<RECOBeacon> collection) {

        //monitoring 시작 후에 monitoring 중인 region에 들어갈 경우 이 callback 메소드가 호출됩니다.
        // 0.2 버전부터 이 callback 메소드가 호출 될 경우,  recoRegion에서 감지된 비콘들을 전달합니다.
        //region 입장시 코드 작성
    }

    @Override
    public void didExitRegion(RECOBeaconRegion recoBeaconRegion) {

        //monitoring 시작 후에 monitoring 중인 region에서 나올 경우 이 callback 메소드가 호출됩니다.
        //region 퇴장시 코드 작성
    }

    @Override
    public void didStartMonitoringForRegion(RECOBeaconRegion recoBeaconRegion) {

        //monitoring이 정상적으로 시작하지 못했을 경우 이 callback 메소드가 호출됩니다.
        //RECOErrorCode는 "Error Code" 를 확인하시기 바랍니다.
        //monitoring 실패 시 코드 작성
    }

    @Override
    public void didDetermineStateForRegion(RECOBeaconRegionState recoBeaconRegionState, RECOBeaconRegion recoBeaconRegion) {

        //monitoring 시작 후에 monitoring 중인 region에 들어가거나 나올 경우
        //(region 의 상태에 변화가 생긴 경우) 이 callback 메소드가 호출됩니다.
        //didEnterRegion, didExitRegion callback 메소드와 함께 호출됩니다.
        //region 상태 변화시 코드 작성
    }

    @Override
    public void monitoringDidFailForRegion(RECOBeaconRegion recoBeaconRegion, RECOErrorCode recoErrorCode) {

    }
}
