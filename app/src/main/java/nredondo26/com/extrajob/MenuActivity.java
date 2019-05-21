package nredondo26.com.extrajob;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import nredondo26.com.extrajob.Adapters.Adapter_ofertas_finalizadas;
import nredondo26.com.extrajob.modelos.Atributos_publicaciones_ofertas;
import static android.support.constraint.Constraints.TAG;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    preferencias preferencias;
    private RecyclerView rv;
    public List<Atributos_publicaciones_ofertas> atributosList;
    public Adapter_ofertas_finalizadas adapter;

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

        atributosList = new ArrayList<>();
        rv = findViewById(R.id.recyclerViewme2);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        adapter = new Adapter_ofertas_finalizadas(atributosList,this);
        rv.setAdapter(adapter);;

        Leerdocumentos1();

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
                        String rz = (String) document.getData().get("Razon_social");
                        preferencias = new preferencias();
                        preferencias.guardar_preferenica(Email,Persona_contacto,null,2,getApplicationContext());
                        preferencias.guardar_preferenica_empresa(getApplicationContext(),Persona_contacto,Email,Telefono,Nit,Actividad_economica,Cargo_ocupacion,Direccion,rz);

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

    public void Leerdocumentos1(){
        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        db.collection("ofertas")
                .whereEqualTo("creador", user.getUid())
                .whereEqualTo("estado", 1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        List<String> ofertas = new ArrayList<>();
                        assert value != null;
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("titulo") != null) {
                                String titulov= (String) doc.getData().get("titulo");
                                String descripcionv= (String) doc.getData().get("descripcion");
                                String direccionv= (String) doc.getData().get("direccion");
                                String fechav= (String) doc.getData().get("fecha");
                                String horariov= (String) doc.getData().get("horario");
                                String remuneracionv= (String) doc.getData().get("remuneracion");
                                String idv= (String) doc.getId();
                                Llenar(titulov,descripcionv ,direccionv,fechav,horariov,remuneracionv,idv);
                            }
                        }
                        Log.d(TAG, "Current cites in CA: " + ofertas);
                    }
                });
    }

    public void Llenar(String titulo, String descrip, String dire, String fech, String horar, String remune, String id) {
        Atributos_publicaciones_ofertas ofertas = new Atributos_publicaciones_ofertas(titulo, descrip, dire, fech, horar, remune,id);
        ofertas.setTitulo(titulo);
        ofertas.setDescipcion(descrip);
        ofertas.setDireccion("Direccion: "+dire);
        ofertas.setFecha("Fecha: "+fech);
        ofertas.setHorario( "Horario: "+horar);
        ofertas.setRemuneracion("Valor: "+remune+" Mil Pesos");
        ofertas.setId(id);
        atributosList.add(ofertas);
        adapter.notifyDataSetChanged();
    }
}
