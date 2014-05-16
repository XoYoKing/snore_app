package com.mobilex.demo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import java.util.Calendar;

public class SnoreAlarmManager {

    public static void setSnoringAlarm(Activity context) {
        Calendar today = Calendar.getInstance();
        Calendar calendar = SnoreData.getInstance().getCalendar();
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        Intent intent = new Intent(context, SnoreActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("Start_Service", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);
        //	PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pendingIntent);
//        String message = "Snore records in "
//                + (calendar.get(Calendar.HOUR) - today.get(Calendar.HOUR))
//                + "(hr) and "
//                + (calendar.get(Calendar.MINUTE) - today.get(Calendar.MINUTE))
//                + "(mins)";
//        Log.d("CHINEDU", "Message:" + message);


        //CustomToast.makeText(context, message).show();
        int hr = calendar.get(Calendar.HOUR) - today.get(Calendar.HOUR);
        int min = (calendar.get(Calendar.MINUTE) - today.get(Calendar.MINUTE));
        SnoreData.getInstance().setHourValue(hr);
        SnoreData.getInstance().setMinuteValue(min);
//        return Html.fromHtml(String.format(context.getString(R.string.message),
//                SnoreData.getInstance().getHourValue(), SnoreData.getInstance().getMinuteValue()));
        //return message;
    }

}
