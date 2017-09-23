package com.greenshadow.studylocker;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.List;

public class AppLockService extends AccessibilityService {
    boolean SERVICE_ON = true;
    DBHandler db = new DBHandler(this);
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        //Configure these here for compatibility with API 13 and below.
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;

        if (Build.VERSION.SDK_INT >= 16)
            //Just in case this helps
            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

        setServiceInfo(config);
    }

    @Override
    public void onCreate(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("lock_changestate");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (state) {
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                if (event.getPackageName() != null && event.getClassName() != null) {
                    String currAppName = event.getPackageName().toString();
                    //ActivityInfo activityInfo = tryGetActivity(currAppName);
                    boolean isActivity = currAppName != null;
                    if (isActivity)
                        Log.d("Locker", currAppName);
                    List lockList = db.getAllApps();
                    Log.d("Locker", lockList.toString());
                    if (lockList.contains(currAppName)) {
                        Log.d("Locker", currAppName + " to be locked");
                        Intent lockIntent = new Intent(this, AppInterstitialActivity.class);
                        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        this.startActivity(lockIntent);
                    }
                }
            }
        }
    }


    private ActivityInfo tryGetActivity(ComponentName componentName) {
        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
    boolean state;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase("lock_changestate")) {
                state = intent.getExtras().getBoolean("state");
                Log.d("ServiceLock",String.valueOf(state));
            }
        }
    };

    @Override
    public void onInterrupt() {}
}


