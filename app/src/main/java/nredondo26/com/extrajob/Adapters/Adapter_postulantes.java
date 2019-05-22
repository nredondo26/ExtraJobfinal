package nredondo26.com.extrajob.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nredondo26.com.extrajob.R;
import nredondo26.com.extrajob.modelos.Atributos_postulantes;
import static android.support.constraint.Constraints.TAG;

public class Adapter_postulantes extends RecyclerView.Adapter<Adapter_postulantes.ViewHolder> {

    private List<Atributos_postulantes> atributosList;
    private Context context;
    private FirebaseFirestore BDraiz;

    public Adapter_postulantes(List<Atributos_postulantes> atributosList, Context context) {
        this.atributosList = atributosList;
        this.context = context;
        this.BDraiz= FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public Adapter_postulantes.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_usuarios_postulados, viewGroup, false);
        return new Adapter_postulantes.ViewHolder(vista);
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull final Adapter_postulantes.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.txt_nomnre.setText(atributosList.get(i).getNombre());
        viewHolder.txt_ocupacion.setText(atributosList.get(i).getOcupacion());
        Glide.with(context).load(atributosList.get(i).getFoto()).into(viewHolder.foto);
        viewHolder.informacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(atributosList.get(i).getInformacion());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

        viewHolder.seleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirmacion de Seleccion")
                        .setMessage("Estas seguro que deseas seleccionar este candidato !")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                assert user != null;
                                Map<String, Object> postulacionaceprtada = new HashMap<>();
                                postulacionaceprtada.put("Id_oferta", atributosList.get(i).getId()  );
                                postulacionaceprtada.put("Id_postulante",atributosList.get(i).getIdofertas());
                                BDraiz.collection("postulacionesaceptadas").document()
                                        .set(postulacionaceprtada)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                DocumentReference washingtonRef =  BDraiz.collection("ofertas").document(atributosList.get(i).getId());
                                                washingtonRef
                                                        .update("estado",1 )
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(context, "Candidato seleccionao correctamente ", Toast.LENGTH_SHORT).show();
                                                               }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("TAG", "Error updating document", e);
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error writing document", e);
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

        TextView txt_nomnre;
        TextView txt_ocupacion;
        ImageView foto;
        Button bid;
        Button informacion,seleccionar;
        CardView carV_usuarios_postu;

        @SuppressLint("CutPasteId")
        ViewHolder(View item){
            super(item);
            txt_nomnre = item.findViewById(R.id.txtnombre);
            txt_ocupacion = item.findViewById(R.id.textocupacion);
            foto = item.findViewById(R.id.fotoperfil);
            bid = item.findViewById(R.id.bseleccionar );
            informacion = item.findViewById(R.id.binfo);
            seleccionar = item.findViewById(R.id.bseleccionar);
            carV_usuarios_postu = item.findViewById(R.id.carV_usuarios_postu);
        }
    }

}
