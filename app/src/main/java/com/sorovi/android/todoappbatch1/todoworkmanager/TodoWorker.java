package com.sorovi.android.todoappbatch1.todoworkmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.sorovi.android.todoappbatch1.R;

public class TodoWorker extends Worker {
    private Context context;
    public TodoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    @Override
    public Result doWork() {
        final String name = getInputData().getString("name");
        sendNotification(name);
        return Result.success();
    }

    private void sendNotification(String name) {
        String CHANNEL_ID = "my_channel";
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle("Todo Alert!").setContentText("It\'s time to do"+name)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            String description ="This channel sends todo notification alert";
            int important = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Todo",important);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify((int)System.currentTimeMillis(),builder.build());
    }
}
