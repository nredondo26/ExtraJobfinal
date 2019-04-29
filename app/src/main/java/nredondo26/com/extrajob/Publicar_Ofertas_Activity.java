package nredondo26.com.extrajob;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Publicar_Ofertas_Activity extends AppCompatActivity {

    EditText edittitulo,editdescripcion,editfecha,edithorario,editdireccion,editremuneracion;
    private FirebaseAuth mAuth;
    FirebaseFirestore BDraiz;
    Button bregistrar_oferta;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar__ofertas_);

        mAuth = FirebaseAuth.getInstance();
        BDraiz= FirebaseFirestore.getInstance();
        edittitulo = findViewById(R.id.edittitulo);
        editdescripcion = findViewById(R.id.editdescripcion);
        editfecha = findViewById(R.id.editfecha);
        edithorario = findViewById(R.id.edithorario);
        editdireccion = findViewById(R.id.editdireccion);
        editremuneracion = findViewById(R.id.editremuneracion);
        bregistrar_oferta = findViewById(R.id.bregistrar_oferta);
        bregistrar_oferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });


    }

    private boolean validateForm() {
        boolean valid = true;
        String titulo = edittitulo.getText().toString();
        if (TextUtils.isEmpty(titulo)) {
            edittitulo.setError("Requerido");
            valid = false;
        } else {
            edittitulo.setError(null);
        }
        String descripcion = editdescripcion.getText().toString();
        if (TextUtils.isEmpty(descripcion)) {
            editdescripcion.setError("Requerido");
            valid = false;
        } else {
            editdescripcion.setError(null);
        }
        String fecha =  editfecha.getText().toString();
        if (TextUtils.isEmpty(fecha)) {
            editfecha.setError("Requerido");
            valid = false;
        } else {
            editfecha.setError(null);
        }
        String horario = edithorario.getText().toString();
        if (TextUtils.isEmpty(horario)) {
            edithorario.setError("Requerido");
            valid = false;
        } else {
            edithorario.setError(null);
        }
        String direccion = editdireccion.getText().toString();
        if (TextUtils.isEmpty(direccion)) {
            editdireccion.setError("Requerido");
            valid = false;
        } else {
            editdireccion.setError(null);
        }

        String remuneracion = editremuneracion.getText().toString();
        if (TextUtils.isEmpty(remuneracion)) {
            editremuneracion.setError("Requerido");
            valid = false;
        } else {
            editremuneracion.setError(null);
        }

        return valid;
    }

    private void Limpiar(){
        edittitulo.setText("");
        editdescripcion.setText("");
        editfecha.setText("");
        edithorario.setText("");
        editdireccion.setText("");
        editremuneracion.setText("");
    }

    private void createAccount() {

        if (!validateForm()) {
            return;
        }

        dialogo();

        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;

        Map<String, Object> oferta= new HashMap<>();
        oferta.put("titulo", edittitulo.getText().toString());
        oferta.put("descripcion", editdescripcion.getText().toString());
        oferta.put("fecha", editfecha.getText().toString());
        oferta.put("horario", edithorario.getText().toString());
        oferta.put("direccion", editdireccion.getText().toString());
        oferta.put("remuneracion", editremuneracion.getText().toString());
        oferta.put("estado", 0);
        oferta.put("creador", user.getUid());

        Random rand = new Random();
        int n = rand.nextInt(20);

        BDraiz.collection("ofertas").document()
                .set(oferta)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Registro de Oferta Exitoso",Toast.LENGTH_SHORT).show();
                        Limpiar();
                        Intent intent = new Intent(getApplicationContext(),Ofertas_Vigentes_Activity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Problemas con el Registro",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void dialogo(){
        progressDoalog = new ProgressDialog(Publicar_Ofertas_Activity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Enviando datos");
        progressDoalog.setTitle("Registando...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
    }

}
