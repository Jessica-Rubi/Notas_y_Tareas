package com.example.notasytareas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDT extends SQLiteOpenHelper {
    private static final String DB_NAME = "mibasesitadatos";
    private static final int DB_VERSION = 1;
    public static final String[]COLUMNS_TAREA =
            {"_id","Titulo","Descripcion",
                    "Fecha","Hora","FormatoFecha","Completado"};
    public static final String TABLE_TAREA_NAME="recordatorio";
    private  final String TABLE_TAREA = "create table recordatorio ("+
            COLUMNS_TAREA[0]+" varchar(20) primary key, "+
            COLUMNS_TAREA[1]+" varchar(100) null," +
            COLUMNS_TAREA[2]+" text not null,"+
            COLUMNS_TAREA[3]+" varchar(25) null,"+
            COLUMNS_TAREA[4]+" varchar(10) null,"+
            COLUMNS_TAREA[5]+" varchar(25) null,"+
            COLUMNS_TAREA[6]+" varchar(25) null);";

    public static final String[] COLUMNS_MULTIMEDIA =
            {"_id","Titulo","Direccion", "Tipo","IDReference","TipoArchivo"};
    public static final String TABLE_MULTIMEDIA_NAME="multimedia";
    private  final String TABLE_MULTIMEDIA = "create table multimedia ("+
            COLUMNS_MULTIMEDIA[0]+" integer primary key autoincrement, "+
            COLUMNS_MULTIMEDIA[1]+" varchar(100) null," +
            COLUMNS_MULTIMEDIA[2]+" text not null," +
            COLUMNS_MULTIMEDIA[3]+" text not null," +
            COLUMNS_MULTIMEDIA[4]+" text not null," +
            COLUMNS_MULTIMEDIA[5]+" text not null);";

    public BDT(Context contexto) {
        super(contexto, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_TAREA);
        sqLiteDatabase.execSQL(TABLE_MULTIMEDIA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS recordatorio");
        onCreate(sqLiteDatabase);
    }
}
