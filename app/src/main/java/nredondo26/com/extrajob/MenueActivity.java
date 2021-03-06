package nredondo26.com.extrajob;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import java.util.Objects;

import nredondo26.com.extrajob.Adapters.Adapter_postulaciones_aceptadas;
import nredondo26.com.extrajob.modelos.Atributos_postulaciones_aceptadas;
import static android.support.constraint.Constraints.TAG;

public class MenueActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    Uri gfoto;
    preferencias preferencias;
    public List<Atributos_postulaciones_aceptadas> atributosList;
    public Adapter_postulaciones_aceptadas adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menue);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = FirebaseFirestore.getInstance();

        user = FirebaseAutenticacion.Auth_user();

        if(user.getPhotoUrl()==null){
            Log.e("Nohay foto","");
        }
     //   Log.e("foto",user.getPhotoUrl().toString());

        assert user != null;
        String email = getIntent().getStringExtra("email");
        String usuario = getIntent().getStringExtra("user");
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        mAuth = FirebaseAuth.getInstance();
        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        ImageView imageView = hView.findViewById(R.id.imgperfil);
        TextView nombre =  hView.findViewById(R.id.nombrem);
        TextView correo =  hView.findViewById(R.id.emailm);
        Glide.with(this).load(user.getPhotoUrl()).into(imageView);
        nombre.setText(usuario);
        correo.setText(email);
        navigationView.setNavigationItemSelectedListener(this);
        gfoto= user.getPhotoUrl();

        atributosList = new ArrayList<>();
        RecyclerView rv = findViewById(R.id.recyclerViewme);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        adapter = new Adapter_postulaciones_aceptadas(atributosList,this);
        rv.setAdapter(adapter);

        Leer_ofertas_aceptadas();
        Leerdocumentos();
    }

    private void Leer_ofertas_aceptadas() {

        db.collection("postulacionesaceptadas")
                .whereEqualTo("Id_postulante", user.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        assert value != null;
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("Id_oferta") != null) {
                                String id_oferta=doc.getString("Id_oferta");

                                assert id_oferta != null;
                                DocumentReference docRef = db.collection("ofertas").document(id_oferta);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            assert document != null;
                                            if (document.exists()) {
                                                String id_empresa= Objects.requireNonNull(Objects.requireNonNull(document.getData()).get("creador")).toString();
                                               final String titulo= Objects.requireNonNull(document.getData().get("titulo")).toString();
                                               final String valor= Objects.requireNonNull(document.getData().get("remuneracion")).toString();
                                               final String direccion= Objects.requireNonNull(document.getData().get("direccion")).toString();

                                               DocumentReference docRef = db.collection("empresa").document(id_empresa);
                                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            assert document != null;
                                                            if (document.exists()) {
                                                                String empresa= Objects.requireNonNull(document.getData().get("Razon_social")).toString();

                                                                llenaruno(titulo,empresa,valor,direccion);

                                                            } else {
                                                                Log.d(TAG, "No such document");
                                                            }
                                                        } else {
                                                            Log.d(TAG, "get failed with ", task.getException());
                                                        }
                                                    }
                                                });
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                            }
                        }
                       // Log.d(TAG, "Current cites in CA: " + cities);
                    }
                });

    }

    public void llenaruno(String titulo, String empresa,String valor,String direccion) {
        Atributos_postulaciones_aceptadas ofertas = new Atributos_postulaciones_aceptadas(titulo, empresa,valor,direccion);
        ofertas.setNombre_empresa(empresa);
        ofertas.setTitulo_oferta("Titulo: "+titulo);
        ofertas.setValor("Remuneracion: "+valor);
        ofertas.setDireccion("Direccion: "+direccion);
        atributosList.add(ofertas);
        adapter.notifyDataSetChanged();
    }

    public void Leerdocumentos(){

        DocumentReference docRef = db.collection("usuarios").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        String ema = (String) Objects.requireNonNull(document.getData()).get("Email");
                        String nom = (String)document.getData().get("Nombre");
                        String ocup = (String) document.getData().get("Ocupacion");
                        String ciud = (String) document.getData().get("Ciudad");
                        String fnac = (String)document.getData().get("Fnacimiento");
                        String docum = (String) document.getData().get("Documento");
                        String tele = (String) document.getData().get("Telefono");
                        preferencias = new preferencias();
                        preferencias.guardar_preferenica(ema,nom,ocup,1,getApplicationContext());
                        preferencias.guardar_preferenica_usuarios(getApplicationContext(),ema,nom,ocup,ciud,fnac,docum,tele);
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
        getMenuInflater().inflate(R.menu.menue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MenueActivity.this, PerfilActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_manage) {
            Intent intent = new Intent(this,Ofertas_Activas_Activity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_send) {
            mAuth.signOut();
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
