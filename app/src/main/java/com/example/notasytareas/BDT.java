package com.example.notasytareas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDT extends SQLiteOpenHelper {
    private static final String DB_NAME = "mibasesitadatos";
    private static final int DB_VERSION = 1;
    public static final String[]COLUMNS_RECORDATORIO =
            {"_id","Titulo","Descripcion",
                    "Fecha","Hora"};
    public static final String TABLE_RECORDATORIO_NAME="recordatorio";
    private  final String TABLE_RECORDATORIO = "create table recordatorio ("+
            COLUMNS_RECORDATORIO[0]+" integer primary key autoincrement, "+
            COLUMNS_RECORDATORIO[1]+" varchar(100) null," +
            COLUMNS_RECORDATORIO[2]+" text not null,"+
            COLUMNS_RECORDATORIO[3]+" varchar(25) null,"+
            COLUMNS_RECORDATORIO[4]+" varchar(10) null);";


    public BDT(Context contexto) {
        super(contexto, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_RECORDATORIO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS recordatorio");
        onCreate(sqLiteDatabase);
    }
}
