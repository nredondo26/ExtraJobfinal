package nredondo26.com.extrajob;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class PerfilempresaActivity extends AppCompatActivity {

    SharedPreferences usertodo;
    TextView persona_contacto,email,telefono,nit,actividad_economica,cargo_ocupacion,direccion,nombre_empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilempresa);
        Toolbar toolbar =  findViewById(R.id.toolbar5);
        toolbar.setTitle("Perfil de la empresa");
        toolbar.setTitleTextColor(0xFFF4F4F4);
        setSupportActionBar(toolbar);

        persona_contacto = findViewById(R.id.email);
        email= findViewById(R.id.nombre);
        telefono= findViewById(R.id.ocuapacion);
        nit= findViewById(R.id.ciudad);
        actividad_economica= findViewById(R.id.fnacimineto);
        cargo_ocupacion= findViewById(R.id.documento);
        direccion= findViewById(R.id.telefono);
        nombre_empresa= findViewById(R.id.textView14);

        usertodo = this.getSharedPreferences("todoempresa", MODE_PRIVATE);
        String Persona_contacto = usertodo.getString("Persona_contacto", "");
        String Email = usertodo.getString("Email", "");
        String Telefono = usertodo.getString("Telefono", "");
        String Nit = usertodo.getString("Nit", "");
        String Actividad_economica = usertodo.getString("Actividad_economica", "");
        String Cargo_ocupacion = usertodo.getString("Cargo_ocupacion", "");
        String Direccion = usertodo.getString("Direccion", "");
        String nombreempresa = usertodo.getString("Razon_social", "");

        persona_contacto.setText(Persona_contacto);
        email.setText(Email);
        telefono.setText(Telefono);
        nit.setText(Nit);
        actividad_economica.setText(Actividad_economica);
        cargo_ocupacion.setText(Cargo_ocupacion);
        direccion.setText(Direccion);
        nombre_empresa.setText(nombreempresa);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
