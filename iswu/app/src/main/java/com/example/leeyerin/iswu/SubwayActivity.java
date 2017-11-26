package com.example.leeyerin.iswu;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeyerin.iswu.bases.BaseService;
import com.example.leeyerin.iswu.model.SpeedStation;
import com.example.leeyerin.iswu.model.SpeedStationInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Leeyerin on 2017. 11. 24..
 */

//http://swopenapi.seoul.go.kr/api/subway/6d754d496a697377313031455a7a4d44/json/shortestRoute/0/5/소요산/잠실나루
//

public class SubwayActivity extends AppCompatActivity
                            implements GestureDetector.OnDoubleTapListener,
                                       GestureDetector.OnGestureListener{

    TextView testTextView;

    private static final String BASE_URL = "http://swopenapi.seoul.go.kr/api/subway/6d754d496a697377313031455a7a4d44/json/shortestRoute/0/5/";

    static public String getBase_URL() {
        return BASE_URL;
    }

    String minTransferMsg;// firstStationName+"에서"+SecondStaionName+ "까지" 얼마 소요됨

    //2터치
    private GestureDetector detector;


    Intent intent;
    SpeechRecognizer mRecognizer;

    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;


    String firstStationName = null; // 출발역
    String SecondStaionName = null; //도착역
    //String SecondStaionName = "상동"; // test

    int level = 0;
    /*
    level 변수
        0 -> 대기
        1 -> 1번째 역 설정
        2 -> 2번째 역 설정
        3 -> 환승 안내 설정
    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway);

        testTextView = (TextView) findViewById(R.id.logText);


        detector = new GestureDetector(this, this);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO
                );
            }
        }


        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(recognitionListener);



        //출발 역 화랑대로 설정
        firstStationName = "화랑대";


        level = 1;
        /***현재 출발지는 000역입니다 다시 설정하시려면 1번터치 틀리시면 2번 터치해주세요***/

        String s = "/***현재 출발지는 " + firstStationName +"역입니다 다시 설정하시려면 1번터치 틀리시면 2번 터치해주세요***/";
        testTextView.setText(s);
        Log.d("seg",s);




    }

    ArrayList<SpeedStationInfo> SpeedStationInfoList = null;



    private void retrofit() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        BaseService baseService = retrofit.create(BaseService.class);
        Call<SpeedStation> call = baseService.getData(firstStationName,SecondStaionName);


        call.enqueue(new Callback<SpeedStation>() {
            @Override
            public void onResponse(Call<SpeedStation> call, Response<SpeedStation> response) {
                Log.d("seg","데이터가져옴");
                SpeedStationInfoList = response.body().getInfoList();

                for(SpeedStationInfo model : SpeedStationInfoList){

                    minTransferMsg = model.getShtTransferMsg();

                    String s = firstStationName+"에서 " +SecondStaionName+ "까지 "+minTransferMsg;
                    testTextView.setText(s);
                    Log.d("seg",s);


                }
            }
            @Override
            public void onFailure(Call<SpeedStation> call, Throwable t) {
                Log.e("seg", "onFailure: Something went wrong: " + t.getMessage());
                Toast.makeText(SubwayActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        this.detector.onTouchEvent(event);

        return detector.onTouchEvent(event);
    }



    public void setFirst(){

        firstStationName = null;


        String s = "출발지를 말해주세요";
        testTextView.setText(s);
        //음성처리//다시 한번 말해주세요
        Log.d("seg",s);


        mRecognizer.startListening(intent);
        //ex)화랑대 값 받았다고 생각

    }

    void setfirstconfirm(){


        String s = "/***현재 출발지는 " + firstStationName +"역입니다 다시 설정하시려면 1번터치 틀리시면 2번 터치해주세요***/";
        testTextView.setText(s);
        Log.d("seg",s);

    }

    public void setSecond(){

        String s = "도착지를 말해주세요";
        testTextView.setText(s);
        Log.d("seg",s);
        //음성처리//다시 한번 말해주세요
        mRecognizer.startListening(intent);
        //ex)화랑대 값 받았다고 생각



    }




    public void Level3Controller() {

        minTransferMsg = null;

        retrofit();



    }

    //한번 터치시
    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {

        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {

        Log.d("seg","두번터치");
        switch (level){
            case 1:

                setFirst();

                break;
            case 2:

                setSecond();

                break;
            case 3:

                break;
        }
        return false;
    }

    //두번 터치시
    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {




        return false;
    }


    private RecognitionListener recognitionListener = new RecognitionListener() {

        //사용자가 말할 준비가 되면 시작하면 호출
        @Override
        public void onReadyForSpeech(Bundle bundle) {

            Log.d("seg","시작");
        }

        //사용자가 말하기 시작할 때 호출
        @Override
        public void onBeginningOfSpeech() {


        }

        //오디오 스트림 사운드 레벨 변경시 호출
        @Override
        public void onRmsChanged(float v) {
        }

        //많은 소리가 수신 될 때 호출(오디오 겹칠때 인거같음)
        @Override
        public void onBufferReceived(byte[] bytes) {
        }

        //사용자가 말하기를 중지하면 호출
        @Override
        public void onEndOfSpeech() {
        }

        //네트워크 또는 인식오류가 발생할 때
        @Override
        public void onError(int i) {
            //textView.setText("너무 늦게 말하면 오류뜹니다");

            NotificationSomethings();

        }

        //인식 결과
        @Override
        public void onResults(Bundle bundle) {

            Log.d("seg","음성인식완료");

            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            //textView.setText(rs[0]);

            if (level ==1) {
                firstStationName = rs[0];
                setfirstconfirm();
            }
            else if(level == 2) {
                SecondStaionName = rs[0];

                String s = "/***도착지는 " + SecondStaionName +"역입니다 다시 설정하시려면 1번터치 틀리시면 2번 터치해주세요***/";
                testTextView.setText(s);
                Log.d("seg",s);
            }



        }

        //부분 인식 결과
        @Override
        public void onPartialResults(Bundle bundle) {
        }

        //향후 이벤트 추가
        @Override
        public void onEvent(int i, Bundle bundle) {
        }
    };

    public void NotificationSomethings(){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL);
        if(android.os.Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(20,builder.build());
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {

        Log.d("seg","한번터치");


        switch (level){
            case 1:

                if(firstStationName != null) {
                    level = 2;
                    setSecond();
                }

                break;
            case 2:
                if(SecondStaionName != null) {
                    level = 3;
                    Level3Controller();
                }
                break;
            case 3:
                break;
        }


        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
