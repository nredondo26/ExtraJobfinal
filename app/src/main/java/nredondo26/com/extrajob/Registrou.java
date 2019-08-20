package nredondo26.com.extrajob;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Registrou extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText editnombre, editidentificacion, editcelular, editemail, editpassword;
    TextView txtfecha, txtocupacion;
    Button bregistrou;
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
    Spinner spinner_documentos, selec3, selec4, spinner_ciudades;
    TextView terminos;
    CheckBox checkpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrou);
        mAuth = FirebaseAuth.getInstance();
        BDraiz = FirebaseFirestore.getInstance();

        spinner_documentos = findViewById(R.id.spinner_documentos);
        selec3 = findViewById(R.id.spinner_sect3);
        selec4 = findViewById(R.id.spinner_sect4);
        spinner_ciudades = findViewById(R.id.spinner_ciudades);
        editnombre = findViewById(R.id.nombre);
        editidentificacion = findViewById(R.id.identificacion);
        editcelular = findViewById(R.id.celular);
        terminos = findViewById(R.id.terminos);
        checkpass = findViewById(R.id.checkpass);
        editemail = findViewById(R.id.email2);
        editpassword = findViewById(R.id.pass);
        editnombre.requestFocus();

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

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.ciudades_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ciudades.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.documento, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_documentos.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapters3 = ArrayAdapter.createFromResource(this, R.array.sectores_array, android.R.layout.simple_spinner_item);
        adapters3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selec3.setAdapter(adapters3);

        selec3.setOnItemSelectedListener(this);

    }

    private void profesiones(int profesiones) {

        if (profesiones == 0) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.RESTAURANTES, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selec4.setAdapter(adapterprofesiones);
        }
        if (profesiones == 1) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.HOTELERIA, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selec4.setAdapter(adapterprofesiones);
        }
        if (profesiones == 2) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.BARES, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selec4.setAdapter(adapterprofesiones);
        }
        if (profesiones == 3) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.EVENTOS, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selec4.setAdapter(adapterprofesiones);
        }
        if (profesiones == 4) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.RETAIL, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selec4.setAdapter(adapterprofesiones);
        }
        if (profesiones == 5) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.TRANSPORTE, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selec4.setAdapter(adapterprofesiones);
        }
        if (profesiones == 6) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.LIMPIEZA, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selec4.setAdapter(adapterprofesiones);
        }
        if (profesiones == 7) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.BELLEZA, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selec4.setAdapter(adapterprofesiones);
        }
        if (profesiones == 8) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.HOSPITALARIO, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selec4.setAdapter(adapterprofesiones);
        }
        if (profesiones == 9) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.MENSAJERIA, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selec4.setAdapter(adapterprofesiones);
        }
        if (profesiones == 10) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.CONSTRUCCION, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selec4.setAdapter(adapterprofesiones);
        }

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
            usuario.put("Tipoidentificacion", spinner_documentos.getSelectedItem().toString());
            usuario.put("Identificacion", editidentificacion.getText().toString());
            usuario.put("Celular", editcelular.getText().toString());
            usuario.put("Email", Objects.requireNonNull(user.getEmail()));
            usuario.put("Sector", selec3.getSelectedItem().toString());
            usuario.put("Profesion", selec4.getSelectedItem().toString());
            usuario.put("Ciudad", spinner_ciudades.getSelectedItem().toString());
            usuario.put("Estado", 0);
            usuario.put("Fecharegistro", FieldValue.serverTimestamp());
            usuario.put("Token",Token);
            usuario.put("Tipo", "empleado");
            BDraiz.collection("usuarios").document(user.getUid()).set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Registro exitoso, te hemos enviado un correo para completar tu proceso.", Toast.LENGTH_SHORT).show();
                    progressDoalog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MenueActivity.class);
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("user", user.getDisplayName());
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Problemas con el Registro del usuario", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

        if (v == bregistrou) {
            if (checkpass.isChecked()) {
                if (EstadoInternet.isOnline(Registrou.this)) {
                    createAccount(editemail.getText().toString(), editpassword.getText().toString());
                } else {
                    Toast.makeText(this, "Necesita conectarse a internet", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Debe aceptar los terminos y condiciones para continuar con el registro", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dialogo() {
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMax(150);
        progressDoalog.setMessage("Enviando datos");
        progressDoalog.setTitle("Registrando...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
    }

    private boolean validateForm() {
        boolean valid = true;

        String nombre = editnombre.getText().toString();
        if (TextUtils.isEmpty(nombre)) {
            editnombre.setError("Requerido");
            valid = false;
        } else {
            editnombre.setError(null);
        }

        String documento = editidentificacion.getText().toString();
        if (TextUtils.isEmpty(documento)) {
            editidentificacion.setError("Requerido");
            valid = false;
        } else {
            editidentificacion.setError(null);
        }

        String telefono = editcelular.getText().toString();
        if (TextUtils.isEmpty(telefono)) {
            editcelular.setError("Requerido");
            valid = false;
        } else {
            editcelular.setError(null);
        }

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
        //adapterView.getItemAtPosition(i)
        switch (i) {
            case 0:
                profesiones(i);
                break;
            case 1:
                profesiones(i);
                break;
            case 2:
                profesiones(i);
                break;
            case 3:
                profesiones(i);
                break;
            case 4:
                profesiones(i);
                break;
            case 5:
                profesiones(i);
                break;
            case 6:
                profesiones(i);
                break;
            case 7:
                profesiones(i);
                break;
            case 8:
                profesiones(i);
                break;
            case 9:
                profesiones(i);
                break;
            case 10:
                profesiones(i);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
