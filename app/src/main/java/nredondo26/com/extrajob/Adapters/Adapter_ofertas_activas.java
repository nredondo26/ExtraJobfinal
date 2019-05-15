package nredondo26.com.extrajob.Adapters;

import android.support.v7.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import nredondo26.com.extrajob.R;
import nredondo26.com.extrajob.modelos.Atributos_publicaciones_ofertas;
import static android.support.constraint.Constraints.TAG;


public class Adapter_ofertas_activas extends RecyclerView.Adapter<Adapter_ofertas_activas.ViewHolder> {

    private List<Atributos_publicaciones_ofertas> atributosList;
    private Context context;
    FirebaseFirestore BDraiz;
    private FirebaseAuth mAuth;

    public Adapter_ofertas_activas(List<Atributos_publicaciones_ofertas> atributosList, Context context) {
        this.atributosList = atributosList;
        this.context = context;
        this.BDraiz= FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public Adapter_ofertas_activas.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ofertas_activas, viewGroup, false);
        return new Adapter_ofertas_activas.ViewHolder(vista);
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull final Adapter_ofertas_activas.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.txt_titulo.setText(atributosList.get(i).getTitulo());
        viewHolder.txt_descipcion.setText(atributosList.get(i).getDescipcion());
        viewHolder.txt_direccion.setText(atributosList.get(i).getDireccion());
        viewHolder.txt_fecha.setText(atributosList.get(i).getFecha());
        viewHolder.txt_horario.setText(atributosList.get(i).getHorario());
        viewHolder.txt_remuneracion.setText(atributosList.get(i).getRemuneracion());
        viewHolder.bpostularse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirmacion de Ofertas")
                        .setMessage("Estas seguro que deseas postularse a esta oferta? ?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                assert user != null;
                                BDraiz.collection("postulaciones")
                                        .whereEqualTo("Id_postulante",user.getUid())
                                        .whereEqualTo("Id_oferta",atributosList.get(i).getId())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful() && Objects.requireNonNull(task.getResult()).size()>0) {
                                                    viewHolder.bpostularse.setEnabled(false);;
                                                    viewHolder.bpostularse.setText("Ya estas postulado");
                                                    Toast.makeText(context,"Ya se postulo anteriormente",Toast.LENGTH_LONG).show();
                                                } else {
                                                    Map<String, Object> postulacion = new HashMap<>();
                                                    postulacion.put("Id_oferta", atributosList.get(i).getId());
                                                    postulacion.put("Id_postulante",user.getUid());
                                                    BDraiz.collection("postulaciones").document()
                                                            .set(postulacion)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    viewHolder.bpostularse.setText("Ya estas postulado");
                                                                    viewHolder.bpostularse.setBackgroundColor(0x00FF00);

                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w(TAG, "Error writing document", e);
                                                                }
                                                            });
                                                    Toast.makeText(context,"Postulacion correcta ..",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context,"No se realizaron cambios",Toast.LENGTH_LONG).show();
                            }
                        }).show();
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
            carV_ofertas_activas = item.findViewById(R.id.carV_ofertas_activas);
        }
    }
}

