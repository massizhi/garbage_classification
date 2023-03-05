package com.example.garbageclassification;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView light;
    private TextView search;
    private ImageView iv;
    private LinearLayout recyclable;
    private LinearLayout food;
    private LinearLayout hazardous;
    private LinearLayout residual;
    private LinearLayout linearLayout2;
//    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (TextView) findViewById(R.id.main_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        recyclable = (LinearLayout) findViewById(R.id.main_recyclable);
        recyclable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
            }
        });
        food = (LinearLayout) findViewById(R.id.main_food);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
            }
        });
        hazardous = (LinearLayout) findViewById(R.id.main_hazardous);
        hazardous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
            }
        });
        residual = (LinearLayout) findViewById(R.id.main_residual);
        residual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
            }
        });

        iv = (ImageView) findViewById(R.id.main_iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });

        linearLayout2 = (LinearLayout) findViewById(R.id.main_ll2);
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
                startActivity(intent);
            }
        });

        light = (TextView) findViewById(R.id.main_light);
        light.setTextColor(Color.parseColor("#4CAF50"));
        textView1 = (TextView) findViewById(R.id.main_tv1);
        textView2 = (TextView) findViewById(R.id.main_tv2);
        textView3 = (TextView) findViewById(R.id.main_tv3);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersonalActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}