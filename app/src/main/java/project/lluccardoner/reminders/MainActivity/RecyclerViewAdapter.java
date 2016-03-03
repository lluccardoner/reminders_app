package project.lluccardoner.reminders.MainActivity;

import android.content.Context;
import android.os.Environment;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import project.lluccardoner.reminders.AddReminderActivity.MyRecorder;


import project.lluccardoner.reminders.Model.ReminderItem;
import project.lluccardoner.reminders.R;

/**
 * Created by Lluc on 05/02/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final int ONE_DAY = 86400000;//milliseconds

    private Context context;

    //Audio recorder and audio player
    private MyRecorder recorder;

    //Slide touch gesture
    private int DELTA = 100;
    private float historicX = 0;
    private float historicY = 0;

    //realm data base
    private RealmResults<ReminderItem> itemList;
    private Realm realm;

    //holder for the recycler view
    private RecyclerViewAdapter.ViewHolder holder;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        //ma_reminder_item.xml
        public RelativeLayout itemLayout;
        public TextView itemTitle, itemDateTime, itemPriority;
        public AppCompatCheckBox itemStatus;
        public ImageView itemPlay, itemStop;
        private ImageView itemDelete;
        private ImageView itemEdit;
        private RelativeLayout itemElements;

        public ViewHolder(View v) {
            super(v);
            itemLayout = (RelativeLayout) v.findViewById(R.id.ri_layout);
            itemTitle = (TextView) v.findViewById(R.id.ri_title);
            itemDateTime = (TextView) v.findViewById(R.id.ri_date);
            itemPriority = (TextView) v.findViewById(R.id.ri_priority);
            itemStatus = (AppCompatCheckBox) v.findViewById(R.id.ri_status);
            itemPlay = (ImageView) v.findViewById(R.id.ri_playaudio);
            itemPlay.setVisibility(View.GONE);
            itemStop = (ImageView) v.findViewById(R.id.ri_stopaudio);
            itemStop.setVisibility(View.GONE);
            itemDelete = (ImageView) v.findViewById(R.id.ri_delete);
            itemDelete.setVisibility(View.GONE);
            itemEdit = (ImageView) v.findViewById(R.id.ri_edit);
            itemEdit.setVisibility(View.GONE);
            itemElements = (RelativeLayout) v.findViewById(R.id.ri_elements);
        }
    }

    // Constructor
    public RecyclerViewAdapter(Context context, RealmResults<ReminderItem> realmresults, Realm realm) {
        this.context = context;
        if (realmresults.size() == 0) {
            Log.d("Adapter", "results empty");
        }
        //TODO work just with RealmResults<>
        this.itemList = realmresults;
        this.realm = realm;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ma_reminder_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        this.holder=holder;
        ReminderItem item = itemList.get(position);
        holder.itemTitle.setText(item.getTitle());
        setPriorityText(holder, item.getPriority());
        if (1 /*Alarm ON*/ == item.getAlarm()) {
            setDateText(holder, item.getDate());
        } else {
            holder.itemDateTime.setVisibility(View.INVISIBLE);
        }
        setMyRecorder();
        setAudioPlayListener(holder, item.getAudioFilePath());
        //setAudioStopListener(holder, item.getAudioFilePath());
        setStatusListener(holder, item);
        setItemLayoutListener(holder);
        setDeleteListener(holder, item);
        setEditListener(holder, item);
    }

    private void setMyRecorder() {
        recorder = new MyRecorder();
    }

    //TODO drag (https://github.com/daimajia/AndroidSwipeLayout)
    //slide touch gesture listener
    private void setItemLayoutListener(final ViewHolder holder) {
        holder.itemLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = MotionEventCompat.getActionMasked(event);

                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        historicX = event.getX();
                        historicY = event.getY();
                        Log.d("Motion event", "touched: " + historicX + ", " + historicY);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        Log.d("Motion event", "released");
                        if (event.getX() - historicX < -DELTA) {
                            //slide to left
                            Log.d("Motion event", "slide to left");
                            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams();
                            //holder.itemElements.setLayoutParams(layoutParams);
                            holder.itemElements.setPadding(0, 0, 240, 0);
                            holder.itemDelete.setVisibility(View.VISIBLE);
                            holder.itemEdit.setVisibility(View.VISIBLE);
                            //TODO visivility if no audio
                            holder.itemPlay.setVisibility(View.VISIBLE);

                            return true;
                        } else if (event.getX() - historicX > DELTA) {
                            //slide to right
                            Log.d("Motion event", "slide to right");
                            holder.itemDelete.setVisibility(View.GONE);
                            holder.itemEdit.setVisibility(View.GONE);
                            holder.itemPlay.setVisibility(View.GONE);
                            holder.itemElements.setPadding(0, 0, 0, 0);
                            return true;
                        }
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });
    }

    private void setStatusListener(final RecyclerViewAdapter.ViewHolder holder, final ReminderItem item) {

        holder.itemStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("Adapter", "Status button pressed");
                    realm.beginTransaction();
                    item.setStatus(1 /*Status.DONE*/);
                    realm.commitTransaction();
                } else {
                    Log.d("Adapter", "Status button not pressed");
                    realm.beginTransaction();
                    item.setStatus(0 /*Status.NOTDONE*/);
                    realm.commitTransaction();

                }
            }
        });

    }

    private void setEditListener(final RecyclerViewAdapter.ViewHolder holder, final ReminderItem item) {
        holder.itemEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO edit
            }
        });
    }

    private void setDeleteListener(final RecyclerViewAdapter.ViewHolder holder, final ReminderItem item) {
        //TODO set listener for deleting item
        holder.itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                //itemList.remove(itemList.indexOf(item)); --> not supported in Realm version
                RealmResults<ReminderItem> res = itemList.where().equalTo(ReminderItem.ID, item.getId()).findAll();
                if(res.size()==1){
                    Log.d("Adapter","item removed. Id: "+Integer.toString(item.getId()));
                    res.clear();
                }
                else{
                    Log.e("Adapter","item not removed");
                }
                realm.commitTransaction();
                notifyDataSetChanged();
            }
        });

    }


    private void setAudioPlayListener(final RecyclerViewAdapter.ViewHolder holder, final String audioFilePath) {
        //TODO set listener for playing audio
        holder.itemPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorder.setFileName(audioFilePath);
                if (!recorder.isPlaying()) {
                    //v.setVisibility(View.GONE);
                    //holder.itemStop.setVisibility(View.VISIBLE);
                    Log.d("audioFilePath", audioFilePath);
                    recorder.onPlay(true);
                }
            }
        });

    }

    private void setAudioStopListener(final RecyclerViewAdapter.ViewHolder holder, final String audioFilePath) {
        //TODO set listener for stoping audio
        // not used
        holder.itemStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorder.setFileName(audioFilePath);
                if (recorder.isPlaying()) {
                    recorder.onPlay(false);
                    v.setVisibility(View.GONE);
                    holder.itemPlay.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void setDateText(ViewHolder holder, Date date) {
        if (date != null) {
            //TODO not done yet
            if (date.equals(new Date())) {
                holder.itemDateTime.setText(R.string.ma_today);

            }
            if (date.equals(new Date(ONE_DAY))) {
                holder.itemDateTime.setText(R.string.ma_tomorrow);

            } else {
                holder.itemDateTime.setText(ReminderItem.FORMAT.format(date));
            }
        }
    }

    private void setPriorityText(RecyclerViewAdapter.ViewHolder holder, int priority) {
        if (0 /*Priority.NONE*/ == priority) {
            holder.itemPriority.setVisibility(View.GONE);
        } else if (1/*Priority.LOW*/ == priority) {
            holder.itemPriority.setVisibility(View.VISIBLE);
            holder.itemPriority.setText(R.string.ar_priorityLow);
        } else if (2/*Priority.MED*/ == priority) {
            holder.itemPriority.setVisibility(View.VISIBLE);
            holder.itemPriority.setText(R.string.ar_priorityMedium);
        } else if (3/*Priority.HIGH*/ == priority) {
            holder.itemPriority.setVisibility(View.VISIBLE);
            holder.itemPriority.setText(R.string.ar_priorityHigh);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void settings() {
        holder.itemElements.setPadding(0, 0, 240, 0);
        holder.itemDelete.setVisibility(View.VISIBLE);
        holder.itemEdit.setVisibility(View.VISIBLE);
        //TODO visivility if no audio
        holder.itemPlay.setVisibility(View.VISIBLE);
    }

    public void add(ReminderItem item) {
        saveItemToDB(item);
        notifyDataSetChanged();
    }

    public void clear() {
        realm.beginTransaction();
        itemList.clear();
        realm.commitTransaction();
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return itemList.get(position);

    }

    public void deleteDone() {
        realm.beginTransaction();
        itemList.where().equalTo(ReminderItem.STATUS, 1).findAll().clear();
        realm.commitTransaction();
        notifyDataSetChanged();
    }

    private void saveItemToDB(ReminderItem item) {
        Log.d("Adapter","save item to DB");
        realm.beginTransaction();
        realm.copyToRealm(item);
        realm.commitTransaction();
        itemList = realm.allObjects(ReminderItem.class);
    }

}
