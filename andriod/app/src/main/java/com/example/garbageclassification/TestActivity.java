package com.example.garbageclassification;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class TestActivity extends AppCompatActivity {
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView light;
    private RadioGroup rg;
    private TextView question;
    private TextView cur;
    private TextView sum;
    private TextView answer1;
    private TextView answer2;
    private Button button;
    private MyDatabaseHelper myDatabaseHelper;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView tvNumber;
    private TextView tvRight;
    private TextView tvRank;
    String garbageDB, classDB;
    private LinearLayout show;
//    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //提示框：用户选择做题数量
        if (sharedPreferences.getInt("sumNumber", 0) == 0) {
            String[] strings = new String[]{"5", "10", "20", "下次一定"};
            AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
            builder.setTitle("请选择做题数量")
                    .setItems(strings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putInt("curNumber", 1);
                            editor.putInt("curRight", 0);
                            if (which == 0) {
                                Toast.makeText(TestActivity.this, "您选择了5题，加油。", Toast.LENGTH_SHORT).show();
                                editor.putInt("sumNumber", 5);
                                editor.apply();
                                Intent intent = new Intent(TestActivity.this, TestActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            } else if (which == 1) {
                                Toast.makeText(TestActivity.this, "您选择了10题，加油。", Toast.LENGTH_SHORT).show();
                                editor.putInt("sumNumber", 10);
                                editor.apply();
                                Intent intent = new Intent(TestActivity.this, TestActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            } else if (which == 2) {
                                Toast.makeText(TestActivity.this, "您选择了20题，加油。", Toast.LENGTH_SHORT).show();
                                editor.putInt("sumNumber", 20);
                                editor.apply();
                                Intent intent = new Intent(TestActivity.this, TestActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            } else {
                                editor.putInt("sumNumber", 0);
                                show = (LinearLayout) findViewById(R.id.test_show);
                                show.setVisibility(View.INVISIBLE);
                            }
                        }
                    }).show();
        } //提示框：当选择的题目数量做完时
        else if (sharedPreferences.getInt("sumNumber", 0) < sharedPreferences.getInt("curNumber", 0)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
            builder.setTitle("结果")
                    .setMessage("您已经做了" + sharedPreferences.getInt("sumNumber", 0)
                            + "题，正确的数量是" + sharedPreferences.getInt("curRight", 0)
                            + "，继续加油哦。").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor.putInt("sumNumber", 0);
                    editor.putInt("curNumber", 0);
                    editor.putInt("curRight", 0);
                    editor.apply();
                    Intent intent = new Intent(TestActivity.this, TestActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            }).show();
        }

        //加载并显示历史做题数据
        tvNumber = (TextView) findViewById(R.id.test_number);
        tvRight = (TextView) findViewById(R.id.test_right);
        tvRank = (TextView) findViewById(R.id.test_rank);
        int number = sharedPreferences.getInt("number", 0);
        String numberString = String.valueOf(number);
        tvNumber.setText(numberString);
        int rightNumber = sharedPreferences.getInt("rightNumber", 0);
        double radio = (number == 0 ? 0 : (double) rightNumber / (double) number * 100);
        String radioString = String.format("%.2f", radio) + "%";
        tvRight.setText(radioString);
        if (rightNumber < 5)
            tvRank.setText("青铜");
        else if (rightNumber < 50)
            tvRank.setText("白银");
        else if (rightNumber < 500)
            tvRank.setText("黄金");
        else
            tvRank.setText("王者");

        //加载题目数 X/X
        cur = (TextView) findViewById(R.id.test_cur);
        sum = (TextView) findViewById(R.id.test_sum);
        cur.setText(String.valueOf(sharedPreferences.getInt("curNumber", 0)));
        sum.setText(String.valueOf(sharedPreferences.getInt("sumNumber", 0)));

        //加载题目数据
        //如果不点击，则不加载题目
        if (sharedPreferences.getInt("sumNumber", 0) == 0
                || sharedPreferences.getInt("sumNumber", 0) < sharedPreferences.getInt("curNumber", 0)) {
            show = (LinearLayout) findViewById(R.id.test_show);
            show.setVisibility(View.INVISIBLE);
        }
        myDatabaseHelper = new MyDatabaseHelper(this, "Classification.db", null, 1);
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
        Random r = new Random();
        int id = r.nextInt(3000) + 1;
        String idString = String.valueOf(id);
        Cursor cursor = sqLiteDatabase.query("Category", null, "id = ?", new String[]{idString}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            garbageDB = cursor.getString(cursor.getColumnIndex("garbage"));
            classDB = cursor.getString(cursor.getColumnIndex("class"));
        } else {
            garbageDB = "玻璃瓶";
            classDB = "可回收物";
        }
        cursor.close();
        question = (TextView) findViewById(R.id.test_question);
        answer1 = (TextView) findViewById(R.id.test_answer1);
        answer2 = (TextView) findViewById(R.id.test_answer2);
        question.setText(garbageDB + "属于哪种垃圾？请选择对应的选项。");
        answer2.setText(garbageDB + "属于" + classDB + "。");

        //点击选项事件
        button = (Button) findViewById(R.id.test_bt);
        rg = (RadioGroup) findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //更新做题数据
                editor.putInt("number", sharedPreferences.getInt("number", 0) + 1);
                editor.putInt("curNumber", sharedPreferences.getInt("curNumber", 0) + 1);
                //确定用户的选择是否正确
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (rb.getText().toString().equals("       " + classDB)) {
                    answer2.setText("正确，" + garbageDB + "属于" + classDB + "。");
                    editor.putInt("rightNumber", sharedPreferences.getInt("rightNumber", 0) + 1);
                    editor.putInt("curRight", sharedPreferences.getInt("curRight", 0) + 1);
                } else {
                    answer2.setText("错误，" + garbageDB + "属于" + classDB + "。");
                }
                editor.apply();
                //设置选择答案后不可点击
                for (int i = 0; i < rg.getChildCount(); i++) rg.getChildAt(i).setEnabled(false);
                //显示隐藏的答案和按钮
                answer1.setVisibility(View.VISIBLE);
                answer2.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
            }
        });

        //跳转下一题事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, TestActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        //底部导航栏事件
        light = (TextView) findViewById(R.id.test_light);
        light.setTextColor(Color.parseColor("#4CAF50"));
        textView1 = (TextView) findViewById(R.id.test_tv1);
        textView2 = (TextView) findViewById(R.id.test_tv2);
        textView3 = (TextView) findViewById(R.id.test_tv3);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, GuideActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, PersonalActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}