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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import nredondo26.com.extrajob.Adapters.Adapter_postulantes;
import nredondo26.com.extrajob.modelos.Atributos_postulantes;


import static android.support.constraint.Constraints.TAG;

public class Usuarios_Postulados_Activity extends AppCompatActivity {

    public List<Atributos_postulantes> atributosList;
    public Adapter_postulantes adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios__postulados_);

        String llave = getIntent().getStringExtra("llave");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        atributosList = new ArrayList<>();
        RecyclerView rv = findViewById(R.id.recyclerView3);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        adapter = new Adapter_postulantes(atributosList,this);
        rv.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();

        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        ver_postulantes(llave);
    }

    public void Llenar(String nombre, String ocupacion,String foto) {

        Atributos_postulantes postulantes = new Atributos_postulantes(nombre,ocupacion,foto);
        postulantes.setNombre(nombre);
        postulantes.setOcupacion(ocupacion);
        postulantes.setFoto(foto);
       /* postulantes.setDireccion("Direccion: "+dire);
        postulantes.setFecha("Fecha: "+fech);
        postulantes.setHorario( "Horario: "+horar);
        postulantes.setRemuneracion("Valor: "+remune+" Mil Pesos");
        postulantes.setId(id);*/
        atributosList.add(postulantes);
        adapter.notifyDataSetChanged();
    }


    public void ver_postulantes(String valor){

        db.collection("postulaciones")
                .whereEqualTo("id_oferta",valor)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        List<String> postulantes = new ArrayList<>();
                        assert value != null;
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("id_oferta") != null) {
                               // String titulov= (String) doc.getData().get("id_oferta");
                                String descripcionv = (String) doc.getData().get("id_usuario");
                                res(descripcionv);
                            }
                        }
                        Log.d(TAG, "Current cites in CA: " + postulantes);
                    }
                });
    }

    public void res(final String descripcionv) {
        Log.e("valor",""+descripcionv);

                db.collection("usuarios").document(descripcionv.trim())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        String nombre= (String) document.getData().get("Nombre");
                        String documento= (String) document.getData().get("Ocupacion");
                        String foto= (String) document.getData().get("Foto");
                        Log.e(TAG, "DocumentSnapshot data: " + foto);

                        Llenar(nombre, documento,foto);

                        Log.e(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.e(TAG, "No such document");
                    }
                } else {
                    Log.e(TAG, "get failed with ", task.getException());
                }
            }
        });

    }



}
