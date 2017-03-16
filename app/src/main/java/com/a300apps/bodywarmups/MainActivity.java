package com.a300apps.bodywarmups;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    private RadioGroup exerciseLength;
    private RadioButton min5, min7, min10;
    private RadioGroup exerciseSet;
    private RadioButton set1, set2, set3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        exerciseLength = (RadioGroup)findViewById(R.id.minutesgroup);
        exerciseLength.check(R.id.min5);
        exerciseSet = (RadioGroup)findViewById(R.id.setsGroup);
        exerciseSet.check(R.id.set1);
        min5 = (RadioButton)findViewById(R.id.min5);
        min7 = (RadioButton)findViewById(R.id.min7);
        min10 = (RadioButton)findViewById(R.id.min10);
        set1 = (RadioButton)findViewById(R.id.set1);
        set2 = (RadioButton)findViewById(R.id.set2);
        set3 = (RadioButton)findViewById(R.id.set3);

        final ImageButton playButton = (ImageButton)this.findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(MainActivity.this, VideoActivity.class);
                videoIntent.putExtra("exerciseLength", CheckLength());
                videoIntent.putExtra("exerciseSet", CheckSet());
                startActivityForResult(videoIntent, 1);
            }
        });

        final ImageButton infoButton = (ImageButton)this.findViewById(R.id.info);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(infoIntent);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                boolean areExercisesFinished = false;
                areExercisesFinished = data.getBooleanExtra("AreExercisesFinished", false);

                if (areExercisesFinished)
                {
                    // Start Finished activity.
                    Intent finishedIntent = new Intent(MainActivity.this, FinishedActivity.class);
                    startActivity(finishedIntent);
                }
            }
        }
    }

    private int CheckLength()
    {
        int selectedId = exerciseLength.getCheckedRadioButtonId();

        if(selectedId == min5.getId()) {
            return 0;
        } else if(selectedId == min7.getId()) {
            return 1;
        } else {
            return 2;
        }
    }

    private int CheckSet()
    {
        int selectedId = exerciseSet.getCheckedRadioButtonId();

        if(selectedId == set3.getId()) {
            return 2;
        } else if(selectedId == set2.getId()) {
            return 1;
        } else {
            return 0;
        }
    }
}