package lt.agmis.proxlock.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import lt.agmis.proxlock.receiver.AdminReceiver;
import lt.agmis.proxlock.service.SensorService;
import lt.agmis.proxlock.util.Utils;

/**
 * Application entry point
 * Must be admin
 */
public class MainActivity extends Activity {

    private static final String EXTRA_EXPLANATION = "Activate admin on device";
    private static final int REQUEST_ENABLE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Utils.isAdminActivated(this)) {
            activateDeviceAdmin();
        } else {
            SensorService.startService(this);
        }
        finish();
    }

    private void activateDeviceAdmin() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, new ComponentName(getApplicationContext(), AdminReceiver.class));
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, EXTRA_EXPLANATION);
        startActivityForResult(intent, REQUEST_ENABLE);
    }

}
