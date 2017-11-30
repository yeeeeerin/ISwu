package com.example.leeyerin.iswu;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOBeaconRegionState;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECOMonitoringListener;
import com.perples.recosdk.RECORangingListener;
import com.perples.recosdk.RECOServiceConnectListener;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Leeyerin on 2017. 11. 24..
 */

public class LocationActivity extends AppCompatActivity implements
        RECOServiceConnectListener,
        RECOMonitoringListener,
        RECORangingListener
{

    public static final String RECO_UUID = "24DDF411-8CF1-440C-87CD-E368DAF9C93E";

    private RECOBeaconManager mRecoManager;

    private boolean mScanRecoOnly = true;
    private boolean mEnableBackgroundTimeout = true;

    private ArrayList<RECOBeaconRegion> mRegions;

    TextView reco1;
    TextView reco2;
    TextView reco3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        reco1 = (TextView)findViewById(R.id.textView);
        reco2 = (TextView)findViewById(R.id.textView2);
        reco3 = (TextView)findViewById(R.id.textView3);

         /* 모니터링할 비콘 등록 */

        mRegions = new ArrayList<RECOBeaconRegion>();

        mRegions.add(new RECOBeaconRegion(RECO_UUID, 501, 16015,"1번"));
        mRegions.add(new RECOBeaconRegion(RECO_UUID, 501 , 24866,"2번"));
        mRegions.add(new RECOBeaconRegion(RECO_UUID, 501 , 18789,"3번"));

        /* manager구현 */
        mRecoManager = RECOBeaconManager.getInstance(this, mScanRecoOnly, mEnableBackgroundTimeout);
        // manager와 reco비콘 서비스와 연결 ( monitoring과 rangging을 위함 )

        mRecoManager.setMonitoringListener(this);
        //scan 시간을 설정
        mRecoManager.setScanPeriod(1);
        //scan 후, 다음 scan 시작 전까지의 시간을 설정
        mRecoManager.setSleepPeriod(10);

        mRecoManager.setRangingListener(this);

        mRecoManager.bind(this);


    }

    /* RECOServiceConnectListener */

    @Override
    public void onServiceConnect() {
        //RECOBeaconService와 연결 시 코드 작성
        /* 모니터링 시작*/
        for(RECOBeaconRegion region : mRegions) {
            try {
                // region의 expiration 시간을 설정
                Log.i("RECOmonitor", "모니터링");
                region.setRegionExpirationTimeMillis(60);
                mRecoManager.startMonitoringForRegion(region);
            } catch (RemoteException e) {
                //RemoteException 발생 시 작성 코드
            } catch (NullPointerException e) {
                //NullPointerException 발생 시 작성 코드
            }
        }

        mRecoManager.setDiscontinuousScan(false);
        /* ranging 시작작 */
       for(RECOBeaconRegion region : mRegions) {
            try {
                Log.i("RECORanging", "랭잉");
                mRecoManager.startRangingBeaconsInRegion(region);
                mRecoManager.requestStateForRegion(region);
            } catch (RemoteException e) {
                //RemoteException 발생 시 작성 코드
            } catch (NullPointerException e) {
                //NullPointerException 발생 시 작성 코드
            }

        }

    } // end onServiceConnect()

    @Override
    public void onServiceFail(RECOErrorCode recoErrorCode) {
        //RECOBeaconService와 연결 되지 않았을 시 코드 작성
    }

    /* RECOMonitoringListener */

    @Override
    public void didEnterRegion(RECOBeaconRegion recoBeaconRegion, Collection<RECOBeacon> collection) {

        //monitoring 시작 후에 monitoring 중인 region에 들어갈 경우 이 callback 메소드가 호출됩니다.
        // 0.2 버전부터 이 callback 메소드가 호출 될 경우,  recoRegion에서 감지된 비콘들을 전달합니다.
        //region 입장시 코드 작성
        Log.i("RECOmonitoring", "regin입장");
    }

    @Override
    public void didExitRegion(RECOBeaconRegion recoBeaconRegion) {

        //monitoring 시작 후에 monitoring 중인 region에서 나올 경우 이 callback 메소드가 호출됩니다.
        //region 퇴장시 코드 작성
        Log.i("RECOmonitoring", "regin퇴장");
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

    /* RECORangingListener */

    @Override
    public void didRangeBeaconsInRegion(Collection<RECOBeacon> collection, RECOBeaconRegion recoBeaconRegion) {
        //ranging 중인 region에서 1초 간격으로 감지된
        //RECOBeacon들 리스트와 함께 이 callback 메소드를 호출합니다.
        //recoRegion에서 감지된 RECOBeacon 리스트 수신 시 작성 코드
        Log.i("RECORanging", "랭잉 성공");
        if (collection.size() == 0) {

        } else {
            ArrayList<RECOBeacon> rangedBeacons = new ArrayList<RECOBeacon>(collection);

            for (RECOBeacon beacon:rangedBeacons) {
                Log.i("RECORanging", "거리 "+beacon.getRssi());
                if (recoBeaconRegion.getUniqueIdentifier().equals("1번"))
                    reco1.setText("거리 "+beacon.getRssi());
                if(recoBeaconRegion.getUniqueIdentifier().equals("2번"))
                    reco2.setText("거리 "+beacon.getRssi());
                if (recoBeaconRegion.getUniqueIdentifier().equals("3번"))
                    reco3.setText("거리 "+beacon.getRssi());
            }

        }

    }

    @Override
    public void rangingBeaconsDidFailForRegion(RECOBeaconRegion recoBeaconRegion, RECOErrorCode recoErrorCode) {

    }

}
