package com.example.garbageclassification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private TextView tv_return;
    private RecyclerView recyclerView;
    private List<String> stringList = new ArrayList<>();
    private EditText things;
    private MyDatabaseHelper myDatabaseHelper;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        things = (EditText)findViewById(R.id.result_things);

        //加载搜索结果界面
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String spSearch = sharedPreferences.getString("search", "");//得到搜索词
        things.setHint(spSearch);

        myDatabaseHelper = new MyDatabaseHelper(this, "Classification.db", null, 1);
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
        recyclerView = (RecyclerView)findViewById(R.id.result_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(ResultActivity.this));
        Cursor cursor = sqLiteDatabase.query("Category", null, "garbage like ?", new String[]{"%"+spSearch+"%"}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                stringList.add(cursor.getString(cursor.getColumnIndex("garbage"))
                        +"    >    "
                        +cursor.getString(cursor.getColumnIndex("class")));
            } while (cursor.moveToNext());
        }
        if(cursor.getCount() == 0)
            stringList.add("dbq，暂时没有该物品的分类。");
        cursor.close();
        recyclerView.setAdapter(new LinearAdapter(ResultActivity.this, stringList, spSearch, new LinearAdapter.OnItemClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(ResultActivity.this, WebActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        }));

        //搜索事件
        searchButton = (Button)findViewById(R.id.result_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thingString = things.getText().toString();
                if(thingString.equals("")) {
                    Toast.makeText(ResultActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
                }else {
                    //存储搜索记录
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("word",thingString);
                    sqLiteDatabase.insert("Record",null ,contentValues);
                    //替换当前搜索词
                    editor = sharedPreferences.edit();
                    editor.putString("search", thingString);
                    editor.apply();
                    //加载搜索结果
                    Intent intent = new Intent(ResultActivity.this, ResultActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            }
        });

        //返回事件
        tv_return = (TextView)findViewById(R.id.result_return);
        tv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}