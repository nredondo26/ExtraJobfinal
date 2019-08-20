package nredondo26.com.extrajob;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity implements View.OnClickListener{
    EditText vemail,vpassword;
    private FirebaseAuth mAuth;
    ProgressDialog progressDoalog;
    FirebaseFirestore BDraiz;
    String tipo;
    SharedPreferences preferencia, preferencia_datos;
    FirebaseUser user;
    CheckBox recordarpass;
    preferencias preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        recordarpass = findViewById(R.id.checkpass);
        vemail=findViewById(R.id.editemail);
        vpassword=findViewById(R.id.editpass);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.blogin).setOnClickListener(this);
        findViewById(R.id.bregistro).setOnClickListener(this);
        BDraiz = FirebaseFirestore.getInstance();

        preferencia_datos = this.getSharedPreferences("recordaruser_pass", MODE_PRIVATE);
        String datos = preferencia_datos.getString("email", "");

        preferencia = this.getSharedPreferences("detallesusuario", MODE_PRIVATE);
        int tipo = preferencia .getInt("tipousuario", 0);

        if (!datos.equals("")) {
            Log.e("ver", "si hay datos en la preferencia");
            vemail.setText(preferencia_datos.getString("email", ""));
            vpassword.setText(preferencia_datos.getString("password", ""));
            recordarpass.setChecked(true);
        } else {
            recordarpass.setChecked(false);
        }


        if(FirebaseAutenticacion.Auth()){

            user=FirebaseAutenticacion.Auth_user();

            if(tipo==1){
                Intent intent = new Intent(getApplicationContext(),MenueActivity.class);
                intent.putExtra("email", user.getEmail());
                intent.putExtra("user", user.getDisplayName());
                startActivity(intent);
            }else{
                Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                intent.putExtra("email", user.getEmail());
                intent.putExtra("user", user.getDisplayName());
                startActivity(intent);
            }
        }

    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }
        dialogo();

        if (recordarpass.isChecked()) {
            preferencias = new preferencias();
            preferencias.Recordaruser_pass(vemail.getText().toString(), vpassword.getText().toString(), getApplicationContext());
        } else {
            preferencias = new preferencias();
            preferencias.eliminar_preferencia("recordaruser_pass", getApplicationContext());
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d("mensaje", "signInWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            DocumentReference docRef = BDraiz.collection("usuarios").document(user.getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        assert document != null;
                                        if (document.exists()) {
                                            tipo = document.getString("Tipo");
                                            assert tipo != null;
                                            if(tipo.equals("empleado")){

                                                Intent intent = new Intent(getApplicationContext(),MenueActivity.class);
                                                intent.putExtra("email", user.getEmail());
                                                intent.putExtra("user", user.getDisplayName());
                                                startActivity(intent);
                                                progressDoalog.dismiss();
                                                finish();
                                            }
                                            if(tipo.equals("empresa")){
                                                Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                                                intent.putExtra("email", user.getEmail());
                                                intent.putExtra("user", user.getDisplayName());
                                                startActivity(intent);
                                                progressDoalog.dismiss();
                                                finish();
                                            }
                                        } else {
                                            DocumentReference docRef = BDraiz.collection("empresa").document(user.getUid());
                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        assert document != null;
                                                        if (document.exists()) {
                                                            tipo = document.getString("Tipo");
                                                            assert tipo != null;
                                                            if(tipo.equals("empleado")){
                                                                Intent intent = new Intent(getApplicationContext(),MenueActivity.class);
                                                                intent.putExtra("email", user.getEmail());
                                                                intent.putExtra("user", user.getDisplayName());
                                                                startActivity(intent);
                                                                progressDoalog.dismiss();
                                                                finish();
                                                            }
                                                            if(tipo.equals("empresa")){
                                                                Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                                                                intent.putExtra("email", user.getEmail());
                                                                intent.putExtra("user", user.getDisplayName());
                                                                startActivity(intent);
                                                                progressDoalog.dismiss();
                                                                finish();
                                                            }
                                                        } else {
                                                            Log.d("TAG", "No such document");
                                                        }
                                                    } else {
                                                        Log.e("TAG", "get failed with ", task.getException());
                                                    }
                                                }
                                            });
                                        }
                                        progressDoalog.dismiss();
                                    } else {
                                        Log.e("TAG", "get failed with ", task.getException());
                                    }
                                }
                            });
                        } else {
                            Log.w("mesaje", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authenticacion fallida.", Toast.LENGTH_SHORT).show();
                            progressDoalog.dismiss();
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(Login.this, "Contraseña o email errado", Toast.LENGTH_SHORT).show();
                            progressDoalog.dismiss();
                        }
                    }
                });
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
        String email = vemail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            vemail.setError("Requerido");
            valid = false;
        } else {
            vemail.setError(null);
        }
        String password = vpassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            vpassword.setError("Requerido");
            valid = false;
        } else {
            vpassword.setError(null);
        }
        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
         if (i == R.id.blogin) {
             signIn(vemail.getText().toString(), vpassword.getText().toString());
         }
         if (i == R.id.bregistro) {
               Intent intent = new Intent(this,MainActivity.class);
               startActivity(intent);
         }
    }

}


