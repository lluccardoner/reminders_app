package project.lluccardoner.reminders.AddReminderActivity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Lluc on 09/02/2016.
 */
public class MyRecorder {

    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private String mFileName;
    private Boolean recording = false;
    private Boolean playing = false;
    public static final String NO_FILE = "no file";

    public MyRecorder(String file) {
        mFileName = file;
    }
    public MyRecorder(){
        mFileName = NO_FILE;
    }

    private void setCompletionListener(){
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playing=false;
            }
        });
    }
    public void setFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public String getFileName() {
        return mFileName;
    }

    public boolean isRecording() {
        return recording;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void onRecord(boolean start) {
        if(!mFileName.equals(NO_FILE)) {
            if (start) {
                startRecording();
            } else {
                stopRecording();
            }
        }
    }

    public void onPlay(boolean start) {
        if(!mFileName.equals(NO_FILE)) {
            if (start) {
                playAudio();
            } else {
                stopAudio();
            }
        }
    }

    private void startRecording() {
        Log.d("Recorder", "Start recording");
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
            recording = true;
        } catch (IOException e) {
            Log.e("Recorder", "prepare() failed on startRecording --> "+e.toString());
            mRecorder.release();
            recording = false;
        }

    }

    private void stopRecording() {
        Log.d("Recorder", "Stop recording");
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        recording = false;
    }

    private void playAudio() {
        Log.d("Recorder", "Play audio");
        mPlayer = new MediaPlayer();
        setCompletionListener();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            playing = true;
        } catch (IOException e) {
            Log.e("Recorder", "prepare() failed on playAudio--> "+e.toString());
            mPlayer.release();
            playing = false;
        }
    }

    private void stopAudio() {
        Log.d("Recorder", "Stop audio");
        mPlayer.release();
        mPlayer = null;
        playing = false;
    }


}
