package com.vrcorp.resepmasakan.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="mahaapss";

    private static final int DATABASE_VERSION = 1;
    private static final String DB_TABLE = "favorit";
    private static final String STU_TABLE = "create table "+DB_TABLE +"(id int primary key,judul TEXT ,gambar TEXT ,url TEXT" +
            " ,des TEXT ,ketegori TEXT ,favorit TEXT)";
    public final static String DATABASE_PATH = "/data/data/com.vrcorp.resepmasakan/databases/";

    Context context;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(STU_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

        // Create tables again
        onCreate(db);
    }


    /* Insert into database*/
    public void insertIntoDB(int id, String judul, String gambar, String url,
                                      String des,
                                      String kategori,String favorit){
        Log.d("insert", "before insert");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("gambar", gambar);
        values.put("url", url);
        values.put("des", des);
        values.put("kategori", kategori);
        values.put("favorit", favorit);
        values.put("judul", judul);
        // 3. insert
        db.insert(DB_TABLE, null, values);
        // 4. close
        db.close();
        Toast.makeText(context, "insert value", Toast.LENGTH_LONG);
        Log.i("insert into DB", "After insert");
    }
    /* Retrive  data from database */
    public List<DBModel> getFromDB(){
        List<DBModel> modelList = new ArrayList<DBModel>();
        String query = "select * from "+DB_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do {
                DBModel model = new DBModel();
                model.setId(cursor.getString(0));
                model.setJudul(cursor.getString(1));
                model.setGambar(cursor.getString(2));
                model.setUrl(cursor.getString(3));
                model.setDes(cursor.getString(4));
                model.setKategori(cursor.getString(5));
                model.setFavorit(cursor.getString(6));
                modelList.add(model);
            }while (cursor.moveToNext());
        }

        Log.d("student data", modelList.toString());
        return modelList;
    }
    /*delete a row from database*/
    public void deletDB(String id){
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(DB_TABLE, "id" + " = ?", new String[] { id });
        db.close();
    }

}