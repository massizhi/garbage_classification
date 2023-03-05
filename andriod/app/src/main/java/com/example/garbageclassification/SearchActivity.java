package com.example.garbageclassification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private Button button;
    private TextView tvReturn;
    private TextView clear;
    private RecyclerView recyclerView1;
    private List<String> stringList1 = new ArrayList<>();
    private RecyclerView recyclerView2;
    private List<String> stringList2 = new ArrayList<>();
    private MyDatabaseHelper myDatabaseHelper;
    private EditText things;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //历史搜索
        myDatabaseHelper = new MyDatabaseHelper(this, "Classification.db", null, 1);
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Record", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                stringList1.add(cursor.getString(cursor.getColumnIndex("word")));
            } while (cursor.moveToNext());
        }
        cursor.close();

        //常见搜索
        String[] categoryList = {"小龙虾", "前男友", "电脑", "塑料袋", "豆浆", "饼干", "瓜子", "手机"};
        for (int i = 0; i < 8; i++)
            stringList2.add(categoryList[i]);
        recyclerView1 = (RecyclerView) findViewById(R.id.search_view1);
        recyclerView1.setLayoutManager(new GridLayoutManager(SearchActivity.this, 3));
        recyclerView1.setAdapter(new GridAdapter(SearchActivity.this, stringList1, new GridAdapter.OnItemClickListener() {
            @Override
            public void onClick(String find) {
                //将搜索词放到sharedPreferences里
                sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("search", find);
                editor.apply();
                //跳转页面
                Intent intent = new Intent(SearchActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        }));

        recyclerView2 = (RecyclerView) findViewById(R.id.search_view2);
        recyclerView2.setLayoutManager(new GridLayoutManager(SearchActivity.this, 3));
        recyclerView2.setAdapter(new GridAdapter(SearchActivity.this, stringList2, new GridAdapter.OnItemClickListener() {
            @Override
            public void onClick(String find) {
                //将搜索词放到sharedPreferences里
                sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("search", find);
                editor.apply();
                //跳转页面
                Intent intent = new Intent(SearchActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        }));

        //执行搜索事件
        things = (EditText) findViewById(R.id.search_things);
        button = (Button) findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thingString = things.getText().toString();
                if(thingString.equals("")) {
                    Toast.makeText(SearchActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                }else {
                    //存储搜索内容
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("word",thingString);
                    sqLiteDatabase.insert("Record",null ,contentValues);
                    //将搜索词放到sharedPreferences里
                    sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("search", thingString);
                    editor.apply();
                    //跳转页面
                    Intent intent = new Intent(SearchActivity.this, ResultActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            }
        });

        //清空表Record
        clear = (TextView) findViewById(R.id.search_clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
                sqLiteDatabase.delete("Record",null,null);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        tvReturn = (TextView) findViewById(R.id.search_return);
        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}