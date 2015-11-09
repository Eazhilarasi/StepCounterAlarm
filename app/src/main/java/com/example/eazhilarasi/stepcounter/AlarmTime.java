package com.example.eazhilarasi.stepcounter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AlarmTime extends AppCompatActivity {

    TimePicker timepicker;
    SeekBar stepsSeekBar;
    long milliseconds;
    int numOfSteps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_time);
        stepsSeekBar = (SeekBar)findViewById(R.id.stepsSeekBar);
        stepsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                if (progress == 0) {
                    numOfSteps = 50;
                } else if (progress > 0 && progress < 50)
                {
                    numOfSteps = progress + 50;
                }
                else
                {
                    numOfSteps = 100;
                }

                Toast.makeText(getApplicationContext(), String.valueOf(progress),Toast.LENGTH_LONG).show();

            }
        });
        timepicker = (TimePicker)findViewById(R.id.timePicker);
        timepicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                milliseconds = hourOfDay * 3600000;
                milliseconds = minute * 60000;
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                int offset = cal.getTimeZone().getOffset(cal.getTimeInMillis());
                milliseconds = cal.getTimeInMillis() + offset;

                Toast.makeText(AlarmTime.this, String.valueOf(milliseconds) + " : " + String.valueOf(System.currentTimeMillis()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setAlarmToRing(View view)
    {
        Intent AlarmIntent = new Intent(AlarmTime.this, AlarmReceiver.class);
        AlarmIntent.putExtra("NoOfSteps", numOfSteps);

        AlarmManager AlmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent Sender = PendingIntent.getBroadcast(this, 0, AlarmIntent, 0);
        AlmMgr.set(AlarmManager.RTC_WAKEUP,  milliseconds, Sender);
    }
}
