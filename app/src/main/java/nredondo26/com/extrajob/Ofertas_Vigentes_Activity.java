package nredondo26.com.extrajob;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import nredondo26.com.extrajob.Adapters.Adapter_publicacion_ofertas;
import nredondo26.com.extrajob.modelos.Atributos_publicaciones_ofertas;
import static android.support.constraint.Constraints.TAG;

public class Ofertas_Vigentes_Activity  extends AppCompatActivity {
    private RecyclerView rv;
    public List<Atributos_publicaciones_ofertas> atributosList;
    public Adapter_publicacion_ofertas adapter;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertas__vigentes_);
        Toolbar toolbar =  findViewById(R.id.toolbar4);
        toolbar.setTitle("Ofertas Vigentes");
        toolbar.setTitleTextColor(0xFFF4F4F4);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        atributosList = new ArrayList<>();
        rv = findViewById(R.id.recyclerView1);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        adapter = new Adapter_publicacion_ofertas(atributosList,this);
        rv.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        Leerdocumentos();
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

    public static void canselar_oferta(String documento,String coleccion){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(coleccion).document(documento)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void Leerdocumentos(){
        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        db.collection("ofertas")
                .whereEqualTo("creador", user.getUid())
                .whereEqualTo("estado", 0)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
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
