package ru.rsue.compass;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private ImageView ivDynamic;
    private TextView tvDegree;
    private float current_degree;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @SuppressLint("ServiceCast")
    private void init()
    {
        ivDynamic = findViewById(R.id.ivDynamic);
        tvDegree = findViewById(R.id.tvDegree);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    }
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    public void onPause()
    {
    super.onPause();
    sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
    float degree = Math.round(event.values[0]);

    tvDegree.setText("Degree from North: "+ Float.toString(degree)+" degrees");

        RotateAnimation ra = new RotateAnimation(current_degree, -degree, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF ,0.5f);

        ra.setDuration(210);

        ra.setFillAfter(true);

        ivDynamic.startAnimation(ra);
        current_degree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}