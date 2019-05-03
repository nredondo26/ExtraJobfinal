package nredondo26.com.extrajob.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.List;

import nredondo26.com.extrajob.R;
import nredondo26.com.extrajob.modelos.Atributos_postulantes;


public class Adapter_postulantes extends RecyclerView.Adapter<Adapter_postulantes.ViewHolder> {

    private List<Atributos_postulantes> atributosList;
    private Context context;

    public Adapter_postulantes(List<Atributos_postulantes> atributosList, Context context) {
        this.atributosList = atributosList;
        this.context = context;
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
       // viewHolder.foto.setText(atributosList.get(i).getDireccion());

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
        Button informacion;
        CardView carV_usuarios_postu;

        ViewHolder(View item){
            super(item);
            txt_nomnre = item.findViewById(R.id.txtnombre);
            txt_ocupacion = item.findViewById(R.id.textocupacion);
            foto = item.findViewById(R.id.fotoperfil);
            bid = item.findViewById(R.id.bseleccionar );
            informacion = item.findViewById(R.id.binfo);
            carV_usuarios_postu = item.findViewById(R.id.carV_usuarios_postu);
        }
    }

}
