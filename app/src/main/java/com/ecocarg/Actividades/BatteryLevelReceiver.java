package com.ecocarg.Actividades;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ecocarg.R;

import static android.content.Context.NOTIFICATION_SERVICE;


public class BatteryLevelReceiver extends BroadcastReceiver {

    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    SharedPreferences sharedPreferences, sharedPreferencesLee;
    boolean noti,solo_una,cargando;
    int car,level;


    @Override
    public void onReceive(Context context, Intent intent) {

        sharedPreferences= context.getSharedPreferences("primeravez",Context.MODE_PRIVATE);
        sharedPreferencesLee=context.getSharedPreferences("paso",Context.MODE_PRIVATE);
        solo_una=sharedPreferencesLee.getBoolean("ya",true);
        if(sharedPreferences.getString("notificaciones","Sin seleccion").equals("si"))
            noti=true;
        else
            noti=false;

        Intent batteryStatusIntent = context.getApplicationContext()
                .registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        level = batteryStatusIntent.getIntExtra("level", 0);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        // Are we charging / charged?
        car = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        cargando = car == BatteryManager.BATTERY_STATUS_CHARGING ||
                car == BatteryManager.BATTERY_STATUS_FULL;

        if ((level==25||level==15)&&noti&&!cargando) {
            if(solo_una) {
                mostar(context);
                SharedPreferences.Editor editor=sharedPreferencesLee.edit();
                editor.putBoolean("ya",false);
                editor.apply();
            }
        }
        else {
            if(!solo_una) {
                SharedPreferences.Editor editor = sharedPreferencesLee.edit();
                editor.putBoolean("ya", true);
                editor.apply();
            }
        }
    }


    private void mostar(Context context) {
        setPendingIntent(context);
        CreateNotificacionChannel(context);
        crearNotificacion(context);
    }

    private  void setPendingIntent(Context context) {
        Intent intent = new Intent(context, Main2Activity.class);
        pendingIntent= PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

    }
    private void CreateNotificacionChannel(Context context) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name="Notitificacion";
            NotificationChannel notificationChannel= new NotificationChannel(CHANNEL_ID,name, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void crearNotificacion(Context context) {
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context.getApplicationContext(),CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ff_sobre);
        builder.setContentTitle("Tienes "+ level+"% de bateria");
        builder.setContentText("Te invitamos a mantenerte cargado, ubicanos");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setContentIntent(pendingIntent);
      //  builder.setFullScreenIntent(pendingIntent, true);
        NotificationManagerCompat notificationManagerCompat =NotificationManagerCompat.from(context.getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID,builder.build());
    }



}