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
import android.widget.Button;
import android.widget.EditText;
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


public class Registroe extends AppCompatActivity implements View.OnClickListener{

    EditText rs,nit,pc,ae,dir,emai,tele,pass,car;
    private FirebaseAuth mAuth;
    FirebaseFirestore BDraiz;
    ProgressDialog progressDoalog;
    Button bregistroe,bdocumentos,brepresentante,brut;
    int PICK_ARCHIVO_REQUEST = 110;
    int PICK_ARCHIVO_REQUEST1 = 111;
    int PICK_ARCHIVO_REQUEST2 = 112;
    Uri archivoUri,archivoUri1,archivoUri2;
    boolean ARCHIVO_STATUS = false;
    boolean ARCHIVO_STATUS1 = false;
    boolean ARCHIVO_STATUS2 = false;
    FirebaseStorage storage;
    String Token;
    FirebaseUser user;
    int valor =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registroe);
        mAuth = FirebaseAuth.getInstance();
        BDraiz= FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance("gs://extrajobapp-65826.appspot.com");
        bregistroe = findViewById(R.id.bregistroe);
        bregistroe.setOnClickListener(this);
        bdocumentos = findViewById(R.id.bdocumentos);
        bdocumentos.setOnClickListener(this);
        brepresentante = findViewById(R.id.brepresentante);
        brepresentante.setOnClickListener(this);
        brut = findViewById(R.id.brut);
        brut.setOnClickListener(this);
        brut.setOnClickListener(this);
        rs = findViewById(R.id.rs);
        nit = findViewById(R.id.nit);
        pc = findViewById(R.id.pc);
        ae = findViewById(R.id.ae);
        dir = findViewById(R.id.dir);
        emai = findViewById(R.id.email);
        tele = findViewById(R.id.tele);
        pass = findViewById(R.id.pass);
        car = findViewById(R.id.car);
        rs.requestFocus();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Token = instanceIdResult.getToken();
                Log.e("Token: ",Token);
            }
        });

    }

    private void dialogo(){
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Enviando datos");
        progressDoalog.setTitle("Registando...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
    }

    private boolean validateForm() {
        boolean valid = true;
        String email = emai.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emai.setError("Requerido");
            valid = false;
        } else {
            emai.setError(null);
        }
        String password = pass.getText().toString();
        if (TextUtils.isEmpty(password)) {
            pass.setError("Requerido");
            valid = false;
        } else {
            pass.setError(null);
        }
        String razonsocial = rs.getText().toString();
        if (TextUtils.isEmpty(razonsocial)) {
            rs.setError("Requerido");
            valid = false;
        } else {
            rs.setError(null);
        }
        String niit = nit.getText().toString();
        if (TextUtils.isEmpty(niit)) {
            nit.setError("Requerido");
            valid = false;
        } else {
            nit.setError(null);
        }
        String persona_contacto = pc.getText().toString();
        if (TextUtils.isEmpty(persona_contacto)) {
            pc.setError("Requerido");
            valid = false;
        } else {
            pc.setError(null);
        }
        String ocuapacion_cargo = car.getText().toString();
        if (TextUtils.isEmpty(ocuapacion_cargo)) {
            car.setError("Requerido");
            valid = false;
        } else {
            car.setError(null);
        }
        String actividad_economica = ae.getText().toString();
        if (TextUtils.isEmpty(actividad_economica)) {
            ae.setError("Requerido");
            valid = false;
        } else {
            ae.setError(null);
        }
        String direccion = dir.getText().toString();
        if (TextUtils.isEmpty(direccion)) {
            dir.setError("Requerido");
            valid = false;
        } else {
            dir.setError(null);
        }
        String telefono = tele.getText().toString();
        if (TextUtils.isEmpty(telefono)) {
            tele.setError("Requerido");
            valid = false;
        } else {
            tele.setError(null);
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
                            subir_archivo();
                        } else {
                            Log.w("MENSAJE", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void subir_archivo() {

        if(Token==null){
            Toast.makeText(this,"problemas con el token",Toast.LENGTH_LONG).show();
        }else{
            user = FirebaseAutenticacion.Auth_user();
            Map<String, Object> usuario= new HashMap<>();
            usuario.put("Nit", nit.getText().toString());
            usuario.put("Razon_social", rs.getText().toString() );
            usuario.put("Persona_contacto", pc.getText().toString());
            usuario.put("Actividad_economica", ae.getText().toString());
            usuario.put("Direccion", dir.getText().toString());
            usuario.put("Telefono", tele.getText().toString());
            usuario.put("Cargo_ocupacion", car.getText().toString());
            usuario.put("Fecharegistro", FieldValue.serverTimestamp());
            usuario.put("Tipo", "empresa");
            usuario.put("Email", Objects.requireNonNull(user.getEmail()));
            usuario.put("Token",Token);
            usuario.put("Documento","");
            usuario.put("Representantelegal","");
            usuario.put("Rut","");

            BDraiz.collection("empresa").document(user.getUid()).set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    recursivo_storage(archivoUri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Problemas con el Registro de la empresa", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public  void recursivo_storage(Uri arch){

        final StorageReference storageRef = storage.getReference();
        final StorageReference childRef;
        StorageReference childRef1 = null;

        if(valor==0){
            childRef1 = storageRef.child(user.getUid()+"/camaracomercio");
        }
        if(valor==1){
            childRef1 = storageRef.child(user.getUid()+"/representantelegal");
        }
        if(valor==2){
            childRef1 = storageRef.child(user.getUid()+"/rut");
        }

        childRef = childRef1;
        assert childRef != null;
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
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(rs.getText().toString()).build();
                        user.updateProfile(profileUpdates);
                        DocumentReference washingtonRef = BDraiz.collection("empresa").document(user.getUid());
                        assert downloadUri != null;
                        washingtonRef.update("Documento", downloadUri.toString());
                        valor=1;
                        recursivo_storage(archivoUri1);
                    }

                    if(valor==1){
                        DocumentReference washingtonRef = BDraiz.collection("empresa").document(user.getUid());
                        assert downloadUri != null;
                        washingtonRef.update("Representantelegal", downloadUri.toString());
                        valor=2;
                        recursivo_storage(archivoUri2);
                    }

                    if(valor==2){
                        DocumentReference washingtonRef = BDraiz.collection("empresa").document(user.getUid());
                        assert downloadUri != null;
                        washingtonRef.update("Rut", downloadUri.toString());
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

    @Override
    public void onClick(View v) {
        if(v==bregistroe) {

            if(EstadoInternet.isOnline(Registroe.this)){
                if (ARCHIVO_STATUS && ARCHIVO_STATUS1 && ARCHIVO_STATUS2) {
                    createAccount(emai.getText().toString(), pass.getText().toString());
                }
                if (!ARCHIVO_STATUS && ARCHIVO_STATUS1 && ARCHIVO_STATUS2) {
                    Toast.makeText(this, "Cargar camara de comercio en un PDF", Toast.LENGTH_LONG).show();
                }
                if (ARCHIVO_STATUS && !ARCHIVO_STATUS1 && ARCHIVO_STATUS2) {
                    Toast.makeText(this, "Cargar representante legal en un PDF", Toast.LENGTH_LONG).show();
                }
                if (ARCHIVO_STATUS && ARCHIVO_STATUS1 && !ARCHIVO_STATUS2) {
                    Toast.makeText(this, "Cargar rup", Toast.LENGTH_LONG).show();
                }
                if (ARCHIVO_STATUS && !ARCHIVO_STATUS1 && !ARCHIVO_STATUS2) {
                    Toast.makeText(this, "Cargar representante legal y rup en un PDF", Toast.LENGTH_LONG).show();
                }
                if (!ARCHIVO_STATUS && ARCHIVO_STATUS1 && !ARCHIVO_STATUS2) {
                    Toast.makeText(this, "Cargar camara de comercio y rup en un PDF", Toast.LENGTH_LONG).show();
                }
                if (!ARCHIVO_STATUS && !ARCHIVO_STATUS1 && ARCHIVO_STATUS2) {
                    Toast.makeText(this, "Cargar camara de comercio y representante legal en un PDF", Toast.LENGTH_LONG).show();
                }
                if(!ARCHIVO_STATUS && !ARCHIVO_STATUS1 && !ARCHIVO_STATUS2){
                    Toast.makeText(this,"Debe cargar los 3 archivos en un PDF",Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(this,"Necesita conectarse a internet",Toast.LENGTH_LONG).show();
            }

        }
        if(v==bdocumentos){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(Intent.createChooser(intent, "Seleccionar un word o pdf"), PICK_ARCHIVO_REQUEST);
        }
        if(v==brepresentante){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(Intent.createChooser(intent, "Seleccionar un word o pdf"), PICK_ARCHIVO_REQUEST1);
        }
        if(v==brut){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(Intent.createChooser(intent, "Seleccionar un word o pdf"), PICK_ARCHIVO_REQUEST2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_ARCHIVO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            archivoUri = data.getData();
            Log.e("archivourl:",""+archivoUri);
            try {
                ARCHIVO_STATUS=true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_ARCHIVO_REQUEST1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            archivoUri1 = data.getData();
            Log.e("archivourl:",""+archivoUri1);
            try {
                ARCHIVO_STATUS1=true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_ARCHIVO_REQUEST2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            archivoUri2 = data.getData();
            Log.e("archivourl:",""+archivoUri2);
            try {
                ARCHIVO_STATUS2=true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}


