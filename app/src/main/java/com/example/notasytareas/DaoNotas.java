package com.example.notasytareas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DaoNotas {
    private Context _contexto;
    private SQLiteDatabase _midb;

    public DaoNotas(Context contexto){
        this._contexto = contexto;
        this._midb = new BDN(contexto).getWritableDatabase();
    }

    public long insert(Notas c){
        ContentValues cv = new ContentValues();

        cv.put(BDN.COLUMNS_NOTA[1],c.getTitulo());
        cv.put(BDN.COLUMNS_NOTA[2],c.getDescripcion());

        return _midb.insert(BDN.TABLE_NOTA_NAME,null,cv) ;
    }

    public long update(Notas c){
        ContentValues cv = new ContentValues();

        cv.put(BDN.COLUMNS_NOTA[1],c.getTitulo());
        cv.put(BDN.COLUMNS_NOTA[2],c.getDescripcion());

        return _midb.update(BDN.TABLE_NOTA_NAME,
                cv,
                "_id=?",
                new String[] { String.valueOf( c.getId())});
    }

    public int delete(String id){
        return  _midb.delete("nota","_id='"+id+"'",null);
    }

    public List<Notas> getAll() {
        List<Notas> studentsArrayList = new ArrayList<Notas>();
        String selectQuery = "SELECT * FROM " + "nota";
        Log.d("", selectQuery);
        SQLiteDatabase db = this._midb;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Notas note = new Notas();
                note.setId(c.getInt(c.getColumnIndex("_id")));
                note.setTitulo(c.getString(c.getColumnIndex("Titulo")));
                note.setDescripcion(c.getString(c.getColumnIndex("Descripcion")));
                studentsArrayList.add(note);
            } while (c.moveToNext());
        }
        return studentsArrayList;
    }
}
