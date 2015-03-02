package com.keybool.vkluchak.kursachtasks;

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;


public class MainActivity extends TabActivity {

    DB db;
    final String LOG_TAG = "myLogs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // откриваем подлючение к ДБ
        db = new DB(this);
        db.open();

        updateMainWindow();
    }

    public void updateMainWindow(){
        // получаем TabHost
        TabHost tabHost = getTabHost();
        // инициализация была выполнена в getTabHost
        // метод setup вызывать не нужно
        TabHost.TabSpec tabSpec;
        Cursor c = db.getWeek();
        int idColNumber = c.getColumnIndex(DB.COLUMN_NUMBER);
        int idColId = c.getColumnIndex(DB.COLUMN_ID);
        if (c.moveToFirst()) {
            do {
                //создаем вкладку и указиваем тег
                tabSpec = tabHost.newTabSpec(c.getString(idColNumber));
                // название вкладки
                tabSpec.setIndicator("Week-" + c.getString(idColNumber));
                // указиваем id компонента из FrameLayout, щн и станет содержимим
                Intent intent = new Intent(this, Item.class);
                intent.putExtra("WeekID", c.getString(idColId));
                tabSpec.setContent(intent);
                // добавляем в корневой елемент
                tabHost.addTab(tabSpec);
            } while (c.moveToNext());
        } else Log.d(LOG_TAG, "0 rows");
        c.close();

        // обработчик переключения вкладок
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                Toast.makeText(getBaseContext(), "Week = " + tabId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onclick(View v){
        switch(v.getId()) {
            case R.id.btnManageTasks:
                Intent intent = new Intent(this, ManageTask.class);
                startActivity(intent);
                break;
            case R.id.btnManageWeek:
                Intent intentWeek = new Intent(this, ManageWeek.class);
                startActivity(intentWeek);
                break;

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        db.close();
    }
}
