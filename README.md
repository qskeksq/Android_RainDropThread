# Thread를 통해 물방울 그리기

- 다중 스레드를 통해 화면 그리기
- ConcurrentModificationException 해결
    - thread-safe 리스트 사용 CopyOnWriteArrayList<RainDrop> synchronizedArrayList = new CopyOnWriteArrayList<>();
    - Observable을 통해 동기화
    - List<RainDrop> synchronizedArrayList = Collections.synchronizedList(new ArrayList<RainDrop>()); -> synchronized(raindrop){ ... }


![](https://github.com/qskeksq/ThreadBasic/blob/master/pic/AC_%5B20171010-184344%5D.gif)

```java
/**
 * 일정 주기로 물방울을 추가해 줌
 */
public void makeDrops(View v) {
    new Thread() {
        @Override
        public void run() {
            // 종료될때까지 무한 반복을 돌려야 한다
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
```

```java
/**
 * 물방울이 그려지는 화면으로 계속해서 스스로를 갱신해준다.
 * 보통 스레드를 사용하는 환경에서는 객체 자체를 스레드로 만들기보다는 외부 스레드에서
 * 객체의 속성을 갱신해 주는 형식으로 사용한다. 캐릭터의 움직임과 상관 없이 계속 배경을 그리고 있는 것이다.
 */
public void runStage() {
    new Thread() {
        @Override
        public void run() {
            while (MainActivity.FLAG) {
                for (int i = 0; i < synchronizedArrayList.size(); i++) {
                    // 화면에 보이지 않는 객체 제거 - 제거해주지 않으면 화면 아래에서 계속 동작한다.
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
```
