package project.lluccardoner.reminders.MainActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import project.lluccardoner.reminders.AddReminderActivity.AddReminderActivity;
import project.lluccardoner.reminders.DataBase.DBHelper;
import project.lluccardoner.reminders.R;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_REMINDER_ITEM_REQUEST = 0;
    private static final String ADD_REMINDER_ACTION = "ADD_REMINDER";
    private static final String TAG = "MainActivity";
    //private static final String FILE_NAME = "ReminderActivityData.txt";
    private int id = 0;
    private Realm realm;

    /*ma_main.xml*/
    private Toolbar toolbar;
    private FloatingActionButton addReminderButton;

    /*ma_content.xml*/
    private RecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ma_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeDB();
        initializeItems();
        initializeViews();
        initializeListeners();

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(this, realm);
        recyclerView.setAdapter(mAdapter);

    }

    private void initializeDB() {
        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        // Get a Realm instance for this thread
        realm = Realm.getInstance(realmConfig);
    }

    private void initializeItems(){
        if(mAdapter.getItemCount()==0){
            RealmResults<ReminderItem> results = realm.allObjects(ReminderItem.class);
            mAdapter.addList(results);
        }
    }



    private void initializeViews() {
        recyclerView = (RecyclerView) findViewById(R.id.ma_recycler_view);
        addReminderButton = (FloatingActionButton) findViewById(R.id.fab);

    }

    private void initializeListeners() {
        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addReminderIntent = new Intent(MainActivity.this, AddReminderActivity.class);
                Bundle b = new Bundle();
                //TODO id
                id++;
                b.putInt(ReminderItem.ID, id);
                addReminderIntent.putExtras(b);
                addReminderIntent.setAction(ADD_REMINDER_ACTION);
                startActivityForResult(addReminderIntent, ADD_REMINDER_ITEM_REQUEST);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.menu_action_delete_all) {
            mAdapter.clear();
        }
        if (id == R.id.menu_action_delete_done) {
            mAdapter.deleteDone();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADD_REMINDER_ITEM_REQUEST && resultCode == RESULT_OK) {
            ReminderItem item = new ReminderItem(data);
            mAdapter.add(item, id);

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter.getItemCount() == 0) {
            //loadItems();
            //loadItemsFromDB();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //saveItems();
        //saveItemsToDB();

    }
///////////////////////////////////////////////// NOT USED

    /*private void loadItems() {
        BufferedReader reader = null;
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(fis));

            String title = null;
            String priority = null;
            String status = null;
            String alarm = null;
            Date date = null;

            while (null != (title = reader.readLine())) {
                priority = reader.readLine();
                status = reader.readLine();
                alarm = reader.readLine();
                date = ReminderItem.FORMAT.parse(reader.readLine());
                mAdapter.add(new ReminderItem(title, Priority.valueOf(priority),
                        ReminderItem.Status.valueOf(status), ReminderItem.Alarm.valueOf(alarm), date));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveItems() {
        PrintWriter writer = null;
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    fos)));

            for (int idx = 0; idx < mAdapter.getItemCount(); idx++) {

                writer.println(mAdapter.getItem(idx));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }

    private void saveItemsToDB()
    {
        Log.d("Main Activity", "Save items");
        DBHelper mDbHelper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        for (int i = 0; i < mAdapter.getItemCount(); i++) {

            ReminderItem item = (ReminderItem) mAdapter.getItem(i);
            ContentValues contentValues = new ContentValues();

            contentValues.put(ReminderItem.ReminderItemEntry.COLUM1_NAME_ID, item.getId());
            contentValues.put(ReminderItem.ReminderItemEntry.COLUM2_NAME_TITLE, item.getmTitle());
            contentValues.put(ReminderItem.ReminderItemEntry.COLUM3_NAME_PRIORITY, item.getmPriority().ordinal());
            contentValues.put(ReminderItem.ReminderItemEntry.COLUM4_NAME_STATUS, item.getmStatus().ordinal());
            contentValues.put(ReminderItem.ReminderItemEntry.COLUM5_NAME_ALARM, item.getmAlarm().ordinal());
            contentValues.put(ReminderItem.ReminderItemEntry.COLUM6_NAME_DATETIME, item.getmDate().toString());
            contentValues.put(ReminderItem.ReminderItemEntry.COLUM7_NAME_AUDIO, item.getmAudioFilePath());


            try {
                db.insertOrThrow(ReminderItem.ReminderItemEntry.TABLE_NAME, null, contentValues);
            } catch (SQLiteConstraintException e) {
                Log.e("Main Activity", "Id item already exists");
            }


        }


    }

    private void loadItemsFromDB() {
        Log.d("Main Activity", "Loadig items");
        DBHelper perDB = new DBHelper(MainActivity.this);
        SQLiteDatabase db = perDB.getReadableDatabase();

        Log.d("Main Activity", ReminderItem.ReminderItemEntry.SQL_CREATE_ENTRIES);
        String[] projection = new String[]{
                ReminderItem.ReminderItemEntry.COLUM1_NAME_ID,
                ReminderItem.ReminderItemEntry.COLUM2_NAME_TITLE,
                ReminderItem.ReminderItemEntry.COLUM3_NAME_PRIORITY,
                ReminderItem.ReminderItemEntry.COLUM4_NAME_STATUS,
                ReminderItem.ReminderItemEntry.COLUM5_NAME_ALARM,
                ReminderItem.ReminderItemEntry.COLUM6_NAME_DATETIME,
                ReminderItem.ReminderItemEntry.COLUM7_NAME_AUDIO

        };
        Cursor cursor = db.query(
                ReminderItem.ReminderItemEntry.TABLE_NAME,//table name
                projection,//the colum to return
                null,// the colums for the WHERE clause
                null,// the values for the WHERE clause
                null,//don't group the rows
                null,//don't filter by row groups
                null// the sort order

        );
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                Priority priority = Priority.NONE;
                int i = cursor.getInt(2);
                if (Priority.LOW.ordinal() == i) {
                    priority = Priority.LOW;
                } else if (Priority.MED.ordinal() == i) {
                    priority = Priority.MED;
                } else if (Priority.HIGH.ordinal() == i) {
                    priority = Priority.HIGH;
                }

                ReminderItem.Status status = ReminderItem.Status.NOTDONE;
                if (ReminderItem.Status.DONE.ordinal() == cursor.getInt(3)) {
                    status = ReminderItem.Status.DONE;
                }
                ReminderItem.Alarm alarm = ReminderItem.Alarm.OFF;
                if (ReminderItem.Alarm.ON.ordinal() == cursor.getInt(4)) {
                    alarm = ReminderItem.Alarm.ON;
                }

                String date = cursor.getString(5);
                String audioPath = cursor.getString(6);

                mAdapter.add(new ReminderItem(id, title, priority, status, alarm, new Date(), audioPath), mAdapter.getItemCount());
                this.id = id;

            } while (cursor.moveToNext());
        }
    }*/
    
}
