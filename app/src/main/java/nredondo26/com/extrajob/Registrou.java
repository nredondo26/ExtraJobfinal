package nredondo26.com.extrajob;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Registrou extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText editnombre, editdocumento, edittelefono, editemail, editciudad, editpassword;
    TextView txtfecha, txtocupacion;
    Button bregistrou, ocupacion, sfoto,shvida;
    ImageButton calendario;
    private FirebaseAuth mAuth;
    FirebaseFirestore BDraiz;
    FirebaseStorage storage;
    ProgressDialog progressDoalog;
    boolean IMAGE_STATUS,ARCHIVO_STATUS = false;
    Uri imageUri,archivoUri;
    ImageView imagenv;
    FirebaseUser user;
    int valor =0;
    String Token;
    Spinner spinner_documentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrou);
        mAuth = FirebaseAuth.getInstance();
        BDraiz = FirebaseFirestore.getInstance();

        spinner_documentos = findViewById(R.id.spinner_documentos);

        bregistrou = findViewById(R.id.bregistrou);
        imagenv = findViewById(R.id.imageView2);

        bregistrou.setOnClickListener(this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Token = instanceIdResult.getToken();
                Log.e("Token: ",Token);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.documento, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_documentos.setAdapter(adapter);


    }

    public  void recursivo_storage(Uri arch){

        final StorageReference storageRef = storage.getReference();
        final StorageReference childRef;

        if(valor==0){
            childRef = storageRef.child(user.getUid()+"/fotosperfil");
        }else{
            childRef = storageRef.child(user.getUid()+"/hojadevida");
        }

        final UploadTask uploadTask = childRef.putFile(arch);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return childRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {

                    Uri downloadUri = task.getResult();

                    if(valor==0){
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        assert user != null;
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(editnombre.getText().toString()).setPhotoUri(downloadUri).build();
                        user.updateProfile(profileUpdates);

                        DocumentReference washingtonRef = BDraiz.collection("usuarios").document(user.getUid());
                        assert downloadUri != null;
                        washingtonRef.update("Foto", downloadUri.toString());

                        valor=1;
                        recursivo_storage(archivoUri);

                    }else{
                        DocumentReference washingtonRef = BDraiz.collection("usuarios").document(user.getUid());
                        assert downloadUri != null;
                        washingtonRef.update("Hoja_vida", downloadUri.toString());

                        Toast.makeText(getApplicationContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        progressDoalog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), MenueActivity.class);
                        intent.putExtra("email", user.getEmail());
                        intent.putExtra("user", user.getDisplayName());
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

    }

    public void subir_archivo() {

        if(Token==null){
            Toast.makeText(this,"problemas con el token",Toast.LENGTH_LONG).show();
        }else{
            user = FirebaseAutenticacion.Auth_user();
            Map<String, Object> usuario = new HashMap<>();
            usuario.put("Nombre",editnombre.getText().toString());
            usuario.put("Documento",editdocumento.getText().toString() );
            usuario.put("Ocupacion", txtocupacion.getText().toString());
            usuario.put("Telefono", edittelefono.getText().toString());
            usuario.put("Ciudad", editciudad.getText().toString());
            usuario.put("Fnacimiento", txtfecha.getText().toString());
            usuario.put("Estado", 0);
            usuario.put("Fecharegistro", FieldValue.serverTimestamp());
            usuario.put("Token",Token);
            usuario.put("Foto", "");
            usuario.put("Hoja_vida","");
            usuario.put("Tipo", "empleado");
            usuario.put("Email", Objects.requireNonNull(user.getEmail()));
            BDraiz.collection("usuarios").document(user.getUid()).set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    recursivo_storage(imageUri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Problemas con el Registro", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

       /* if (v == calendario) {
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
        }*/
        if (v == bregistrou) {

            if(EstadoInternet.isOnline(Registrou.this)){
                if (IMAGE_STATUS && ARCHIVO_STATUS){
                    createAccount(editemail.getText().toString(), editpassword.getText().toString());
                }
            }else{
                Toast.makeText(this,"Necesita conectarse a internet",Toast.LENGTH_LONG).show();
            }

        }

    }


    private void dialogo() {
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMax(150);
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
        if ("16-04-2019" .equals(fecha)) {
            txtfecha.setError("Requerido");
            valid = false;
        } else {
            txtfecha.setError(null);
        }
        return valid;
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }
        dialogo();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("MENSAJE", "createUserWithEmail:success");
                    subir_archivo();
                } else {
                    Log.w("MENSAJE", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(Registrou.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
                progressDoalog.dismiss();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
