package nredondo26.com.extrajob;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.support.constraint.Constraints.TAG;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    preferencias preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String email = getIntent().getStringExtra("email");
        String usuario = getIntent().getStringExtra("user");
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        ImageView imageView = hView.findViewById(R.id.imgperfil);
        TextView nombre =  hView.findViewById(R.id.nombrem);
        TextView correo =  hView.findViewById(R.id.emailm);
        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/extrajobapp-65826.appspot.com/o/iconoempresam.png?alt=media&token=490ffb05-c24f-44a1-b86b-56aefa7f1db9").into(imageView);
        nombre.setText(usuario);
        correo.setText(email);
        navigationView.setNavigationItemSelectedListener(this);

        Leerdocumentos();
    }

    public void Leerdocumentos(){

       DocumentReference docRef = db.collection("empresa").document(user.getUid());
        Log.e("",docRef.toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        String Persona_contacto = (String)document.getData().get("Persona_contacto");
                        String Email = (String) document.getData().get("Email");
                        String Telefono = (String) document.getData().get("Telefono");
                        String Nit = (String) document.getData().get("Nit");
                        String Actividad_economica = (String)document.getData().get("Actividad_economica");
                        String Cargo_ocupacion = (String) document.getData().get("Cargo_ocupacion");
                        String Direccion = (String) document.getData().get("Direccion");
                        preferencias = new preferencias();
                        preferencias.guardar_preferenica(Email,Persona_contacto,null,2,getApplicationContext());
                        preferencias.guardar_preferenica_empresa(getApplicationContext(),Persona_contacto,Email,Telefono,Nit,Actividad_economica,Cargo_ocupacion,Direccion);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            Intent intent = new Intent(MenuActivity.this, PerfilempresaActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_publicar) {
            Intent intent = new Intent(this,Publicar_Ofertas_Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_vigentes) {
            Intent intent = new Intent(this,Ofertas_Vigentes_Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_cerrar) {
            mAuth.signOut();
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
