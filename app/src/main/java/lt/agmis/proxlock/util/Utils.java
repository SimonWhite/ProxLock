package lt.agmis.proxlock.util;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;

import lt.agmis.proxlock.receiver.AdminReceiver;

/**
 * Created by simonasb on 15.10.13.
 */
public class Utils {

    public static boolean isAdminActivated(Context context) {
        DevicePolicyManager policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        return policyManager.isAdminActive(new ComponentName(context, AdminReceiver.class));
    }

}
