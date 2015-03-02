package com.keybool.vkluchak.kursachtasks;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;


public class Item extends Activity {
    DB db;
    int weekId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);

        // откриваем подлючение к ДБ
        db = new DB(this);
        db.open();

        //создание LinearLayout
        LinearLayout linLayout = new LinearLayout(this);
        //установить вертикальную ориентацию
        linLayout.setOrientation(LinearLayout.VERTICAL);
        //создать LayoutParams
        LayoutParams linLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        //устанавливаем linLayout как корневой елемент екрана
        setContentView(linLayout, linLayoutParam);

        LayoutParams lpView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);


        LinearLayout.LayoutParams leftMarginParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftMarginParams.leftMargin = 50;

        Intent intent = getIntent();

        String weekIdS = intent.getStringExtra("WeekID");
        if(weekIdS != null){
            weekId = Integer.parseInt(weekIdS);
        }else
            weekId = 1000000;
        try {

            Cursor c = db.getTask();
            int idOfTask = c.getColumnIndex(DB.COLUMN_TASK_ID);
            int idColIndex = c.getColumnIndex(DB.COLUMN_NAME);
            int idColStatus = c.getColumnIndex(DB.COLUMN_STATUS);
            int idColTaskWeek = c.getColumnIndex(DB.COLUMN_WEEK);

            if (c.moveToFirst()) {
                    do {
                        String i = c.getString(idColTaskWeek);
                        if(i != null){
                        int taskWeek = Integer.parseInt(i);
                        if (taskWeek == weekId) {
                            Button btn = new Button(this);
                            //виводим имя таска и статус в виде текста
                            btn.setText("Id - " + c.getString(idOfTask)
                                    + "  " + c.getString(idColIndex)
                                    +  "  -  " + c.getString(idColStatus));
                            linLayout.addView(btn, lpView);
                        }}

                    } while (c.moveToNext());
            }
            c.close();

        }catch(Exception e){}
    }
}