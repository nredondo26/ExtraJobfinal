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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;


public class Registroe extends AppCompatActivity implements View.OnClickListener{

    EditText rs,nit,pc,ae,dir,emai,tele,pass,car;
    private FirebaseAuth mAuth;
    FirebaseFirestore BDraiz;
    ProgressDialog progressDoalog;
    Button bregistroe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registroe);

        mAuth = FirebaseAuth.getInstance();
        BDraiz= FirebaseFirestore.getInstance();

        bregistroe = findViewById(R.id.bregistroe);
        bregistroe.setOnClickListener(this);

        rs = findViewById(R.id.rs);
        nit = findViewById(R.id.nit);
        pc = findViewById(R.id.pc);
        ae = findViewById(R.id.ae);
        dir = findViewById(R.id.dir);
        emai = findViewById(R.id.email);
        tele = findViewById(R.id.tele);
        pass = findViewById(R.id.pass);
        car = findViewById(R.id.car);
    }


    private void dialogo(){
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Validando datos");
        progressDoalog.setTitle("validando");
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

    private void createAccount(String email, String password, String DisplayName) {
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

                            final FirebaseUser user = mAuth.getCurrentUser();

                            actulizar_perfil(rs.getText().toString());

                            Map<String, Object> usuario= new HashMap<>();
                            usuario.put("Nit", nit.getText().toString());
                            usuario.put("Persona_contacto", pc.getText().toString());
                            usuario.put("Actividad_economica", ae.getText().toString());
                            usuario.put("Direccion", dir.getText().toString());
                            usuario.put("Telefono", tele.getText().toString());
                            usuario.put("Cargo_ocupacion", car.getText().toString());
                            usuario.put("tipo", "empresa");

                            dialogo();

                            BDraiz.collection("empresa").document(user.getUid()).set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Registro Exitoso",Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                                    intent.putExtra("email", user.getEmail());
                                    intent.putExtra("user", user.getDisplayName());
                                    startActivity(intent);
                                    progressDoalog.dismiss();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"Problemas con el Registro",Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("MENSAJE", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
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
        if(v==bregistroe){
            createAccount(emai.getText().toString(),pass.getText().toString(),rs.getText().toString());
        }
    }

}


