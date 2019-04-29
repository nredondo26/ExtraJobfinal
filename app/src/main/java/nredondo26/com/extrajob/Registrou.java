package nredondo26.com.extrajob;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Registrou extends AppCompatActivity  implements View.OnClickListener {

    EditText editnombre, editdocumento, edittelefono, editemail, editciudad, editpassword;
    TextView txtfecha, txtocupacion;
    Button bregistrou, ocupacion;
    ImageButton calendario;
    private FirebaseAuth mAuth;
    FirebaseFirestore BDraiz;
    FirebaseStorage storage;
    ProgressDialog progressDoalog;
    String  resultado="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrou);

        mAuth = FirebaseAuth.getInstance();
        txtfecha = findViewById(R.id.txtfecha);
        calendario = findViewById(R.id.bcalendario);
        editemail = findViewById(R.id.editemail);
        editpassword = findViewById(R.id.editpassword);
        txtocupacion = findViewById(R.id.txtocupacion);
        editnombre = findViewById(R.id.editnombre);
        editdocumento = findViewById(R.id.editdocumento);
        ocupacion = findViewById(R.id.ocupacion);
        edittelefono = findViewById(R.id.edittelefono);
        editciudad = findViewById(R.id.editciudad);
        BDraiz = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        calendario.setOnClickListener(this);
        bregistrou = findViewById(R.id.bregistrou);
        bregistrou.setOnClickListener(this);
        ocupacion.setOnClickListener(this);

    }

    private void dialogo() {
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Enviando datos");
        progressDoalog.setTitle("Registando...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = editemail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editemail.setError("Requerido");
            valid = false;
        } else {
            editemail.setError(null);
        }
        String password = editpassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editpassword.setError("Requerido");
            valid = false;
        } else {
            editpassword.setError(null);
        }

        String nombre = editnombre.getText().toString();
        if (TextUtils.isEmpty(nombre)) {
            editnombre.setError("Requerido");
            valid = false;
        } else {
            editnombre.setError(null);
        }
        String documento = editdocumento.getText().toString();
        if (TextUtils.isEmpty(documento)) {
            editdocumento.setError("Requerido");
            valid = false;
        } else {
            editdocumento.setError(null);
        }

        String telefono = edittelefono.getText().toString();
        if (TextUtils.isEmpty(telefono)) {
            edittelefono.setError("Requerido");
            valid = false;
        } else {
            edittelefono.setError(null);
        }

        String ciudad = editciudad.getText().toString();
        if (TextUtils.isEmpty(ciudad)) {
            editciudad.setError("Requerido");
            valid = false;
        } else {
            editciudad.setError(null);
        }

        String fecha = txtfecha.getText().toString();
        if ("16-04-2019".equals(fecha)) {
            txtfecha.setError("Requerido");
            valid = false;
        } else {
            txtfecha.setError(null);
        }

        return valid;
    }

    private void createAccount(String email, String password) {
        Log.d("mensaje", "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        dialogo();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("MENSAJE", "createUserWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();

                            actulizar_perfil(editnombre.getText().toString());

                            Map<String, Object> usuario = new HashMap<>();
                            // usuario.put("Nombre", editnombre.getText().toString());
                            usuario.put("Documento", editdocumento.getText().toString());
                            usuario.put("Ocupacion", txtocupacion.getText().toString());
                            usuario.put("Telefono", edittelefono.getText().toString());
                            usuario.put("Ciudad", editciudad.getText().toString());
                            usuario.put("fnacimiento", txtfecha.getText().toString());
                            usuario.put("tipo", "empleado");

                            dialogo();

                            assert user != null;
                            BDraiz.collection("usuarios").document(user.getUid()).set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),MenueActivity.class);
                                    intent.putExtra("email", user.getEmail());
                                    intent.putExtra("user", user.getDisplayName());
                                    startActivity(intent);
                                    progressDoalog.dismiss();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Problemas con el Registro", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("MENSAJE", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Registrou.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                        progressDoalog.dismiss();
                    }
                });
        // [END create_user_with_email]
    }

    public void actulizar_perfil(String name){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                //   .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        assert user != null;
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User profile updated.");
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {

        if (v == calendario) {

            final Calendar c = Calendar.getInstance();

            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONDAY);
            int mDay = c.get(Calendar.MONDAY);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            int mes = (monthOfYear + 1);

                            txtfecha.setText(dayOfMonth + "-" + mes + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == bregistrou) {
            createAccount(editemail.getText().toString(), editpassword.getText().toString());
        }

        if (v == ocupacion) {
            resultado="";
            createSimpleDialog(this);
        }

    }

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
                                resultado+=zona[item]+"-";
                            }
                        })

                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("tag","valor:"+resultado);
                                String cadena = resultado.substring(0, resultado.length() - 1);
                                txtocupacion.setText(cadena);
                            }
                        });

                android.app.AlertDialog dialogIcon = builder.create();
                dialogIcon.show();
            }
        });

    }


}
