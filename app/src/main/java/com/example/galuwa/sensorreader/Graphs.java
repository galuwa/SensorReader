package com.example.galuwa.sensorreader;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;


public class Graphs extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);
        buildGraph();
    }

    public void buildGraph(){

        Intent intent = getIntent();
        String data = intent.getExtras().getString("data");
        GraphView graph = (GraphView) findViewById(R.id.graph);

        //DataPoint[] butt = new DataPoint[] {};
        int counter = 0;

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();

        Scanner scanner = new Scanner(data);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            float f = Float.parseFloat(line);
            DataPoint value = new DataPoint(counter, f);
            System.out.println(value.getX());
            System.out.println(value.getY());
            counter++;

            series.appendData(value, true, counter);


        }
        scanner.close();

        //DataPoint test = new DataPoint(1,1);




       // series.appendData(test,true,1);

        /*
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 9),
                new DataPoint(5, 2),
                new DataPoint(6, 2),
                new DataPoint(8, 6)
        });*/


        graph.addSeries(series);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graphs, menu);
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
}
