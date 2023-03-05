package com.example.garbageclassification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecurityActivity extends AppCompatActivity {
    private Button back;
    private TextView disappear;
    private TextView clear;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        myDatabaseHelper = new MyDatabaseHelper(this, "Classification.db", null, 1);
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        disappear = (TextView) findViewById(R.id.security_disappear);
        disappear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SecurityActivity.this,
                        "用户" + sharedPreferences.getString("name", "不存在")
                                + "已注销账号，请重新登录。", Toast.LENGTH_SHORT).show();
                editor.putString("name", null);
                editor.putString("note", null);
                editor.putString("registerTime", null);
                editor.putInt("number", 0);
                editor.putInt("rightNumber", 0);
                editor.putString("search", null);
                editor.putInt("curNumber", 0);
                editor.putInt("curRight", 0);
                editor.putInt("sumNumber", 0);
                editor.apply();

                sqLiteDatabase.delete("Record", null, null);

                Intent intent = new Intent(SecurityActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        clear = (TextView) findViewById(R.id.security_clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt("number", 0);
                editor.putInt("rightNumber", 0);
                editor.putInt("curNumber", 0);
                editor.putInt("curRight", 0);
                editor.putInt("sumNumber", 0);
                editor.apply();
                Toast.makeText(SecurityActivity.this,
                        "题测数据已清空", Toast.LENGTH_SHORT).show();
            }
        });

        back = (Button) findViewById(R.id.security_return);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecurityActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });
    }
}