package com.example.galuwa.sensorreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity implements SensorEventListener {


    public GenQueue<Float> SuperQueue;
    public GenQueue<Float> RotQueue;


    public final static String EXTRA_MESSAGE = "";
    public final static int SHAKE_THRESHOLD = 0;
    public final static int TIME_THRESHOLD = 100;


    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Sensor senRotVector;


    long lastUpdate = 0;


    float lastAccel_x = 0;
    float lastAccel_y = 0;
    float lastAccel_z = 0;


    float gravity_x = 0;
    float gravity_y = 0;
    float gravity_z = 0;


    float rot_x = 0;
    float rot_y = 0;
    float rot_z = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        //senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);



        senRotVector = senSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);


        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this, senRotVector, SensorManager.SENSOR_DELAY_NORMAL);
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

        TextView view2 = (TextView) findViewById(R.id.button2);
        TextView view3 = (TextView) findViewById(R.id.button3);
        TextView view4 = (TextView) findViewById(R.id.button4);
        TextView view5 = (TextView) findViewById(R.id.button5);
        view2.setVisibility(View.VISIBLE);
        view3.setVisibility(View.VISIBLE);
        view4.setVisibility(View.VISIBLE);
        view5.setVisibility(View.VISIBLE);

        if (view.getText().toString().equals("Start")) {
            view.setText("Stop");
            SuperQueue = new GenQueue<Float>();
            RotQueue = new GenQueue<Float>();
        } else if (view.getText().toString().equals("Stop"))
            view.setText("Start");


    }


    public void openGraphX(View v) {

        String queueToString = "";

        while (SuperQueue.hasItems()) {

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

    public void openGraphY(View v) {

        String queueToString = "";

        while (SuperQueue.hasItems()) {

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


    public void openGraphZ(View v) {

        String queueToString = "";


        while (SuperQueue.hasItems()) {

            SuperQueue.dequeue();
            SuperQueue.dequeue();

            float zValue = SuperQueue.dequeue();
            queueToString += zValue + "\n";

        }


        Intent intent2 = new Intent(this, Graphs.class);
        intent2.putExtra("data", queueToString);
        startActivity(intent2);
    }



    public void exportcsv(View v) {


        String queueToString = "";


        try{exportCSV();}catch(Exception e){System.out.println("You Broke Everything");}


    }


    public void exportCSV() throws IOException {
        {

            System.out.println("Exporting as a CSV");
            File folder = new File(Environment.getExternalStorageDirectory()
                    + "/Rehab");


            boolean var = false;
            if (!folder.exists())
                var = folder.mkdir();


            System.out.println("Folder was created" + var);


            final String filename = folder.toString() + "/" + "Data.csv";
            System.out.println("The file location is: " + filename);


            new Thread() {
                public void run() {
                    try {

                        FileWriter fw = new FileWriter(filename);


                        while (SuperQueue.hasItems()) {

                            //Test X graph building
                            System.out.println("Super Queue Has Items");

                            float xValue = SuperQueue.dequeue();
                            float yValue = SuperQueue.dequeue();
                            float zValue = SuperQueue.dequeue();


                            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                            String format = s.format(new Date());

                            fw.append(format);
                            fw.append(',');

                            fw.append(xValue+"");
                            fw.append(',');

                            fw.append(yValue+"");
                            fw.append(',');

                            fw.append(zValue+"");
                            fw.append(',');

                            fw.append('\n');


                        }

                        fw.append('\n');


                        fw.close();

                    } catch (Exception e) {

                        System.out.println("Error Encountered");
                    }

                }
            }.start();

        }

    }



    public void onSensorChanged(SensorEvent sensorEvent) {

        // Holds the identity of a sensor
        Sensor mySensor = sensorEvent.sensor;




        // Gets Text value of button that initiates measuring values to check whether or not data should be aggregated
        TextView buttonValue = (TextView) findViewById(R.id.button);

        // Checks whether STOP is on and if data should be observed
        if((buttonValue.getText().toString().equals("Stop"))) {

            System.out.println("Sensor Type is: " + mySensor.getType());

            // Checks Sensor Types and does corresponding manipulations on sensor data
            //if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

                // gets current time in milliseconds
                long curTime = System.currentTimeMillis();

                if ((curTime - lastUpdate) > TIME_THRESHOLD) {
                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;

                    // Gets x,y, and z values from accelerometer and eliminates effect of gravity
                    float accel_x = sensorEvent.values[0] - gravity_x;
                    float accel_y = sensorEvent.values[1] - gravity_y;
                    float accel_z = sensorEvent.values[2] - gravity_z;


                    // Finds magnitude of acceleration
                    float speed = Math.abs(accel_x + accel_y + accel_z - lastAccel_x - lastAccel_y - lastAccel_z) / diffTime * 10000;

                    if ((speed > SHAKE_THRESHOLD)) {

                        // USED TO CALCULATE SHAKE_THRESHOLD
                        lastAccel_x = accel_x;
                        lastAccel_y = accel_y;
                        lastAccel_z = accel_z;

                        // Rounds accelerometer data
                        DecimalFormat df = new DecimalFormat("#.##");

                        // Displays Everything
                        TextView view = (TextView) findViewById(R.id.textboxthing);
                        //view.setText("X Axis: " + df.format(lastAccel_x) + "");
                        view.setText("X Axis: " + df.format(lastAccel_x) + " " + "X Rot: " + df.format(rot_x));
                        SuperQueue.enqueue(lastAccel_x);
                        RotQueue.enqueue(rot_x);

                        TextView view2 = (TextView) findViewById(R.id.textboxthing2);
                       // view2.setText("Y Axis: " + df.format(lastAccel_y) + "");
                        view2.setText("Y Axis: " + df.format(lastAccel_y) + " " + "Y Rot: " + df.format(rot_y));
                        SuperQueue.enqueue(lastAccel_y);
                        RotQueue.enqueue(rot_y);

                        TextView view3 = (TextView) findViewById(R.id.textboxthing3);
                        //view3.setText("Z Axis: " + df.format(lastAccel_z) + "");
                        view3.setText("Z Axis: " + df.format(lastAccel_z) + " " + "Z Rot: " + df.format(rot_z));
                        SuperQueue.enqueue(lastAccel_z);
                        RotQueue.enqueue(rot_y);

                    } // IF MAGNITUDE OF ACCELERATION IS LARGE ENOUGH END
                } // IF TIME INTERVAL IS LARGE ENOUGH END
            } // ACCELEROMETER END


            else if (mySensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {

                /*
                *  The angle (rot) is returned as a negative fraction (-1<rot<0) when it is going
                *  from 0 to 180 degrees and it is returned positive fraction (0<rot<1)
                *  when going from 180 to 360 degrees
                */

                rot_x = sensorEvent.values[0];
                rot_y = sensorEvent.values[1];
                rot_z = sensorEvent.values[2];

                if (rot_x < 0) {
                    rot_x = -180*rot_x;
                } else {
                    rot_x = 360- (180*rot_x);
                }


                if (rot_y < 0) {
                    rot_y = -180*rot_y;
                } else {
                    rot_y = 360- (180*rot_y);
                }


                if (rot_z < 0) {
                    rot_z = -180*rot_z;
                } else {
                    rot_z = 360 - (180*rot_z);
                }


                System.out.println("Rotation X = "+ rot_x);
                System.out.println("Rotation Y = "+ rot_y);
                System.out.println("Rotation Z = "+ rot_z);

            } // GRAVITY END


        } // IF "STOP" IS ON BUTTON END


    } // FUNCTION , NO TOUCHIE (Unless you want the program to not work -> Then go ahead)



    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}



