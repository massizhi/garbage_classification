package com.example.garbageclassification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private Button button;
    private EditText name;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private MyDatabaseHelper myDatabaseHelper;
//    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //创建数据库Classification.db及相应表格（Record，Category）
        myDatabaseHelper = new MyDatabaseHelper(this, "Classification.db", null, 1);
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

        //将txt文件中的垃圾种类对应表载入到数据库
        Cursor cursor = sqLiteDatabase.query("Category", null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            try {
                //文件读取到stringList
                InputStream inputStream = getResources().openRawResource(R.raw.trash);
                InputStreamReader read = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                List<String> stringList = new ArrayList<>();
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    stringList.add(lineTxt);
                }
                read.close();
                //放入数据库中
                for (int i = 0; i < stringList.size(); i++) {
                    String temp = stringList.get(i);
                    String[] list = temp.split("(\\s)+");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("garbage", list[0]);
                    contentValues.put("class", list[1]);
                    sqLiteDatabase.insert("Category", null, contentValues);
                }
            } catch (Exception e) {
                System.out.println("读取文件内容出错");
                e.printStackTrace();
            }
        }
        cursor.close();

        //登录
        name = (EditText) findViewById(R.id.login_name);
        //使用sharedPreferences存储账号信息
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String spName = sharedPreferences.getString("name", "");
        //自动登录
        if (!spName.equals("")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(LoginActivity.this, "用户" + spName + "自动登录成功", Toast.LENGTH_SHORT).show();
            Log.v("showAll", sharedPreferences.getAll().toString());
        }
        //点击登录
        button = (Button) findViewById(R.id.login_bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = name.getText().toString();
                //判断各种登录情况
                if (nameString.equals("") || nameString == null) {
                    Toast.makeText(LoginActivity.this, "请输入您的用户名", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("name", nameString);
                    editor.putString("note", "");
                    editor.putString("registerTime", new Date().toString());
                    editor.putInt("number", 0);
                    editor.putInt("rightNumber", 0);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "用户" + nameString + "登录成功", Toast.LENGTH_SHORT).show();
                    Log.v("showAll", sharedPreferences.getAll().toString());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}