package com.example.notasytareas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DaoTareas {
    private Context _contexto;
    private SQLiteDatabase _midb;

    public DaoTareas(Context contexto){
        this._contexto = contexto;
        this._midb = new BDT(contexto).getWritableDatabase();
    }


    public long insert(Tareas c){

        ContentValues cv = new ContentValues();

        cv.put(BDT.COLUMNS_RECORDATORIO[1],c.getTitulo());
        cv.put(BDT.COLUMNS_RECORDATORIO[2],c.getDescripcion());
        cv.put(BDT.COLUMNS_RECORDATORIO[3],c.getFecha());
        cv.put(BDT.COLUMNS_RECORDATORIO[4],c.getHora());

        return _midb.insert(BDT.TABLE_RECORDATORIO_NAME,null,cv) ;

    }



    public long update(Tareas c){
        ContentValues cv = new ContentValues();

        cv.put(BDT.COLUMNS_RECORDATORIO[1],c.getTitulo());
        cv.put(BDT.COLUMNS_RECORDATORIO[2],c.getDescripcion());
        cv.put(BDT.COLUMNS_RECORDATORIO[3],c.getFecha());
        cv.put(BDT.COLUMNS_RECORDATORIO[4],c.getHora());

        return _midb.update(BDT.TABLE_RECORDATORIO_NAME,
                cv,
                "_id=?",
                new String[] { String.valueOf( c.getId())});

    }

    public int delete(String id){
        return  _midb.delete("recordatorio","_id='"+id+"'",null);
    }



    public List<Tareas> Buscar(String nombre){
        List<Tareas> ls=null;

        Cursor c = _midb.query(BDT.TABLE_RECORDATORIO_NAME,
                BDT.COLUMNS_RECORDATORIO,
                null,
                null,
                null,
                null,
                BDT.COLUMNS_RECORDATORIO[1]);

        if (c.moveToFirst()) {
            ls = new ArrayList<Tareas>();
            do {
                Tareas ctc = new Tareas();

                ctc.setId(
                        c.getInt(
                                c.getColumnIndex(
                                        BDT.COLUMNS_RECORDATORIO[0])
                        )
                );

                ctc.setId(c.getInt(0));
                ctc.setTitulo(c.getString(1));
                ctc.setDescripcion(c.getString(2));
                ctc.setFecha(c.getString(3));
                ctc.setHora(c.getString(4));


                if(c.getString(1).toUpperCase().startsWith(nombre.toUpperCase())) {
                    ls.add(ctc);
                }

            }while(c.moveToNext());
        }

        return ls;
    }

    public List<Tareas> getAllNotificacines() {
        List<Tareas> studentsArrayList = new ArrayList<Tareas>();
        String selectQuery = "SELECT  * FROM " + "recordatorio";
        Log.d("", selectQuery);
        SQLiteDatabase db = this._midb;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Tareas recordatorio = new Tareas();
                recordatorio.setId(c.getInt(c.getColumnIndex("_id")));
                recordatorio.setTitulo(c.getString(c.getColumnIndex("Titulo")));
                recordatorio.setDescripcion(c.getString(c.getColumnIndex("Descripcion")));
                recordatorio.setFecha(c.getString(c.getColumnIndex("Fecha")));
                recordatorio.setHora(c.getString(c.getColumnIndex("Hora")));
                studentsArrayList.add(recordatorio);
            } while (c.moveToNext());
        }
        return studentsArrayList;
    }

    public List<Tareas> notificacionescumplidas(String fecha) {
        List<Tareas> studentsArrayList = new ArrayList<Tareas>();
        String selectQuery = "SELECT  * FROM " + "recordatorio WHERE Fecha='"+fecha+"'";
        Log.d("", selectQuery);
        SQLiteDatabase db = this._midb;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Tareas recordatorio = new Tareas();
                recordatorio.setId(c.getInt(c.getColumnIndex("_id")));
                recordatorio.setTitulo(c.getString(c.getColumnIndex("Titulo")));
                recordatorio.setDescripcion(c.getString(c.getColumnIndex("Descripcion")));
                recordatorio.setFecha(c.getString(c.getColumnIndex("Fecha")));
                recordatorio.setHora(c.getString(c.getColumnIndex("Hora")));
                studentsArrayList.add(recordatorio);
            } while (c.moveToNext());
        }
        return studentsArrayList;
    }



    public ArrayList<Tareas> obtenercontacto(String id) {
        ArrayList<Tareas> studentsArrayList = new ArrayList<Tareas>();
        String selectQuery = "select * from recordatorio where _id='"+id+"'";
        Log.d("", selectQuery);
        SQLiteDatabase db = this._midb;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Tareas recordatorio = new Tareas();
                recordatorio.setId(c.getInt(c.getColumnIndex("_id")));
                recordatorio.setTitulo(c.getString(c.getColumnIndex("Titulo")));
                recordatorio.setDescripcion(c.getString(c.getColumnIndex("Descripcion")));
                recordatorio.setFecha(c.getString(c.getColumnIndex("Fecha")));
                recordatorio.setHora(c.getString(c.getColumnIndex("Hora")));
                studentsArrayList.add(recordatorio);
            } while (c.moveToNext());
        }
        return studentsArrayList;
    }
}
