package lt.agmis.proxlock.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import lt.agmis.proxlock.service.SensorService;

/**
 * Created by simonasb on 15.10.13.
 */
public class AdminReceiver extends DeviceAdminReceiver {

    public void onEnabled(Context context, Intent intent) {
        SensorService.startService(context);
    }

    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "Do you really want to disable admin?";
    }

    public void onDisabled(Context context, Intent intent) {

    }

    public void onPasswordChanged(Context context, Intent intent) {

    }

    public void onPasswordFailed(Context context, Intent intent) {

    }

    public void onPasswordSucceeded(Context context, Intent intent) {

    }

}
