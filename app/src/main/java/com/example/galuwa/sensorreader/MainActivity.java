package com.example.galuwa.sensorreader;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

import java.text.DecimalFormat;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    public final static String EXTRA_MESSAGE = "com.example.frank.myfirstapplication.MESSAGE";
    public final static int SHAKE_THRESHOLD = 1000;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    String Change2 = "im starting to hate myself.git";

    long lastUpdate = 0;
    float last_x = 0;
    float last_y = 0;
    float last_z = 0;

    float previousX = 0;
    float previousY = 0;
    float previousZ = 0;

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


    public void buttonPress(View v) {

        TextView view = (TextView) findViewById(R.id.button);

        if(view.getText().toString().equals("Start"))
            view.setText("Stop");

        else if(view.getText().toString().equals("Stop"))
            view.setText("Start");


    }

    public void openLog(View v){

        Intent intent = new Intent(this, LogActvitiy.class);
        startActivity(intent);
    }


    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {

                }


                previousX = last_x;
                previousY = last_y;
                previousZ = last_z;

                if(Math.abs(x - previousX) > .3){

                    last_x = x;

                }

                if(Math.abs(y - previousY) > .3){

                    last_y = y;

                }

                if(Math.abs(z - previousZ) > .3){

                    last_z = z;

                }



            }


            DecimalFormat df = new DecimalFormat("#.##");

            TextView buttonValue = (TextView) findViewById(R.id.button);


            if(buttonValue.getText().toString().equals("Stop")) {

                TextView view = (TextView) findViewById(R.id.textboxthing);
                view.setText("X Axis: " + df.format(last_x) + "");

                TextView view2 = (TextView) findViewById(R.id.textboxthing2);
                view2.setText("Y Axis: " + df.format(last_y) + "");

                TextView view3 = (TextView) findViewById(R.id.textboxthing3);
                view3.setText("Z Axis: " + df.format(last_z) + "");

            }
        }


    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }




}
