package com.example.notasytareas;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatosNotas extends AppCompatActivity implements NotasFragment.OnFragmentInteractionListener, View.OnClickListener{
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final String LOG_TAG = "AudioRecordTest";
    private static String fileName = null;

    List<Multimedia> arcMul = new ArrayList<Multimedia>();
    List<Multimedia> arcMulAc = new ArrayList<Multimedia>();
    List<Multimedia> listMul = new ArrayList<Multimedia>();

    Button guardar;
    Button actualizar;
    ImageButton audio;
    ListView lista;

    boolean mStartRecording = true;
    boolean mStartPlaying = true;
    String timeStamp = "";
    String idTimeStamp = "";

    MediaRecorder recorder = null;
    MediaPlayer player = null;

    EditText titulo;
    EditText descripcion;

    private String id = "";
    private Multimedia Mulsaiv;
    int position;
    int b;
    String operaciones[] =
            new String[]
                    {"Reproducir", "Eliminar"};
    String operacionesv2[] =
            new String[]
                    {"Detener"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_datos);
        mapear();
        insertar();
        actualizar();
        grabar();
        try {
            Bundle bundle = getIntent().getExtras();

            if (bundle.getString("operacion").equalsIgnoreCase("0")) {
                actualizar.setVisibility(View.INVISIBLE);
                b = 0;
            } else if (bundle.getString("operacion").equalsIgnoreCase("1")) {
                id = bundle.getString("id");
                titulo.setText(bundle.getString("titulo"));
                descripcion.setText(bundle.getString("descripcion"));
                b = 1;
                arcMulAc = new DaoMultimedia(DatosNotas.this).getAll(id);
                listMul = new DaoMultimedia(DatosNotas.this).getAll(id);
                cargardatos();
                guardar.setVisibility(View.INVISIBLE);
            }
        }catch (Exception err){}
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mulsaiv = new Multimedia();
                Mulsaiv.setId(adp.getItem(i).getId());
                Mulsaiv.setTitulo(adp.getItem(i).getTitulo());
                Mulsaiv.setDireccion(adp.getItem(i).getDireccion());
                Mulsaiv.setTipo(adp.getItem(i).getTipo());
                Mulsaiv.setIdReference(adp.getItem(i).getIdReference());
                position = i;
                btnList_click();
                return false;
            }
        });
    }

    public void mapear(){
        actualizar = (Button)findViewById(R.id.btnactualizar);
        titulo = (EditText) findViewById(R.id.txttitulo);
        descripcion = (EditText) findViewById(R.id.txtdescripcion);
        lista = (ListView) findViewById(R.id.listaMultimedia);
    }

    public void insertar(){
        guardar=(Button)findViewById(R.id.btnguardarNota);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                List<Multimedia> listMuls = new ArrayList<Multimedia>();
                Intent atras = new Intent();

                Notas obj = new Notas();
                obj.setId(idTimeStamp);
                obj.setTitulo(titulo.getText().toString());
                obj.setDescripcion(descripcion.getText().toString());

                if(arcMul.size() != 0){
                    DaoMultimedia reg = new DaoMultimedia(DatosNotas.this);
                    for (int i = 0; i < arcMul.size(); i++){
                        Multimedia Mul = new Multimedia();
                        Mul.setTitulo(arcMul.get(i).getTitulo());
                        Mul.setDireccion(arcMul.get(i).getDireccion());
                        Mul.setTipo(arcMul.get(i).getTipo());
                        Mul.setIdReference(idTimeStamp);
                        listMuls.add(Mul);
                    }
                    for (int j = 0; j < listMuls.size(); j++){
                        System.out.println("die "+listMuls.get(j).getDireccion());
                    }
                    reg.insert(listMuls);
                }

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
                obj.setId(id);
                obj.setTitulo(titulo.getText().toString());
                obj.setDescripcion(descripcion.getText().toString());

                if(listMul.size() != 0 && arcMulAc.size() == 0){
                    DaoMultimedia del = new DaoMultimedia(DatosNotas.this);
                    del.delete(id);
                }
                System.out.println(arcMulAc.size());
                if(arcMulAc.size() != 0){
                    List<Multimedia> listMul = new ArrayList<Multimedia>();
                    Multimedia Mul = new Multimedia();
                    DaoMultimedia reg = new DaoMultimedia(DatosNotas.this);
                    for (int i = 0; i < arcMulAc.size(); i++){
                        Mul.setTitulo(arcMulAc.get(i).getTitulo());
                        Mul.setDireccion(arcMulAc.get(i).getDireccion());
                        Mul.setTipo(arcMulAc.get(i).getTipo());
                        Mul.setIdReference(id);
                        listMul.add(Mul);
                    }
                    reg.delete(id);
                    reg.insert(listMul);
                }

                atras.putExtra("minota", obj);
                setResult(RESULT_OK, atras);
                finish();
            }
        });
    }

    public void grabar(){
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        audio = (ImageButton) findViewById(R.id.imageBtnAudioNota);
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mStartRecording){
                    timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    fileName = getExternalCacheDir().getAbsolutePath();
                    fileName += "/" + timeStamp + ".3gp";
                }
                onRecord(mStartRecording);
                if (mStartRecording) {
                    audio.setImageResource(android.R.drawable.ic_notification_overlay);
                } else {
                    audio.setImageResource(android.R.drawable.presence_audio_away);
                    Multimedia obj = new Multimedia();
                    obj.setTitulo(timeStamp);
                    obj.setDireccion(fileName);
                    obj.setTipo("audio");
                    arcMul.add(obj);
                    arcMulAc.add(obj);
                    cargardatos();
                }
                mStartRecording = !mStartRecording;
            }
        });
    }

    ArrayAdapter<Multimedia> adp;
    public void cargardatos(){
        try {
            if (b == 0) {
                adp = new ArrayAdapter<Multimedia>(DatosNotas.this,
                        android.R.layout.simple_list_item_1,arcMul);
                lista.setAdapter(adp);
            } else if (b == 1) {
                DaoMultimedia dao = new DaoMultimedia(DatosNotas.this);
                adp = new ArrayAdapter<Multimedia>(DatosNotas.this,
                        android.R.layout.simple_list_item_1,arcMulAc);
                lista.setAdapter(adp);
            }
        }catch (Exception err){}
    }

    public void  btnList_click(){
        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Operacion a Realizar")
                        .setIcon(R.mipmap.ic_launcher)
                        .setItems(operaciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(operaciones[which].equalsIgnoreCase(operaciones[0])){
                                    fileName = Mulsaiv.getDireccion();
                                    onPlay(mStartPlaying);
                                    if (mStartPlaying) {
//                                        playButton.setText("Detener Reproducción");
                                    } else {
//                                        playButton.setText("Reproducir Audio");
                                    }
                                    mStartPlaying = !mStartPlaying;
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
                        .setTitle("Esta Seguro de Eliminar")
                        .setIcon(android.R.drawable.ic_delete)
                        .setMessage("Si elimina este registro no se podra recuperar")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(b == 0){
                                    arcMul.remove(position);
                                }else if(b == 1){
                                    arcMulAc.remove(position);
                                }
                                cargardatos();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
