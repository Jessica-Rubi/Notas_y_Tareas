package com.example.notasytareas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class DatosNotas extends AppCompatActivity implements NotasFragment.OnFragmentInteractionListener, View.OnClickListener{
    Button guardar;
    Button actualizar;

    EditText titulo;
    EditText descripcion;

    private String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_datos);
        mapear();
        insertar();
        actualizar();

        try {
            Bundle bundle = getIntent().getExtras();

            if (bundle.getString("operacion").equalsIgnoreCase("0")) {
                actualizar.setVisibility(View.INVISIBLE);

            } else if (bundle.getString("operacion").equalsIgnoreCase("1")) {
                id = bundle.getString("id");
                //Toast.makeText(getBaseContext(),id,Toast.LENGTH_SHORT).show();
                titulo.setText(bundle.getString("titulo"));
                descripcion.setText(bundle.getString("descripcion"));
                guardar.setVisibility(View.INVISIBLE);
            }
        }catch (Exception err){}
    }

    public void mapear(){
        actualizar = (Button)findViewById(R.id.btnactualizar);
        titulo = (EditText) findViewById(R.id.txttitulo);
        descripcion = (EditText) findViewById(R.id.txtdescripcion);
    }

    public void insertar(){
        guardar=(Button)findViewById(R.id.btnguardarNota);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent atras = new Intent();

                Notas obj = new Notas();
                obj.setTitulo(titulo.getText().toString());
                obj.setDescripcion(descripcion.getText().toString());

                atras.putExtra("minota", obj);

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

                Notas obj = new Notas();
                obj.setId(Integer.parseInt(id));
                obj.setTitulo(titulo.getText().toString());
                obj.setDescripcion(descripcion.getText().toString());

                atras.putExtra("minota", obj);

                setResult(RESULT_OK, atras);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
