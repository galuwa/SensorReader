package com.example.galuwa.sensorreader;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DecimalFormat;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    public final static String EXTRA_MESSAGE = "com.example.frank.myfirstapplication.MESSAGE";
    public final static int SHAKE_THRESHOLD = 1000;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    long lastUpdate = 0;
    float last_x = 0;
    float last_y = 0;
    float last_z = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this,senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 600) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {

                }

                last_x = x;
                last_y = y;
                last_z = z;

                //Assuming Laying flat
                //last_z -= 9.81;

            }
            System.out.println(last_x);

            DecimalFormat df = new DecimalFormat("#.##");

            TextView view = (TextView) findViewById(R.id.textboxthing);
            view.setText("X is: " + df.format(last_x) + "");

            TextView view2 = (TextView) findViewById(R.id.textboxthing2);
            view2.setText("Y is: " + df.format(last_y) + "");

            TextView view3 = (TextView) findViewById(R.id.textboxthing3);
            view3.setText("Z is: " + df.format(last_z) + "");
        }


    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }




}
