package com.looksea.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONObject;

/**
 * Root resource (exposed at "home" path)
 */
@Path("home")
public class Home {

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getIt() {
        JSONObject obj = new JSONObject();
	obj.put("response", "hahaha");
        return obj.toString();
    }
}
