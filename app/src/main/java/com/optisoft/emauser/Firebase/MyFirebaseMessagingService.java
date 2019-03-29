package com.optisoft.emauser.Firebase;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.optisoft.emauser.Activity.DistressMessageActivity;
import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by OptiSoft_A on 1/20/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static final String NOTIFICATION_ID = "1";
    public static String notification_title = "";
    private CommonPrefrence commonPrefrence = new CommonPrefrence();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (commonPrefrence.getUserLoginSharedPref(this) == null){
            return;
        }

        Log.d(TAG, "AASHU From: " + remoteMessage.getFrom());
        Log.d(TAG, "AASHU Message data payload:" + remoteMessage.getData());
        Map<String, String> map = remoteMessage.getData();

        String user_id = map.get("user");
        if (!user_id.equals("ALL")){
            if (!commonPrefrence.getUserLoginSharedPref(this).getUserId().equalsIgnoreCase(user_id)){
                return;
            }
        }


        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "AASHU Message data payload: " + remoteMessage.getData());

            Map<String, String> mMap = remoteMessage.getData();
            if (mMap.containsKey("type")){
                if (mMap.get("type").equals("DISTRESS")){
                    String data = mMap.get("payloadData");
                    ArrayList<String> stringList = commonPrefrence.getDistressSharedPref(this);
                    if (stringList == null){ stringList = new ArrayList<>(); }
                    stringList.add(data);
                    commonPrefrence.setDistressSharedPref(this, stringList);
                    createDistressNotification(remoteMessage.getData());
                }else {
                    createNotification(remoteMessage.getData());
                }
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "AASHU Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void createNotification( Map<String, String> map ) {
        int notificationId = new Random().nextInt();
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        intent.putExtra("title", map.get("title"));
        intent.putExtra("message", map.get("message"));
        intent.putExtra("type", map.get("type"));
        intent.putExtra("data", map.get("payloadData"));
        intent.putExtra("user", map.get("user"));
        PendingIntent resultIntent2 = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setPriority(NotificationCompat.PRIORITY_MAX) //HIGH, MAX, FULL_SCREEN and setDefaults(Notification.DEFAULT_ALL) will make it a Heads Up Display Style
                .setDefaults(Notification.DEFAULT_ALL) // also requires VIBRATE permission
                .setSmallIcon(R.drawable.logo) // Required!
                .setContentTitle(map.get("title"))
                .setContentText(map.get("message"))
                .setAutoCancel(true)
                .setContentIntent(resultIntent2);

        NotificationManager notifyMgr = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyMgr.notify(notificationId, builder.build());
    }


    private void createDistressNotification( Map<String, String> map ) {
        int notificationId = new Random().nextInt();
        Intent intent = new Intent(this, DistressMessageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        PendingIntent resultIntent2 = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setPriority(NotificationCompat.PRIORITY_MAX) //HIGH, MAX, FULL_SCREEN and setDefaults(Notification.DEFAULT_ALL) will make it a Heads Up Display Style
                .setDefaults(Notification.DEFAULT_ALL) // also requires VIBRATE permission
                .setSmallIcon(R.drawable.logo) // Required!
                .setContentTitle(map.get("title"))
                .setContentText(map.get("message"))
                .setAutoCancel(true)
                .setContentIntent(resultIntent2);

        NotificationManager notifyMgr = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyMgr.notify(notificationId, builder.build());
    }

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}