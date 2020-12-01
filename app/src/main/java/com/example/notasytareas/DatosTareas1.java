package com.example.notasytareas;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DatosTareas1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_datos_tareas);
        Bundle bundle = getIntent().getExtras();
        String val = bundle.getString("operacion");
        String valId = bundle.getString("id");
        String valTi = bundle.getString("titulo");
        String valDe = bundle.getString("descripcion");
        String valFe = bundle.getString("fecha");
        String valHo = bundle.getString("hora");
        String valCo = bundle.getString("completa");

        Bundle args = new Bundle();
        args.putString("operacion1", val);
        args.putString("id1", valId);
        args.putString("titulo1", valTi);
        args.putString("descripcion1", valDe);
        args.putString("fecha1", valFe);
        args.putString("hora1", valHo);
        args.putString("completa1", valCo);
        DatosTareasFragment datosTareasFragment = new DatosTareasFragment();
        datosTareasFragment.setArguments(args);
        if(findViewById(R.id.datos_tareas) != null &&
                getSupportFragmentManager().findFragmentById(R.id.datos_tareas) == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.datos_tareas, datosTareasFragment).commit();
        }
    }

    public void ver(String uri, int tipo){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        if(tipo == 1){
            bundle.putString("urlArc", uri);
            ArchivoFragment archivoFragment = new ArchivoFragment();
            archivoFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.beginTransaction().replace(R.id.datos_tareas, archivoFragment).addToBackStack(null).commit();
        }else if(tipo == 2){
            bundle.putString("urlImg", uri);
            ImagenFragment imagenFragment = new ImagenFragment();
            imagenFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.beginTransaction().replace(R.id.datos_tareas, imagenFragment).addToBackStack(null).commit();
        }else if(tipo == 3){
            bundle.putString("urlVid", uri);
            VideoFragment videoFragment = new VideoFragment();
            videoFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.beginTransaction().replace(R.id.datos_tareas, videoFragment).addToBackStack(null).commit();
        }
    }
}
