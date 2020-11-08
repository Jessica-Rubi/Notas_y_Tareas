package com.example.notasytareas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DatosTareas extends AppCompatActivity implements TareasFragment.OnFragmentInteractionListener, View.OnClickListener {
    Button guardar;
    Button actualizar;

    ImageButton btnVideo;
    EditText titulo;
    EditText descripcion;
    EditText fecha;
    EditText hora;
    // Variables.
    private String ruta_imagen; // La ruta de la imagen que el usuario eligio
    // para la imagen de su persona.
    private int SELECCIONAR_IMAGEN = 237487;

    private  int dia,mes,year,h,minutos;
    private String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_datos_tarea);
        mapear();
        insertar();
        actualizar();
//        btnVideo = findViewById(R.id.imageBtnVideoTarea);
//        btnVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent siguiente = new Intent(getApplication(),Multimedia.class);
//                siguiente.putExtra("operacion", "0");
//                startActivityForResult(siguiente,1000);
//            }
//        });
        /**
         * Al hacer click en el boton agregar Persona se abre una ventana para la edicion de una
         * nueva persona..
         */
        try {
            Bundle bundle = getIntent().getExtras();

            if (bundle.getString("operacion").equalsIgnoreCase("0")) {
                actualizar.setVisibility(View.INVISIBLE);

            } else if (bundle.getString("operacion").equalsIgnoreCase("1")) {
                id = bundle.getString("id");
                //Toast.makeText(getBaseContext(),id,Toast.LENGTH_SHORT).show();
                titulo.setText(bundle.getString("titulo"));
                descripcion.setText(bundle.getString("descripcion"));
                fecha.setText(bundle.getString("fecha"));
                hora.setText(bundle.getString("hora"));
                guardar.setVisibility(View.INVISIBLE);
            }
        }catch (Exception err){}
    }

    final Calendar c= Calendar.getInstance();

    public void mapear(){
        actualizar = (Button)findViewById(R.id.btnactualizar);

        titulo = (EditText) findViewById(R.id.txttitulo);
        descripcion = (EditText) findViewById(R.id.txtdescripcion);
        fecha = (EditText) findViewById(R.id.txtfecha);
        fecha.setOnClickListener(this);
        hora   = (EditText) findViewById(R.id.txthora);
        hora.setOnClickListener(this);
    }

    public void insertar(){
        guardar=(Button)findViewById(R.id.btnguardarTarea);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent atras = new Intent();

                Tareas alum = new Tareas();
                alum.setTitulo(titulo.getText().toString());
                alum.setDescripcion(descripcion.getText().toString());
                alum.setFecha(fecha.getText().toString());
                alum.setHora(hora.getText().toString());

                atras.putExtra("mirecordatorio", alum);

                setResult(RESULT_OK, atras);
                finish();


            }
        });
    }

    public void actualizar(){
        actualizar=(Button)findViewById(R.id.btnactualizar);
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent atras = new Intent();

                Tareas alum = new Tareas();

                alum.setId(Integer.parseInt(id));
                alum.setTitulo(titulo.getText().toString());
                alum.setDescripcion(descripcion.getText().toString());
                alum.setFecha(fecha.getText().toString());
                alum.setHora(hora.getText().toString());

                atras.putExtra("mirecordatorio", alum);

                setResult(RESULT_OK, atras);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view==fecha){
            final Calendar c= Calendar.getInstance();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            year=c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    fecha.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                }
            }
                    ,year,mes,dia);
            datePickerDialog.show();
        }
        if (view==hora){
            final Calendar c= Calendar.getInstance();
            h=c.get(Calendar.HOUR_OF_DAY);
            minutos=c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hora.setText(hourOfDay+":"+minute);
                }
            },h,minutos,false);
            timePickerDialog.show();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
