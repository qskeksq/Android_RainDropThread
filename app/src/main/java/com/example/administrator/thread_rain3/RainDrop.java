package com.example.administrator.thread_rain3;

/**
 * Created by Administrator on 2017-10-10.
 */

public class RainDrop {

    float x;
    float y;
    float speed;
    float radius;
    int color;

    float limit;

    public RainDrop(float x, float y, float speed, float radius, int color, float limit) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.radius = radius;
        this.color = color;
        this.limit = limit;
    }

//    이렇게 객체마다 스레드가 있는 경우 한 화면에 스레드가 60-100개씩 생성되기 때문에
//    시스템에 엄청난 무리가 생긴다. 따라서 전체 값만 모아서 한 곳에서 갱신하는 방법으로 하자. 주로 게임에서 이렇게 한다.
//    @Override
//    public void run() {
//        // 스레드는 항상 생명주기가 있어야 함
//        while(y < limit) {
//            y += speed;
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
