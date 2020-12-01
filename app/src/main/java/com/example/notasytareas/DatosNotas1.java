package com.example.notasytareas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DatosNotas1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_datos_notas);
        Bundle bundle = getIntent().getExtras();
        String val = bundle.getString("operacion");
        String valId = bundle.getString("id");
        String valTi = bundle.getString("titulo");
        String valDe = bundle.getString("descripcion");

        Bundle args = new Bundle();
        args.putString("operacion1", val);
        args.putString("id1", valId);
        args.putString("titulo1", valTi);
        args.putString("descripcion1", valDe);
        DatosNotasFragment datosNotasFragment = new DatosNotasFragment();
        datosNotasFragment.setArguments(args);
        if(findViewById(R.id.datos_notas) != null &&
                getSupportFragmentManager().findFragmentById(R.id.datos_notas) == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.datos_notas, datosNotasFragment).commit();
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
            fragmentManager.beginTransaction().replace(R.id.datos_notas, archivoFragment).addToBackStack(null).commit();
        }else if(tipo == 2){
            bundle.putString("urlImg", uri);
            ImagenFragment imagenFragment = new ImagenFragment();
            imagenFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.beginTransaction().replace(R.id.datos_notas, imagenFragment).addToBackStack(null).commit();
        }else if(tipo == 3){
            bundle.putString("urlVid", uri);
            VideoFragment videoFragment = new VideoFragment();
            videoFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.beginTransaction().replace(R.id.datos_notas, videoFragment).addToBackStack(null).commit();
        }
    }
}
