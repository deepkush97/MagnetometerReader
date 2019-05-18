package com.afreet.magnetometerreader;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *  This app is made for reading the magnetometer reading from the android phone
 *  and then transmit that reading to the provided IP Address and Port Address.
 *
 *  It uses the cross-platform socket connection.
 *  The android phone is provided as the client of the socket.
 *  And it can be connected from any socket program, the IP address and Port
 *  Address of the server side should be provided in the android app and it will
 *  transmit the magnetometer reading to that server.
 */


public class MainActivity extends AppCompatActivity  implements SensorEventListener {

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    private int lastdegree=0;
    private String ipAddress, portNo;
    private boolean flag = false;

    private EditText inputIP, inputPort;
    private Button btnStart;
    private TextView tvHeading;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        inputIP = (EditText) findViewById(R.id.inputIP);
        inputPort = (EditText) findViewById(R.id.inputPort);
        btnStart = (Button) findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=true;
            }
        });
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }

        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
            int intazimuthInDegress = (int)(Math.toDegrees(azimuthInRadians)+360)%360;

            changeText(intazimuthInDegress);
            mCurrentDegree = -azimuthInDegress;
        }
    }

    public void changeText(int degree){
        if(Math.abs(lastdegree- degree)>2){

            tvHeading.setText(String.valueOf(degree));
            String m = String.valueOf(degree);
            ipAddress = inputIP.getText().toString().trim();
            portNo = inputPort.getText().toString().trim();
            if(flag) {
                SendMessage message = new SendMessage();
                message.execute(ipAddress, portNo, m);
            }
            lastdegree = degree;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }
}
