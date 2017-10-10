package com.example.administrator.thread_rain3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2017-10-10.
 */
public class CustomView extends View {

    Paint paint;

    /**
     * 일반 리스트의 경우 스레드로 삭제, 추가를 함과 동시에 리스트를 사용하면
     * ConcurrentModificationException 이 발생한다. 따라서 리스트를 thread-safe 한 리스트로 바꿔준다.
     * CopyOnWriteArrayList 혹은 Observable 을 통해 Rx 적용할 수 있다.
     */
    List<RainDrop> rainDrops = new ArrayList<>();
    CopyOnWriteArrayList<RainDrop> synchronizedArrayList = new CopyOnWriteArrayList<>();

    public CustomView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.CYAN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (RainDrop rainDrop : synchronizedArrayList) {
            paint.setColor(rainDrop.color);
            canvas.drawCircle(rainDrop.x, rainDrop.y, rainDrop.radius, paint);
        }
    }

    public void addRainDrop(RainDrop drop) {
        rainDrops.add(drop);
        synchronizedArrayList.add(drop);
    }

    /**
     * RainDrop 이 추가 되는 되지 않든 계속 화면을 그려준다.
     * 보통 게임은 이렇게 작동한다. 배경은 캐릭터가 움직이든 움직이지 않든 계속 그리고 있다.
     */
    public void runStage() {
        new Thread() {
            @Override
            public void run() {
                while (MainActivity.FLAG) {
                    for (int i = 0; i < synchronizedArrayList.size(); i++) {
                        // 화면에 보이지 않는 객체 제거
                        if (synchronizedArrayList.get(i).y > synchronizedArrayList.get(i).limit) {
                            synchronizedArrayList.remove(i);
                            i--;
                            Log.e("제거됨", "=============");
                            // 객체가 스레드를 가지고 있지 않은 대신 좌표값만 하나의 스레드에서 좌표값만 계속 갱신해준다
                        } else {
                            synchronizedArrayList.get(i).y += synchronizedArrayList.get(i).speed;
                        }
                    }
                    // UI-Thread 가 아닌 경우(서브스레드인 경우) postInvalidate 사용
                    postInvalidate();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
