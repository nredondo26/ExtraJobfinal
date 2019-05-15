package nredondo26.com.extrajob;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class PerfilActivity extends AppCompatActivity {

    SharedPreferences usertodo;
    TextView email,nombre,ocupacion,ciudad,fnaci,documen,telef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar =  findViewById(R.id.toolbar4);
        toolbar.setTitle("Perfil de Usuario");
        toolbar.setTitleTextColor(0xFFF4F4F4);
        setSupportActionBar(toolbar);

        email= findViewById(R.id.email);
        nombre= findViewById(R.id.nombre);
        ocupacion= findViewById(R.id.ocuapacion);
        ciudad= findViewById(R.id.ciudad);
        fnaci= findViewById(R.id.fnacimineto);
        documen= findViewById(R.id.documento);
        telef= findViewById(R.id.telefono);

        usertodo = this.getSharedPreferences("todousuario", MODE_PRIVATE);
        String Email = usertodo.getString("Email", "");
        String Nombre = usertodo.getString("Nombre", "");
        String Ocupacion = usertodo.getString("Ocupacion", "");
        String Ciudad = usertodo.getString("Ciudad", "");
        String Fnacimiento = usertodo.getString("Fnacimiento", "");
        String Documento = usertodo.getString("Documento", "");
        String Telefono = usertodo.getString("Telefono", "");

        email.setText(Email);
        nombre.setText(Nombre);
        ocupacion.setText(Ocupacion);
        ciudad.setText(Ciudad);
        fnaci.setText(Fnacimiento);
        documen.setText(Documento);
        telef.setText(Telefono);

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
