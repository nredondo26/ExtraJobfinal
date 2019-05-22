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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class Registrou extends AppCompatActivity  implements View.OnClickListener {

    EditText editnombre, editdocumento, edittelefono, editemail, editciudad, editpassword;
    TextView txtfecha, txtocupacion;
    Button bregistrou, ocupacion, sfoto,shvida;
    ImageButton calendario;
    private FirebaseAuth mAuth;
    FirebaseFirestore BDraiz;
    FirebaseStorage storage;
    ProgressDialog progressDoalog;
    String resultado = "";
    boolean IMAGE_STATUS,ARCHIVO_STATUS = false;
    int PICK_IMAGE_REQUEST = 111;
    int PICK_ARCHIVO_REQUEST = 110;
    Uri imageUri,archivoUri;
    ImageView imagenv;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrou);
        mAuth = FirebaseAuth.getInstance();
        BDraiz = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance("gs://extrajobapp-65826.appspot.com");
        txtfecha = findViewById(R.id.txtfecha);
        calendario = findViewById(R.id.bcalendario);
        editemail = findViewById(R.id.editemail);
        editpassword = findViewById(R.id.editpassword);
        txtocupacion = findViewById(R.id.textocupacion);
        editnombre = findViewById(R.id.editnombre);
        editdocumento = findViewById(R.id.editdocumento);
        ocupacion = findViewById(R.id.bocupacion);
        sfoto = findViewById(R.id.bsubirfoto);
        edittelefono = findViewById(R.id.edittelefono);
        editciudad = findViewById(R.id.editciudad);
        bregistrou = findViewById(R.id.bregistrou);
        imagenv = findViewById(R.id.imageView2);
        shvida = findViewById(R.id.bsubirhojadevida);
        shvida.setOnClickListener(this);
        calendario.setOnClickListener(this);
        bregistrou.setOnClickListener(this);
        ocupacion.setOnClickListener(this);
        sfoto.setOnClickListener(this);
        editnombre.requestFocus();
    }

    public void subir_archivo(final String nombre_foto) {
        dialogo();
        if (imageUri != null &&  archivoUri !=null) {
            for (int i = 0; i<=1; i++) {

                if (i == 0) {
                    final StorageReference storageRef = storage.getReference();
                    final StorageReference childRef = storageRef.child(nombre_foto + ".jpg");
                    final UploadTask uploadTask = childRef.putFile(imageUri);
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
                                user = FirebaseAuth.getInstance().getCurrentUser();
                                assert user != null;
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(editnombre.getText().toString())
                                        .setPhotoUri(downloadUri)
                                        .build();
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("TAG", "User profile updated.");
                                                }
                                            }
                                        });

                                Map<String, Object> usuario = new HashMap<>();
                                usuario.put("Token","");
                                usuario.put("Nombre",editnombre.getText().toString());
                                usuario.put("Documento",editdocumento.getText().toString() );
                                usuario.put("Ocupacion", txtocupacion.getText().toString());
                                usuario.put("Telefono", edittelefono.getText().toString());
                                usuario.put("Ciudad", editciudad.getText().toString());
                                usuario.put("Fnacimiento", txtfecha.getText().toString());
                                usuario.put("Estado", 0);
                                usuario.put("Fecharegistro", FieldValue.serverTimestamp());
                                assert downloadUri != null;
                                usuario.put("Foto", downloadUri.toString());
                                usuario.put("Hoja_vida","");
                                usuario.put("Tipo", "empleado");
                                usuario.put("Email", Objects.requireNonNull(user.getEmail()));
                                BDraiz.collection("usuarios").document(user.getUid()).set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Problemas con el Registro", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }

                if(i==1){
                    final StorageReference storageRef = storage.getReference();
                    final StorageReference childRef = storageRef.child(nombre_foto + ".pdf");
                    final UploadTask uploadTask = childRef.putFile(archivoUri);
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

                                user = FirebaseAuth.getInstance().getCurrentUser();
                                final DocumentReference  washingtonRef =  BDraiz.collection("usuarios").document(user.getUid());
                                washingtonRef.update("Hoja_vida", Objects.requireNonNull(task.getResult()).toString());

                                FirebaseInstanceId.getInstance().getInstanceId()
                                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                if (!task.isSuccessful()) {
                                                    Log.w("token", "getInstanceId failed", task.getException());
                                                    return;
                                                }
                                                String token = Objects.requireNonNull(task.getResult()).getToken();
                                                washingtonRef.update("Token",token );

                                                Toast.makeText(getApplicationContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                                progressDoalog.dismiss();
                                                Intent intent = new Intent(getApplicationContext(), MenueActivity.class);
                                                intent.putExtra("email", user.getEmail());
                                                intent.putExtra("user", user.getDisplayName());
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                }
                        }
                    });
                }
                progressDoalog.dismiss();
            }
        } else {
            Toast.makeText(Registrou.this, "Debe seleccionar una foto y un archivo respectivamente", Toast.LENGTH_SHORT).show();
        }
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
            if (IMAGE_STATUS && ARCHIVO_STATUS){
                createAccount(editemail.getText().toString(), editpassword.getText().toString());
            }
            if(IMAGE_STATUS && !ARCHIVO_STATUS){
                Toast.makeText(getApplicationContext(),"Por favor cargue su hoja de vida",Toast.LENGTH_LONG).show();
            }
            if(!IMAGE_STATUS && ARCHIVO_STATUS){
                Toast.makeText(getApplicationContext(),"Por favor una imagen",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"deber cargar una foto y su hoja de vida",Toast.LENGTH_LONG).show();
            }
        }
        if (v == ocupacion) {
            resultado="";
            Octener_ocupacion(this);
        }
        if (v == sfoto) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), PICK_IMAGE_REQUEST);
        }
        if(v == shvida){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "Seleccionar un word o pdf"), PICK_ARCHIVO_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imagenv.setImageBitmap(bitmap);
                IMAGE_STATUS=true;
            } catch (Exception e) {
                e.printStackTrace();
             }
        }
        if (requestCode == PICK_ARCHIVO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            archivoUri = data.getData();
            Log.e("archivourl:",""+archivoUri);
            try {
                ARCHIVO_STATUS=true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void Octener_ocupacion(final Context context) {
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

                int itemSelected = 0;
                builder.setTitle("Ocupaciones").setSingleChoiceItems(zona, itemSelected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                                resultado+=zona[selectedIndex]+"-";
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
                progressDoalog.dismiss();
            }
        });
    }

    private void dialogodos() {
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Octeniendo Datos");
        progressDoalog.setTitle("Ocupaciones Disponibles....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
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
                    final FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    subir_archivo(user.getUid());
                } else {
                    Log.w("MENSAJE", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(Registrou.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
                progressDoalog.dismiss();
            }
        });
    }

}
