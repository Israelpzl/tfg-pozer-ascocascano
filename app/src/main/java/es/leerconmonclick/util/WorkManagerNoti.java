package es.leerconmonclick.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.leeconmonclick.R;
import com.example.leeconmonclick.TaskListActivity;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WorkManagerNoti extends Worker {


    public WorkManagerNoti(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static void saveNoti(Long duration, Data data, String tag){
        OneTimeWorkRequest noti = new OneTimeWorkRequest.Builder(WorkManagerNoti.class)
                .setInitialDelay(duration, TimeUnit.MILLISECONDS).addTag(tag)
                .setInputData(data).build();
        WorkManager intance = WorkManager.getInstance();
        intance.enqueue(noti);
    }

    private void oreo (String tittle, String description){
        String id  = "message";
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),id);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel nc = new NotificationChannel(id, "new", NotificationManager.IMPORTANCE_HIGH);
            nc.setDescription("Notification work");
            nc.setShowBadge(true);
            assert notificationManager !=null;
            notificationManager.createNotificationChannel(nc);
        }

        Intent intent = new Intent(getApplicationContext(), TaskListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 ,intent, PendingIntent.FLAG_ONE_SHOT);

        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(tittle)
                .setTicker("Nueva notificación")
                .setSmallIcon(R.mipmap.icon_leerconmonclick)
                .setContentText(description)
                .setContentIntent(pendingIntent)
                .setContentInfo("nuevo");

        Random random = new Random();
        int idNotify = random.nextInt(8000);
        assert notificationManager !=null;
        notificationManager.notify(idNotify,builder.build());

    }

    @NonNull
    @Override
    public Result doWork() {

        String tittle = getInputData().getString("tittle");
        String description = getInputData().getString("description");
        int id  = (int) getInputData().getLong("idNoti",0);

        oreo(tittle,description);

        return Result.success();
    }
}
