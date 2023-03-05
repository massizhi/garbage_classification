package com.example.garbageclassification;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity {
    private Button back;
    private Button update;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private EditText etName;
    private EditText etNote;
    private TextView tvTime;
    private TextView tvData;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //加载个人信息到页面
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        etName = (EditText) findViewById(R.id.info_name);
        etNote = (EditText) findViewById(R.id.info_note);
        tvTime = (TextView) findViewById(R.id.info_time);
        tvData = (TextView) findViewById(R.id.info_data);
        etName.setHint(sharedPreferences.getString("name", "不存在"));
        etNote.setHint(sharedPreferences.getString("note", ""));
        tvTime.setText(sharedPreferences.getString("registerTime", ""));
        tvData.setText(sharedPreferences.getInt("rightNumber", 0) + " / "
                + sharedPreferences.getInt("number", 0));

        //更新个人信息
        update = (Button) findViewById(R.id.info_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String note = etNote.getText().toString();
                if (name == null || name.equals("") || note == null) {
                    Toast.makeText(InfoActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("name",name);
                    editor.putString("note",note);
                    editor.apply();
                    Toast.makeText(InfoActivity.this, "用户信息更新成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(InfoActivity.this, InfoActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }

            }
        });

        //返回至我的界面
        back = (Button) findViewById(R.id.info_return);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });
    }
}