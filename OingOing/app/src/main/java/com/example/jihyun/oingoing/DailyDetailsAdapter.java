package com.example.jihyun.oingoing;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jihyun on 2017-07-09.
 */

public class DailyDetailsAdapter extends BaseAdapter {

    private ArrayList<DailyDetailsModel> dailyDetailsArrayList;
    private Context context;
    private LayoutInflater inflater;

    public DailyDetailsAdapter(Context context, ArrayList<DailyDetailsModel> dailyDetailsArrayList) {
        this.context = context;
        this.dailyDetailsArrayList = dailyDetailsArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dailyDetailsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dailyDetailsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder;
        if (v == null) {
            v = inflater.inflate(R.layout.inflate_list_item_2, null);
            holder = new Holder();
            holder.setMoney = (TextView) v.findViewById(R.id.setMoney);
            holder.startDate=(TextView) v.findViewById(R.id.startDate);
            holder.endDate=(TextView) v.findViewById(R.id.endDate);
            holder.ivDelete=(ImageView)v.findViewById(R.id.ivDelete);

            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }
        holder.setMoney.setText(""+dailyDetailsArrayList.get(position).getMoney_set());
        //holder.startDate.setText(dailyDetailsArrayList.get(position).getstartDate());
        //holder.endDate.setText(dailyDetailsArrayList.get(position).getEndDate());

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowConfirmDialog(context,dailyDetailsArrayList.get(position).getId(), position);
            }
        });

        return v;
    }
    class Holder {
        TextView setMoney, startDate, endDate;
        ImageView ivDelete;
    }
    public static void ShowConfirmDialog(Context context,final int personId,final int position)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage("정말 삭제하시겠습니까?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        DailyList.getInstance().deleteData(personId,position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
