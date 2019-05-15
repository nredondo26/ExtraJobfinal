package nredondo26.com.extrajob;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import static android.content.Context.MODE_PRIVATE;

public class preferencias {

    void guardar_preferenica(String emaill, String userr, String categoriaa, int tipo, Context context){
        SharedPreferences userDetails = context.getSharedPreferences("detallesusuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetails.edit();
        if(tipo==1){
            editor.putInt("tipousuario",tipo);
            editor.putString("email",emaill);
            editor.putString("user",userr);
            editor.putString("categoria",categoriaa);
        }if(tipo==2){
            editor.putInt("tipousuario",tipo);
            editor.putString("email",emaill);
            editor.putString("user",userr);
        }
        editor.apply();
        Log.e("preferencias","guardadas");
    }

    void guardar_preferenica_usuarios(Context context, String Email ,String Nombre,String Ocupacion,String Ciudad,String Fnacimiento,String Documento,String Telefono){
        SharedPreferences userDetails = context.getSharedPreferences("todousuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetails.edit();

        editor.putString("Email",Email);
        editor.putString("Nombre",Nombre);
        editor.putString("Ocupacion",Ocupacion);
        editor.putString("Ciudad",Ciudad);
        editor.putString("Fnacimiento",Fnacimiento);
        editor.putString("Documento",Documento);
        editor.putString("Telefono",Telefono);

        editor.apply();
        Log.e("preferencia del usuario","guardada");
    }

}
