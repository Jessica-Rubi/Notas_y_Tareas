package com.example.notasytareas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DaoMultimedia {
    private Context _contexto;
    private SQLiteDatabase _midb;

    public DaoMultimedia(Context contexto){
        this._contexto = contexto;
        this._midb = new BDN(contexto).getWritableDatabase();
    }

    public long insert(List<Multimedia> c){
        ContentValues cv = new ContentValues();

        for (Multimedia mul: c)
        {
            cv.put(BDN.COLUMNS_MULTIMEDIA[1],mul.getTitulo());
            cv.put(BDN.COLUMNS_MULTIMEDIA[2],mul.getDireccion());
            cv.put(BDN.COLUMNS_MULTIMEDIA[3],mul.getTipo());
            cv.put(BDN.COLUMNS_MULTIMEDIA[4],mul.getIdReference());
            _midb.insert(BDN.TABLE_MULTIMEDIA_NAME,null,cv);
        }

        return 0;
    }

    public long update(Multimedia c){
        ContentValues cv = new ContentValues();

        cv.put(BDN.COLUMNS_MULTIMEDIA[1],c.getTitulo());
        cv.put(BDN.COLUMNS_MULTIMEDIA[2],c.getDescripcion());
        cv.put(BDN.COLUMNS_MULTIMEDIA[3],c.getDireccion());
        cv.put(BDN.COLUMNS_MULTIMEDIA[4],c.getTipo());

        return _midb.update(BDN.TABLE_MULTIMEDIA_NAME,
                cv,
                "_id=?",
                new String[] { String.valueOf( c.getId())});
    }

    public int delete(String id){
        return  _midb.delete("multimedia","IDReference='"+id+"'",null);
    }

    public List<Multimedia> getAll(String idR) {
        List<Multimedia> studentsArrayList = new ArrayList<Multimedia>();
        String selectQuery = "SELECT * FROM " + "multimedia" + " WHERE IDReference = '"+ idR +"'";
        Log.d("", selectQuery);
        SQLiteDatabase db = this._midb;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Multimedia sound = new Multimedia();
                sound.setId(c.getInt(c.getColumnIndex("_id")));
                sound.setTitulo(c.getString(c.getColumnIndex("Titulo")));
                sound.setDireccion(c.getString(c.getColumnIndex("Direccion")));
                sound.setTipo(c.getString(c.getColumnIndex("Tipo")));
                sound.setIdReference(c.getString(c.getColumnIndex("IDReference")));
                studentsArrayList.add(sound);
            } while (c.moveToNext());
        }
        return studentsArrayList;
    }
}
