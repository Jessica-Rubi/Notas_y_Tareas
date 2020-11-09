package com.example.notasytareas;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityTareas extends AppCompatActivity {
    ListView lista;
    EditText txtbuscar;
    Button BotonCambioANotas;

    String operaciones[] =
            new String[]
                    {"Actualizar", "Eliminar", "Marcar como Completada"};

    private Tareas tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tareas);
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
        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Operacion a Realizar")
                        .setIcon(R.mipmap.ic_launcher)
                        .setItems(operaciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (operaciones[which].equalsIgnoreCase(operaciones[0])) {
                                    Intent siguiente = new Intent(getApplication(), DatosTareas.class);

                                    siguiente.putExtra("operacion", "1");
                                    siguiente.putExtra("id", tarea.getId() + "");
                                    siguiente.putExtra("titulo", tarea.getTitulo());
                                    siguiente.putExtra("descripcion", tarea.getDescripcion());
                                    siguiente.putExtra("fecha", tarea.getFecha());
                                    siguiente.putExtra("hora", tarea.getHora());
                                    startActivityForResult(siguiente, 1001);
                                }
                                if (operaciones[which].equalsIgnoreCase(operaciones[1])) {
                                    confirmacion();
                                }
                                dialog.dismiss();
                            }
                        })
                        .create();
        dialog.show();
    }

    public void confirmacion() {
        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Esta Seguro de Eliminar")
                        .setIcon(android.R.drawable.ic_delete)
                        .setMessage("Si elimina este registro no se podra recuperar")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DaoTareas dao = new DaoTareas(getBaseContext());
                                if (dao.delete(tarea.getId() + "") > 0) {
                                    Toast.makeText(getBaseContext(), "Recordatorio Eliminado", Toast.LENGTH_SHORT).show();
                                    cargardatos();
                                } else {
                                    Toast.makeText(getBaseContext(), "Recordatorio no Eliminado", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
        dialog.show();
    }

    ArrayAdapter<Tareas> adp;

    public void cargardatos() {
        DaoTareas dao = new DaoTareas(ActivityTareas.this);
        adp = new ArrayAdapter<Tareas>(ActivityTareas.this,
                android.R.layout.simple_list_item_1, dao.getAllNotificacines());
        lista.setAdapter(adp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1000) {
            try {
                Tareas objcontacto = (Tareas) data.getSerializableExtra("mirecordatorio");

                DaoTareas dao = new DaoTareas(ActivityTareas.this);
                if (dao.insert(new Tareas(objcontacto.getTitulo(), objcontacto.getDescripcion(), objcontacto.getFecha(), objcontacto.getHora(), objcontacto.getFordate())) > 0) {
                    Toast.makeText(getBaseContext(), "Recordatorio Insertado", Toast.LENGTH_SHORT).show();
                    cargardatos();
                } else {
                    Toast.makeText(getBaseContext(), "No se pudo Insertar el Recordatorio", Toast.LENGTH_SHORT).show();
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
                cargardatos();

                if (dao.update(recordatorio) > 0) {
                    Toast.makeText(getBaseContext(), "Recordatorio Actualizado", Toast.LENGTH_SHORT).show();
                    cargardatos();
                } else {
                    Toast.makeText(getBaseContext(), "No se pudo Actualizar el Recordatorio", Toast.LENGTH_SHORT).show();
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
            Intent siguiente = new Intent(getApplication(), DatosTareas.class);
            siguiente.putExtra("operacion", "0");
            startActivityForResult(siguiente, 1000);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
