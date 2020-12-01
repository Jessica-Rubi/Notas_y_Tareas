package com.example.notasytareas;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DaoTareas dao = new DaoTareas(context);
        Calendar c = Calendar.getInstance();
        String fecha = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
        String hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        for (int i = 0; i < dao.getAll().size(); i++) {
            if (dao.getAll().get(i).getHora().equals(hora)) {
                NotificationCompat.Builder mBuilder;
                NotificationManager mNotifyMgr = (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);
                int icono = android.R.drawable.ic_dialog_info;
                Intent intent1 = new Intent(context, ActivityTareas.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
                mBuilder = new NotificationCompat.Builder(context.getApplicationContext())
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(icono)
                        .setContentTitle(dao.getAll().get(i).getTitulo())
                        .setContentText(dao.getAll().get(i).getDescripcion())
                        .setVibrate(new long[]{100, 250, 100, 500})
                        .setAutoCancel(true);
                mNotifyMgr.notify(1, mBuilder.build());
            }
        }
    }
}
