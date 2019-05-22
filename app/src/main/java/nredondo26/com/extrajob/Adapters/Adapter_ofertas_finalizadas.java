package nredondo26.com.extrajob.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

import nredondo26.com.extrajob.R;
import nredondo26.com.extrajob.modelos.Atributos_publicaciones_ofertas;

import static android.support.constraint.Constraints.TAG;

public class Adapter_ofertas_finalizadas extends RecyclerView.Adapter<Adapter_ofertas_finalizadas.ViewHolder>{
    private List<Atributos_publicaciones_ofertas> atributosList;
    private Context context;
    private FirebaseFirestore db;

    public Adapter_ofertas_finalizadas(List<Atributos_publicaciones_ofertas> atributosList, Context context) {
        this.atributosList = atributosList;
        this.context = context;
        this.db= FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public Adapter_ofertas_finalizadas.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ofertas_aceptadase, viewGroup, false);
        return new Adapter_ofertas_finalizadas.ViewHolder(vista);
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull final Adapter_ofertas_finalizadas.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.txt_titulo.setText(atributosList.get(i).getTitulo());
        viewHolder.txt_descipcion.setText(atributosList.get(i).getDescipcion());
        viewHolder.txt_direccion.setText(atributosList.get(i).getDireccion());
        viewHolder.txt_fecha.setText(atributosList.get(i).getFecha());
        viewHolder.txt_horario.setText(atributosList.get(i).getHorario());
        viewHolder.txt_remuneracion.setText(atributosList.get(i).getRemuneracion());
        viewHolder.bpostularse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_oferta=atributosList.get(i).getId();
                db.collection("postulacionesaceptadas")
                        .whereEqualTo("Id_oferta", id_oferta)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        String documento= Objects.requireNonNull(document.getData().get("Id_postulante")).toString();
                                        DocumentReference docRef = db.collection("usuarios").document(documento);
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    assert document != null;
                                                    if (document.exists()) {
                                                        Toast.makeText(context,"Su empleado es :"+document.getData().get("Nombre"),Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Log.d(TAG, "No such document");
                                                    }
                                                } else {
                                                    Log.d(TAG, "get failed with ", task.getException());
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return atributosList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_titulo;
        TextView txt_descipcion;
        TextView txt_direccion;
        TextView txt_fecha;
        TextView txt_horario;
        TextView txt_remuneracion;
        Button bpostularse;
        CardView carV_ofertas_activas;

        ViewHolder(View item){
            super(item);
            txt_titulo = item.findViewById(R.id.titulocm);
            txt_descipcion = item.findViewById(R.id.descripcioncm);
            txt_direccion = item.findViewById(R.id.direccioncm);
            txt_fecha = item.findViewById(R.id.fechacm);
            txt_horario = item.findViewById(R.id.horariocm);
            txt_remuneracion = item.findViewById(R.id.remuneracioncm);
            bpostularse = item.findViewById(R.id.bpostularse);
            carV_ofertas_activas = item.findViewById(R.id.carV_ofertas_finalizadas);
        }
    }
}

