package nredondo26.com.extrajob.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import nredondo26.com.extrajob.R;
import nredondo26.com.extrajob.modelos.Atributos_postulaciones_aceptadas;

public class Adapter_postulaciones_aceptadas extends RecyclerView.Adapter<Adapter_postulaciones_aceptadas.ViewHolder> {

    private List<Atributos_postulaciones_aceptadas> atributosList;

    public  Adapter_postulaciones_aceptadas(List<Atributos_postulaciones_aceptadas> atributosList, Context context) {
        this.atributosList = atributosList;
    }

    @NonNull
    @Override
    public Adapter_postulaciones_aceptadas.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_postulados_listos, viewGroup, false);
        return new Adapter_postulaciones_aceptadas.ViewHolder(vista);
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull final Adapter_postulaciones_aceptadas.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.txttitulo.setText(atributosList.get(i).getTitulo_oferta());
        viewHolder.txtempresa.setText(atributosList.get(i).getNombre_empresa());
        viewHolder.txtvalor.setText(atributosList.get(i).getValor());
        viewHolder.txtdireccion.setText(atributosList.get(i).getDireccion());
    }

    @Override
    public int getItemCount() {
        return atributosList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txttitulo;
        TextView txtempresa;
        TextView txtvalor;
        TextView txtdireccion;
        CardView carV_listpostulados ;

        ViewHolder(View item){
            super(item);
            txttitulo = item.findViewById(R.id.txttitulo);
            txtempresa = item.findViewById(R.id.txtempresa);
            txtvalor = item.findViewById(R.id.txtvalor);
            txtdireccion = item.findViewById(R.id.txtdireccion);
            carV_listpostulados = item.findViewById(R.id.carV_listpostulados);
        }
    }


}
