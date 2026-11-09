package com.hmg.validation;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.car.Car;
import android.car.VehiclePropertyIds;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.property.CarPropertyManager;
import android.car.drivingstate.CarUxRestrictions;
import android.car.drivingstate.CarUxRestrictionsManager;

public class MainActivity extends Activity {
  private static final String TAG = "HMG_AAOS_PROBE";
  private TextView text;
  private Car car;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    text = new TextView(this);
    text.setTextSize(24);
    text.setText("Connecting to car service…");
    setContentView(text);
    car = Car.createCar(this, new Handler(Looper.getMainLooper()), Car.CAR_WAIT_TIMEOUT_WAIT_FOREVER,
      (connectedCar, ready) -> {
        Log.i(TAG, "lifecycle_ready=" + ready);
        if (!ready) return;
        StringBuilder result = new StringBuilder();
        try {
          CarPropertyManager props = (CarPropertyManager) connectedCar.getCarManager(Car.PROPERTY_SERVICE);
          readInt(props, VehiclePropertyIds.GEAR_SELECTION, "gear_selection", result);
          readFloat(props, VehiclePropertyIds.PERF_ODOMETER, "perf_odometer", result);
        } catch (Exception e) { append(result, "property_manager_error", e.toString()); }
        try {
          CarUxRestrictionsManager ux = (CarUxRestrictionsManager) connectedCar.getCarManager(Car.CAR_UX_RESTRICTION_SERVICE);
          CarUxRestrictions restrictions = ux.getCurrentCarUxRestrictions();
          append(result, "ux_restrictions", "active=" + restrictions.getActiveRestrictions() + ",requiresDO=" + restrictions.isRequiresDistractionOptimization());
        } catch (Exception e) { append(result, "ux_error", e.toString()); }
        runOnUiThread(() -> text.setText(result.toString()));
      });
  }
  private static void readInt(CarPropertyManager p, int id, String key, StringBuilder out) {
    try { CarPropertyValue<Integer> v = p.getProperty(Integer.class, id, 0); append(out, key, v == null ? "null" : String.valueOf(v.getValue())); }
    catch (Exception e) { append(out, key, e.toString()); }
  }
  private static void readFloat(CarPropertyManager p, int id, String key, StringBuilder out) {
    try { CarPropertyValue<Float> v = p.getProperty(Float.class, id, 0); append(out, key, v == null ? "null" : String.valueOf(v.getValue())); }
    catch (Exception e) { append(out, key, e.toString()); }
  }
  private static void append(StringBuilder out, String key, String value) {
    String line = key + "=" + value;
    Log.i(TAG, line);
    out.append(line).append('\n');
  }
  @Override public void onDestroy() { if (car != null) car.disconnect(); super.onDestroy(); }
}
