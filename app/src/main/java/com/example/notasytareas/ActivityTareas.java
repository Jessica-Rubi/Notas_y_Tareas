package com.example.notasytareas;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityTareas extends AppCompatActivity {
    ListView lista;
    EditText txtbuscar;
    Button BotonCambioANotas;
    android.content.res.Resources res;

    String operaciones[];
    String operacionesv2[];

    private Tareas tarea;
    ListaAdapterT adapter;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tareas);
        res = getResources();
        operaciones = new String[] {res.getString(R.string.Actualizar), res.getString(R.string.Eliminar), res.getString(R.string.Marcar)};
        operacionesv2 = new String[] {res.getString(R.string.Actualizar), res.getString(R.string.Eliminar), res.getString(R.string.Desmarcar)};
        BotonCambioANotas = findViewById(R.id.btnNotas);
        txtbuscar = (EditText) findViewById(R.id.txtbuscarTarea);
        lista = (ListView) findViewById(R.id.listaTareas);
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                tarea = new Tareas();
                tarea.setId(adp.getItem(i).getId());
                tarea.setTitulo(adp.getItem(i).getTitulo());
                tarea.setDescripcion(adp.getItem(i).getDescripcion());
                tarea.setFecha(adp.getItem(i).getFecha());
                tarea.setHora(adp.getItem(i).getHora());
                tarea.setFordate(adp.getItem(i).getFordate());
                tarea.setCompletado(adp.getItem(i).getCompletado());
                position = i;
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
        BotonCambioANotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityTareas.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void btnList_click() {
        if(tarea.getCompletado().equals("no")){
            AlertDialog dialog =
                    new AlertDialog.Builder(this)
                            .setTitle(res.getString(R.string.Operacion))
                            .setIcon(R.mipmap.ic_launcher)
                            .setItems(operaciones, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (operaciones[which].equalsIgnoreCase(operaciones[0])) {
                                        Intent siguiente = new Intent(getApplication(), DatosTareas1.class);

                                        siguiente.putExtra("operacion", "1");
                                        siguiente.putExtra("id", tarea.getId() + "");
                                        siguiente.putExtra("titulo", tarea.getTitulo());
                                        siguiente.putExtra("descripcion", tarea.getDescripcion());
                                        siguiente.putExtra("fecha", tarea.getFecha());
                                        siguiente.putExtra("hora", tarea.getHora());
                                        siguiente.putExtra("completa", tarea.getCompletado());
                                        startActivityForResult(siguiente, 1001);
                                    }
                                    if (operaciones[which].equalsIgnoreCase(operaciones[1])) {
                                        confirmacion();
                                    }
                                    if (operaciones[which].equalsIgnoreCase(operaciones[2])) {
                                        DaoTareas dt = new DaoTareas(ActivityTareas.this);
                                        dt.updateC(tarea.getId(), "si");
                                        cargardatos();
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .create();
            dialog.show();
        }else {
            AlertDialog dialog =
                    new AlertDialog.Builder(this)
                            .setTitle(res.getString(R.string.Operacion))
                            .setIcon(R.mipmap.ic_launcher)
                            .setItems(operacionesv2, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (operacionesv2[which].equalsIgnoreCase(operacionesv2[0])) {
                                        Intent siguiente = new Intent(getApplication(), DatosTareas1.class);

                                        siguiente.putExtra("operacion", "1");
                                        siguiente.putExtra("id", tarea.getId() + "");
                                        siguiente.putExtra("titulo", tarea.getTitulo());
                                        siguiente.putExtra("descripcion", tarea.getDescripcion());
                                        siguiente.putExtra("fecha", tarea.getFecha());
                                        siguiente.putExtra("hora", tarea.getHora());
                                        siguiente.putExtra("completa", tarea.getCompletado());
                                        startActivityForResult(siguiente, 1001);
                                    }
                                    if (operacionesv2[which].equalsIgnoreCase(operacionesv2[1])) {
                                        confirmacion();
                                    }
                                    if (operacionesv2[which].equalsIgnoreCase(operacionesv2[2])) {
                                        DaoTareas dt = new DaoTareas(ActivityTareas.this);
                                        dt.updateC(tarea.getId(), "no");
                                        cargardatos();
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .create();
            dialog.show();
        }
    }

    public void confirmacion() {
        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(res.getString(R.string.Seguro))
                        .setIcon(android.R.drawable.ic_delete)
                        .setMessage(res.getString(R.string.Recuperar))
                        .setPositiveButton(res.getString(R.string.Aceptar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DaoTareas dao = new DaoTareas(getBaseContext());
                                if (dao.delete(tarea.getId() + "") > 0) {
                                    Toast.makeText(getBaseContext(), res.getString(R.string.TareaE), Toast.LENGTH_SHORT).show();
                                    cargardatos();
                                } else {
                                    Toast.makeText(getBaseContext(), res.getString(R.string.TareaNoE), Toast.LENGTH_SHORT).show();
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

    ArrayAdapter<Tareas> adp;
    int[] imagenes = {
            android.R.drawable.checkbox_off_background,
            android.R.drawable.checkbox_on_background
    };

    public void cargardatos() {
        DaoTareas dao = new DaoTareas(ActivityTareas.this);
        String[] titulos = new String[dao.getAll().size()];
        int[] listImagenes = new int[dao.getAll().size()];
        for (int k = 0; k < dao.getAll().size(); k++){
            titulos[k] = "Título: " + dao.getAll().get(k).getTitulo() +
            "\nDescripción: "+ dao.getAll().get(k).getDescripcion() +
                    "\nFecha: "+ dao.getAll().get(k).getFecha() +
                    "\nHora: "+ dao.getAll().get(k).getHora() +
                    "\n";
            if(dao.getAll().get(k).getCompletado().equals("si")){
                    listImagenes[k] = imagenes[1];
            }else if(dao.getAll().get(k).getCompletado().equals("no")){
                    listImagenes[k] = imagenes[0];
            }
        }
        adapter = new ListaAdapterT(this, titulos, listImagenes);
        adp = new ArrayAdapter<Tareas>(ActivityTareas.this,
                android.R.layout.simple_list_item_1, dao.getAll());
        lista.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1000) {
            try {
                Tareas objcontacto = (Tareas) data.getSerializableExtra("mirecordatorio");

                DaoTareas dao = new DaoTareas(ActivityTareas.this);
                if (dao.insert(new Tareas(objcontacto.getId(), objcontacto.getTitulo(), objcontacto.getDescripcion(), objcontacto.getFecha(), objcontacto.getHora(), objcontacto.getFordate(), objcontacto.getCompletado())) > 0) {
                    Toast.makeText(getBaseContext(), res.getString(R.string.TareaI), Toast.LENGTH_SHORT).show();
                    cargardatos();
                } else {
                    Toast.makeText(getBaseContext(), res.getString(R.string.TareaNoI), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception err) {
                Toast.makeText(getBaseContext(), err.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if ((resultCode == RESULT_OK && requestCode == 1001) || requestCode == 1002) {
            try {
                Tareas objcontacto = (Tareas) data.getSerializableExtra("mirecordatorio");

                DaoTareas dao = new DaoTareas(ActivityTareas.this);
                Tareas recordatorio = new Tareas();
                recordatorio.setId(objcontacto.getId());
                recordatorio.setTitulo(objcontacto.getTitulo());
                recordatorio.setDescripcion(objcontacto.getDescripcion());
                recordatorio.setFecha(objcontacto.getFecha());
                recordatorio.setHora(objcontacto.getHora());
                recordatorio.setFordate(objcontacto.getFordate());
                cargardatos();

                if (dao.update(recordatorio) > 0) {
                    Toast.makeText(getBaseContext(), res.getString(R.string.TareaA), Toast.LENGTH_SHORT).show();
                    cargardatos();
                } else {
                    Toast.makeText(getBaseContext(), res.getString(R.string.TareaNoA), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception err) {
                Toast.makeText(getBaseContext(), err.getMessage(), Toast.LENGTH_LONG).show();
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
            Intent siguiente = new Intent(getApplication(), DatosTareas1.class);
            siguiente.putExtra("operacion", "0");
            startActivityForResult(siguiente, 1000);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
