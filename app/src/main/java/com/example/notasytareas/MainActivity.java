package com.example.notasytareas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ListView lista;
    EditText txtbuscar;
    Button BotonCambioATareas;
    android.content.res.Resources res;
    String operaciones[];
    private Notas note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res = getResources();
        operaciones = new String[] {res.getString(R.string.Actualizar), res.getString(R.string.Eliminar)};
        BotonCambioATareas = findViewById(R.id.btnTareas);
        BotonCambioATareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ActivityTareas.class);
                startActivity(intent);
                finish();
            }
        });
        txtbuscar = (EditText) findViewById(R.id.txtbuscar);
        lista =(ListView)findViewById(R.id.listaNotas);
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                note= new Notas();
                note.setId(adp.getItem(i).getId());
                note.setTitulo(adp.getItem(i).getTitulo());
                note.setDescripcion(adp.getItem(i).getDescripcion());
                btnList_click();
                return false;
            }
        });
        cargardatos();

        txtbuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adp.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void  btnList_click(){
        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(res.getString(R.string.Operacion))
                        .setIcon(R.mipmap.ic_launcher)
                        .setItems(operaciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(operaciones[which].equalsIgnoreCase(operaciones[0])){
                                    Intent siguiente = new Intent(getApplication(), DatosNotas.class);

                                    siguiente.putExtra("operacion", "1");
                                    siguiente.putExtra("id", note.getId()+"");
                                    siguiente.putExtra("titulo", note.getTitulo());
                                    siguiente.putExtra("descripcion", note.getDescripcion());
                                    startActivityForResult(siguiente,1001);
                                }
                                if(operaciones[which].equalsIgnoreCase(operaciones[1])){
                                    confirmacion();
                                }
                                dialog.dismiss();
                            }
                        })
                        .create();
        dialog.show();
    }

    public void confirmacion(){
        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(res.getString(R.string.Seguro))
                        .setIcon(android.R.drawable.ic_delete)
                        .setMessage(res.getString(R.string.Recuperar))
                        .setPositiveButton(res.getString(R.string.Aceptar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DaoNotas dao = new DaoNotas(getBaseContext());
                                DaoMultimedia daoM = new DaoMultimedia(getBaseContext());
                                if(dao.delete(note.getId()+"")>0){
                                    daoM.delete(note.getId()+"");
                                    Toast.makeText(getBaseContext(),res.getString(R.string.NotaE),Toast.LENGTH_SHORT).show();
                                    cargardatos();
                                }else{
                                    Toast.makeText(getBaseContext(),res.getString(R.string.NotaNoE),Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(res.getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
        dialog.show();
    }

    ArrayAdapter<Notas> adp;
    public void cargardatos(){
        DaoNotas dao = new DaoNotas(MainActivity.this);
        adp = new ArrayAdapter<Notas>(MainActivity.this,
                android.R.layout.simple_list_item_1,dao.getAll());
        lista.setAdapter(adp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode == 1000) {
            try {
                Notas objcontacto = (Notas) data.getSerializableExtra("minota");

                DaoNotas dao = new DaoNotas(MainActivity.this);
                if(dao.insert(new Notas(objcontacto.getId(),objcontacto.getTitulo(),objcontacto.getDescripcion()))>0) {
                    Toast.makeText(getBaseContext(), res.getString(R.string.NotaI), Toast.LENGTH_SHORT).show();
                    cargardatos();
                }else{
                    Toast.makeText(getBaseContext(), res.getString(R.string.NotaNoI), Toast.LENGTH_SHORT).show();
                }
            }catch (Exception err){
                Toast.makeText(getBaseContext(),err.getMessage(),Toast.LENGTH_LONG).show();
            }
        }else if((resultCode==RESULT_OK && requestCode == 1001) || requestCode == 1002){
            try {
                Notas objcontacto = (Notas) data.getSerializableExtra("minota");

                DaoNotas dao = new DaoNotas(MainActivity.this);
                Notas note = new Notas();
                note.setId(objcontacto.getId());
                note.setTitulo(objcontacto.getTitulo());
                note.setDescripcion(objcontacto.getDescripcion());
                cargardatos();

                if(dao.update(note)>0) {
                    Toast.makeText(getBaseContext(), res.getString(R.string.NotaA), Toast.LENGTH_SHORT).show();
                    cargardatos();
                }else{
                    Toast.makeText(getBaseContext(), res.getString(R.string.NotaNoA), Toast.LENGTH_SHORT).show();
                }
            }catch (Exception err){
                Toast.makeText(getBaseContext(),err.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.insertar) {
            Intent siguiente = new Intent(getApplication(),DatosNotas.class);
            siguiente.putExtra("operacion", "0");
            startActivityForResult(siguiente,1000);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}