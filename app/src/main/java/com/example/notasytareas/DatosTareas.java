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
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DatosTareas extends AppCompatActivity implements TareasFragment.OnFragmentInteractionListener, View.OnClickListener {
    Button guardar;
    Button actualizar;

    EditText titulo;
    EditText descripcion;
    EditText fecha;
    EditText hora;

    private  int dia,mes,year,h,minutos;
    private String id = "";
    String fordate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_datos_tarea);
        mapear();
        insertar();
        actualizar();
        try {
            Bundle bundle = getIntent().getExtras();

            if (bundle.getString("operacion").equalsIgnoreCase("0")) {
                actualizar.setVisibility(View.INVISIBLE);

            } else if (bundle.getString("operacion").equalsIgnoreCase("1")) {
                id = bundle.getString("id");
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

                Tareas obj = new Tareas();
                obj.setTitulo(titulo.getText().toString());
                obj.setDescripcion(descripcion.getText().toString());
                obj.setFecha(fecha.getText().toString());
                obj.setHora(hora.getText().toString());
                obj.setFordate(fordate);
                System.out.println("1: "+obj.getFordate());

                atras.putExtra("mirecordatorio", obj);
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

                Tareas obj = new Tareas();
                obj.setId(Integer.parseInt(id));
                obj.setTitulo(titulo.getText().toString());
                obj.setDescripcion(descripcion.getText().toString());
                obj.setFecha(fecha.getText().toString());
                obj.setHora(hora.getText().toString());
                obj.setFordate(fordate);
                System.out.println("2: "+obj.getFordate());

                atras.putExtra("mirecordatorio", obj);
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
                    fordate = year + "-" + (monthOfYear+1)+ "-" + dayOfMonth;
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
