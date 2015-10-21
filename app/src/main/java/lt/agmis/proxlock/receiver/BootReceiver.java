package lt.agmis.proxlock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import lt.agmis.proxlock.service.SensorService;
import lt.agmis.proxlock.util.Utils;

/**
 * Created by simonasb on 15.10.12.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Utils.isAdminActivated(context)) {
            SensorService.startService(context);
        }
    }

}
