package com.example.notasytareas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListaAdapter extends BaseAdapter {
    // Declare Variables
    Context context;
    String[] titulos;
    int[] imagenes;
    LayoutInflater inflater;

    public ListaAdapter(Context context, String[] titulos, int[] imagenes) {
        this.context = context;
        this.titulos = titulos;
        this.imagenes = imagenes;
    }

    @Override
    public int getCount() {
        return titulos.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Declare Variables
        TextView txtTitle;
        ImageView imgImg;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.lista_formato, viewGroup, false);

        // Locate the TextViews in listview_item.xml
        txtTitle = (TextView) itemView.findViewById(R.id.tituloLista);
        imgImg = (ImageView) itemView.findViewById(R.id.iconLista);

        // Capture position and set to the TextViews
        txtTitle.setText(titulos[i]);
        imgImg.setImageResource(imagenes[i]);

        return itemView;
    }
}
