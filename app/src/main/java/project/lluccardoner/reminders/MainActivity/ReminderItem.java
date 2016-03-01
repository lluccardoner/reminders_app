package project.lluccardoner.reminders.MainActivity;

import android.content.Intent;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Lluc on 31/01/2016.
 */
public class ReminderItem extends RealmObject {

    //Priority LOW(1), MED(2), HIGH(3), NONE(0);
    //Status NOTDONE(0), DONE(1);
    //Alarm ON(1), OFF(0);

    public final static String NO_AUDIO = "no audio";
    public final static String NO_REPEAT = "never";

    public static final String ID = "id";
    public final static String TITLE = "title";
    public final static String PRIORITY = "priority";
    public final static String STATUS = "status";
    public final static String ALARM = "alarm";
    public final static String DATE = "date";
    public static final String AUDIO = "audio";

    public final static String FILENAME = "filename";

    public final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "dd/MM/yyyy HH:mm", Locale.US);

    private String mTitle = new String();
    private int mPriority = 0;
    private int mStatus = 0;
    private int mAlarm = 0;
    private Date mDate = new Date();
    private String mAudioFilePath;
    private int id;

    public ReminderItem() {

    }

    public ReminderItem(int id, String title, int priority, int status, int alarm, Date date, String audioPath) {
        this.id = id;
        this.mTitle = title;
        this.mPriority = priority;
        this.mStatus = status;
        this.mAlarm = alarm;
        this.mDate = date;
        this.mAudioFilePath = audioPath;
    }

    public ReminderItem(Intent intent) {
        mTitle = intent.getStringExtra(ReminderItem.TITLE);
        mPriority = intent.getIntExtra(ReminderItem.PRIORITY,0);
        mStatus = intent.getIntExtra(ReminderItem.STATUS,0);
        mAlarm = intent.getIntExtra(ReminderItem.ALARM,0);

        try {
            String date = intent.getStringExtra(ReminderItem.DATE);
            if (1==mAlarm /*ON*/) {
                mDate = ReminderItem.FORMAT.parse(date);
            } else {
                mDate = null;
            }

        } catch (ParseException e) {
            mDate = null;
        }
        mAudioFilePath = intent.getStringExtra(ReminderItem.AUDIO);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getmPriority() {
        return mPriority;
    }

    public void setmPriority(int mPriority) {
        this.mPriority = mPriority;
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getmAudioFilePath() {
        return mAudioFilePath;
    }

    public void setmAudioFilePath(String mAudioFilePath) {
        this.mAudioFilePath = mAudioFilePath;
    }

    public int getmAlarm() {
        return mAlarm;
    }

    public void setmAlarm(int mAlarm) {
        this.mAlarm = mAlarm;
    }

    public static void packageIntent(Intent intent, String title, int priority, int status, int alarm, String date, String audioPath) {

        intent.putExtra(ReminderItem.TITLE, title);
        intent.putExtra(ReminderItem.PRIORITY, priority);
        intent.putExtra(ReminderItem.STATUS, status);
        intent.putExtra(ReminderItem.ALARM, alarm);
        intent.putExtra(ReminderItem.DATE, date);
        intent.putExtra(ReminderItem.AUDIO, audioPath);
    }


    /*public static abstract class ReminderItemEntry implements BaseColumns {

        public static final String TABLE_NAME = "reminders";

        public static final String COLUM1_NAME_ID = "id";
        public static final String COLUM2_NAME_TITLE = "title";
        public static final String COLUM3_NAME_PRIORITY = "priority";
        public static final String COLUM4_NAME_STATUS = "status";
        public static final String COLUM5_NAME_ALARM = "alarm";
        public static final String COLUM6_NAME_DATETIME = "dateTime";
        public static final String COLUM7_NAME_AUDIO = "audio";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUM1_NAME_ID + " INTEGER PRIMARY KEY, " +
                        COLUM2_NAME_TITLE + " TEXT, " +
                        COLUM3_NAME_PRIORITY + " INTEGER, " +
                        COLUM4_NAME_STATUS + " INTEGER, " +
                        COLUM5_NAME_ALARM + " INTEGER, " +
                        COLUM6_NAME_DATETIME + " TEXT, " +
                        COLUM7_NAME_AUDIO + " TEXT " +
                        " )";


        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }*/

}
