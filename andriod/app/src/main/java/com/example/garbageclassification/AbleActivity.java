package com.example.garbageclassification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class AbleActivity extends AppCompatActivity {
    private Button back;
    private Button setting;
    private Switch sw1;
    private Switch sw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_able);

        sw1 = (Switch)findViewById(R.id.able_sw1);
        sw2 = (Switch)findViewById(R.id.able_sw2);
        sw1.setEnabled(false);
        sw2.setEnabled(false);

        //跳转至设置该应用管理界面,点击权限即可修改权限。
        setting = (Button) findViewById(R.id.able_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", AbleActivity.this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        //返回至我的页面
        back = (Button) findViewById(R.id.able_return);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AbleActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });
    }
}