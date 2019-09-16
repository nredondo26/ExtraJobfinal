package nredondo26.com.extrajob;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Registroe extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText rs, nit, pc, ae, dir, emai, tele, tele2, pass, car, urlempresa;
    private FirebaseAuth mAuth;
    FirebaseFirestore BDraiz;
    ProgressDialog progressDoalog;
    Button bregistroe;
    Spinner spinner_ciudades, spinner_sectores, spinner_profesiones;
    String Token;
    FirebaseUser user;
    TextView terminos;
    CheckBox checkpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registroe);
        mAuth = FirebaseAuth.getInstance();
        BDraiz= FirebaseFirestore.getInstance();
        bregistroe = findViewById(R.id.bregistroe);
        urlempresa = findViewById(R.id.urlempresa);
        spinner_ciudades = findViewById(R.id.spinner_ciudades);
        spinner_sectores = findViewById(R.id.spinner_sect);
        spinner_profesiones = findViewById(R.id.spinner_profesiones);
        rs = findViewById(R.id.rs);
        nit = findViewById(R.id.nit);
        pc = findViewById(R.id.pc);
        terminos = findViewById(R.id.terminos);
        checkpass = findViewById(R.id.checkpass);
        dir = findViewById(R.id.dir);
        emai = findViewById(R.id.email);
        tele = findViewById(R.id.tele);
        tele2 = findViewById(R.id.tele2);
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ciudades_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ciudades.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapters = ArrayAdapter.createFromResource(this, R.array.sectores_array, android.R.layout.simple_spinner_item);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sectores.setAdapter(adapters);

        spinner_sectores.setOnItemSelectedListener(this);
        bregistroe.setOnClickListener(this);

        terminos.setText(Html.fromHtml("- extrajob.com.co"));

    }

    private void dialogo(){
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Enviando datos");
        progressDoalog.setTitle("Registrando...");
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
        String telefono2 = tele2.getText().toString();
        if (TextUtils.isEmpty(telefono2)) {
            tele2.setError("Requerido");
            valid = false;
        } else {
            tele2.setError(null);
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
            usuario.put("Razon_social", rs.getText().toString());
            usuario.put("Nit", nit.getText().toString());
            usuario.put("Nombre_responsable", pc.getText().toString());
            usuario.put("Cargo_ocupacion", car.getText().toString());
            usuario.put("sector", spinner_sectores.getSelectedItem().toString());
            usuario.put("profesion", spinner_profesiones.getSelectedItem().toString());
            usuario.put("Direccion", dir.getText().toString());
            usuario.put("ciudad", spinner_ciudades.getSelectedItem().toString());
            usuario.put("Email", Objects.requireNonNull(user.getEmail()));
            usuario.put("Telefono", tele.getText().toString());
            usuario.put("Telefono2", tele2.getText().toString());
            if (urlempresa.getText().equals("")) {
                usuario.put("urlempresa", "NULL");
            } else {
                usuario.put("urlempresa", urlempresa.getText().toString());
            }
            usuario.put("Fecharegistro", FieldValue.serverTimestamp());
            usuario.put("Tipo", "empresa");
            usuario.put("Token",Token);

            BDraiz.collection("empresa").document(user.getUid()).set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    Toast.makeText(getApplicationContext(), "Problemas con el Registro de la empresa", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if(v==bregistroe) {

            if (checkpass.isChecked()) {

                if (EstadoInternet.isOnline(Registroe.this)) {
                    createAccount(emai.getText().toString(), pass.getText().toString());
                } else {
                    Toast.makeText(this, "Necesita conectarse a internet", Toast.LENGTH_LONG).show();
                }
            } else {
                //  Log.e("prueba","se metio por el false");
                Toast.makeText(getApplicationContext(), "Debe aceptar los terminos y condiciones para continuar con el registro", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void profesiones(int profesiones) {

        if (profesiones == 0) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.RESTAURANTES, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_profesiones.setAdapter(adapterprofesiones);
        }
        if (profesiones == 1) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.HOTELERIA, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_profesiones.setAdapter(adapterprofesiones);
        }
        if (profesiones == 2) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.BARES, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_profesiones.setAdapter(adapterprofesiones);
        }
        if (profesiones == 3) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.EVENTOS, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_profesiones.setAdapter(adapterprofesiones);
        }
        if (profesiones == 4) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.RETAIL, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_profesiones.setAdapter(adapterprofesiones);
        }
        if (profesiones == 5) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.TRANSPORTE, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_profesiones.setAdapter(adapterprofesiones);
        }
        if (profesiones == 6) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.LIMPIEZA, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_profesiones.setAdapter(adapterprofesiones);
        }
        if (profesiones == 7) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.BELLEZA, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_profesiones.setAdapter(adapterprofesiones);
        }
        if (profesiones == 8) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.HOSPITALARIO, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_profesiones.setAdapter(adapterprofesiones);
        }
        if (profesiones == 9) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.MENSAJERIA, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_profesiones.setAdapter(adapterprofesiones);
        }
        if (profesiones == 10) {
            ArrayAdapter<CharSequence> adapterprofesiones = ArrayAdapter.createFromResource
                    (this, R.array.CONSTRUCCION, android.R.layout.simple_spinner_item);
            adapterprofesiones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_profesiones.setAdapter(adapterprofesiones);
        }

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


