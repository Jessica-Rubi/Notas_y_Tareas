package com.example.notasytareas;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatosTareasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatosTareasFragment extends Fragment implements View.OnClickListener{
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final String LOG_TAG = "AudioRecordTest";
    private static String fileName = null;
    android.content.res.Resources res;

    List<Multimedia> arcMul = new ArrayList<Multimedia>();
    List<Multimedia> arcMulAc = new ArrayList<Multimedia>();
    List<Multimedia> listMul = new ArrayList<Multimedia>();

    Button guardar;
    Button actualizar;
    ImageButton audio;
    ImageButton imagen;
    ImageButton video;
    ImageButton archivo;
    ImageButton recordar;
    ListView lista;

    boolean mStartRecording = true;
    boolean mStartPlaying = true;
    String timeStamp = "";
    String idTimeStamp = "";
    String tipoArc;

    MediaRecorder recorder = null;
    MediaPlayer player = null;
    MediaPlayer mediaPlayer = null;

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
    DatosTareas1 datosTareas1;
    Context context;
    ListaAdapter adapter;
    int position;
    int b;

    String operaciones[];
    String operacionesv2[];
    String operacionesv3[];
    String operacionesv4[];
    String operacionesv5[];

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DatosTareasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if(context instanceof DatosTareas1){
            datosTareas1 = (DatosTareas1) context;
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DatosTareasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DatosTareasFragment newInstance(String param1, String param2) {
        DatosTareasFragment fragment = new DatosTareasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tareas, container, false);
        res = getResources();
        operaciones = new String[] {res.getString(R.string.Reproducir), res.getString(R.string.Eliminar)};
        operacionesv2 = new String[] {res.getString(R.string.Detener)};
        operacionesv3 = new String[] {res.getString(R.string.Ver), res.getString(R.string.Eliminar)};
        operacionesv4 = new String[] {res.getString(R.string.Archivo),res.getString(R.string.Audio),res.getString(R.string.Imagen),res.getString(R.string.Video)};
        operacionesv5 = new String[] {res.getString(R.string.Nunca), res.getString(R.string.Dias)};
        mapear(v);
        insertar(v);
        actualizar(v);
        grabar(v);
        tomarImg(v);
        tomarVid(v);
        abrirArchivo(v);
        tipoRecor(v);
        try {
            Bundle bundle = getArguments();
            if (bundle.getString("operacion1").equalsIgnoreCase("0")) {
                actualizar.setVisibility(View.INVISIBLE);
                b = 0;
            } else if (bundle.getString("operacion1").equalsIgnoreCase("1")) {
                id = bundle.getString("id1");
                titulo.setText(bundle.getString("titulo1"));
                descripcion.setText(bundle.getString("descripcion1"));
                fecha.setText(bundle.getString("fecha1"));
                hora.setText(bundle.getString("hora1"));
                comp = bundle.getString("completa1");
                b = 1;
                arcMulAc = new DaoMultimediaT(getContext()).getAll(id);
                listMul = new DaoMultimediaT(getContext()).getAll(id);
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
                Mulsaiv.setTipoArchivo(adp.getItem(i).getTipoArchivo());
                position = i;
                btnList_click();
                return false;
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    final Calendar c= Calendar.getInstance();
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public void mapear(View v){
        actualizar = (Button) v.findViewById(R.id.btnactualizar);
        titulo = (EditText) v.findViewById(R.id.txttitulo);
        descripcion = (EditText) v.findViewById(R.id.txtdescripcion);
        fecha = (EditText) v.findViewById(R.id.txtfecha);
        fecha.setOnClickListener(this);
        hora   = (EditText) v.findViewById(R.id.txthora);
        hora.setOnClickListener(this);
        lista = (ListView) v.findViewById(R.id.listaMultimedia);
    }

    int r = 1;
    public void tipoRecor(View v){
    recordar = (ImageButton) v.findViewById(R.id.imageBtnRecordatorio);
    recordar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog dialog =
                    new AlertDialog.Builder(getContext())
                            .setTitle(res.getString(R.string.Repetir))
                            .setIcon(R.mipmap.ic_launcher)
                            .setItems(operacionesv5, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(operacionesv5[which].equalsIgnoreCase(operacionesv5[0])){
                                        r = 1;
                                    }
                                    if(operacionesv5[which].equalsIgnoreCase(operacionesv5[1])){
                                        r = 2;
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .create();
            dialog.show();
        }
    });
    }

    public void recordatorio(){
        alarmMgr = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hora1);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 1);

        alarmMgr.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), alarmIntent);
    }

    public void recordatorio1(){
        alarmMgr = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hora1);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 1);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY ,alarmIntent);
    }

    public void insertar(View v){
        guardar=(Button) v.findViewById(R.id.btnguardarTarea);
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
                    DaoMultimediaT reg = new DaoMultimediaT(getContext());
                    for (int i = 0; i < arcMul.size(); i++){
                        Multimedia Mul = new Multimedia();
                        Mul.setTitulo(arcMul.get(i).getTitulo());
                        Mul.setDireccion(arcMul.get(i).getDireccion());
                        Mul.setTipo(arcMul.get(i).getTipo());
                        Mul.setIdReference(idTimeStamp);
                        Mul.setTipoArchivo(arcMul.get(i).getTipoArchivo());
                        listMuls.add(Mul);
                    }
                    reg.insert(listMuls);
                }
                recordatorio();
//                if(r == 1){
//                    recordatorio();
//                }else {
//                    recordatorio1();
//                }

                atras.putExtra("mirecordatorio", obj);
                getActivity().setResult(Activity.RESULT_OK, atras);
                getActivity().finish();
            }
        });
    }

    public void actualizar(View v){
        actualizar=(Button) v.findViewById(R.id.btnactualizar);
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
                    DaoMultimediaT del = new DaoMultimediaT(getContext());
                    del.delete(id);
                }
                System.out.println(arcMulAc.size());
                if(arcMulAc.size() != 0){
                    List<Multimedia> listMul = new ArrayList<Multimedia>();
                    DaoMultimediaT reg = new DaoMultimediaT(getContext());
                    for (int i = 0; i < arcMulAc.size(); i++){
                        Multimedia Mul = new Multimedia();
                        Mul.setTitulo(arcMulAc.get(i).getTitulo());
                        Mul.setDireccion(arcMulAc.get(i).getDireccion());
                        Mul.setTipo(arcMulAc.get(i).getTipo());
                        Mul.setIdReference(id);
                        Mul.setTipoArchivo(arcMulAc.get(i).getTipoArchivo());
                        listMul.add(Mul);
                    }
                    reg.delete(id);
                    reg.insert(listMul);
                }


                atras.putExtra("mirecordatorio", obj);
                getActivity().setResult(Activity.RESULT_OK, atras);
                getActivity().finish();
            }
        });
    }

    public void grabar(View v){
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        audio = (ImageButton) v.findViewById(R.id.imageBtnaudioTarea);
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mStartRecording){
                    timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    fileName = getContext().getExternalCacheDir().getAbsolutePath();
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
                    obj.setTipoArchivo("N/A");
                    arcMul.add(obj);
                    arcMulAc.add(obj);
                    cargardatos();
                }
                mStartRecording = !mStartRecording;
            }
        });
    }

    public void tomarImg(View v){
        imagen = (ImageButton) v.findViewById(R.id.imageBtnimagenTarea);
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntentFotoOriginal();
            }
        });
    }

    public void tomarVid(View v){
        video = (ImageButton) v.findViewById(R.id.imageBtnVideoTarea);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakeVideoIntent();
            }
        });
    }

    public void abrirArchivo(View v){
        archivo = (ImageButton) v.findViewById(R.id.imageBtnArchivoTarea);
        archivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog =
                        new AlertDialog.Builder(getContext())
                                .setTitle(res.getString(R.string.Operacion))
                                .setIcon(R.mipmap.ic_launcher)
                                .setItems(operacionesv4, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(operacionesv4[which].equalsIgnoreCase(operacionesv4[0])){
                                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                            intent.setType("text/plain");
                                            tipoArc = "archivo";
                                            startActivityForResult(intent, 777);
                                        }
                                        if(operacionesv4[which].equalsIgnoreCase(operacionesv4[1])){
                                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                            intent.setType("audio/*");
                                            tipoArc = "audio";
                                            startActivityForResult(intent, 777);
                                        }
                                        if(operacionesv4[which].equalsIgnoreCase(operacionesv4[2])){
                                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                            intent.setType("image/*");
                                            tipoArc = "imagen";
                                            startActivityForResult(intent, 777);
                                        }
                                        if(operacionesv4[which].equalsIgnoreCase(operacionesv4[3])){
                                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                            intent.setType("video/*");
                                            tipoArc = "video";
                                            startActivityForResult(intent, 777);
                                        }
                                        dialog.dismiss();
                                    }
                                })
                                .create();
                dialog.show();
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
                    }else if(arcMul.get(k).getTipo().equals("archivo")){
                        listImagenes[k] = imagenes[2];
                    }
                }
                adapter = new ListaAdapter(getContext(), titulos, listImagenes);
                adp = new ArrayAdapter<Multimedia>(getContext(),
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
                    }else if(arcMulAc.get(k).getTipo().equals("archivo")){
                        listImagenes[k] = imagenes[2];
                    }
                }
                adapter = new ListaAdapter(getContext(), titulos, listImagenes);
                adp = new ArrayAdapter<Multimedia>(getContext(),
                        android.R.layout.simple_list_item_1,arcMulAc);
                lista.setAdapter(adapter);
            }
        }catch (Exception err){}
    }

    public void  btnList_click(){
        if(Mulsaiv.getTipo().equals("audio")){
            if(mStartPlaying){
                AlertDialog dialog =
                        new AlertDialog.Builder(getContext())
                                .setTitle(res.getString(R.string.Operacion))
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
            }else{
                AlertDialog dialog =
                        new AlertDialog.Builder(getContext())
                                .setTitle(res.getString(R.string.Operacion))
                                .setIcon(R.mipmap.ic_launcher)
                                .setItems(operacionesv2, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(operacionesv2[which].equalsIgnoreCase(operacionesv2[0])){
                                            fileName = Mulsaiv.getDireccion();
                                            onPlay(mStartPlaying);
                                            if (mStartPlaying) {
//                                        playButton.setText("Detener Reproducción");
                                            } else {
//                                        playButton.setText("Reproducir Audio");
                                            }
                                            mStartPlaying = !mStartPlaying;
                                        }
                                        dialog.dismiss();
                                    }
                                })
                                .create();
                dialog.show();
            }
        }else if(Mulsaiv.getTipo().equals("imagen")){
            AlertDialog dialog =
                    new AlertDialog.Builder(getContext())
                            .setTitle(res.getString(R.string.Operacion))
                            .setIcon(R.mipmap.ic_launcher)
                            .setItems(operacionesv3, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(operacionesv3[which].equalsIgnoreCase(operacionesv3[0])){
                                        datosTareas1.ver(Mulsaiv.getDireccion(),2);
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
                    new AlertDialog.Builder(getContext())
                            .setTitle(res.getString(R.string.Operacion))
                            .setIcon(R.mipmap.ic_launcher)
                            .setItems(operacionesv3, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(operacionesv3[which].equalsIgnoreCase(operacionesv3[0])){
                                        datosTareas1.ver(Mulsaiv.getDireccion(),3);
                                    }
                                    if(operacionesv3[which].equalsIgnoreCase(operacionesv3[1])){
                                        confirmacion();
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .create();
            dialog.show();
        }else if(Mulsaiv.getTipo().equals("archivo") && !(Mulsaiv.getTipoArchivo().equals("audio"))){
            AlertDialog dialog =
                    new AlertDialog.Builder(getContext())
                            .setTitle(res.getString(R.string.Operacion))
                            .setIcon(R.mipmap.ic_launcher)
                            .setItems(operacionesv3, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(operacionesv3[which].equalsIgnoreCase(operacionesv3[0])){
                                        if(Mulsaiv.getTipoArchivo().equals("archivo")){
                                            datosTareas1.ver(Mulsaiv.getDireccion(),1);
                                        }else if(Mulsaiv.getTipoArchivo().equals("imagen")){
                                            datosTareas1.ver(Mulsaiv.getDireccion(),2);
                                        }else if(Mulsaiv.getTipoArchivo().equals("video")){
                                            datosTareas1.ver(Mulsaiv.getDireccion(),3);
                                        }
                                    }
                                    if(operacionesv3[which].equalsIgnoreCase(operacionesv3[1])){
                                        confirmacion();
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .create();
            dialog.show();
        }else if(Mulsaiv.getTipo().equals("archivo") && Mulsaiv.getTipoArchivo().equals("audio")){
            if(mStartPlaying){
                AlertDialog dialog =
                        new AlertDialog.Builder(getContext())
                                .setTitle(res.getString(R.string.Operacion))
                                .setIcon(R.mipmap.ic_launcher)
                                .setItems(operaciones, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(operaciones[which].equalsIgnoreCase(operaciones[0])){
                                            fileName = Mulsaiv.getDireccion();
                                            if(mStartPlaying){
                                                mediaPlayer = new MediaPlayer();
                                                Uri myUri = Uri.parse(Mulsaiv.getDireccion());
                                                try {
                                                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                                    mediaPlayer.setDataSource(getContext(), myUri);
                                                    mediaPlayer.prepare();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                mediaPlayer.start();
                                            }else {
                                                mediaPlayer.release();
                                                mediaPlayer = null;
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
            }else {
                AlertDialog dialog =
                        new AlertDialog.Builder(getContext())
                                .setTitle(res.getString(R.string.Operacion))
                                .setIcon(R.mipmap.ic_launcher)
                                .setItems(operacionesv2, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(operacionesv2[which].equalsIgnoreCase(operacionesv2[0])){
                                            if(mStartPlaying){
                                                mediaPlayer = new MediaPlayer();
                                                Uri myUri = Uri.parse(Mulsaiv.getDireccion());
                                                try {
                                                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                                    mediaPlayer.setDataSource(getContext(), myUri);
                                                    mediaPlayer.prepare();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                mediaPlayer.start();
                                            }else {
                                                mediaPlayer.release();
                                                mediaPlayer = null;
                                            }
                                            mStartPlaying = !mStartPlaying;
                                        }
                                        dialog.dismiss();
                                    }
                                })
                                .create();
                dialog.show();
            }
        }
    }

    public void confirmacion(){
        AlertDialog dialog =
                new AlertDialog.Builder(getContext())
                        .setTitle(res.getString(R.string.Seguro))
                        .setIcon(android.R.drawable.ic_delete)
                        .setMessage(res.getString(R.string.Recuperar))
                        .setPositiveButton(res.getString(R.string.Aceptar), new DialogInterface.OnClickListener() {
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
                        .setNegativeButton(res.getString(R.string.Cancelar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK){
            Multimedia obj = new Multimedia();
            obj.setTitulo(imageFileName);
            obj.setDireccion(photoURI.toString());
            obj.setTipo("imagen");
            obj.setTipoArchivo("N/A");
            arcMul.add(obj);
            arcMulAc.add(obj);
            cargardatos();
        }
        if(requestCode == REQUEST_VIDEO_CAPTURE && resultCode == getActivity().RESULT_OK){
            Multimedia obj = new Multimedia();
            obj.setTitulo(videoFileName);
            obj.setDireccion(videoURI.toString());
            obj.setTipo("video");
            obj.setTipoArchivo("N/A");
            arcMul.add(obj);
            arcMulAc.add(obj);
            cargardatos();
        }
        if(requestCode == 777){
            Uri uriDc = data.getData();
            String[] titulo = uriDc.toString().split("/");
            Multimedia obj = new Multimedia();
            obj.setTitulo(titulo[titulo.length-1]);
            obj.setDireccion(uriDc.toString());
            obj.setTipo("archivo");
            obj.setTipoArchivo(tipoArc);
            arcMul.add(obj);
            arcMulAc.add(obj);
            cargardatos();
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        String timeStampI = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStampI + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getContext(),
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
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
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
        if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException ex) {
            }
            if (videoFile != null) {
                videoURI = FileProvider.getUriForFile(getContext(),
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
        if (!permissionToRecordAccepted ){
            audio.setEnabled(false);
        }

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

    int dia1 = 0, mes1 = 0, year1 = 0;
    int hora1 = 0, min = 0;

    @Override
    public void onClick(View view) {
        if(view==fecha){
            final Calendar c= Calendar.getInstance();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            year=c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    fecha.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                    fordate = year + "-" + (monthOfYear+1)+ "-" + dayOfMonth;
                    dia1 = dayOfMonth;
                    mes1 = monthOfYear+1;
                    year1 = year;
                }
            }
                    ,year,mes,dia);
            datePickerDialog.show();
        }
        if (view==hora){
            final Calendar c= Calendar.getInstance();
            h=c.get(Calendar.HOUR_OF_DAY);
            minutos=c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hora.setText(hourOfDay+":"+minute);
                    hora1 = hourOfDay;
                    min = minute;
                }
            },h,minutos,false);
            timePickerDialog.show();
        }
    }
}