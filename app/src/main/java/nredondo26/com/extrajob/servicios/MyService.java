package nredondo26.com.extrajob.servicios;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import nredondo26.com.extrajob.R;
import nredondo26.com.extrajob.preferencias;

public class MyService extends Service {

    String ocup;
    int  tipo;
    SharedPreferences userDetails;
    preferencias preferencias;
    Context context;
    private static final String TAG = "jiatai";
    private MyHandler handler;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context= this;
        preferencias= new preferencias();
        userDetails = this.getSharedPreferences("detallesusuario", MODE_PRIVATE);
        ocup = userDetails.getString("categoria", "");
        tipo = userDetails.getInt("tipousuario",0);
        handler = new MyHandler();
    }

    private void createErrorNotification() {
        Notification notification = new Notification.Builder(this).build();
        startForeground(0, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String id = "my_channel_01";
        CharSequence name = "hola";
        String description = "hola2";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true); mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mNotificationManager.createNotificationChannel(mChannel);
        String CHANNEL_ID = "my_channel_01";
        Notification notification = new Notification.Builder(this)
                .setContentTitle("New Message") .setContentText("You've received new messages.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setChannelId(CHANNEL_ID)
                .build();
        startForeground(1,notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        int type = intent.getIntExtra("type",1);
        Log.d(TAG, "the create notification type is " + type + "----" + (type == 1 ? "true" : "false"));
        if(type == 1){
            createNotificationChannel();
        }else{
            createErrorNotification();
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(startId);
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            stopSelf(msg.what);
        }
    }

}
