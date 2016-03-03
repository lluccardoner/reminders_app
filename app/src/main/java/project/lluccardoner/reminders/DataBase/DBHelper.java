package project.lluccardoner.reminders.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lluc on 08/02/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    ///////////////////// NOT USED ///////////////////////////////////////////////////////////////

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DBHelper.db";

    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO Data base
        //db.execSQL(ReminderItem.ReminderItemEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
