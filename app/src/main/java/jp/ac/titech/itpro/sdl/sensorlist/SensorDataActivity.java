package jp.ac.titech.itpro.sdl.sensorlist;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SensorDataActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "SensorDataActivity";
    private static final int MAXDATASIZE = 8;

    private TextView sensorNameView;
    private TextView sensorTypeView;
    private TextView sensorTypeStringView;
    private TextView vendorView;
    private TextView delayView;
    private TextView accuracyView;
    private TextView timestampView;
    private TextView dataSizeView;
    private TextView[] valueView;

    private SensorManager sensorMgr;
    private Sensor sensor;

    public static final String SENSORPOS_EXTRA = "sensor_pos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_sensor_data);

        sensorNameView = findViewById(R.id.sensor_name_view);
        sensorTypeView = findViewById(R.id.sensor_type_view);
        sensorTypeStringView = findViewById(R.id.sensor_type_string_view);
        vendorView = findViewById(R.id.vendor_view);
        delayView = findViewById(R.id.delay_view);
        accuracyView = findViewById(R.id.accuracy_view);
        timestampView = findViewById(R.id.timestamp_view);
        dataSizeView = findViewById(R.id.data_size_view);
        valueView = new TextView[MAXDATASIZE];
        valueView[0] = findViewById(R.id.value0_view);
        valueView[1] = findViewById(R.id.value1_view);
        valueView[2] = findViewById(R.id.value2_view);
        valueView[3] = findViewById(R.id.value3_view);
        valueView[4] = findViewById(R.id.value4_view);
        valueView[5] = findViewById(R.id.value5_view);
        valueView[6] = findViewById(R.id.value6_view);
        valueView[7] = findViewById(R.id.value7_view);

        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorMgr == null) {
            Toast.makeText(this, R.string.toast_no_sensor_manager, Toast.LENGTH_LONG).show();
            finish();
        }
        List<Sensor> sensors = new ArrayList<>();
        for (Sensor sensor : sensorMgr.getSensorList(Sensor.TYPE_ALL)) {
            if (Util.sensorTypeName(sensor) != null)
                sensors.add(sensor);
        }
        int pos = getIntent().getIntExtra(SENSORPOS_EXTRA, -1);
        if (pos >= 0 && pos < sensors.size()) {
            sensor = sensors.get(pos);
        }
        else {
            Toast.makeText(this, R.string.toast_no_such_sensor, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        sensorNameView.setText(sensor.getName());
        sensorTypeView.setText(Util.sensorTypeName(sensor));
        sensorTypeStringView.setText(sensor.getStringType());
        vendorView.setText(sensor.getVendor());
        delayView.setText(getString(R.string.delay_format, sensor.getMinDelay(), sensor.getMaxDelay()));
        sensorMgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        sensorMgr.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        accuracyView.setText(getString(R.string.int_format, event.accuracy));
        timestampView.setText(getString(R.string.long_format, event.timestamp));
        dataSizeView.setText(getString(R.string.int_format, event.values.length));
        int n = event.values.length;
        n = n > MAXDATASIZE ? MAXDATASIZE : n;
        for (int i = 0; i < n; i++) {
            valueView[i].setText(getString(R.string.float_format, event.values[i]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        accuracyView.setText(getString(R.string.int_format, accuracy));
    }
}
