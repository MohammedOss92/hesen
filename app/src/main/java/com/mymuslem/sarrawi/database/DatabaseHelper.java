package com.mymuslem.sarrawi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mymuslem.sarrawi.model.Adapter_zekr;
import com.mymuslem.sarrawi.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NgocTri on 11/7/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "hesenmoslem.db";
    public static final String DBLOCATION = "/data/data/com.mymuslem.sarrawi/databases/";
    public static Context mContext;
    private SQLiteDatabase mDatabase;
    private static DatabaseHelper sInstance;


    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if(mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if(mDatabase!=null) {
            mDatabase.close();
        }
    }

    public List<Product> getListProduct() {
        Product product = null;
        List<Product> productList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM azkar_type", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            product = new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),              cursor.getString(3));
            productList.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return productList;
    }

    public ArrayList<Product> getAllPrayer() {

        ArrayList<Product> contactList = new ArrayList<Product>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + "azkar_type";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("cursor", cursor.toString());
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product row = new Product();

                row.setId(Integer.parseInt(cursor.getString(0)));
                row.setName(cursor.getString(1));
                row.setName_filter(cursor.getString(3));
                row.setFav(Integer.parseInt(cursor.getString(2)));
                // Adding contact to list
                contactList.add(row);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return contactList;
    }
    public ArrayList<Adapter_zekr> getAllPrayerzekr(int typeID) {

        ArrayList<Adapter_zekr> contactList = new ArrayList<Adapter_zekr>();
        // Select All Query
        String selectQuery = "SELECT * FROM zekr where name_id='" + typeID + "' ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("cursor", cursor.toString());
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Adapter_zekr row = new Adapter_zekr();
                row.setID_zekr(Integer.parseInt(cursor.getString(0)));
                row.setName_ID(Integer.parseInt(cursor.getString(1)));
                row.setDescription(cursor.getString(2));
                row.setHint(cursor.getString(4));
                row.setCounter(cursor.getString(5));

                // Adding contact to list
                contactList.add(row);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return contactList;
    }

    public ArrayList<Product> DataFaVourit() {
        Product m;
        ArrayList<Product> Data = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
//        db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, 0);
        String SelectQuery = "Select * from azkar_type where FAV= 1";
        Cursor cursor = db.rawQuery(SelectQuery, null);
        Log.e("cursor", cursor.toString());
//        Cursor cursor = db.rawQuery("Select * from vmsg where FAV= 1", null);
        if (cursor.moveToFirst()) {

            do {
                Product row = new Product();
                row.setId(Integer.parseInt(cursor.getString(0)));
                row.setName(cursor.getString(1));
                row.setName_filter(cursor.getString(3));
                row.setFav(Integer.parseInt(cursor.getString(2)));


                Data.add(row);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return Data;
    }
    public void fav(int id, String Name,String Name_Filter,int fav) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        values.put("ID", id);
        values.put("Name", Name);
        values.put("Name_Filter", Name_Filter);
        values.put("Fav", fav);
        // Inserting Row
        db.update("azkar_type", values, "ID=" + id, null);

        db.close();
        // Closing database connection
    }

    public ArrayList<Product> getAllPrayer(String text) {
        text = "%" + text + "%";
        ArrayList<Product> contactList = new ArrayList<Product>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + "azkar_type" + " WHERE Name_filter  LIKE '" + text + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("cursor", cursor.toString());
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product row = new Product();
             /*   Log.e (" cursor.getString (1)", cursor.getString (1) + "");// send
                Log.e (" cursor.getString type", cursor.getString (2) + "");// type
                Log.e (" cursor.getString date", cursor.getString (3) + "");// date
                Log.e (" cursor.getString (4)", cursor.getString (4) + "");// time
*/
                row.setId(Integer.parseInt(cursor.getString(0)));
                row.setName(cursor.getString(1));
                row.setName_filter(cursor.getString(3));
                row.setFav(Integer.parseInt(cursor.getString(2)));

                // Adding contact to list
                contactList.add(row);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return contact list
        return contactList;
    }

    public String getMsgTitleByTitleID(int msgType) {

        String result = "";
        SQLiteDatabase db = getReadableDatabase();

        Cursor countCursor = db.rawQuery("SELECT Name from azkar_type where ID=" + msgType, null);
        if (countCursor.moveToFirst()) {
            result = countCursor.getString(0);
        }
        countCursor.close();
        db.close();

        return result;
    }

    public static DatabaseHelper getInstance(Context contexts) {
        mContext= contexts;
        if (sInstance == null) {
            sInstance = new DatabaseHelper(contexts.getApplicationContext());
        }
        return sInstance;
    }
}
