package com.example.jihyun.oingoing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SMSReceiver extends BroadcastReceiver {
    public SMSReceiver() {
    }

    public SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages = null;
        String strMessage = "";
        String sender = "";
        String datee = "";

        if (myBundle != null) {
            Object[] pdus = (Object[]) myBundle.get("pdus");
            messages = new SmsMessage[pdus.length];

            for (int i = 0; i < messages.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                sender += messages[i].getOriginatingAddress();
                strMessage += messages[i].getMessageBody();
                Date date = new Date(messages[i].getTimestampMillis());
                datee = date.toString();
                Log.d("문자 내용", "수신시간" + date.toString() + "발신자 : " + sender + ", 내용 : " + strMessage);

            }
            Log.d("", "발신자 : " + sender);

            //뷰리스트에 추가
            Intent myIntent = new Intent(context, MainActivity.class);

            // 플래그를 이용해 추가..?
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            myIntent.putExtra("number", sender);
            myIntent.putExtra("message", strMessage);
            myIntent.putExtra("timestamp", datee);

            context.startActivity(myIntent);

            //카드내역수신문자번호(사용자가 임의 지정) -> 근데 이거 미납중이라던가 그런내용 구분할 필요 있을듯..
            if (sender.equals("01046175932")) {
                //지출내역에 등록
                Toast.makeText(context, "SMS From:" + sender + "\n" + strMessage, Toast.LENGTH_SHORT).show();


            }

        }
    }
}
