package com.example.garbageclassification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Security;
import java.util.Date;

public class PersonalActivity extends AppCompatActivity {
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView light;
    private TextView addition;
    private TextView info;
    private TextView security;
    private TextView able;
    private TextView response;
    private TextView about;
    private Button back;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private MyDatabaseHelper myDatabaseHelper;
//    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        myDatabaseHelper = new MyDatabaseHelper(this, "Classification.db", null, 1);
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

        textView1 = (TextView) findViewById(R.id.personal_tv1);
        textView2 = (TextView) findViewById(R.id.personal_tv2);
        textView3 = (TextView) findViewById(R.id.personal_tv3);
        info = (TextView) findViewById(R.id.personal_info);
        addition = (TextView) findViewById(R.id.personal_addition);
        security = (TextView) findViewById(R.id.personal_security);
        able = (TextView) findViewById(R.id.personal_able);
        response = (TextView) findViewById(R.id.personal_response);
        about = (TextView) findViewById(R.id.personal_about);
        back = (Button) findViewById(R.id.personal_back);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });
        addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });
        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, SecurityActivity.class);
                startActivity(intent);
            }
        });
        able.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, AbleActivity.class);
                startActivity(intent);
            }
        });
        response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, ResponseActivity.class);
                startActivity(intent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        //退出登录
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonalActivity.this,
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

                sqLiteDatabase.delete("Record",null,null);

                Intent intent = new Intent(PersonalActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        light = (TextView) findViewById(R.id.personal_light);
        light.setTextColor(Color.parseColor("#4CAF50"));
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, GuideActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, TestActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}