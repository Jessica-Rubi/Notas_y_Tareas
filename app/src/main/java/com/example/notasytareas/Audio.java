package com.example.notasytareas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Audio extends AppCompatActivity {
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    ListView lista;
    EditText descripcion;
    String timeStamp = "";
    Button agregar;
    Button save;

    Button recordButton = null;
    MediaRecorder recorder = null;
    boolean mStartRecording = true;

    Button playButton = null;
    MediaPlayer player = null;
    boolean mStartPlaying = true;

    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_audio);
        recordButton = (Button) findViewById(R.id.btnGrabar);
        playButton = (Button) findViewById(R.id.btnReproducir);
        lista = (ListView) findViewById(R.id.listaAudios);
        descripcion = (EditText) findViewById(R.id.txtdescripcionVideo);
        agregar = (Button) findViewById(R.id.btnAgregarAudio);
        save = (Button) findViewById(R.id.btnGuardarAudios);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mStartRecording){
                    timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    fileName = getExternalCacheDir().getAbsolutePath();
                    fileName += "/" + timeStamp + ".3gp";
                }
                System.out.println(fileName);
                onRecord(mStartRecording);
                if (mStartRecording) {
                    recordButton.setText("Detener Grabación");
                } else {
                    recordButton.setText("Grabar Audio");
                }
                mStartRecording = !mStartRecording;
            }
        });
        cargardatos();
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    playButton.setText("Detener Reproducción");
                } else {
                    playButton.setText("Reproducir Audio");
                }
                mStartPlaying = !mStartPlaying;
            }
        });
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Multimedia obj = new Multimedia();
                obj.setTitulo(timeStamp);
                obj.setDescripcion(descripcion.getText().toString());
                obj.setDireccion(fileName);
                obj.setTipo("audio");
                try {
                    DaoMultimedia dao = new DaoMultimedia(Audio.this);
                    if(dao.insert(new Multimedia(obj.getTitulo(),obj.getDescripcion(), obj.getDireccion(), obj.getTipo()))>0) {
                        Toast.makeText(getBaseContext(), "Audio Insertado", Toast.LENGTH_SHORT).show();
                        cargardatos();
                    }else{
                        Toast.makeText(getBaseContext(), "No se pudo Insertar el Audio", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception err){
                    Toast.makeText(getBaseContext(),err.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
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

    ArrayAdapter<Multimedia> adp;
    public void cargardatos(){
        DaoMultimedia dao = new DaoMultimedia(Audio.this);
        adp = new ArrayAdapter<Multimedia>(Audio.this,
                android.R.layout.simple_list_item_1,dao.getAll());
        lista.setAdapter(adp);
    }
}
