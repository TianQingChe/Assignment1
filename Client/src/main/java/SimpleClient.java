import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.concurrent.atomic.AtomicLong;

public class SimpleClient implements Runnable{
    private static AtomicLong requestCounter;
    private static AtomicLong successCounter;

    private WebTarget webTarget;
    private Client client;
    private final String BASE_URI;
    private final int iterNum;

    public SimpleClient(String uri,int iterNum) {
        client = ClientBuilder.newClient();
        BASE_URI = uri;
        webTarget = client.target(BASE_URI);
        requestCounter = new AtomicLong(0);
        successCounter = new AtomicLong(0);
        this.iterNum = iterNum;
    }

    private <T> T postText(Object requestEntity, Class<T> responseType) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.TEXT_PLAIN), responseType);
    }

    private String getStatus() throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public void close() {
        client.close();
    }

    public void run() {
        for(int i = 0; i < iterNum; i++){
            requestCounter.incrementAndGet();
            if(this.getStatus().equals("alive")){
                successCounter.incrementAndGet();
            }

            requestCounter.incrementAndGet();
            final String test = "Keep visiting you!";
            if(this.postText(test,Integer.class) == test.length()){
                successCounter.incrementAndGet();
            }
        }
    }

    public long getRequestCounter() {
        return requestCounter.get();
    }

    public long getSuccessCounter() {
        return successCounter.get();
    }
}
