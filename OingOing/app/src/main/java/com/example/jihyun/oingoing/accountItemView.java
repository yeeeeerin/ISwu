package com.example.jihyun.oingoing;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by samsung on 2017-05-27.
 */

public class accountItemView extends LinearLayout {
    TextView textview;
    TextView textview2;
    ImageView imageview;

    public accountItemView(Context context) {
        super(context);
        init(context);
    }

    public accountItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.account_item,this,true);
        textview=(TextView)findViewById(R.id.breakdownText);
        textview2=(TextView)findViewById(R.id.acountText);
        imageview=(ImageView)findViewById(R.id.categoryImage);
    }

    public void setBreakdown(String extendentureItem){
        textview.setText(extendentureItem);
    }
    public void setAccount(int account){
        textview2.setText(String.valueOf(account));
    }
    public void setImage(int resId){
        imageview.setImageResource(resId);
    }

}
