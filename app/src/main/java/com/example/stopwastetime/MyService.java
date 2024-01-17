package com.example.stopwastetime;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.stopwastetime.model.BlackListPackage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyService extends Service {
    static boolean isBlockActive = true;
    static boolean goQuestionBlock = true;
    static LocalTime endBlockTime;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    String foregroundApp = getForegroundApp();
                    if (MainActivity.blackList_Package.contains(new BlackListPackage(foregroundApp)) && isBlockActive) {
                        Intent i;
                        if (goQuestionBlock) {
                            i = new Intent(getBaseContext(), InitialQuestionActivity.class);
                            i.putExtra("foregroundApp", foregroundApp);
                        } else {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                            i = new Intent(getBaseContext(), BlockTimeActivity.class);
                            i.putExtra("endBlockTime", endBlockTime.format(formatter));
                            i.putExtra("foregroundApp", foregroundApp);
                        }
                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setPackage(getPackageName());
                        startActivity(i);
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        final String channelID = "foreground service ID";
        NotificationChannel channel = new NotificationChannel(channelID, channelID, NotificationManager.IMPORTANCE_LOW);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notifaction = new Notification.Builder(this, channelID).setContentText("funziona")
                .setContentTitle("la mia app").setSmallIcon(R.drawable.ic_launcher_background);

        startForeground(1001, notifaction.build());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private String getForegroundApp() {
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
        long currentTime = System.currentTimeMillis();
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 1000 * 10, currentTime);
        String topPackageName = "";
        if (stats != null) {
            long lastUsedAppTime = 0;
            for (UsageStats usageStats : stats) {
                if (usageStats.getLastTimeUsed() > lastUsedAppTime) {
                    topPackageName = usageStats.getPackageName();
                    lastUsedAppTime = usageStats.getLastTimeUsed();
                }
            }
        }
        return topPackageName;
    }


}