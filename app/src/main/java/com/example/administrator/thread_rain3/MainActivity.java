package com.example.administrator.thread_rain3;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout stage;
    CustomView view;
    DisplayMetrics metrics;
    Random random = new Random();


    // 메인 플래그를 static 으로 선언하면 좋다. 메인이 죽으면 전부 죽기 때문
    public static boolean FLAG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        metrics = getResources().getDisplayMetrics();

        stage = (ConstraintLayout) findViewById(R.id.stage);
        // 생성자 호출
        view = new CustomView(this);
        // addView 되는 순간 onDraw() 가 호출되는 것임
        stage.addView(view);
        view.runStage();
    }

    /**
     * 일정 주기로 물방울을 추가해 줌
     */
    public void makeDrops(View v) {
        new Thread() {
            @Override
            public void run() {
                while (FLAG) {
                    // 물방울 크기, 속도, 위치를 각각 다르게 설정
                    int x = random.nextInt(metrics.widthPixels);
                    int r = random.nextInt(30) + 20;
                    int s = random.nextInt(5);
                    RainDrop rainDrop = new RainDrop(x, -r, s, r, Color.RED, metrics.heightPixels);
                    view.addRainDrop(rainDrop);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}

