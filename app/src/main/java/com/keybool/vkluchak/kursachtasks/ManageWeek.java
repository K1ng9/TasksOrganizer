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

import java.io.IOException;

/**
 * Created by vkluc_000 on 23.02.2015.
 */
public class ManageWeek extends Activity {
    final String LOG_TAG = "myLogs";

    int week;
    Button btnAddW, btnUpdW;
    EditText etNumber, etIdW;

    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manageweek);

        btnAddW = (Button) findViewById(R.id.btnAddW);
        btnUpdW = (Button) findViewById(R.id.btnUpdW);

        etIdW = (EditText) findViewById(R.id.etIDW);
        etNumber = (EditText) findViewById(R.id.etNumber);

        // откриваем подлючение к ДБ
        db = new DB(this);
        db.open();

    }

    public void onclickW(View v){
        try {
            String numb = etNumber.getText().toString();
            //данние из полей в переменние
            if (numb != "") {
                // из текста делаем количество
                week = Integer.parseInt(numb);
            } else week = 0;

            switch (v.getId()) {
                case R.id.btnAddW:
                    Log.d(LOG_TAG, "----Insert in Week: ----");
                    db.addWeek(week);
                    Log.d(LOG_TAG, "----Insert in Week is done: ----");
                    break;
                case R.id.btnUpdW:
                    break;
                case R.id.btnDeleteW:
                    Log.d(LOG_TAG, "went dell " + etIdW);
                    if (etIdW.getText().toString() != "") {
                        String id = etIdW.getText().toString();
                        int idWeek = Integer.parseInt(id);
                        db.delWeek(idWeek);
                    }
                    break;
            }
        }catch (Exception ex){}
    }
}
