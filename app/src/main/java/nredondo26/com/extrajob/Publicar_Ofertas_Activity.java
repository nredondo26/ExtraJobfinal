package nredondo26.com.extrajob;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Publicar_Ofertas_Activity extends AppCompatActivity {

    EditText edittitulo,editdescripcion,edithorario,editdireccion,editremuneracion;
    TextView editcategoria,editfecha;
    private FirebaseAuth mAuth;
    FirebaseFirestore BDraiz;
    Button bregistrar_oferta,bcategoria;
    ImageButton bcalendario;
    ProgressDialog progressDoalog;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar__ofertas_);
        Toolbar toolbar =  findViewById(R.id.toolbar3);
        toolbar.setTitle("Publicar Oferta");
        toolbar.setTitleTextColor(0xFFF4F4F4);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        BDraiz= FirebaseFirestore.getInstance();
        edittitulo = findViewById(R.id.edittitulo);
        editdescripcion = findViewById(R.id.editdescripcion);
        editfecha = findViewById(R.id.editfecha);
        edithorario = findViewById(R.id.edithorario);
        editdireccion = findViewById(R.id.editdireccion);
        editremuneracion = findViewById(R.id.editremuneracion);
        editcategoria = findViewById(R.id.txtcategoria);

        bcalendario = findViewById(R.id.bcalendario2);
        bcalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONDAY);
                int mDay = c.get(Calendar.MONDAY);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Publicar_Ofertas_Activity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int mes = (monthOfYear + 1);
                                editfecha.setText(dayOfMonth + "-" + mes + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        bcategoria = findViewById(R.id.bbcategoria);
        bcategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EstadoInternet.isOnline(Publicar_Ofertas_Activity.this)){
                    createSimpleDialog(Publicar_Ofertas_Activity.this);
                }else{
                    Toast.makeText(Publicar_Ofertas_Activity.this,"Necesita conectarse a internet",Toast.LENGTH_LONG).show();
                }
            }
        });

        bregistrar_oferta = findViewById(R.id.bregistrar_oferta);
        bregistrar_oferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editcategoria.getText().equals("Seleccione Categoria")){
                    Toast.makeText(Publicar_Ofertas_Activity.this,"Debe selecionar una categoria",Toast.LENGTH_LONG).show();
                }else{
                    createAccount();
                }
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
        String categoria = editcategoria.getText().toString();
        if (TextUtils.isEmpty(categoria)) {
            editcategoria.setError("Requerido");
            valid = false;
        } else {
            editcategoria.setError(null);
        }
        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, PerfilempresaActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void Limpiar(){
        edittitulo.setText("");
        editdescripcion.setText("");
        editfecha.setText("");
        edithorario.setText("");
        editdireccion.setText("");
        editremuneracion.setText("");
        editcategoria.setText("");
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
        oferta.put("estado",0);
        oferta.put("categoria",editcategoria.getText().toString());
        oferta.put("creador", user.getUid());
        BDraiz.collection("ofertas").document()
                .set(oferta)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Registro de Oferta Exitoso",Toast.LENGTH_SHORT).show();
                        Limpiar();
                        Intent intent = new Intent(getApplicationContext(),Ofertas_Vigentes_Activity.class);
                        startActivity(intent);
                        finish();
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

    private void dialogodos() {
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Octeniendo Datos");
        progressDoalog.setTitle("Categoria Disponibles....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
    }

    void createSimpleDialog(final Context context) {
        dialogodos();
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
                builder.setTitle("categorias");
                builder.setItems(zona, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editcategoria.setText(zona[which]);
                    }
                });
                android.app.AlertDialog dialogIcon = builder.create();
                dialogIcon.show();
                progressDoalog.dismiss();
            }
        });
    }

}
