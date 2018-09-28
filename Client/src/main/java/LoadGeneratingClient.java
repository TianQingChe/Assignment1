import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LoadGeneratingClient {

    private static long requestCounter;

    private static long successCounter;

    class Runner implements Runnable{
        private WebTarget webTarget;
        private Client client;
        private final String BASE_URI;
        private final int iterNum;
        private CountDownLatch latch;

        public Runner(String uri,int iterNum,CountDownLatch latch){
            BASE_URI = uri;
            client = ClientBuilder.newClient();
            webTarget = client.target(BASE_URI);
            this.iterNum = iterNum;
            this.latch = latch;
        }

        public void run() {
            for(int i = 0; i < iterNum; i++){
                countRequest();
                if(this.getStatus().equals("alive")){
                    countSuccess();
                }

                countRequest();
                final String test = "Keep visiting you!";
                if(this.postText(test,Integer.class) == test.length()){
                    countSuccess();
                }
            }
            latch.countDown();
            close();
        }

        public void close() {
            client.close();
        }

        private <T> T postText(Object requestEntity, Class<T> responseType) throws ClientErrorException {
            return webTarget.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.TEXT_PLAIN), responseType);
        }

        private String getStatus() throws ClientErrorException {
            WebTarget resource = webTarget;
            return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
        }
    }

    public static void main(String[] args) {
//         write your code here
        LoadGeneratingClient test = new LoadGeneratingClient();

        requestCounter = 0;
        successCounter = 0;

        //default value
        int maxThreadNum = 50;
        int iterNum = 100;
        String ip = "127.0.0.1";
        String port = "8080";

        //receive arguments
        maxThreadNum = Integer.valueOf(args[0]);
        iterNum = Integer.valueOf(args[1]);
        ip = args[2];
        port = args[3];

        //initialization
//        final String uri = "http://" + ip+":"+port+"/api/server";
        final String uri = "http://" + ip+":"+port+"/RESTfulServer/api/server";
        final ExecutorService threadPool = Executors.newFixedThreadPool(maxThreadNum);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Client starting...... Time:" + df.format(new Date()));
        long start = System.currentTimeMillis();

//        test.warmUp(maxThreadNum,uri,iterNum,threadPool);
//
//        test.load(maxThreadNum,uri,iterNum,threadPool);

        //warm up phase
        int warmThreadNum = (int) (maxThreadNum * 0.1);
        System.out.println("Warmup phase: All threads running......");
        long curStart = System.currentTimeMillis();

        test.doTask(warmThreadNum,uri,iterNum,threadPool);

        long curEnd = System.currentTimeMillis();
        String timeInSeconds = String.format("%.1f", (curEnd - curStart) / 1000.0);
        System.out.println("Warmup phase complete: Time " + timeInSeconds + " seconds");


        //load phase
        int loadThreadNum = (int) (maxThreadNum * 0.5);
        System.out.println("Loading phase: All threads running......");
        curStart = System.currentTimeMillis();

        test.doTask(loadThreadNum,uri,iterNum,threadPool);

        curEnd = System.currentTimeMillis();
        timeInSeconds = String.format("%.1f", (curEnd - curStart) / 1000.0);
        System.out.println("Loading phase complete: Time " + timeInSeconds + " seconds");



        //peak phase
        System.out.println("Peak phase: All threads running......");
        curStart = System.currentTimeMillis();

        test.doTask(maxThreadNum,uri,iterNum,threadPool);

        curEnd = System.currentTimeMillis();
        timeInSeconds = String.format("%.1f", (curEnd - curStart) / 1000.0);
        System.out.println("Peak phase complete: Time " + timeInSeconds + " seconds");



        //cool down phase
        int coolThreadNum = (int) (maxThreadNum * 0.25);
        System.out.println("Cooldown phase: All threads running......");
        curStart = System.currentTimeMillis();

        test.doTask(coolThreadNum,uri,iterNum,threadPool);

        curEnd = System.currentTimeMillis();
        timeInSeconds = String.format("%.1f", (curEnd - curStart) / 1000.0);
        System.out.println("Cooldown phase complete: Time " + timeInSeconds + " seconds");

        threadPool.shutdown();
        try {
            threadPool.awaitTermination(1, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //end
        System.out.println("================================================");
        System.out.println("Total number of requests sent: " + requestCounter);
        System.out.println("Total number of Successful responses: " + successCounter);
        String totalSeconds = String.format("%.3f",(System.currentTimeMillis() - start) / 1000.0);
        System.out.println("Test Wall Time: " + totalSeconds + " s");
    }


    public void doTask(int threadNum,String uri,int iterNum,ExecutorService threadPool){
        //warm up

        CountDownLatch latch = new CountDownLatch(threadNum);

        for(int i = 0; i < threadNum; i++){
            threadPool.execute(new Runner(uri,iterNum,latch));
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public synchronized void countRequest(){
        requestCounter++;
    }

    public synchronized void countSuccess(){
        successCounter++;
    }

}
