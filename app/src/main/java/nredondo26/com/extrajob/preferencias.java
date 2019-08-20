package nredondo26.com.extrajob;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class preferencias {

    void Recordaruser_pass(String user, String pass, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("recordaruser_pass", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", user);
        editor.putString("password", pass);
        editor.apply();
        Log.e("email y passwors", "guardadas");
    }

    void eliminar_preferencia(String nombre_preferencia, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(nombre_preferencia, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear().commit();
        Log.e("email y passwors", "preferencia eliminada");
    }

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

    void guardar_preferenica_empresa(Context context, String Persona_contacto ,String Email,String Telefono,String Nit,String Actividad_economica,String Cargo_ocupacion,String Direccion,String rz){
        SharedPreferences userDetails = context.getSharedPreferences("todoempresa", MODE_PRIVATE);
        SharedPreferences.Editor editor = userDetails.edit();

        editor.putString("Persona_contacto",Persona_contacto);
        editor.putString("Email",Email);
        editor.putString("Telefono",Telefono);
        editor.putString("Nit",Nit);
        editor.putString("Actividad_economica",Actividad_economica);
        editor.putString("Cargo_ocupacion",Cargo_ocupacion);
        editor.putString("Direccion",Direccion);
        editor.putString("Razon_social",rz);

        editor.apply();
        Log.e("preferencia_empresa","guardada");
    }

}
