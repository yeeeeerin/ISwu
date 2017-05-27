package com.example.jihyun.oingoing;

import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TabHost tabHost;
    final static String LOG_TAG = "myLogs";
    private static int id = 1;
    //private FloatingActionButton fabAddPerson;
    FloatingActionButton fab1, fab2, fab3, fab4;

    private Realm myRealm;
    private ListView lvPersonNameList;
    private static ArrayList<DataDetailsModel> dataDetailsModelArrayList = new ArrayList<>();
    private DataDetailsAdapter dataDetailsAdapter;
    private AlertDialog.Builder subDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "MainActivity.OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myRealm = Realm.getInstance(MainActivity.this);
        lvPersonNameList = (ListView) findViewById(R.id.lvPersonNameList);
        dataDetailsAdapter = new DataDetailsAdapter(MainActivity.this, dataDetailsModelArrayList);
        getAllWidgets();
        bindWidgetsWithEvents();
        getAllUsers();
        tabHost=(TabHost)findViewById(R.id.tabHost);

        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("").setContent(R.id.tabMonth).setIndicator("월별"));
        tabHost.addTab(tabHost.newTabSpec("").setContent(R.id.tabWeek).setIndicator("주별"));
        tabHost.addTab(tabHost.newTabSpec("").setContent(R.id.tabDay).setIndicator("일별"));

        tabHost.setCurrentTab(0);

        ImageView addbtn=(ImageView) findViewById(R.id.addBtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DailyMoneySet.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });

        ImageView viewList=(ImageView) findViewById(R.id.viewList);
        viewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DataList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
//추가
        fab1 = (FloatingActionButton)findViewById(R.id.fab_1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab_2);
        fab4 = (FloatingActionButton)findViewById(R.id.fab_4);
        fab3 = (FloatingActionButton)findViewById(R.id.fab_3);

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleFab();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrUpdatePersonDetailsDialog(null,-1);
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateSpend.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }

    private void ToggleFab() {
        // 버튼들이 보여지고있는 상태인 경우 숨겨줍니다.
        if(fab1.getVisibility() == View.VISIBLE) {
            fab1.hide();
            fab2.hide();
            fab4.hide();
            fab1.animate().translationY(0);
            fab2.animate().translationY(0);
            fab4.animate().translationY(0);
        }
        // 버튼들이 숨겨져있는 상태인 경우 위로 올라오면서 보여줍니다.
        else {
            // 중심이 되는 버튼의 높이 + 마진 만큼 거리를 계산합니다.
            int dy = fab3.getHeight() + 20;
            fab1.show();
            fab2.show();
            fab4.show();
            // 계산된 거리만큼 이동하는 애니메이션을 입력합니다.
            fab4.animate().translationY(-dy*3);
            fab1.animate().translationY(-dy*2);
            fab2.animate().translationY(-dy);
        }
    }

    private void getAllWidgets() {
        Log.e(LOG_TAG, "MainActivity.getAllWidgets");
        //fabAddPerson = (FloatingActionButton) findViewById(R.id.fab);
        fab2 = (FloatingActionButton)findViewById(R.id.fab_2);
        lvPersonNameList = (ListView) findViewById(R.id.lvPersonNameList);
    }
    private void bindWidgetsWithEvents() {
        Log.e(LOG_TAG, "MainActivity.bindWidgetsWithEvents");
        //fabAddPerson.setOnClickListener(this);
        fab2.setOnClickListener(this);

    }

   //수정
    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.fab_2:
//                addOrUpdatePersonDetailsDialog(null,-1);
//                break;
//            case R.id.fab_1:
//                Toast.makeText(MainActivity.this, "영수증인식", Toast.LENGTH_SHORT).show();
//                //Intent intent = new Intent(getApplicationContext(), UpdateSpend.class);
//                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                //getApplicationContext().startActivity(intent);
//                break;
//
 //       }
    }

    public void addOrUpdatePersonDetailsDialog(final DataDetailsModel model,final int position) {
//subdialog
        Log.e(LOG_TAG, "MainActivity.addOrUpdatePersonDetailsDialog");
        subDialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage("모두 입력해주세요")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg2, int which) {
                        dlg2.cancel();
                    }
                });
//maindialog
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.income_dialog, null);
        AlertDialog.Builder mainDialog = new AlertDialog.Builder(MainActivity.this);
        mainDialog.setView(promptsView);
        final EditText etAddCategory = (EditText) promptsView.findViewById(R.id.setCategory);
        final EditText etAddIncome = (EditText) promptsView.findViewById(R.id.setIncome);
        if (model != null) {
            etAddCategory.setText(model.getName());
            etAddIncome.setText(String.valueOf(model.getPrice()));
        }
        mainDialog.setCancelable(false)
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog dialog = mainDialog.create();
        dialog.show();
        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utility.isBlankField(etAddCategory) && !Utility.isBlankField(etAddIncome)) {
                    DataDetailsModel dataDetailsModel = new DataDetailsModel();
                    dataDetailsModel.setName(etAddCategory.getText().toString());
                    dataDetailsModel.setPrice(Integer.parseInt(etAddIncome.getText().toString()));
                    if (model == null)  //이부분 이상이상
                        addDataToRealm(dataDetailsModel);
                    else
                        updatePersonDetails(dataDetailsModel, position, model.getId());
                    dialog.cancel();
                } else {
                    subDialog.show();
                }
            }
        });
    }
    private void addDataToRealm(DataDetailsModel model) {
        Log.e(LOG_TAG, "MainActivity.addDataToRealm");
        myRealm.beginTransaction();
        DataDetailsModel dataDetailsModel = myRealm.createObject(DataDetailsModel.class);
        dataDetailsModel.setId(id);
        dataDetailsModel.setName(model.getName());
        dataDetailsModel.setPrice(model.getPrice());
        dataDetailsModelArrayList.add(dataDetailsModel);
        myRealm.commitTransaction();
        dataDetailsAdapter.notifyDataSetChanged();
        id++;
    }

    public void updatePersonDetails(DataDetailsModel model,int position,int personID) {
        Log.e(LOG_TAG, "MainActivity.updatePersonDetails");
        DataDetailsModel editPersonDetails = myRealm.where(DataDetailsModel.class).equalTo("id", personID).findFirst();
        myRealm.beginTransaction();
        editPersonDetails.setName(model.getName());
        editPersonDetails.setPrice(model.getPrice());
        myRealm.commitTransaction();
        dataDetailsModelArrayList.set(position, editPersonDetails);
        dataDetailsAdapter.notifyDataSetChanged();
    }
    private void getAllUsers() {
        Log.e(LOG_TAG, "MainActivity.getAllUsers");
        RealmResults<DataDetailsModel> results = myRealm.where(DataDetailsModel.class).findAll();
        myRealm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            dataDetailsModelArrayList.add(results.get(i));
        }
        if(results.size()>0)
            id = myRealm.where(DataDetailsModel.class).max("id").intValue() + 1;
        myRealm.commitTransaction();
        dataDetailsAdapter.notifyDataSetChanged();
    }
    // db삭제

}