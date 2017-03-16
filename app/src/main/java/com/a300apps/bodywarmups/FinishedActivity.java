package com.a300apps.bodywarmups;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class FinishedActivity extends AppCompatActivity {

    private TextView weekView, monthView, yearView, overallView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Get text handles.
        weekView = (TextView) findViewById(R.id.weekView);
        monthView = (TextView) findViewById(R.id.monthView);
        yearView = (TextView) findViewById(R.id.yearView);
        overallView = (TextView) findViewById(R.id.overallView);

        // Stored preferences for finishedTimeStamps.
        final String finishedTimeStampsString = "finishedTimeStamps";
        final String DATE_FORMAT_NOW = "yyyy-MM-dd hh:mm";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        Set<String> hs = preferences.getStringSet(finishedTimeStampsString, new HashSet<String>());
        Set<String> finishedTimeStamps = new HashSet<String>(hs);

        // Add current time to the set.
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        Date now = new Date();
        String stringDate = sdf.format(now) ;
        finishedTimeStamps.add(stringDate);

        // Update setting.s
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(finishedTimeStampsString, finishedTimeStamps);
        editor.commit();

        int thisWeekCount = 0;
        int thisMonthCount = 0;
        int thisYearCount = 0;
        int overallCount = 0;

        // Loop through all time stamps.
        for (String stamp : finishedTimeStamps) {
            Date date;
            try
            {
                date = sdf.parse(stamp);
            }
            catch (ParseException e) {
                continue;
            }

            Calendar calNow = Calendar.getInstance();
            calNow.setTime(now);
            Calendar calDate = Calendar.getInstance();
            calDate.setTime(date);

            overallCount++;
            if (calNow.get(Calendar.YEAR) == calDate.get(Calendar.YEAR)) {
                thisYearCount++;
            }
            if ((calNow.get(Calendar.MONTH) == calDate.get(Calendar.MONTH)) &&
                    (calNow.get(Calendar.YEAR) == calDate.get(Calendar.YEAR))) {
                thisMonthCount++;
            }
            if ((calNow.get(Calendar.WEEK_OF_YEAR) == calDate.get(Calendar.WEEK_OF_YEAR)) &&
                    (calNow.get(Calendar.YEAR) == calDate.get(Calendar.YEAR))) {
                thisWeekCount++;
            }
        }

        weekView.setText(Integer.toString(thisWeekCount));
        monthView.setText(Integer.toString(thisMonthCount));
        yearView.setText(Integer.toString(thisYearCount));
        overallView.setText(Integer.toString(overallCount));
    }
}
