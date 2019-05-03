package nredondo26.com.extrajob.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import nredondo26.com.extrajob.Ofertas_Vigentes_Activity;
import nredondo26.com.extrajob.R;
import nredondo26.com.extrajob.Usuarios_Postulados_Activity;
import nredondo26.com.extrajob.modelos.Atributos_publicaciones_ofertas;


public class Adapter_publicacion_ofertas extends RecyclerView.Adapter<Adapter_publicacion_ofertas.ViewHolder> {

    private List<Atributos_publicaciones_ofertas> atributosList;
    private Context context;

    public Adapter_publicacion_ofertas(List<Atributos_publicaciones_ofertas> atributosList, Context context) {
        this.atributosList = atributosList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ofertas_public, viewGroup, false);
        return new ViewHolder(vista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {

        viewHolder.txt_titulo.setText(atributosList.get(i).getTitulo());
        viewHolder.txt_descipcion.setText(atributosList.get(i).getDescipcion());
        viewHolder.txt_direccion.setText(atributosList.get(i).getDireccion());
        viewHolder.txt_fecha.setText(atributosList.get(i).getFecha());
        viewHolder.txt_horario.setText(atributosList.get(i).getHorario());
        viewHolder.txt_remuneracion.setText(atributosList.get(i).getRemuneracion());

        viewHolder.bcancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Confirmacion de Ofertas")
                        .setMessage("Estas seguro que deseas cancelar la oferta ?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Ofertas_Vigentes_Activity.canselar_oferta(atributosList.get(i).getId(),"ofertas");
                                Intent refresh = new Intent(context, Ofertas_Vigentes_Activity.class);
                                context.startActivity(refresh);
                                ((Activity) context).finish();
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

        viewHolder.carV_publicacion_ofertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Usuarios_Postulados_Activity.class);
                intent.putExtra("llave",atributosList.get(i).getId());
                context.startActivity(intent);

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
        Button bcancelar;
        CardView carV_publicacion_ofertas;

        ViewHolder(View item){
            super(item);
            txt_titulo = item.findViewById(R.id.titulocm);
            txt_descipcion = item.findViewById(R.id.descripcioncm);
            txt_direccion = item.findViewById(R.id.direccioncm);
            txt_fecha = item.findViewById(R.id.fechacm);
            txt_horario = item.findViewById(R.id.horariocm);
            txt_remuneracion = item.findViewById(R.id.remuneracioncm);
            bcancelar = item.findViewById(R.id.bcanselar);
            carV_publicacion_ofertas = item.findViewById(R.id.carV_publicacion_ofertas);
        }
    }


}
