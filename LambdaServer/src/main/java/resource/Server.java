package resource;


import java.util.Map;
import java.util.HashMap;

import javax.ws.rs.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("server")
public class Server {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getStatus() {
        return ("alive");
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public int postText(String content) {
        return (content.length());
    }
}