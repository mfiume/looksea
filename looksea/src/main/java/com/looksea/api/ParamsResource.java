package com.looksea.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author mfiume
 */
@Path("params/{username}")
public class ParamsResource {

    @GET
    public String get(@Context UriInfo ui) {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
        MultivaluedMap<String, String> pathParams = ui.getPathParameters();

        String s = "";

        s += "Query params = ";
        for (String key : queryParams.keySet()) {
            s += key + ":" + queryParams.getFirst(key) + " ";
        }
        s += "  |  Path params = ";
        for (String key : pathParams.keySet()) {
            s += key + ":" + pathParams.getFirst(key) + " ";
        }

        return s;
    }
}
