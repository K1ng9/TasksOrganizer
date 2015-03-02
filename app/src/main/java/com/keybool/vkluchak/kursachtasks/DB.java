package com.keybool.vkluchak.kursachtasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLClientInfoException;
import java.sql.SQLException;

/**
 * Created by vkluc_000 on 21.02.2015.
 */
public class DB {
    final String LOG_TAG = "myLogs";

    private static final String DB_NAME = "mydb4";
    private static final int DB_VERSION = 9;

    public static final String DB_TABLE_WEEK = "week";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NUMBER = "number";

    private static final String DB_TABLE_TASK = "task";
    public static final String COLUMN_TASK_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_WEEK = "week";

    public static final String COLUMN_STATUS_PR = "progress";
    public static final String COLUMN_STATUS_F = "finished";

    // таблица недели
    private static final String DB_CREATE_WEEK =
            "create table " + DB_TABLE_WEEK + " ("
                    + COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_NUMBER +" int" +");";

    // таблица задания
    private static final String DB_CREATE_TASK =
            "create table " + DB_TABLE_TASK + " ("
                    + COLUMN_TASK_ID + " integer primary key autoincrement, " +
                    COLUMN_NAME + " text, " +
                    COLUMN_STATUS + " text, " +
                    COLUMN_WEEK + " int, " +
                    "FOREIGN KEY(" + COLUMN_WEEK + ") REFERENCES " + DB_TABLE_WEEK + "(" + COLUMN_ID + "));";

    private final Context mCtx;

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx){
        mCtx = ctx;
    }

    // открить подключение
    public void open(){
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрить подлючение
    public void close(){
        if(mDBHelper!=null)
            mDBHelper.close();
    }

    // --------------------------------------WEEK операци-------------------------------------------
    // получить все данные из таблицы DB_WEEK
    public Cursor getAllWeek(){return mDB.query(DB_TABLE_WEEK, null, null, null, null, null, null);}

    // добавить запись WEEK
    public void addWeek(int number) {
        if(number != 0){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NUMBER, number);
        long rowID = mDB.insert(DB_TABLE_WEEK, null, cv);
        Log.d(LOG_TAG, "Week row inserted, ID = " + rowID);
        }
    }
    // удалить запись из DB_TABLE_WEEK
    public void delWeek(int id) {
        Log.d(LOG_TAG, "delete " +id);
        //mDB.delete(DB_TABLE_WEEK, null, null);
        mDB.delete(DB_TABLE_WEEK, COLUMN_ID + " = " + id, null);}

    //
    public Cursor getWeek(){
        // написать вивод по id ПОТОМ ----
        return mDB.query(DB_TABLE_WEEK, null, null, null, null, null, null);
    }

    /*public Cursor getWeekSelect(){
        // написать вивод по id с зависимими тасками
        String sqlQuery = "select "
                + "from "+DB_TABLE_WEEK+" as W "
                +"inner join " + DB_TABLE_TASK + " as T "
                +"on  W."+COLUMN_ID +" = T."+COLUMN_WEEK;
        return mDB.rawQuery(sqlQuery, null);
    }*/
    // ------------------------------- TASK операции -----------------------------------------------
    // добавить запись Task + TaskID в WEEK
    public void addTask(String name, String status, int idWeek) {
        try {
            //int statusInt = Integer.parseInt(status);
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME, name);
            //if (statusInt == 1)
            //    cv.put(COLUMN_STATUS, COLUMN_STATUS_PR);
            //else if(statusInt == 2)
            //    cv.put(COLUMN_STATUS, COLUMN_STATUS_F);
            cv.put(COLUMN_STATUS, status);
            if (idWeek != 0) {
                cv.put(COLUMN_WEEK, idWeek);
            }
            long rowID = mDB.insert(DB_TABLE_TASK, null, cv);
            Log.d(LOG_TAG, "Task row inserted, ID = " + rowID);
            Log.d(LOG_TAG, "Task row inserted, week = " + idWeek);
        }catch (Exception e ){
            Log.d(LOG_TAG, e.toString());

        }
    }

    // данные по телефонам конкретной группы
    public Cursor getTaskById(long week) {
        return mDB.query(DB_TABLE_TASK, null, COLUMN_WEEK + " = "
                + week, null, null, null, null);
    }

    // поменять состояние таска на сделаное или не сделаное
    public void updTask(String id,  String status, int idWeek ){
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_STATUS, status);
            cv.put(COLUMN_WEEK, idWeek);
            int updCount = mDB.update(DB_TABLE_TASK, cv, "id = ?",
                    new String[]{id});
            Log.d(LOG_TAG, "updated Task count = " + updCount + " status = " + status);
        }catch (Exception e) {}
    }

    // вивести таск
    public Cursor getTask(){
        // написать вивод по id ПОТОМ ----
        return mDB.query(DB_TABLE_TASK, null, null, null, null, null, null);
    }

    // удалить запись из DB_TABLE_TASK
    public void delTask(int id) {
        try {
        //mDB.delete(DB_TABLE_TASK, null, null);
        mDB.delete(DB_TABLE_TASK, COLUMN_TASK_ID + " = " + id, null);
            Log.d(LOG_TAG, "Deleted - " +id);
        }catch (Exception exeption){}

        }

    // ------------------------------ DATA BASE class ----------------------------------------------
    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TASK);
            db.execSQL(DB_CREATE_WEEK);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            if (!db.isReadOnly()) {
                // Enable foreign key constraints
                db.execSQL("PRAGMA foreign_keys=ON;");
            }
        }
    }

}
