package project.lluccardoner.reminders.AddReminderActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lluc on 10/02/2016.
 */
public class MyAlarmManager extends BroadcastReceiver {

    ///////////////////// NOT IMPLEMENTED YET /////////////////////////////////////////////////////

    private ArrayList<PendingIntent> alarmList;
    public static final String REQUEST_CODE = "requestCode";
    public static final String REPEAT = "repeat";
    public static final int REPEAT_NEVER = 0;
    public static final int REPEAT_DAYLY = 1;
    public static final int REPEAT_WEEKLY = 2;
    public static final int REPEAT_MONTHLY = 3;

    private static final long DAY_MILL = 86400000; //day in milliseconds
    private static final long WEEK_MILL = 604800000; //week in milliseconds
    //private static final long MONTH_MILL = 2629746000; //month in milliseconds



    public MyAlarmManager() {
        this.alarmList = new ArrayList<>();
    }

    //TODO alarm manager

    @Override
    public void onReceive(Context context, Intent intent) {

        /*PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        // Put here YOUR code.
        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example

        wl.release();*/
    }

    public void setAlarm(Context context,int requestCode, Date dueDate, int repeat)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, MyAlarmManager.class);
        intent.putExtra(REQUEST_CODE, requestCode);
        intent.putExtra(REPEAT, repeat);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        alarmList.add(pendingIntent);

        switch (repeat){
            case REPEAT_NEVER:
                break;
            case REPEAT_DAYLY:
                am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), DAY_MILL, pendingIntent); // Millisec * Second * Minute
                break;
            case REPEAT_WEEKLY:
                am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), WEEK_MILL, pendingIntent); // Millisec * Second * Minute
                break;
            default:
                break;

        }

    }

    public void cancelAlarm(Context context, int requestCode)
    {
        Intent intent = new Intent(context, MyAlarmManager.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        alarmList.remove(sender);
    }

}
