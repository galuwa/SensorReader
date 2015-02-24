package com.example.galuwa.sensorreader;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.DecimalFormat;



public class MainActivity extends ActionBarActivity implements SensorEventListener {



    public GenQueue<Float> SuperQueue;

    public final static String EXTRA_MESSAGE = "";
    public final static int SHAKE_THRESHOLD = 1000;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    String Change2 = "im starting to hate myself.git";

    long lastUpdate = 0;
    float lastAccel_x = 0;
    float lastAccel_y = 0;
    float lastAccel_z = 0;

    float secondLastX = 0;
    float secondLastY = 0;
    float secondLastZ = 0;

    //This is test stuff for the Queue




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

        //TextView view2 = (TextView) findViewById(R.id.button2);
        TextView view3 = (TextView) findViewById(R.id.button3);
        TextView view4 = (TextView) findViewById(R.id.button4);
        TextView view5 = (TextView) findViewById(R.id.button5);
        //view2.setEnabled(true);
        //view2.setVisibility(View.VISIBLE);
        view3.setVisibility(View.VISIBLE);
        view4.setVisibility(View.VISIBLE);
        view5.setVisibility(View.VISIBLE);

        if(view.getText().toString().equals("Start")) {
            view.setText("Stop");
            SuperQueue = new GenQueue<Float>();
        }

        else if(view.getText().toString().equals("Stop"))
            view.setText("Start");




    }

    public void openLog(View v){

        String queueToString = "";

        while(SuperQueue.hasItems()){

            float xValue = SuperQueue.dequeue();
            queueToString += "X: " + xValue + "\n";

            float yValue = SuperQueue.dequeue();
            queueToString += "Y: " + yValue + "\n";

            float zValue = SuperQueue.dequeue();
            queueToString += "Z: " + zValue + "\n";


        }

        //System.out.println(queueToString);



        Intent intent = new Intent(this, LogActvitiy.class);
        intent.putExtra("data", queueToString);
        startActivity(intent);
    }


    public void openGraphX(View v){

        String queueToString = "";

        while(SuperQueue.hasItems()){

            //Test X graph building

            float xValue = SuperQueue.dequeue();
            queueToString += xValue + "\n";

            SuperQueue.dequeue();
            SuperQueue.dequeue();
            //queueToString += yValue + "\n";

            //float zValue = SuperQueue.dequeue();
            //queueToString += zValue + "\n";


        }

        Intent intent2 = new Intent(this, Graphs.class);
        intent2.putExtra("data", queueToString);
        startActivity(intent2);
    }

    public void openGraphY(View v){

        String queueToString = "";

        while(SuperQueue.hasItems()){

            //Test X graph building

            SuperQueue.dequeue();

            float YValue = SuperQueue.dequeue();
            queueToString += YValue + "\n";

            SuperQueue.dequeue();


            //float yValue = SuperQueue.dequeue();
            //queueToString += yValue + "\n";

            //float zValue = SuperQueue.dequeue();
            //queueToString += zValue + "\n";


        }

        Intent intent2 = new Intent(this, Graphs.class);
        intent2.putExtra("data", queueToString);
        startActivity(intent2);
    }

    public void openGraphZ(View v){

        String queueToString = "";

        while(SuperQueue.hasItems()){

            //Test X graph building

            SuperQueue.dequeue();
            SuperQueue.dequeue();

            float zValue = SuperQueue.dequeue();
            queueToString += zValue + "\n";

            //float yValue = SuperQueue.dequeue();
            //queueToString += yValue + "\n";

            //float zValue = SuperQueue.dequeue();
            //queueToString += zValue + "\n";


        }

        Intent intent2 = new Intent(this, Graphs.class);
        intent2.putExtra("data", queueToString);
        startActivity(intent2);
    }


    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float accel_x = sensorEvent.values[0];
            float accel_y = sensorEvent.values[1];
            float accel_z = sensorEvent.values[2];


            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(accel_x + accel_y + accel_z - lastAccel_x - lastAccel_y - lastAccel_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {

                }


                secondLastX = lastAccel_x;
                secondLastY = lastAccel_y;
                secondLastZ = lastAccel_z;


                if (Math.abs(accel_x - secondLastX) > .3) {

                    lastAccel_x = accel_x;

                }

                if (Math.abs(accel_y - secondLastY) > .3) {

                    lastAccel_y = accel_y;

                }

                if (Math.abs(accel_z - secondLastZ) > .3) {

                    lastAccel_z = accel_z;

                }


            }


            DecimalFormat df = new DecimalFormat("#.##");

            TextView buttonValue = (TextView) findViewById(R.id.button);


            if (buttonValue.getText().toString().equals("Stop")) {

                TextView view = (TextView) findViewById(R.id.textboxthing);
                view.setText("X Axis: " + df.format(lastAccel_x) + "");
                SuperQueue.enqueue(lastAccel_x);


                TextView view2 = (TextView) findViewById(R.id.textboxthing2);
                view2.setText("Y Axis: " + df.format(lastAccel_y) + "");
                SuperQueue.enqueue(lastAccel_y);


                TextView view3 = (TextView) findViewById(R.id.textboxthing3);
                view3.setText("Z Axis: " + df.format(lastAccel_z) + "");
                SuperQueue.enqueue(lastAccel_z);



            }
        } else if (mySensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {

        }
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }




}
