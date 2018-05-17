package jp.ac.titech.itpro.sdl.sensorlist;

import android.hardware.Sensor;

import java.lang.reflect.Field;

class Util {
    public static String sensorTypeName(Sensor sensor) {
        try {
            Class klass = sensor.getClass();
            for (Field field : klass.getFields()) {
                String fieldName = field.getName();
                if (fieldName.startsWith("TYPE_") && field.getInt(klass) == sensor.getType())
                    return fieldName;
            }
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
