package nredondo26.com.extrajob;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

import javax.annotation.Nullable;
import nredondo26.com.extrajob.Adapters.Adapter_ofertas_activas;
import nredondo26.com.extrajob.modelos.Atributos_publicaciones_ofertas;
import static android.support.constraint.Constraints.TAG;

public class Ofertas_Activas_Activity extends AppCompatActivity {

    private RecyclerView rv;
    public List<Atributos_publicaciones_ofertas> atributosList;
    public Adapter_ofertas_activas adapter;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String ocupacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertas__activas_);

        mAuth = FirebaseAuth.getInstance();
        atributosList = new ArrayList<>();
        rv = findViewById(R.id.recyclerView2);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        adapter = new Adapter_ofertas_activas(atributosList,this);
        rv.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();

        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        octener_ocuapacion(user);

    }

    public void octener_ocuapacion(FirebaseUser user){
        DocumentReference docRef = db.collection("usuarios").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        ocupacion = String.valueOf(document.getData().get("Ocupacion"));

                        db.collection("ofertas")
                                .whereEqualTo("categoria",ocupacion )
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
                                                String idv= doc.getId();
                                                Llenar(titulov,descripcionv ,direccionv,fechav,horariov,remuneracionv,idv);
                                            }
                                        }
                                        Log.d(TAG, "Current cites in CA: " + ofertas);
                                    }
                                });


                    } else {
                        Log.d(TAG, "No such document");
                    }
                }
            }
        });

    }

    public void Leerdocumentos(){


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
