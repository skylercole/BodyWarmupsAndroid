package com.a300apps.bodywarmups;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import java.util.Timer;
import java.util.TimerTask;

public class VideoActivity extends AppCompatActivity {
    private VideoView myVideoView;
    private MediaController mediaControls;
    private Timer myTimer;
    private Resources res;
    private MediaPlayer mps;
    private TextView secondsLeft, curTotal, title, subTitle, desc;

    private int position = 1; //video seek pos
    private int exerciseLength; // from Main activity
    private int exerciseSet; // from Main activity
    private int secsPerLength; // set seconds size based on exerciseLength
    private int curExercise = 1; // counter for up to 10, logic in Timer
    private int secsLeft; // seconds counter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        secondsLeft = (TextView) findViewById(R.id.lblSecLeft);
        curTotal = (TextView) findViewById(R.id.lblCurTotal);
        title = (TextView) findViewById(R.id.lblTitle);
        subTitle = (TextView) findViewById(R.id.lblSubTitle);
        desc = (TextView) findViewById(R.id.lblDesc);

        res = getResources();
        setTexts();

        // Get parameters from the main activity.
        Bundle bundle = getIntent().getExtras();
        exerciseLength = bundle.getInt("exerciseLength");
        exerciseSet = bundle.getInt("exerciseSet");

        // Get the second constants.
        switch (exerciseLength)
        {
            case 0:
                secsPerLength = res.getInteger(R.integer.secsFor5Min);
                break;
            case 1:
                secsPerLength = res.getInteger(R.integer.secsFor7Min);
                break;
            case 2:
                secsPerLength = res.getInteger(R.integer.secsFor10Min);
                break;
        }

        secsLeft = secsPerLength;

        // Start the timer
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 1000);

        //initialize the VideoView
        myVideoView = (VideoView) findViewById(R.id.video_view);
        //set the uri of the video to be played
        myVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.big_buck_bunny));
        myVideoView.requestFocus();
        myVideoView.start();

        // set the media controller
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                    // reference to check if video is playing in Timer method
                    mps = mp;

                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                        setTexts();

                        // 2nd param disables forward, backward buttons
                        mediaControls = new MediaController(VideoActivity.this, false);
                        myVideoView.setMediaController(mediaControls);
                        // and set its position on screen
                        mediaControls.setAnchorView(myVideoView);

                        // disable seekbar
                        int topContainerId = getResources().getIdentifier("mediacontroller_progress", "id", "android");
                        SeekBar seekBarVideo = (SeekBar) mediaControls.findViewById(topContainerId);
                        seekBarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                                seekBar.setEnabled(false);
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                seekBar.setEnabled(false);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //we use onSaveInstanceState in order to store the video playback position for orientation change
        savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
        savedInstanceState.putInt("SecsLeft", secsLeft);
        savedInstanceState.putInt("CurrentExercise", curExercise);
        myTimer.cancel();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        secsLeft = savedInstanceState.getInt("SecsLeft");
        curExercise = savedInstanceState.getInt("CurrentExercise");
        //we use onRestoreInstanceState in order to play the video playback from the stored position
        position = savedInstanceState.getInt("Position");
        myVideoView.seekTo(position);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        // kill timer when exiting activity or app will crash
        myTimer.cancel();
    }

    private void setTexts()
    {
        curTotal.setText(curExercise + "/10");
    }

    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.
        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }

   //This method runs in the same thread as the UI.
    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            if (mps != null && mps.isPlaying()){

                if (secsLeft == 0) {
                    myVideoView.stopPlayback();

                    if (curExercise < 10) {
                        curExercise++;
                        myVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.big_buck_bunny));
                        myVideoView.requestFocus();
                        myVideoView.start();
                        secsLeft = secsPerLength;
                    } else {
                        Intent intent = new Intent();
                        // Main activity will know to start Finished activity
                        intent.putExtra("AreExercisesFinished", true);
                        setResult(RESULT_OK, intent);
                        onBackPressed(); // call onPause and exit activity
                    }
                }

               if (secsLeft == secsPerLength) {
                    secondsLeft.setText("- 3 -");
                    secondsLeft.setTextColor(Color.GREEN);
                }
                if (secsLeft == secsPerLength - 1) {
                    secondsLeft.setText("- 2 -");
                    secondsLeft.setTextColor(Color.YELLOW);
                }
                if (secsLeft == secsPerLength - 2) {
                    secondsLeft.setText("- 1 -");
                    secondsLeft.setTextColor(Color.RED);
                }
                if (secsLeft < secsPerLength - 2) {
                    secondsLeft.setText(String.valueOf(secsLeft));
                    secondsLeft.setTextColor(Color.GREEN);
                }

                secsLeft--;
            }
        }
    };
}