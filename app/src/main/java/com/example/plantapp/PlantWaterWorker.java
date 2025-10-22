package com.example.plantapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Random;

public class PlantWaterWorker extends Worker {

    private final String[] plantNames = {
            "Aloe Vera", "Peace Lily", "Spider Plant", "Money Plant", "Cactus", "Tulip"
    };

    public PlantWaterWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Random random = new Random();

        for (int i = 0; i < plantNames.length; i++) {
            int moisture = random.nextInt(101);
            if (moisture < 40) {
                sendLowMoistureNotification(getApplicationContext(), plantNames[i], moisture, i);
            }
        }

        return Result.success();
    }

    private void sendLowMoistureNotification(Context context, String plantName, int moisture, int index) {
        // ✅ Open MainActivity
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                index,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "plant_alerts")
                .setSmallIcon(R.drawable.ic_water_drop)
                .setContentTitle("🚨 " + plantName + " Needs Water")
                .setContentText("Moisture is " + moisture + "%. Please water your plant 🌿")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                == android.content.pm.PackageManager.PERMISSION_GRANTED
                || android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) {

            NotificationManagerCompat.from(context).notify(2000 + index, builder.build());
        }
    }
}
