package com.example.eazhilarasi.stepcounter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class PutAlarmOff extends AppCompatActivity implements SensorEventListener {

    TextView txtView;
    private boolean mInitialized; // used for initializing sensor only once

    private SensorManager mSensorManager;
    int noOfStepsNeeded;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;
    double mLastX = 0;
    double mLastY = 0;
    double mLastZ = 0;
    int stepsCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_alarm_off);
       txtView = (TextView) findViewById(R.id.NoOfSteps);
        noOfStepsNeeded = getIntent().getExtras().getInt("NoOfSteps");

        // Initialize Accelerometer sensor
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        startSensor();

    }

    private void startSensor() {
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_put_alarm_off, menu);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        // event object contains values of acceleration, read those
        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];

        final double alpha = 0.8; // constant for our filter below

        double[] gravity = {0,0,0};

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        x = event.values[0] - gravity[0];
        y = event.values[1] - gravity[1];
        z = event.values[2] - gravity[2];

        if (!mInitialized) {
            // sensor is used for the first time, initialize the last read values
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            mInitialized = true;
        } else {
            // sensor is already initialized, and we have previously read values.
            // take difference of past and current values and decide which
            // axis acceleration was detected by comparing values

            double deltaX = Math.abs(mLastX - x);
            double deltaY = Math.abs(mLastY - y);
            double deltaZ = Math.abs(mLastZ - z);
            if (deltaX < NOISE)
                deltaX = (float) 0.0;
            if (deltaY < NOISE)
                deltaY = (float) 0.0;
            if (deltaZ < NOISE)
                deltaZ = (float) 0.0;
            mLastX = x;
            mLastY = y;
            mLastZ = z;

            if (deltaX > deltaY) {
                // Horizontal shake
                // do something here if you like

            } else if (deltaY > deltaX) {
                // Vertical shake
                // do something here if you like

            } else if ((deltaZ > deltaX) && (deltaZ > deltaY)) {
                // Z shake
                stepsCount = stepsCount + 1;
                if (stepsCount > 0) {
                    txtView.setText(String.valueOf(stepsCount));
                }

                if(stepsCount >= noOfStepsNeeded)
                {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    Ringtone r = RingtoneManager.getRingtone(this, notification);
                    r.stop();
                }
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
