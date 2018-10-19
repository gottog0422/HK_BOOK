package com.example.sh.hk_book.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.sh.hk_book.data.Content;
import com.example.sh.hk_book.db.DB_Manager;

import java.util.Calendar;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");

            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for (int i = 0; i < messages.length; i++) {
                String format = bundle.getString("format");
                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i], format);
            }

            String message = smsMessage[0].getMessageBody().toString();

            if (message.contains("현대카드 M2")) {
                String[] tmpStr = message.split("\n");
                String[] tmpPrice = tmpStr[2].split("원");
                String price = tmpPrice[0];
                price = price.replace(",", "");
                Integer i_price = Integer.parseInt(price);

                Calendar calendar = Calendar.getInstance();
                Integer year = calendar.get(Calendar.YEAR);
                Integer month = calendar.get(Calendar.MONTH) + 1;
                Integer day = calendar.get(Calendar.DAY_OF_MONTH);

                DB_Manager dbManager = new DB_Manager(context, "household.db", null, 1);
                dbManager.insertData(i_price, Content.EXPENSE, year, month, day);
            } else if (message.contains("신한체크승인")) {
                String str_pirce = "(금액)";
                int io_str_price = message.indexOf(str_pirce);

                String price = message.substring(io_str_price + str_pirce.length(), message.substring(io_str_price).indexOf("원") + io_str_price);

                price = price.replace(",", "");
                Integer i_price = Integer.parseInt(price);

                Calendar calendar = Calendar.getInstance();
                Integer year = calendar.get(Calendar.YEAR);
                Integer month = calendar.get(Calendar.MONTH) + 1;
                Integer day = calendar.get(Calendar.DAY_OF_MONTH);

                DB_Manager dbManager = new DB_Manager(context, "household.db", null, 1);
                dbManager.insertData(i_price, Content.EXPENSE, year, month, day);
            }
        }
    }
}
