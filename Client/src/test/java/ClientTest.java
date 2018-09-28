import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientTest {
//    private SimpleClient client = new SimpleClient();
//    @Test
//    public void getTest() {
//
//        String response = client.getStatus();
//
//        System.out.println(response);
//    }
//
//    @Test
//    public void postTest(){
//        String response = client.postText("Let me see",Integer.class).toString();
//
//        System.out.println(response);
//    }
//    @Test
//    public void loadTest_remote(){
//        int maxThreadNum = 5000;
//        int iterNum = 100;
//        String ip = "http://34.226.31.240";
//        String port = "8080";
//
//        String uri = ip+":"+port+"/RESTfulServer/api/server";
//
//        SimpleClient runner = new SimpleClient(uri);
//
//        //warm up
//        long start = System.currentTimeMillis();
//        System.out.println("SimpleClient starting...... Time:" + System.currentTimeMillis());
//        System.out.println("Warmup phase: All threads running......");
//        int warmNum = (int) (maxThreadNum * 0.1);
//        for(int i = 0; i < warmNum; i++){
//            Thread t = new Thread(runner);
//            t.start();
//        }
//        long end = System.currentTimeMillis();
//        String timeInSeconds = String.format("%.1f", (end - start) / 1000.0);
//        System.out.println("Warmup phase complete: Time " + timeInSeconds + "seconds");
//
//
//        //loading
//        int loadNum = (int) (maxThreadNum * 0.5);
//        System.out.println("Loading phase: All threads running......");
//        start = System.currentTimeMillis();
//        for(int i = 0; i < loadNum; i++){
//            Thread t = new Thread(runner);
//            t.start();
//        }
//        end = System.currentTimeMillis();
//        timeInSeconds = String.format("%.1f", (end - start) / 1000.0);
//        System.out.println("Loading phase complete: Time " + timeInSeconds + "seconds");
//
//        //peak
//        System.out.println("Peak phase: All threads running......");
//        start = System.currentTimeMillis();
//        for(int i = 0; i < maxThreadNum; i++){
//            Thread t = new Thread(runner);
//            t.start();
//        }
//        end = System.currentTimeMillis();
//        timeInSeconds = String.format("%.1f", (end - start) / 1000.0);
//        System.out.println("Peak phase complete: Time " + timeInSeconds + "seconds");
//
//        //cool down
//        int coolNum = (int) (maxThreadNum * 0.25);
//        System.out.println("Cooldown phase: All threads running......");
//        start = System.currentTimeMillis();
//        for(int i = 0; i < coolNum; i++){
//            Thread t = new Thread(runner);
//            t.start();
//        }
//        end = System.currentTimeMillis();
//        timeInSeconds = String.format("%.1f", (end - start) / 1000.0);
//        System.out.println("Cooldown phase complete: Time " + timeInSeconds + "seconds");
//    }
//
//    @Test
//    public void loadTest_local(){
//        int maxThreadNum = 50;
//        int iterNum = 100;
//        String ip = "http://127.0.0.1";
//        String port = "8080";
//
//        String uri = ip+":"+port+"/RESTfulServer/api/server";
//
//        SimpleClient runner = new SimpleClient(uri);
//
//        //warm up
//        long start = System.currentTimeMillis();
//        System.out.println("SimpleClient starting...... Time:" + System.currentTimeMillis());
//        System.out.println("Warmup phase: All threads running......");
//        int warmNum = (int) (maxThreadNum * 0.1);
//        for(int i = 0; i < warmNum; i++){
//            Thread t = new Thread(runner);
//            t.start();
//        }
//        long end = System.currentTimeMillis();
//        String timeInSeconds = String.format("%.1f", (end - start) / 1000.0);
//        System.out.println("Warmup phase complete: Time " + timeInSeconds + "seconds");
//
//
//        //loading
//        int loadNum = (int) (maxThreadNum * 0.5);
//        System.out.println("Loading phase: All threads running......");
//        start = System.currentTimeMillis();
//        for(int i = 0; i < loadNum; i++){
//            Thread t = new Thread(runner);
//            t.start();
//        }
//        end = System.currentTimeMillis();
//        timeInSeconds = String.format("%.1f", (end - start) / 1000.0);
//        System.out.println("Loading phase complete: Time " + timeInSeconds + "seconds");
//
//        //peak
//        System.out.println("Peak phase: All threads running......");
//        start = System.currentTimeMillis();
//        for(int i = 0; i < maxThreadNum; i++){
//            Thread t = new Thread(runner);
//            t.start();
//        }
//        end = System.currentTimeMillis();
//        timeInSeconds = String.format("%.1f", (end - start) / 1000.0);
//        System.out.println("Peak phase complete: Time " + timeInSeconds + "seconds");
//
//        //cool down
//        int coolNum = (int) (maxThreadNum * 0.25);
//        System.out.println("Cooldown phase: All threads running......");
//        start = System.currentTimeMillis();
//        for(int i = 0; i < coolNum; i++){
//            Thread t = new Thread(runner);
//            t.start();
//        }
//        end = System.currentTimeMillis();
//        timeInSeconds = String.format("%.1f", (end - start) / 1000.0);
//        System.out.println("Cooldown phase complete: Time " + timeInSeconds + "seconds");
//    }

//    @Test
//    public void countTest(){
//        int maxThreadNum = 10;
//        int iterNum = 10;
//        String ip = "34.226.31.240";
//        String port = "8080";
//
//        final String uri = "http://" + ip+":"+port+"/RESTfulServer/api/server";
//
//        final SimpleClient runner = new SimpleClient(uri,iterNum);
//
//        final ExecutorService threadPool = Executors.newFixedThreadPool(maxThreadNum);
//
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println("Client starting...... Time:" + df.format(new Date()));
//
//
//
//
//
//        //warm up
//        int warmNum = (int) (maxThreadNum * 0.1);
//
//        for(int i = 0; i < iterNum; i++){
//            threadPool.execute(new Thread(runner));
//        }
//
//        long start = System.currentTimeMillis();
//        long curStart = System.currentTimeMillis();
//        System.out.println("Warmup phase: All threads running......");
//        CountDownLatch startGate = new CountDownLatch(1);
//        CountDownLatch endGate = new CountDownLatch(warmNum);
//        startGate.countDown();
//        try {
//            //主线程阻塞,等待其他所有 worker 线程完成后再执行
//            endGate.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        long curEnd = System.currentTimeMillis();
//        String timeInSeconds = String.format("%.3f", (curEnd - curStart) / 1000.0);
//        System.out.println("Warmup phase complete: Time " + timeInSeconds + " seconds");
//    }

//    @Test
//    public void warmTest(){
//        LoadGeneratingClient test = new LoadGeneratingClient();
//
//        //default value
//        int maxThreadNum = 20;
//        int iterNum = 100;
//        String ip = "127.0.0.1";
//        String port = "8080";
//
//        //initialization
//        final String uri = "http://" + ip+":"+port+"/api/server";
////        final String uri = "http://" + ip+":"+port+"/RESTfulServer/api/server";
//        final ExecutorService threadPool = Executors.newFixedThreadPool(maxThreadNum);
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println("Client starting...... Time:" + df.format(new Date()));
//        long start = System.currentTimeMillis();
//
////        test.warmUp(maxThreadNum,uri,iterNum,threadPool);
////
////        test.load(maxThreadNum,uri,iterNum,threadPool);
//
//        int warmNum = (int) (maxThreadNum * 0.1);
//        System.out.println("Warmup phase: All threads running......");
//        long curStart = System.currentTimeMillis();
//        test.doTask(warmNum,uri,iterNum,threadPool);
//        long curEnd = System.currentTimeMillis();
//        String timeInSeconds = String.format("%.3f", (curEnd - curStart) / 1000.0);
//        System.out.println("Warmup phase complete: Time " + timeInSeconds + " seconds");
//    }

}
