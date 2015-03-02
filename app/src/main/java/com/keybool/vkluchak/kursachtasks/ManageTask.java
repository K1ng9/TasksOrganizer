package com.keybool.vkluchak.kursachtasks;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by vkluc_000 on 23.02.2015.
 */
public class ManageTask extends Activity {
    final String LOG_TAG = "myLogs";

    Button btnAdd, btnUpd, btnDelete;
    EditText etName, etId, etStatus, etWeek;
    int week;

    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtask);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnUpd = (Button) findViewById(R.id.btnUpd);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        etStatus = (EditText) findViewById(R.id.etStatus);
        etId = (EditText) findViewById(R.id.etID);
        etName = (EditText) findViewById(R.id.etName);
        etWeek = (EditText) findViewById(R.id.etWeek);

        // откриваем подлючение к ДБ
        db = new DB(this);
        db.open();
    }

    public void onclickT(View v){
        //данние из полей в переменние
        String id = etId.getText().toString();
        String name = etName.getText().toString();
        String status = etStatus.getText().toString();
        String Week = etWeek.getText().toString();

        // из текста делаем количество
        if(Week != null)
        if( Week != "" ){
            week = Integer.parseInt(Week);
        Log.d(LOG_TAG, "Task "+ name + " =  Week: " + week);
        } else week=0;

        switch (v.getId()){
            case R.id.btnAdd:
                Log.d(LOG_TAG, "----Insert in Task : ----");
               db.addTask(name, status, week);
                break;
            case R.id.btnUpd:
                if(id != "") {
                    Log.d(LOG_TAG, "Update Task " + id);
                    //int i = Integer.parseInt(id);
                    if ((status == DB.COLUMN_STATUS_PR) || (status ==  DB.COLUMN_STATUS_F)) {
                        db.updTask(id, status, week);
                    }else db.updTask(id, "no status", week);
                }
                break;
            case R.id.btnDelete:
                try {
                    if (id != "") {
                        int idTask = Integer.parseInt(id);
                        db.delTask(idTask);
                    }
                }catch (Exception e){}
                break;
        }

    }
}
