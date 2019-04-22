package nredondo26.com.extrajob;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener{

    EditText vemail,vpassword;
    private FirebaseAuth mAuth;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        vemail=findViewById(R.id.editemail);
        vpassword=findViewById(R.id.editpass);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.blogin).setOnClickListener(this);
        findViewById(R.id.bregistro).setOnClickListener(this);
    }

    private void signIn(String email, String password) {
        Log.d("mensaje", "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        dialogo();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("mensaje", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // enviar datos a activi nueva despues del login

                            Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                            intent.putExtra("email", user.getEmail());
                            intent.putExtra("user", user.getDisplayName());
                            startActivity(intent);
                            finish();

                        } else {
                            Log.w("mesaje", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                        if (!task.isSuccessful()) {
                            Toast.makeText(Login.this, "Contraseña o email errado", Toast.LENGTH_SHORT).show();
                        }
                        progressDoalog.dismiss();
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


