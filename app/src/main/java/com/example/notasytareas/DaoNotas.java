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

    public List<Notas> Buscar(String nombre){
        List<Notas> ls=null;

        Cursor c = _midb.query(BDN.TABLE_NOTA_NAME,
                BDN.COLUMNS_NOTA,
                null,
                null,
                null,
                null,
                BDN.COLUMNS_NOTA[1]);

        if (c.moveToFirst()) {
            ls = new ArrayList<Notas>();
            do {
                Notas ctc = new Notas();

                ctc.setId(
                        c.getInt(
                                c.getColumnIndex(
                                        BDN.COLUMNS_NOTA[0])
                        )
                );

                ctc.setId(c.getInt(0));
                ctc.setTitulo(c.getString(1));
                ctc.setDescripcion(c.getString(2));

                if(c.getString(1).toUpperCase().startsWith(nombre.toUpperCase())) {
                    ls.add(ctc);
                }

            }while(c.moveToNext());
        }
        return ls;
    }

    public List<Notas> getAllNotificacines() {
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

    public ArrayList<Notas> obtenercontacto(String id) {
        ArrayList<Notas> studentsArrayList = new ArrayList<Notas>();
        String selectQuery = "select * from notas where _id='"+id+"'";
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
