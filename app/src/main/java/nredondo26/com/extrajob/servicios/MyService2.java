package nredondo26.com.extrajob.servicios;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import nredondo26.com.extrajob.preferencias;

import static android.support.constraint.Constraints.TAG;

public class MyService2 extends Service {

    String ocup;
    int  tipo;
    FirebaseFirestore db;
    Context context;
    SharedPreferences userDetails;

    nredondo26.com.extrajob.preferencias preferencias;

    public MyService2() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context= this;
        preferencias= new preferencias();
        db = FirebaseFirestore.getInstance();
        userDetails = this.getSharedPreferences("detallesusuario", MODE_PRIVATE);
        ocup = userDetails.getString("categoria", "");
        tipo = userDetails.getInt("tipousuario",0);
        Log.e("proceso_servicio","Servicio creado");

        notificacion(0);

    }

    public void notificacion(int id){
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("").setContentText("").build();

            startForeground(id, notification);

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(tipo==1){

            Log.e("valores_servicio","entro en la opcion 1");
            ocup = userDetails.getString("categoria", "");
            db = FirebaseFirestore.getInstance();
            db.collection("ofertas")
                    .whereEqualTo("categoria", ocup)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.w(TAG, "listen:error", e);
                                return;
                            }

                            assert snapshots != null;
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                switch (dc.getType()) {
                                    case ADDED:

                                        notificacion(1);

                                        break;
                                    case MODIFIED:

                                        notificacion(2);

                                        break;
                                    case REMOVED:

                                        notificacion(3);

                                        break;
                                }
                            }
                        }
                    });
        }
        return START_STICKY;

        //return super.onStartCommand(intent, flags, startId);
    }
}
