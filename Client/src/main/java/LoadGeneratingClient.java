import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LoadGeneratingClient {

    private static long requestCounter;

    private static long successCounter;

    private static List<Long> latencyLst = new ArrayList<Long>();

    static class Runner implements Runnable{
        private WebTarget webTarget;
        private Client client;
        private final String BASE_URI;
        private final int iterNum;
        private CountDownLatch latch;
        final String test = "Keep visiting you!";

        public Runner(String uri,int iterNum,CountDownLatch latch){
            this.BASE_URI = uri;
            this.client = ClientBuilder.newClient();
            this.webTarget = client.target(BASE_URI);
            this.iterNum = iterNum;
            this.latch = latch;
        }

        public void run() {
            long start = System.currentTimeMillis();
            for(int i = 0; i < iterNum; i++){
                countRequest();
                start = System.currentTimeMillis();
                String response = this.getStatus();
                latencyLst.add(System.currentTimeMillis() - start);

                if(response.equals("alive")){
                    countSuccess();
                }

                countRequest();
                start = System.currentTimeMillis();
                int resLen = this.postText(test,Integer.class);
                latencyLst.add(System.currentTimeMillis() - start);

                if(resLen == test.length()){
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
        int maxThreadNum = 5000;
        int iterNum = 100;
        String ip = "127.0.0.1";
        String port = "8080";

//        //receive arguments
//        maxThreadNum = Integer.valueOf(args[0]);
//        iterNum = Integer.valueOf(args[1]);
//        ip = args[2];
//        port = args[3];

        //initialization
//        final String uri = "http://" + ip+":"+port+"/api/server";
//        final String uri = "http://" + ip+":"+port+"/RESTfulServer/api/server";
        final String uri = "https://jpuc277e1l.execute-api.us-east-1.amazonaws.com/Prod/server";
//        final String uri = args[2];
        final ExecutorService threadPool = Executors.newFixedThreadPool(maxThreadNum);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Client starting...... Time:" + df.format(new Date()));
        long start = System.currentTimeMillis();

        test.doTask_meaureLatency(maxThreadNum,uri,iterNum,threadPool);

        threadPool.shutdown();
        try {
            threadPool.awaitTermination(1, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        

        //end
        System.out.println("================================================");
        String totalSeconds = String.format("%.3f",(System.currentTimeMillis() - start) / 1000.0);
        System.out.println("Test Wall Time: " + totalSeconds + " seconds");
        System.out.println("Total number of requests sent: " + requestCounter);
        System.out.println("Total number of Successful responses: " + successCounter);
        System.out.println("Overall throughput across all phases: " + requestCounter + " requests/" + totalSeconds + " seconds");
        System.out.println("Mean latency: " + test.getMeanLatency() + "ms");
        System.out.println("Median latency: " + test.getMedianLatency() + "ms");
        System.out.println("95th percentile latency: " + test.get95Percent() + "ms");
        System.out.println("99th percentile latency: " + test.get99Percent() + "ms");
    }

    long get95Percent(){
        int index = (int) (latencyLst.size() * 0.95);
        return latencyLst.get(index);
    }

    long get99Percent(){
        int index = (int) (latencyLst.size() * 0.99);
        return latencyLst.get(index);
    }

    long getMeanLatency(){
        long sum = 0;
        for(int i=0;i<latencyLst.size();i++){
            sum += latencyLst.get(i);
        }
        return (sum / latencyLst.size());
    }

    long getMedianLatency(){
        Collections.sort(latencyLst);
        int size = latencyLst.size();
        int mid = size / 2;
        if(size % 2 == 1){
            return latencyLst.get(mid);
        }else{
            int mid2 = mid - 1;
            return (latencyLst.get(mid) + latencyLst.get(mid2)) / 2;
        }
    }

    void doTask_meaureLatency(int threadNum,String uri,int iterNum, ExecutorService threadPool){
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


    static synchronized void countRequest(){
        requestCounter++;
    }

    static synchronized void countSuccess(){
        successCounter++;
    }

}
