package com.example.notasytareas;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatosTareas extends AppCompatActivity implements TareasFragment.OnFragmentInteractionListener, View.OnClickListener {
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
    ImageButton imagen;
    ImageButton video;
    ListView lista;

    boolean mStartRecording = true;
    boolean mStartPlaying = true;
    String timeStamp = "";
    String idTimeStamp = "";

    MediaRecorder recorder = null;
    MediaPlayer player = null;

    EditText titulo;
    EditText descripcion;
    EditText fecha;
    EditText hora;

    private  int dia,mes,year,h,minutos;
    private String id = "";
    String fordate = "";
    String comp;
    private Multimedia Mulsaiv;
    private String imageFileName;
    private String videoFileName;
    ListaAdapter adapter;
    int position;
    int b;
    String operaciones[] =
            new String[]
                    {"Reproducir", "Eliminar"};
    String operacionesv2[] =
            new String[]
                    {"Detener"};
    String operacionesv3[] =
            new String[]
                    {"Ver", "Eliminar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_datos_tarea);
        mapear();
        insertar();
        actualizar();
        grabar();
        tomarImg();
        tomarVid();
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle.getString("operacion").equalsIgnoreCase("0")) {
                actualizar.setVisibility(View.INVISIBLE);
                b = 0;
            } else if (bundle.getString("operacion").equalsIgnoreCase("1")) {
                id = bundle.getString("id");
                titulo.setText(bundle.getString("titulo"));
                descripcion.setText(bundle.getString("descripcion"));
                fecha.setText(bundle.getString("fecha"));
                hora.setText(bundle.getString("hora"));
                comp = bundle.getString("completa");
                b = 1;
                arcMulAc = new DaoMultimediaT(DatosTareas.this).getAll(id);
                listMul = new DaoMultimediaT(DatosTareas.this).getAll(id);
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

    final Calendar c= Calendar.getInstance();

    public void mapear(){
        actualizar = (Button)findViewById(R.id.btnactualizar);
        titulo = (EditText) findViewById(R.id.txttitulo);
        descripcion = (EditText) findViewById(R.id.txtdescripcion);
        fecha = (EditText) findViewById(R.id.txtfecha);
        fecha.setOnClickListener(this);
        hora   = (EditText) findViewById(R.id.txthora);
        hora.setOnClickListener(this);
        lista = (ListView) findViewById(R.id.listaMultimedia);
    }

    public void insertar(){
        guardar=(Button)findViewById(R.id.btnguardarTarea);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                List<Multimedia> listMuls = new ArrayList<Multimedia>();
                Intent atras = new Intent();

                Tareas obj = new Tareas();
                obj.setId(idTimeStamp);
                obj.setTitulo(titulo.getText().toString());
                obj.setDescripcion(descripcion.getText().toString());
                obj.setFecha(fecha.getText().toString());
                obj.setHora(hora.getText().toString());
                obj.setFordate(fordate);
                obj.setCompletado("no");
                System.out.println("1: "+obj.getFordate());

                if(arcMul.size() != 0){
                    DaoMultimediaT reg = new DaoMultimediaT(DatosTareas.this);
                    for (int i = 0; i < arcMul.size(); i++){
                        Multimedia Mul = new Multimedia();
                        Mul.setTitulo(arcMul.get(i).getTitulo());
                        Mul.setDireccion(arcMul.get(i).getDireccion());
                        Mul.setTipo(arcMul.get(i).getTipo());
                        Mul.setIdReference(idTimeStamp);
                        listMuls.add(Mul);
                    }
                    reg.insert(listMuls);
                }

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
                obj.setId(id);
                obj.setTitulo(titulo.getText().toString());
                obj.setDescripcion(descripcion.getText().toString());
                obj.setFecha(fecha.getText().toString());
                obj.setHora(hora.getText().toString());
                obj.setFordate(fordate);
                obj.setCompletado(comp);
                System.out.println("2: "+obj.getFordate());

                if(listMul.size() != 0 && arcMulAc.size() == 0){
                    DaoMultimediaT del = new DaoMultimediaT(DatosTareas.this);
                    del.delete(id);
                }
                System.out.println(arcMulAc.size());
                if(arcMulAc.size() != 0){
                    List<Multimedia> listMul = new ArrayList<Multimedia>();
                    DaoMultimediaT reg = new DaoMultimediaT(DatosTareas.this);
                    for (int i = 0; i < arcMulAc.size(); i++){
                        Multimedia Mul = new Multimedia();
                        Mul.setTitulo(arcMulAc.get(i).getTitulo());
                        Mul.setDireccion(arcMulAc.get(i).getDireccion());
                        Mul.setTipo(arcMulAc.get(i).getTipo());
                        Mul.setIdReference(id);
                        listMul.add(Mul);
                    }
                    reg.delete(id);
                    reg.insert(listMul);
                }

                atras.putExtra("mirecordatorio", obj);
                setResult(RESULT_OK, atras);
                finish();
            }
        });
    }

    public void grabar(){
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        audio = (ImageButton) findViewById(R.id.imageBtnaudioTarea);
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

    public void tomarImg(){
        imagen = (ImageButton) findViewById(R.id.imageBtnimagenTarea);
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntentFotoOriginal();
            }
        });
    }

    public void tomarVid(){
        video = (ImageButton) findViewById(R.id.imageBtnVideoTarea);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakeVideoIntent();
            }
        });
    }

    ArrayAdapter<Multimedia> adp;
    int[] imagenes = {
            android.R.drawable.presence_video_online,
            android.R.drawable.ic_menu_camera,
            android.R.drawable.ic_input_get,
            android.R.drawable.presence_audio_away
    };

    public void cargardatos(){
        try {
            String[] titulos;
            int[] listImagenes;
            if (b == 0) {
                titulos = new String[arcMul.size()];
                listImagenes = new int[arcMul.size()];
                for (int k = 0; k < arcMul.size(); k++){
                    titulos[k] = arcMul.get(k).getTitulo();
                    if(arcMul.get(k).getTipo().equals("audio")){
                        listImagenes[k] = imagenes[3];
                    }else if(arcMul.get(k).getTipo().equals("imagen")){
                        listImagenes[k] = imagenes[1];
                    }else if(arcMul.get(k).getTipo().equals("video")){
                        listImagenes[k] = imagenes[0];
                    }
                }
                adapter = new ListaAdapter(this, titulos, listImagenes);
                adp = new ArrayAdapter<Multimedia>(DatosTareas.this,
                        android.R.layout.simple_list_item_1,arcMul);
                lista.setAdapter(adapter);
            } else if (b == 1) {
                titulos = new String[arcMulAc.size()];
                listImagenes = new int[arcMulAc.size()];
                for (int k = 0; k < arcMulAc.size(); k++){
                    titulos[k] = arcMulAc.get(k).getTitulo();
                    if(arcMulAc.get(k).getTipo().equals("audio")){
                        listImagenes[k] = imagenes[3];
                    }else if(arcMulAc.get(k).getTipo().equals("imagen")){
                        listImagenes[k] = imagenes[1];
                    }else if(arcMulAc.get(k).getTipo().equals("video")){
                        listImagenes[k] = imagenes[0];
                    }
                }
                adapter = new ListaAdapter(this, titulos, listImagenes);
                adp = new ArrayAdapter<Multimedia>(DatosTareas.this,
                        android.R.layout.simple_list_item_1,arcMulAc);
                lista.setAdapter(adapter);
            }
        }catch (Exception err){}
    }

    public void  btnList_click(){
        if(Mulsaiv.getTipo().equals("audio")){
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
        }else if(Mulsaiv.getTipo().equals("imagen")){
            AlertDialog dialog =
                    new AlertDialog.Builder(this)
                            .setTitle("Operacion a Realizar")
                            .setIcon(R.mipmap.ic_launcher)
                            .setItems(operacionesv3, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(operacionesv3[which].equalsIgnoreCase(operacionesv3[0])){
                                        Intent intentImg = new Intent(DatosTareas.this, Imagen.class);
                                        intentImg.putExtra("urlImg", Mulsaiv.getDireccion());
                                        startActivity(intentImg);
                                    }
                                    if(operacionesv3[which].equalsIgnoreCase(operacionesv3[1])){
                                        confirmacion();
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .create();
            dialog.show();
        }else if (Mulsaiv.getTipo().equals("video")){
            AlertDialog dialog =
                    new AlertDialog.Builder(this)
                            .setTitle("Operacion a Realizar")
                            .setIcon(R.mipmap.ic_launcher)
                            .setItems(operacionesv3, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(operacionesv3[which].equalsIgnoreCase(operacionesv3[0])){
                                        Intent intentVid = new Intent(DatosTareas.this, Video.class);
                                        intentVid.putExtra("urlVid", Mulsaiv.getDireccion());
                                        startActivity(intentVid);
                                    }
                                    if(operacionesv3[which].equalsIgnoreCase(operacionesv3[1])){
                                        confirmacion();
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .create();
            dialog.show();
        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
            Multimedia obj = new Multimedia();
            obj.setTitulo(imageFileName);
            obj.setDireccion(photoURI.toString());
            obj.setTipo("imagen");
            arcMul.add(obj);
            arcMulAc.add(obj);
            cargardatos();
        }
        if(requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK){
            Multimedia obj = new Multimedia();
            obj.setTitulo(videoFileName);
            obj.setDireccion(videoURI.toString());
            obj.setTipo("video");
            arcMul.add(obj);
            arcMulAc.add(obj);
            cargardatos();
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        String timeStampI = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStampI + "_";
        File storageDir = DatosTareas.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 2;
    Uri photoURI;

    private void dispatchTakePictureIntentFotoOriginal() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(DatosTareas.this.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(DatosTareas.this,
                        "com.example.notasytareas.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    String currentVideoPath;

    private File createVideoFile() throws IOException {
        String timeStampV = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        videoFileName = "MP4_" + timeStampV + "_";
        File storageDir = DatosTareas.this.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File image = File.createTempFile(
                videoFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );
        currentVideoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_VIDEO_CAPTURE = 1;
    Uri videoURI;

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(DatosTareas.this.getPackageManager()) != null) {
            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException ex) {
            }
            if (videoFile != null) {
                videoURI = FileProvider.getUriForFile(DatosTareas.this,
                        "com.example.notasytareas.fileprovider",
                        videoFile);
                takeVideoIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, videoURI);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        }
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
