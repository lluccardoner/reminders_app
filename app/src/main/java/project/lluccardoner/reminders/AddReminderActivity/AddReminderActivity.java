package project.lluccardoner.reminders.AddReminderActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import project.lluccardoner.reminders.MainActivity.MainActivity;
import project.lluccardoner.reminders.MainActivity.ReminderItem;

import project.lluccardoner.reminders.R;

/**
 * Created by Lluc on 31/01/2016.
 */
public class AddReminderActivity extends AppCompatActivity {

    /**
     * Set alarm
     * Set audio
     */

    //ar_main.xml
    private FloatingActionButton doneFabButton;
    private Toolbar arToolbar;
    private ImageView backArrow;

    //ar_content.xml
    //Title
    private EditText titleEditText;
    //Set alarm
    private SwitchCompat alarmSwitch;
    private RelativeLayout dateTimeLayout;
    private TextView dateView;
    private TextView timeView;
    private RelativeLayout repeatLayout;
    private TextView repeatView;
    private ImageView repeatArrow;
    private TextView never, dayly, weekly;
    //Set priority
    private SwitchCompat prioritySwitch;
    private RelativeLayout priorityLayout;
    private RadioGroup priorityGroup;
    private AppCompatRadioButton low, med, high;
    //Set audio
    private SwitchCompat audioSwitch;
    private RelativeLayout audioLayout;
    private TextView audioView;
    private ImageView record;
    private String audioPath = ReminderItem.NO_AUDIO;
    private MyRecorder recorder;
    private ImageView play, stop, clear;

    //attr
    private static String dateString;
    private static String timeString;
    private boolean remindChecked, priorityChecked, audioChecked;
    private Date mDate;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ar_main);
        arToolbar = (Toolbar) findViewById(R.id.ar_toolbar);
        setSupportActionBar(arToolbar);
        Bundle b = getIntent().getExtras();
        id = b.getInt(ReminderItem.ID);
        audioPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio_reminder_" + id;
        recorder = new MyRecorder(audioPath);
        initializeViews();
        setDefaultDateTime();
        initializeListeners();


    }


    private void initializeViews() {

        //ar_main.xml views
        doneFabButton = (FloatingActionButton) findViewById(R.id.ar_fab);
        backArrow = (ImageView) findViewById(R.id.ar_back_arrow);

        //ar_content.xml views
        //Title
        titleEditText = (EditText) findViewById(R.id.ar_titleEditText);
        //Set alarm
        alarmSwitch = (SwitchCompat) findViewById(R.id.ar_switch_setalarm);
        dateTimeLayout = (RelativeLayout) findViewById(R.id.ar_rl_datetime);
        dateTimeLayout.setVisibility(View.GONE);
        dateView = (TextView) findViewById(R.id.ar_date);
        timeView = (TextView) findViewById(R.id.ar_time);
        repeatLayout = (RelativeLayout) findViewById(R.id.ar_rl_repeat);
        repeatLayout.setVisibility(View.GONE);
        repeatView = (TextView) findViewById(R.id.ar_repeatView);
        repeatArrow = (ImageView) findViewById(R.id.ar_repeat_arrow);
        never = (TextView) findViewById(R.id.ar_repeat_never);
        never.setVisibility(View.GONE);
        dayly = (TextView) findViewById(R.id.ar_repeat_dayly);
        dayly.setVisibility(View.GONE);
        weekly = (TextView) findViewById(R.id.ar_repeat_weekly);
        weekly.setVisibility(View.GONE);
        //Set priority
        prioritySwitch = (SwitchCompat) findViewById(R.id.ar_switch_setpriority);
        priorityLayout = (RelativeLayout) findViewById(R.id.ar_rl_priority);
        priorityLayout.setVisibility(View.GONE);
        priorityGroup = (RadioGroup) findViewById(R.id.ar_rb_priority);
        low = (AppCompatRadioButton) findViewById(R.id.ar_rb_lowpriority);
        med = (AppCompatRadioButton) findViewById(R.id.ar_rb_medpriority);
        high = (AppCompatRadioButton) findViewById(R.id.ar_rb_highpriority);
        //Set audio
        audioSwitch = (SwitchCompat) findViewById(R.id.ar_switch_setaudio);
        audioView = (TextView) findViewById(R.id.ar_record);
        record = (ImageView) findViewById(R.id.ar_audio_record);
        audioLayout = (RelativeLayout) findViewById(R.id.ar_rl_audio);
        audioLayout.setVisibility(View.GONE);
        play = (ImageView) findViewById(R.id.ar_audio_play);
        play.setVisibility(View.GONE);
        stop = (ImageView) findViewById(R.id.ar_audio_stop);
        stop.setVisibility(View.GONE);
        clear = (ImageView) findViewById(R.id.ar_audio_clear);
        clear.setVisibility(View.GONE);

    }

    private void initializeListeners() {

        doneFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleString = getReminderTitle();
                String fullDate = getFullDate();
                int alarm = getAlarmStatus();
                int priority = getPriority();
                int status = getStatus();
                String fileName = getFileName();
                Intent data = new Intent();
                //TODO audio path
                ReminderItem.packageIntent(data, titleString, priority, status, alarm, fullDate, fileName);
                setResult(RESULT_OK, data);
                finish();
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dateTimeLayout.setVisibility(View.VISIBLE);
                    repeatLayout.setVisibility(View.VISIBLE);
                } else {
                    dateTimeLayout.setVisibility(View.GONE);
                    repeatLayout.setVisibility(View.GONE);
                }
            }
        });
        dateTimeLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO setAlarm
            }
        });
        repeatArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO setRepeat

                repeatArrow.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
                repeatView.setVisibility(View.GONE);
                never.setVisibility(View.VISIBLE);
                dayly.setVisibility(View.VISIBLE);
                weekly.setVisibility(View.VISIBLE);
            }
        });

        never.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeatArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                repeatView.setText(R.string.ar_repeat_never);
                repeatView.setVisibility(View.VISIBLE);
                never.setVisibility(View.GONE);
                dayly.setVisibility(View.GONE);
                weekly.setVisibility(View.GONE);
            }
        });
        dayly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeatArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                repeatView.setText(R.string.ar_repeat_dayly);
                repeatView.setVisibility(View.VISIBLE);
                never.setVisibility(View.GONE);
                dayly.setVisibility(View.GONE);
                weekly.setVisibility(View.GONE);
            }
        });
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeatArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                repeatView.setText(R.string.ar_repeat_weekly);
                repeatView.setVisibility(View.VISIBLE);
                never.setVisibility(View.GONE);
                dayly.setVisibility(View.GONE);
                weekly.setVisibility(View.GONE);
            }
        });


        prioritySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    priorityLayout.setVisibility(View.VISIBLE);
                } else {
                    priorityLayout.setVisibility(View.GONE);
                }
            }
        });

        priorityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ar_rb_lowpriority:
                        low.setTextColor(Color.RED);
                        med.setTextColor(Color.BLACK);
                        high.setTextColor(Color.BLACK);
                        break;
                    case R.id.ar_rb_medpriority:
                        low.setTextColor(Color.BLACK);
                        med.setTextColor(Color.RED);
                        high.setTextColor(Color.BLACK);
                        break;
                    case R.id.ar_rb_highpriority:
                        low.setTextColor(Color.BLACK);
                        med.setTextColor(Color.BLACK);
                        high.setTextColor(Color.RED);
                        break;
                }
            }
        });


        audioSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    audioLayout.setVisibility(View.VISIBLE);
                } else {
                    audioLayout.setVisibility(View.GONE);
                }
            }
        });

        record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //TODO record audio
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        //TODO hold
                        audioView.setText(R.string.ar_recording);
                        if(recorder.isRecording()){
                            recorder.onRecord(false);
                        }
                        recorder.onRecord(true);
                        Log.d("Add audio", "Action down");

                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        if(recorder.isRecording()) {
                            audioView.setText(R.string.ar_recorded);
                            recorder.onRecord(false);
                            record.setVisibility(View.GONE);
                            play.setVisibility(View.VISIBLE);
                            stop.setVisibility(View.VISIBLE);
                            clear.setVisibility(View.VISIBLE);
                        }
                        else{
                            audioView.setText(R.string.ar_record);
                        }
                        Log.d("Add audio", "Action up");

                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_OUTSIDE:
                    case MotionEvent.ACTION_CANCEL:
                        audioView.setText(R.string.ar_record);
                        if(recorder.isRecording()) {
                            recorder.onRecord(false);
                        }
                        Log.d("Add audio", "Action outside or canceled");
                        return true;
                }
                return false;
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioView.setText(R.string.ar_playing);
                if(!recorder.isPlaying()) {
                    recorder.onPlay(true);
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioView.setText(R.string.ar_stoped);
                if(recorder.isPlaying()) {
                    recorder.onPlay(false);
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recorder.isPlaying()) {
                    recorder.onPlay(false);
                }
                audioView.setText(R.string.ar_record);
                play.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
                clear.setVisibility(View.GONE);
                record.setVisibility(View.VISIBLE);
            }
        });
    }

    private String getFileName() {
        if(audioSwitch.isChecked()){
            return recorder.getFileName();
        }
        else {
            return ReminderItem.NO_AUDIO;
        }
    }

    private int getAlarmStatus() {
        if (alarmSwitch.isChecked()) {
            return 1; //Alarm ON
        } else {
            return 0; //Alarm OFF
        }
    }

    private void setDefaultDateTime() {

        mDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(mDate);

        setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));

        dateView.setText(dateString);

        setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                c.get(Calendar.MILLISECOND));

        timeView.setText(timeString);
    }

    private static void setDateString(int year, int monthOfYear, int dayOfMonth) {

        // Increment monthOfYear for Calendar/Date -> Time Format setting
        monthOfYear++;
        dateString = dayOfMonth + "/" + monthOfYear + "/" + year;
    }

    private static void setTimeString(int hourOfDay, int minute, int mili) {
        String hour = "" + hourOfDay;
        String min = "" + minute;

        if (hourOfDay < 10)
            hour = "0" + hourOfDay;
        if (minute < 10)
            min = "0" + minute;

        timeString = hour + ":" + min;
    }

    private int getPriority() {
        if (prioritySwitch.isChecked()) {
            switch (priorityGroup.getCheckedRadioButtonId()) {
                case R.id.ar_rb_lowpriority: {
                    return 1; //Priority.LOW
                }
                case R.id.ar_rb_medpriority: {
                    return 2; //Priority.MED
                }
                case R.id.ar_rb_highpriority: {
                    return 3; //Priority.HIGH
                }
                default:
                    return 0; //Priority.NONE
            }
        } else {
            return 0; //Priority.NONE
        }
    }

    private int getStatus() {

        return 0;
    }

    private String getReminderTitle() {
        return titleEditText.getText().toString();
    }

    private String getFullDate() {
        if (alarmSwitch.isChecked()) {
            return dateString + " " + timeString;
        } else {
            return " ";
        }

    }

    /*public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setDateString(year, monthOfYear, dayOfMonth);
            dateView.setText(dateString);
        }

    }*/

    /*public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            setTimeString(hourOfDay, minute, 0);

            timeView.setText(timeString);
        }
    }*/

    /*private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }*/
}
