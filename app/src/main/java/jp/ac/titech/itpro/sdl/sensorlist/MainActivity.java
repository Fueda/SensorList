package jp.ac.titech.itpro.sdl.sensorlist;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        ListView sensorListView = findViewById(R.id.sensor_list_view);
        ArrayAdapter<Sensor> sensorListAdapter =
                new ArrayAdapter<Sensor>(this, 0, new ArrayList<Sensor>()) {
                    @Override
                    public @NonNull
                    View getView(int pos, @Nullable View view, @NonNull ViewGroup parent) {
                        if (view == null) {
                            LayoutInflater inflater = LayoutInflater.from(getContext());
                            view = inflater.inflate(android.R.layout.simple_list_item_2,
                                    parent, false);
                        }
                        Sensor sensor = getItem(pos);
                        if (sensor != null) {
                            TextView sensorNameText = view.findViewById(android.R.id.text1);
                            TextView sensorTypeText = view.findViewById(android.R.id.text2);
                            sensorNameText.setText(sensor.getName());
                            sensorTypeText.setText(Util.sensorTypeName(sensor));
                        }
                        return view;
                    }
                };
        sensorListView.setAdapter(sensorListAdapter);
        sensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Intent intent = new Intent(MainActivity.this, SensorDataActivity.class);
                intent.putExtra(SensorDataActivity.SENSORPOS_EXTRA, pos);
                startActivity(intent);
            }
        });

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager == null) {
            Toast.makeText(this, R.string.toast_no_sensor_manager, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        List<Sensor> sensors = new ArrayList<>();
        for (Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_ALL)) {
            if (Util.sensorTypeName(sensor) != null)
                sensors.add(sensor);
        }
        sensorListAdapter.addAll(sensors);
    }
}
