package com.example.cs160_ej.lordofrepresentatives;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

public class VoteViewActivity extends Activity implements SensorEventListener
{
    protected TextView countyAndState;
    protected TextView obama;
    protected TextView romney;

    private SensorManager manager;
    private Sensor accel;
    private float x = Float.NaN;
    private float y = Float.NaN;
    private float z = Float.NaN;

    private final int MIN_SHAKE_SPEED = 30;

    public static boolean started = false;
    public static VoteViewActivity currInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_view);

        started = true;
        currInstance = this;

        randomVote();

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public static void randVote()
    {
        currInstance.randomVote();
    }

    public void randomVote()
    {
        Random randomizer = new Random();

        countyAndState = (TextView) findViewById(R.id.county_and_state);
        obama = (TextView) findViewById(R.id.obama);
        romney = (TextView) findViewById(R.id.romney);

        String randomZip = "";
        randomZip += Integer.toString(randomizer.nextInt(10))
                + Integer.toString(randomizer.nextInt(10))
                + Integer.toString(randomizer.nextInt(10))
                + Integer.toString(randomizer.nextInt(10))
                + Integer.toString(randomizer.nextInt(10));
        countyAndState.setText(randomZip + ", " + "Random State #" + randomizer.nextInt(50));
        int obamaVotes = randomizer.nextInt(101);
        int romneyVotes = 100 - obamaVotes;
        obama.setText("Obama votes: " + Integer.toString(obamaVotes));
        romney.setText("Romney votes: " + Integer.toString(romneyVotes));
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {

            float currX = event.values[0];
            float currY = event.values[1];
            float currZ = event.values[2];

            if (!((Float.isNaN(x)) || (Float.isNaN(y)) || (Float.isNaN(z))))
            {
                if ((Math.abs(currX - x) >= MIN_SHAKE_SPEED)
                        || (Math.abs(currY - y) >= MIN_SHAKE_SPEED)
                        || (Math.abs(currZ - z) >= MIN_SHAKE_SPEED))
                {
                    Intent intent = new Intent(getBaseContext(), VoteViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    randomVote();
                }
            }

            x = currX;
            y = currY;
            z = currZ;

            Thread thread = new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        Thread.sleep(500);
                    }
                    catch (Exception e)
                    {

                    }
                }
            };
            thread.start();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
}
