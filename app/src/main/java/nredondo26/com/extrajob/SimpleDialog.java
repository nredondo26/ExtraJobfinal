package nredondo26.com.extrajob;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


class SimpleDialog  {

    public String  resultado;

    void createSimpleDialog(final Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference docRef = db.collection("categoriao");
        docRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                final String[] zona = new String[queryDocumentSnapshots.getDocuments().size()];

                for(int i=0; i<queryDocumentSnapshots.getDocuments().size(); i++){
                    zona[i]=queryDocumentSnapshots.getDocuments().get(i).getString("Nombre");
                }

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

                builder.setTitle("Ocupaciones").setMultiChoiceItems(zona, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {

                                resultado+="-"+zona[item];
                                Log.e("Dialog", "Op: " +resultado);

                            }
                        })

                        .setNegativeButton("Cancelar",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dialog != null) {
                                    dialog.cancel();
                                }
                            }
                        })

                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(resultado == null){
                                    Toast.makeText(context,"Debe selecionar almenos una ocupación",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                android.app.AlertDialog dialogIcon = builder.create();
                dialogIcon.show();
            }
        });

    }

}