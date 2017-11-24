package com.example.pc.testoink;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    final static String LOG_TAG = "myLogs";

    //그 전 화면의 intent에서 date정보를 가져오는 함수
    static String intent_setDate;



    /* 뷰 */
    private TextView mTxtPercent; // 달성률 __ click 금액 다이얼로그
    private TextView mRestPercent; //회전 후 나오는거
    private TextView mTxtCalcDate; // 오늘날짜 __ click 달력 다이얼로그
    private ImageView mImgScrEvent; // 스크롤 이벤트 버튼
    private FloatingActionButton mBtnAddUpdate; // 플로팅 __ click 수입,지출 기록 다이얼로그
    private FloatingActionButton mBtnMoneySet; // 플로팅 __ click 일일 지출액 설정
    private TextView dailyset;
    private TextView remMoney;

    /*  private CustomCalendarDialog mCalendarDialog= new CustomCalendarDialog(this);
*/
    private ScrollView scrollView;
    private AlertDialog.Builder subDialog;

    /*3d 회전*/
    private boolean isFront = true;
    private int DURATION = 500;
    private ViewGroup mContainer;
    private LinearLayout frontView, backView;
    private float centerX;
    private float centerY;


    /* 어댑터 */
    private Adapter mAdapter;
    private DataDetailsAdapter dataAdapter;

    /* DB */
    public static int id = 1;
    private Realm myRealm;
    private ListView mIncExpList;
    private static ArrayList<DataDetailsModel> dataDetailsModelArrayList = new ArrayList<>();
    private DataDetailsAdapter dataDetailsAdapter;
    private int money_sum=0; //추가
    private static MainActivity instance;

    String remainMoney; // 남은돈
    String SetDate; // 선택 날짜 설정
    String currnet_Date;//원래 오늘 날짜

    int setmoney;

    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-M-d", Locale.KOREA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //그 전 화면의 intent에서 date정보를 가져오는 구문


        /* 스플래쉬 화면*/
        startActivity(new Intent(MainActivity.this, Splash.class));

        /*3d회전*/
        this.mContainer = (ViewGroup) findViewById(R.id.container);
        this.frontView = (LinearLayout) findViewById(R.id.imageView3);
        this.backView = (LinearLayout) findViewById(R.id.imageView4);
        /////db///////////

        Date dd=new Date();
        SetDate=transFormat.format(dd);

        if(intent_setDate != null){
            SetDate=intent_setDate;
            Log.d("day", SetDate);
        }

        currnet_Date = transFormat.format(dd); //현재날짜


        mIncExpList = (ListView) findViewById(R.id.list_use);
        myRealm = Realm.getInstance(MainActivity.this);
        instance = this;
        setPersonDetailsAdapter();
        getAllUsers();


        mTxtPercent = (TextView) findViewById(R.id.txt_percent);
        mRestPercent = (TextView) findViewById(R.id.rest_percent);
        mTxtCalcDate = (TextView) findViewById(R.id.txt_calendarDate);
        mImgScrEvent = (ImageView) findViewById(R.id.img_scrollEvent);
        mBtnAddUpdate = (FloatingActionButton) findViewById(R.id.btn_add);

        dailyset=(TextView)findViewById(R.id.txt_dailySet);
        remMoney=(TextView)findViewById(R.id.txt_remain);

        dataAdapter=new DataDetailsAdapter(this,dataDetailsModelArrayList);
        mIncExpList.setAdapter(dataAdapter);

        scrollView = (ScrollView) findViewById(R.id.ScrollView);
        setmoney = 0;
        getDailyMoney();


        //버튼 클릭 시 회전
        frontView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyRotation(0f, 90f, 180f, 0f);
                mRestPercent.setText(money_sum + "원");
            }
        });
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyRotation(180f, 270f, 360f, 0f);

            }
        });




        // 날짜 클릭시
        mTxtCalcDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),CalenderDialog.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


        /* 스트롤 이벤트버튼 클릭시 */
        mImgScrEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* 자동스크롤 */
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });

        /* 사용한 리스트???????????????????????????????  */
        setPersonDetailsAdapter();

        /* 플로팅 버튼 클릭시 */
        mBtnAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //새로추가를 눌렀을 때 null을 줌
                addOrUpdatePersonDetailsDialog(null, -1);
            }
        });

        /* */
        dailyset.setText(Integer.toString(setmoney));
        remMoney.setText(Integer.toString(setmoney-money_sum));

    } // end onCreate


    //일일설정약 db에서 데이터 가져오기 , 빼기
    private void getDailyMoney(){

        try {
            Date d = new SimpleDateFormat("yyyy-M-d").parse( SetDate);
            Log.e("ee", d.toString()+"날짜 date변");

            RealmResults<DailyMoneyModel> results = myRealm.where(DailyMoneyModel.class)
                    .lessThanOrEqualTo("startDate",d)
                    .greaterThanOrEqualTo("endDate",d)
                    .findAll();
//        Log.e("ee", results.get(results.size()-1).getEndDate());
            myRealm.beginTransaction();



            if (results.size()>0) {
                setmoney=results.get(0).getMoney_set();
                String string = Integer.toString(setmoney - money_sum);
                Log.e("money", "일일설정액 - 선택한 날짜 " + string);
                // /* 일일 설정액 초과시 알림
                if(Integer.valueOf(string)<0) {
                    NotificationSomethings();
                }

                remainMoney=Integer.toString((setmoney-money_sum)/results.get(0).getMoney_set()*100);
                mTxtPercent.setText(remainMoney+"%");
            }

            myRealm.commitTransaction();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


///--------------------db관련 함수들-----------------------

    public static MainActivity getInstance() {
        Log.e(LOG_TAG, "DataList.getInstance");
        return instance;
    }
    ///////////////////////////////////////////////////////////////////////////////////
    private void setPersonDetailsAdapter() {
        Log.e(LOG_TAG, "DataList.setPersonDetailsAdapter");
        dataDetailsAdapter = new DataDetailsAdapter(MainActivity.this, dataDetailsModelArrayList);
        mIncExpList.setAdapter(dataDetailsAdapter);//데이터 리스트 보여주는 함수
    }

    /* 데이터 리스트 가져오는 함수 */
    private void getAllUsers() {
        Log.e(LOG_TAG, "DataList.getAllUsers");
        dataDetailsModelArrayList.clear();
        RealmResults<DataDetailsModel> results = myRealm.where(DataDetailsModel.class).equalTo("date",  SetDate).findAll();
        myRealm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            dataDetailsModelArrayList.add(results.get(i));
        }
        if (results.size() > 0)
            id = myRealm.where(DataDetailsModel.class).max("id").intValue() + 1;
        myRealm.commitTransaction();
        dataDetailsAdapter.notifyDataSetChanged();

        // 남은 금액 계산
        money_sum = 0;
        for (int j = 0; j < dataDetailsModelArrayList.size(); j++) {
            if(dataDetailsModelArrayList.get(j).isInOrOut()) {
                money_sum += dataDetailsModelArrayList.get(j).getPrice();
            } else{
                money_sum -= dataDetailsModelArrayList.get(j).getPrice();
            }

        }
        String string = Integer.toString(money_sum);
        Log.e("money", string);
    }

    public void deleteData(int personId, int position) {
        Log.e(LOG_TAG, "DataList.deletePerson");
        RealmResults<DataDetailsModel> results = myRealm.where(DataDetailsModel.class).equalTo("id", personId).findAll();

        myRealm.beginTransaction();
        results.remove(0);
        myRealm.commitTransaction();
        dataDetailsModelArrayList.remove(position);
        dataDetailsAdapter.notifyDataSetChanged();
    } // end deleteData

    public DataDetailsModel searchData(int personId) {
        Log.e(LOG_TAG, "DataList.searchPerson");
        RealmResults<DataDetailsModel> results = myRealm.where(DataDetailsModel.class).equalTo("id", personId).findAll();
        myRealm.beginTransaction();
        myRealm.commitTransaction();
        return results.get(0);
    } // end DateDetailsModel

    /* 일일 설정액 dialog*/

   /* 데이터 삽입함수 */

    private void addDataToRealm(DataDetailsModel model) {
        Log.e(LOG_TAG, "DataList.addDataToRealm");

        myRealm.beginTransaction();

        DataDetailsModel dataDetailsModel = myRealm.createObject(DataDetailsModel.class);
        dataDetailsModel.setId(id); //id+남아있는리스트개수를 해줘야해
        dataDetailsModel.setName(model.getName());
        dataDetailsModel.setPrice(model.getPrice());
        dataDetailsModel.setDate(model.getDate());
        dataDetailsModel.setInOrOut(model.isInOrOut()); //수입
        dataDetailsModelArrayList.add(dataDetailsModel);

        myRealm.commitTransaction();
        id++;
        dataDetailsAdapter.notifyDataSetChanged();

    } // end addDataToRealm



    /* 데이터 업데이트 함수 (수정) */
    public void updatePersonDetails(DataDetailsModel model, int position, int personID) {
        Log.e(LOG_TAG, "MainActivity.updatePersonDetails");
        DataDetailsModel editPersonDetails = myRealm.where(DataDetailsModel.class).equalTo("id", personID).findFirst();
        myRealm.beginTransaction();
        editPersonDetails.setName(model.getName());
        editPersonDetails.setPrice(model.getPrice());
        myRealm.commitTransaction();
        dataDetailsModelArrayList.set(position, editPersonDetails);
        dataDetailsAdapter.notifyDataSetChanged();
    } // end updatePersonDetails


    /* 데이터를 추가 + 삭제 하는 함수 */
    public void addOrUpdatePersonDetailsDialog(final DataDetailsModel model, final int position) {

   /* subdialog */
        // 입력이 다 되지 않았을 경우
        subDialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage("모두 입력해주세요")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg2, int which) {
                        dlg2.cancel();
                    }
                });

   /* maindialog */
        LayoutInflater li = LayoutInflater.from(MainActivity.this);// 뷰를 띄워주는 역할
        View promptsView = li.inflate(R.layout.mainincome_dialog, null);// 정보를 입력하는 창 연결, 뷰 생성
        AlertDialog.Builder mainDialog = new AlertDialog.Builder(MainActivity.this);// 다이얼 로그 생성하기 위한 빌더 얻기
        mainDialog.setView(promptsView);// 알림창 지정된 레이아웃을 띄운다
        mainDialog.setTitle("기록해요 꿀");


        //이 변수들은 income_dialog.xml에서 가져온 아이들, 즉 한 엑티비티에 뷰를 두개 가져온 것이다
        //위에서 View promptsViewView이 문장을 통해 뷰를 생성했기 때문에 사용이 가능하다
        final EditText etAddCategory = (EditText) promptsView.findViewById(R.id.setCategory);
        final EditText etAddIncome = (EditText) promptsView.findViewById(R.id.setIncome);
        TextView tv_datee = (TextView) promptsView.findViewById(R.id.tv_datee);
        tv_datee.setText(currnet_Date);

        final RadioGroup rg = (RadioGroup) promptsView.findViewById(R.id.dialog_rg);
        int checkedId = rg.getCheckedRadioButtonId();
        final RadioButton rb1 = (RadioButton) rg.findViewById(R.id.dialog_rb_income);
        final RadioButton rb2 = (RadioButton) rg.findViewById(R.id.dialog_rb_spend);
        //String checked= rb.getText().toString();

        //라디오버튼으로 배열 가져오기 => 안됨
        if (rb1.isChecked()) {
            mAdapter = ArrayAdapter.createFromResource(this,
                    R.array.UISpinner,//배열 가져온다
                    android.R.layout.simple_spinner_item);
            //etAddCategory.setAdapter(adapter);
            Toast.makeText(getApplicationContext(), rb1.getText().toString(), Toast.LENGTH_SHORT).show();
        } else if (rb2.isChecked()) {
            mAdapter = ArrayAdapter.createFromResource(this,
                    R.array.USSpinner,//배열 가져온다
                    android.R.layout.simple_spinner_item);
            //etAddCategory.setAdapter(adapter);
            Toast.makeText(getApplicationContext(), rb2.toString(), Toast.LENGTH_SHORT).show();
        }


        //모델이 없다면, 즉 새로운 데이터를 입력한다면
        //버튼을 눌렀을 때 이 함수에 null,-l을 매개변수로 주는것을 볼 수 있다. null을 준 의미가 새로운 데이터를 생성하기 위함임
        //뷰를 띄우고 기다림
        if (model != null) {
            etAddCategory.setText(String.valueOf(model.getName()));//스피너와 연결!!
            etAddIncome.setText(String.valueOf(model.getPrice()));

        }
        mainDialog.setCancelable(false)//back키 설정 안함
                .setPositiveButton("Ok", null)//ok버튼 설정
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {// cancel버튼 설정
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = mainDialog.create();//다이얼 로그 객체 얻어오기
        dialog.show();// 다이얼로그 보여주기
        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);//ok버튼 누르게 된다면
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력칸이 비어있는지 확인하고 다 채워졌다면 데이터를 추가 or업데이트, 빈 칸이 있다면 채우라는 다이얼로그띄움
                // if (!Utility.isBlankField(etAddCategory) && !Utility.isBlankField(etAddIncome)) {
                if (!Utility.isBlankField(etAddIncome)) {
                    if (rb1.isChecked()) {
                        //String selItem = (String) etAddCategory.getSelectedItem();
                        DataDetailsModel dataDetailsModel = new DataDetailsModel();
                        dataDetailsModel.setName(etAddCategory.getText().toString());
                        dataDetailsModel.setPrice(Integer.parseInt(etAddIncome.getText().toString()));
                        dataDetailsModel.setDate(currnet_Date); //date추가
                        dataDetailsModel.setInOrOut(false);

                        Log.d("ee", dataDetailsModel.getDate().toString());

                        if (model == null)//데이터베이스를 새로 생성하겠다!!
                            addDataToRealm(dataDetailsModel);
                        else//기존에 있던 데이터를 업데이트하겠다!!
                            updatePersonDetails(dataDetailsModel, position, model.getId());
                        dialog.cancel();
                    } else if (rb2.isChecked()) {
                        //String selItem = (String) etAddCategory.getSelectedItem();
                        DataDetailsModel dataDetailsModel = new DataDetailsModel();
                        dataDetailsModel.setName(etAddCategory.getText().toString());
                        dataDetailsModel.setPrice(Integer.parseInt(etAddIncome.getText().toString()));
                        dataDetailsModel.setDate(currnet_Date); //date추가
                        dataDetailsModel.setInOrOut(true); //지출


                        dataDetailsAdapter.notifyDataSetChanged();

                        Log.d("eeeeeeeeeeeeeeeeeeee", dataDetailsModel.getDate().toString());

                        if (model == null) {//데이터베이스를 새로 생성하겠다!!
                            addDataToRealm(dataDetailsModel);
                        } else//기존에 있던 데이터를 업데이트하겠다!!
                            updatePersonDetails(dataDetailsModel, position, model.getId());
                        dialog.cancel();
                    }
                } else {//다이얼 로그가 비워져 있다면 이것을 보여줘!!
                    subDialog.show();
                }
            }
        });

    }// end addOrUpdate


    /* db삭제 */
    // 앱이 종료되었을  onCreate와 반대로 액티비티가 종료 될 때 onDestroy가 나타난다
    protected void onDestroy() {
        Log.e(LOG_TAG, "MainActivity.onDestroy");
        super.onDestroy();
        dataDetailsModelArrayList.clear();
        myRealm.close();
    }

    ///--------------------db함수 끝-------------------

    //푸시알림 설정
    public void NotificationSomethings(){
        Resources res=getResources();
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setContentTitle("일일설정액 초과!")
                .setContentText("그만 써!")
                .setTicker("일일 설정액 초과!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res,R.mipmap.ic_launcher))
                .setAutoCancel(true)
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

    //3d회전 구현 함수
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        switch (keyCode) {
            case KeyEvent.KEYCODE_SPACE:
                if (isFront) {
                    applyRotation(0f, 90f, 180f, 0f);
                } else {
                    applyRotation(180f, 270f, 360f, 0f);
                }
                break;
        }
        return false;
    }

    private void applyRotation(float start, float mid, float end, float depth) {
        this.centerX = mContainer.getWidth() / 2.0f;
        this.centerY = mContainer.getHeight() / 2.0f;

        Rotate3dAnimation rot = new Rotate3dAnimation(start, mid, centerX, centerY,
                depth, true);
        rot.setDuration(DURATION);
        // rot.setInterpolator(new AccelerateInterpolator());
        rot.setAnimationListener(new DisplayNextView(mid, end, depth));
        mContainer.startAnimation(rot);
    }

    private class DisplayNextView implements Animation.AnimationListener {
        private float mid;
        private float end;
        private float depth;

        public DisplayNextView(float mid, float end, float depth) {
            this.mid = mid;
            this.end = end;
            this.depth = depth;
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mContainer.post(new Runnable() {
                public void run() {
                    if (isFront) {
                        frontView.setVisibility(View.GONE);
                        backView.setVisibility(View.VISIBLE);
                        isFront = false;
                    } else {
                        frontView.setVisibility(View.VISIBLE);
                        backView.setVisibility(View.GONE);
                        isFront = true;
                    }

                    Rotate3dAnimation rot = new Rotate3dAnimation(mid, end, centerX,
                            centerY, depth, false);
                    rot.setDuration(DURATION);
                    rot.setInterpolator(new AccelerateInterpolator());
                    mContainer.startAnimation(rot);
                }
            });
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }
}// end MainActivity