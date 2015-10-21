package lt.agmis.proxlock.service;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by simonasb on 15.10.12.
 */
public class SensorService extends Service implements SensorEventListener {

    private static final String TAG = SensorService.class.getSimpleName();

    private static final int SENSOR_OPEN = 100;
    private static final int SENSOR_CLOSED = 3;

    private static int DELAY_AFTER_LOCKED = 1000; //1 second;
    private static int DELAY_SHORT = 1500; //1.5 seconds;
    private static int DELAY_LONG = 2 * DELAY_SHORT; //3 seconds;

    private Handler handler;
    private PowerManager powerManager;
    private DevicePolicyManager policyManager;

    private boolean isServiceNew;
    private boolean canTurnOn = true;

    public static void startService(Context context) {
        context.startService(new Intent(context, SensorService.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isServiceNew = true;
        handler = new Handler(Looper.myLooper());
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        policyManager = ((DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE));
        initSensor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void initSensor() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "Service created");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // The first values wakes up the device once the service is created
        if(isServiceNew) {
            isServiceNew = false;
            return;
        }
        // handle SENSOR_OPEN and SENSOR_CLOSED values;
        switch ((int) event.values[0]) {
            case SENSOR_OPEN:
                turnScreenOn();
                break;
            case SENSOR_CLOSED:
                turnScreenOff();
                break;
        }
    }

    private boolean isScreenOn() {
        return powerManager.isInteractive();
    }

    private void turnScreenOn() {
        if(isScreenOn()) {
            Log.d(TAG, "Removing callback");
            handler.removeCallbacks(screenOffTask);
        } else {
            wakeScreen();
        }
    }

    private void wakeScreen() {
        if(canTurnOn) {
            Log.d(TAG, "Turning on");
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
            wakeLock.acquire();
            wakeLock.release();
        }
    }

    private void turnScreenOff() {
        if(isScreenOn()) {
            int delay = DELAY_SHORT;
            Log.d(TAG, "Turning the screen off in " + delay + " ms");
            handler.postDelayed(screenOffTask, delay);
        }
    }

    private final Runnable screenOffTask = new Runnable() {
        @Override
        public void run() {
            policyManager.lockNow();
            reenableLockIn(DELAY_AFTER_LOCKED);
            Log.d(TAG, "Screen turned Off");
        }
    };

    private void reenableLockIn(int milliseconds) {
        canTurnOn = false;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                canTurnOn = true;
                Log.d(TAG, "We can turn on again");
            }
        }, milliseconds);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.unregisterListener(this);
        super.onDestroy();
        Log.d(TAG, "Destroyed");
    }
}
