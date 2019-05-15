package nredondo26.com.extrajob;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import nredondo26.com.extrajob.servicios.MyService;
import nredondo26.com.extrajob.servicios.MyService2;

public class AutoArranque extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        startServicesStarterActivity(context);
    }

    private void startServicesStarterActivity(Context context) {

       // Intent intent = new Intent(context, MyService2.class);
        //context.startService(intent);

        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            Intent intent = new Intent(context,MyService2.class);
            context.startForegroundService(intent);
        }else{
            Intent intent = new Intent(context,MyService2.class);
            context.startService(intent);
        }*/

    }


}
