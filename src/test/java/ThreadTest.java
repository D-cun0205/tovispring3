public class ThreadTest implements Runnable {

    @Override
    public void run() {
        while(true) {

            System.out.println("***** <<" + Thread.currentThread().getName() + ">> *****");

            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

            aMethod();
        }
    }

    public void aMethod() {
        Thread.dumpStack(); //aMethod,
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new ThreadTest(), "첫번째 쓰레드");
        t1.start();
        Thread t2 = new Thread(new ThreadTest(), "두번째 쓰레드");
        t2.start();

        int threadCnt = Thread.activeCount(); // 실행중인 쓰레드 개수
        Thread[] threads = new Thread[threadCnt];

        for(int i = 0; i < threadCnt; i++) {
            Thread t = threads[i];
            System.out.println("쓰레드 명 : " + t.getName());
        }
    }
}
