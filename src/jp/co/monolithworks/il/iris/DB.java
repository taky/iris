package jp.co.monolithworks.il.iris;

import android.database.*;
import android.database.sqlite.*;
import android.content.ContentValues;
import android.content.Context;
import android.app.Application;
import android.widget.*;
import android.util.*;

import java.io.*;
import java.util.*;

public class DB{

    private Context mContext;
    private SQLiteDatabase mDb;

    public DB(Context context){
        mContext = context;
        DBHelper dh = new DBHelper(mContext);
        mDb = dh.getWritableDatabase();
    }

    public void insert(ContentValues data){
        try{
            long ret = mDb.insert("fridge_table", null, data);
        }catch(SQLiteException e){
            throw e;
        }finally{
            cleanup();
        }
    }
    
    public void update(){
        try{
            String sql = "update fridge_table set consume_limit = consume_limit -1 ";
            mDb.execSQL(sql);
        }catch(SQLiteException e){
            throw e;
        }finally{
            cleanup();
        }
    }

    public List<Map<String,String>> query(){
        String query = "SELECT jan_code,category_name,category_icon,bar_code,consume_limit,record_date FROM fridge_table ORDER BY consume_limit";
        Cursor c = null;
        List<Map<String,String>> items = new LinkedList<Map<String,String>>();
        Log.w("DB","DB query");
        try{
            c = mDb.rawQuery(query,null);
            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                Map<String,String> item = new HashMap<String,String>();
                item.put("jan_code",c.getString(0));
                item.put("category_name",c.getString(1));
                item.put("category_icon",c.getString(2));
                item.put("bar_code",c.getString(3));
                item.put("consume_limit",c.getString(4));
                item.put("record_date", c.getString(5));
                items.add(item);
            }
        }catch(SQLiteException e){
            throw e;
        }finally {
            if(c != null){
                c.close();
            }
            cleanup();
            return items;
        }
    }

    public void delete(String[] bar_code){
        try{
            String str = "bar_code=?";
            int i = mDb.delete("fridge_table",str,bar_code);
        }catch(SQLiteException e){
            throw e;
        }finally{
            cleanup();
        }
    }

    private void cleanup() {
        if (mDb != null) {
            mDb.close();
            mDb = null;
        }
    }

    private class DBHelper extends SQLiteOpenHelper{
        private final static String DB_NAME = "fridge_db";
        private final static int VERSION = 1;

        public DBHelper(Context context){
            super(context,DB_NAME,null,VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            String str;
            str = "create table fridge_table ";
            str += "(id integer primary key autoincrement";
            str += ",jan_code text";
            str += ",category_name text";
            str += ",category_icon text";
            str += ",bar_code text";
            str += ",consume_limit text";
            str += ",record_date text)";
            db.execSQL(str);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        }
    }

}