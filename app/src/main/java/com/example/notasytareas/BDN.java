package com.example.notasytareas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDN extends SQLiteOpenHelper {
    private static final String DB_NAME = "dbnotas";
    private static final int DB_VERSION = 1;
    public static final String[] COLUMNS_NOTA =
            {"_id","Titulo","Descripcion"};
    public static final String TABLE_NOTA_NAME="nota";
    private  final String TABLE_NOTA = "create table nota ("+
            COLUMNS_NOTA[0]+" integer primary key autoincrement, "+
            COLUMNS_NOTA[1]+" varchar(100) null," +
            COLUMNS_NOTA[2]+" text not null);";

    public static final String[] COLUMNS_MULTIMEDIA =
            {"_id","Titulo","Descripcion","Direccion", "Tipo"};
    public static final String TABLE_MULTIMEDIA_NAME="multimedia";
    private  final String TABLE_MULTIMEDIA = "create table multimedia ("+
            COLUMNS_MULTIMEDIA[0]+" integer primary key autoincrement, "+
            COLUMNS_MULTIMEDIA[1]+" varchar(100) null," +
            COLUMNS_MULTIMEDIA[2]+" text null," +
            COLUMNS_MULTIMEDIA[3]+" text not null," +
            COLUMNS_MULTIMEDIA[4]+" text not null);";

    public BDN(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_NOTA);
        sqLiteDatabase.execSQL(TABLE_MULTIMEDIA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS nota");
        onCreate(sqLiteDatabase);
    }
}
