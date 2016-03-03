package project.lluccardoner.reminders.Model;

import android.content.Intent;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;

/**
 * Created by Lluc on 31/01/2016.
 */
public class ReminderItem extends RealmObject {

    //Priority LOW(1), MED(2), HIGH(3), NONE(0);
    //Status NOTDONE(0), DONE(1);
    //Alarm ON(1), OFF(0);

    public final static String NO_AUDIO = "no audio";
    public final static String NO_REPEAT = "never"; //not used yet

    public static final String ID = "Id";
    public final static String TITLE = "Title";
    public final static String PRIORITY = "Priority";
    public final static String STATUS = "Status";
    public final static String ALARM = "Alarm";
    public final static String DATE = "Date";
    public static final String AUDIO = "Audio";
    public final static String FILENAME = "AudioFilePath";

    public final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "dd/MM/yyyy HH:mm", Locale.US);

    private String Title = new String();
    private int Priority = 0;
    private int Status = 0;
    private int Alarm = 0;
    private Date Date = new Date();
    private String AudioFilePath;
    private int Id;

    public ReminderItem() {

    }

    public ReminderItem(int id, String title, int priority, int status, int alarm, Date date, String audioPath) {
        this.Id = id;
        this.Title = title;
        this.Priority = priority;
        this.Status = status;
        this.Alarm = alarm;
        this.Date = date;
        this.AudioFilePath = audioPath;
    }

    public ReminderItem(Intent intent) {
        Id = intent.getIntExtra(ReminderItem.ID,-1);
        if(Id==-1){
            Log.e("ReminderItem", "wrong id");
        }
        Title = intent.getStringExtra(ReminderItem.TITLE);
        Priority = intent.getIntExtra(ReminderItem.PRIORITY,0);
        Status = intent.getIntExtra(ReminderItem.STATUS,0);
        Alarm = intent.getIntExtra(ReminderItem.ALARM,0);

        //todo change
        try {
            String date = intent.getStringExtra(ReminderItem.DATE);
            if (1== Alarm /*ON*/) {
                Date = ReminderItem.FORMAT.parse(date);
            } else {
                Date = null;
            }

        } catch (ParseException e) {
            Date = null;
        }
        AudioFilePath = intent.getStringExtra(ReminderItem.AUDIO);
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public int getPriority() {
        return Priority;
    }

    public void setPriority(int priority) {
        this.Priority = priority;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        this.Status = status;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        this.Date = date;
    }

    public String getAudioFilePath() {
        return AudioFilePath;
    }

    public void setAudioFilePath(String audioFilePath) {
        this.AudioFilePath = audioFilePath;
    }

    public int getAlarm() {
        return Alarm;
    }

    public void setAlarm(int alarm) {
        this.Alarm = alarm;
    }

    public static void packageIntent(Intent intent, int id, String title, int priority, int status, int alarm, String date, String audioPath) {

        intent.putExtra(ReminderItem.ID,id);
        intent.putExtra(ReminderItem.TITLE, title);
        intent.putExtra(ReminderItem.PRIORITY, priority);
        intent.putExtra(ReminderItem.STATUS, status);
        intent.putExtra(ReminderItem.ALARM, alarm);
        intent.putExtra(ReminderItem.DATE, date);
        intent.putExtra(ReminderItem.AUDIO, audioPath);
    }


    /////////// NOT USED //////////////////////////////////////////////////////////////////////////////
    /*public static abstract class ReminderItemEntry implements BaseColumns {

        public static final String TABLE_NAME = "reminders";

        public static final String COLUM1_NAME_ID = "Id";
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
