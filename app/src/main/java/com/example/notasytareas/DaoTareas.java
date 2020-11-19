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

        cv.put(BDT.COLUMNS_TAREA[1],c.getTitulo());
        cv.put(BDT.COLUMNS_TAREA[2],c.getDescripcion());
        cv.put(BDT.COLUMNS_TAREA[3],c.getFecha());
        cv.put(BDT.COLUMNS_TAREA[4],c.getHora());
        cv.put(BDT.COLUMNS_TAREA[5], c.getFordate());

        return _midb.insert(BDT.TABLE_TAREA_NAME,null,cv) ;
    }

    public long update(Tareas c){
        ContentValues cv = new ContentValues();

        cv.put(BDT.COLUMNS_TAREA[1],c.getTitulo());
        cv.put(BDT.COLUMNS_TAREA[2],c.getDescripcion());
        cv.put(BDT.COLUMNS_TAREA[3],c.getFecha());
        cv.put(BDT.COLUMNS_TAREA[4],c.getHora());
        cv.put(BDT.COLUMNS_TAREA[5], c.getFordate());

        return _midb.update(BDT.TABLE_TAREA_NAME,
                cv,
                "_id=?",
                new String[] { String.valueOf( c.getId())});
    }

    public int delete(String id){
        return  _midb.delete("recordatorio","_id='"+id+"'",null);
    }

    public List<Tareas> getAll() {
        List<Tareas> studentsArrayList = new ArrayList<Tareas>();
        String selectQuery = "SELECT * FROM " + "recordatorio" + " ORDER BY FormatoFecha ASC";
        Log.d("", selectQuery);
        SQLiteDatabase db = this._midb;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Tareas tarea = new Tareas();
                tarea.setId(c.getInt(c.getColumnIndex("_id")));
                tarea.setTitulo(c.getString(c.getColumnIndex("Titulo")));
                tarea.setDescripcion(c.getString(c.getColumnIndex("Descripcion")));
                tarea.setFecha(c.getString(c.getColumnIndex("Fecha")));
                tarea.setHora(c.getString(c.getColumnIndex("Hora")));
                tarea.setFordate(c.getString(c.getColumnIndex("FormatoFecha")));
                System.out.println(tarea.getFordate());
                studentsArrayList.add(tarea);
            } while (c.moveToNext());
        }
        return studentsArrayList;
    }
}
