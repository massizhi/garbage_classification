package com.example.garbageclassification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garbageclassification.fragment.FourFragment;
import com.example.garbageclassification.fragment.OneFragment;
import com.example.garbageclassification.fragment.ThreeFragment;
import com.example.garbageclassification.fragment.TwoFragment;

public class GuideActivity extends AppCompatActivity {
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView light;
    private OneFragment oneFragment;
    private TwoFragment twoFragment;
    private ThreeFragment threeFragment;
    private FourFragment fourFragment;
    private TextView textViewOne;
    private TextView textViewTwo;
    private TextView textViewThree;
    private TextView textViewFour;
//    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        oneFragment = new OneFragment();
        getFragmentManager().beginTransaction().add(R.id.guide_ll2, oneFragment).commitAllowingStateLoss();

        textViewOne = (TextView) findViewById(R.id.guide_one);
        textViewOne.setTextColor(Color.parseColor("#4CAF50"));
        textViewTwo = (TextView) findViewById(R.id.guide_two);
        textViewThree = (TextView) findViewById(R.id.guide_three);
        textViewFour = (TextView) findViewById(R.id.guide_four);
        textViewOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oneFragment == null) {
                    oneFragment = new OneFragment();
                }
                textViewOne.setTextColor(Color.parseColor("#4CAF50"));
                textViewTwo.setTextColor(Color.parseColor("#000000"));
                textViewThree.setTextColor(Color.parseColor("#000000"));
                textViewFour.setTextColor(Color.parseColor("#000000"));
                getFragmentManager().beginTransaction().replace(R.id.guide_ll2, oneFragment).commitAllowingStateLoss();
            }
        });
        textViewTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (twoFragment == null) {
                    twoFragment = new TwoFragment();
                }
                textViewTwo.setTextColor(Color.parseColor("#4CAF50"));
                textViewOne.setTextColor(Color.parseColor("#000000"));
                textViewThree.setTextColor(Color.parseColor("#000000"));
                textViewFour.setTextColor(Color.parseColor("#000000"));
                getFragmentManager().beginTransaction().replace(R.id.guide_ll2, twoFragment).commitAllowingStateLoss();
            }
        });
        textViewThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (threeFragment == null) {
                    threeFragment = new ThreeFragment();
                }
                textViewThree.setTextColor(Color.parseColor("#4CAF50"));
                textViewOne.setTextColor(Color.parseColor("#000000"));
                textViewTwo.setTextColor(Color.parseColor("#000000"));
                textViewFour.setTextColor(Color.parseColor("#000000"));
                getFragmentManager().beginTransaction().replace(R.id.guide_ll2, threeFragment).commitAllowingStateLoss();
            }
        });
        textViewFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fourFragment == null) {
                    fourFragment = new FourFragment();
                }
                textViewFour.setTextColor(Color.parseColor("#4CAF50"));
                textViewOne.setTextColor(Color.parseColor("#000000"));
                textViewTwo.setTextColor(Color.parseColor("#000000"));
                textViewThree.setTextColor(Color.parseColor("#000000"));
                getFragmentManager().beginTransaction().replace(R.id.guide_ll2, fourFragment).commitAllowingStateLoss();
            }
        });

        light = (TextView) findViewById(R.id.guide_light);
        light.setTextColor(Color.parseColor("#4CAF50"));
        textView1 = (TextView) findViewById(R.id.guide_tv1);
        textView2 = (TextView) findViewById(R.id.guide_tv2);
        textView3 = (TextView) findViewById(R.id.guide_tv3);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, TestActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, PersonalActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}