package com.example.notasytareas;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Imagen extends AppCompatActivity {
    ImageView img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_imagen);
        String valor = getIntent().getExtras().getString("urlImg");
        Uri myUri = Uri.parse(valor);
        img = (ImageView) findViewById(R.id.imgImg);
        img.setImageURI(myUri);
    }
}
