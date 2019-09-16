package nredondo26.com.extrajob;

import android.annotation.SuppressLint;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

public class Publicar_Ofertas_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText edittitulo,editdescripcion,edithorario,editdireccion,editremuneracion;
    TextView editcategoria, fechaini, fechafin, fechavigencia;
    FirebaseFirestore BDraiz;
    Spinner spinner_sectores, spinner_profesiones;
    Button bregistrar_oferta;
    ProgressDialog progressDoalog;
    ImageButton calendarioini, calendariofin, calendariovigencia;
    String todohora;
    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    private FirebaseAuth mAuth;

    @SuppressLint({"ResourceAsColor", "WrongViewCast"})
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
        calendarioini = findViewById(R.id.calendarioini);
        calendariofin = findViewById(R.id.calendariofin);
        calendariovigencia = findViewById(R.id.calendariofin2);

        spinner_sectores = findViewById(R.id.spinner_sect2);
        spinner_profesiones = findViewById(R.id.spinner_profesiones2);
        editdescripcion = findViewById(R.id.editdescripcion);
        editdireccion = findViewById(R.id.editdireccion);
        editremuneracion = findViewById(R.id.editremuneracion);
        fechaini = findViewById(R.id.fechaini);
        fechafin = findViewById(R.id.fechafin);
        fechavigencia = findViewById(R.id.fechafin2);

        calendarioini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                @SuppressLint("WrongConstant") int mMonth = c.get(Calendar.MONDAY);
                @SuppressLint("WrongConstant") int mDay = c.get(Calendar.MONDAY);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Publicar_Ofertas_Activity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int mes = (monthOfYear + 1);
                                todohora = dayOfMonth + "-" + mes + "-" + year;
                                obtenerHorainicio();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        calendariofin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                @SuppressLint("WrongConstant") int mMonth = c.get(Calendar.MONDAY);
                @SuppressLint("WrongConstant") int mDay = c.get(Calendar.MONDAY);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Publicar_Ofertas_Activity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int mes = (monthOfYear + 1);
                                todohora = dayOfMonth + "-" + mes + "-" + year;
                                obtenerHorafin();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        calendariovigencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                @SuppressLint("WrongConstant") int mMonth = c.get(Calendar.MONDAY);
                @SuppressLint("WrongConstant") int mDay = c.get(Calendar.MONDAY);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Publicar_Ofertas_Activity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int mes = (monthOfYear + 1);
                                todohora = dayOfMonth + "-" + mes + "-" + year;
                                fechavigencia.setText(dayOfMonth + "-" + mes + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        ArrayAdapter<CharSequence> adapters = ArrayAdapter.createFromResource(this, R.array.sectores_array, android.R.layout.simple_spinner_item);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sectores.setAdapter(adapters);

        spinner_sectores.setOnItemSelectedListener(this);

        bregistrar_oferta = findViewById(R.id.bregistrar_oferta);
        bregistrar_oferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void obtenerHorainicio() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String AM_PM;
                        if (hourOfDay < 12) {
                            AM_PM = "a.m.";
                        } else {
                            AM_PM = "p.m.";
                        }
                        fechaini.setText(todohora + "-" + hourOfDay + ":" + minute + " " + AM_PM);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private void obtenerHorafin() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String AM_PM;
                        if (hourOfDay < 12) {
                            AM_PM = "a.m.";
                        } else {
                            AM_PM = "p.m.";
                        }
                        fechafin.setText(todohora + "-" + hourOfDay + ":" + minute + " " + AM_PM);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private boolean validateForm() {
        boolean valid = true;
        String descripcion = editdescripcion.getText().toString();
        if (TextUtils.isEmpty(descripcion)) {
            editdescripcion.setError("Requerido");
            valid = false;
        } else {
            editdescripcion.setError(null);
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
        String fechai = fechaini.getText().toString();
        if (TextUtils.isEmpty(fechai)) {
            fechaini.setError("Requerido");
            valid = false;
        } else {
            fechaini.setError(null);
        }
        String fechaf = fechafin.getText().toString();
        if (TextUtils.isEmpty(fechaf)) {
            fechafin.setError("Requerido");
            valid = false;
        } else {
            fechafin.setError(null);
        }
        String fechav = fechavigencia.getText().toString();
        if (TextUtils.isEmpty(fechav)) {
            fechavigencia.setError("Requerido");
            valid = false;
        } else {
            fechavigencia.setError(null);
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

    private void createAccount() {
        if (!validateForm()) {
            return;
        }
        dialogo();
        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        Map<String, Object> oferta= new HashMap<>();
        oferta.put("sector", spinner_sectores.getSelectedItem().toString());
        oferta.put("profesion", spinner_profesiones.getSelectedItem().toString());
        oferta.put("descripcion", editdescripcion.getText().toString());
        oferta.put("direccion", editdireccion.getText().toString());
        oferta.put("remuneracion", editremuneracion.getText().toString());
        oferta.put("fechaini", fechaini.getText().toString());
        oferta.put("fechafin", fechafin.getText().toString());
        oferta.put("fechavigencia", fechavigencia.getText().toString());
        oferta.put("estado",0);
        oferta.put("creador", user.getUid());
        BDraiz.collection("ofertas").document()
                .set(oferta)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDoalog.dismiss();
                        Toast.makeText(getApplicationContext(),"Registro de Oferta Exitoso",Toast.LENGTH_SHORT).show();
                        //Limpiar();
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
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
