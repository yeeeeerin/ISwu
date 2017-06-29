package com.example.jihyun.oingoing;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;


public class DailyAdapter extends BaseAdapter {

    ArrayList<accountItem> items = new ArrayList<accountItem>();
    Context context;

    public DailyAdapter(Context context) {
        this.context = context;
    }

    public void addAdapter(accountItem item){
        items.add(item);
    }

    // 데이터 개수
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // position몇번째 뷰
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        accountItemView view=new accountItemView(context.getApplicationContext());
        accountItem item =items.get(position);
        view.setBreakdown(item.getExpenditureItem());
        view.setAccount(item.getAccount());
        view.setImage(item.getResid());

        return view;
    }



}
