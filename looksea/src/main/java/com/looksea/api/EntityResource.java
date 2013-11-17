package com.looksea.api;

import com.looksea.db.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Root resource (exposed at "clothing" path)
 */
@Path("entity")
public class EntityResource {

    private static int count = 0;

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "text/plain" media type.
     *
     * @param ui
     * @param uriInfo
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@Context UriInfo ui) {
        try {

            MultivaluedMap<String, String> queryParams = ui.getQueryParameters();

            boolean doStart = false;
            int start = 0;
            if (queryParams.containsKey("start")) {
                doStart = true;
                start = Integer.parseInt(queryParams.getFirst("start"));
            }

            boolean doLimit = false;
            int limit = 0;
            if (queryParams.containsKey("limit")) {
                doLimit = true;
                limit = Integer.parseInt(queryParams.getFirst("limit"));
            }

            String query = "SELECT * FROM entity";
            if (doLimit && doStart) {
                query = String.format("SELECT * FROM entity LIMIT %d,%d", start, limit);
            }

            Connection c = DBConnection.getConnection();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);

            JSONArray enitities = new JSONArray();
            while (rs.next()) {
                int id = rs.getInt(1);
                int user_id = rs.getInt(2);
                String comment = rs.getString(3);
                String url = "http://www.looksea.co/resources/media/large/" + rs.getString(4);

                JSONObject jo = new JSONObject();
                jo.put("id",id);
                jo.put("user_id",user_id);
                jo.put("comment",comment);
                jo.put("url", url);

                JSONObject entity = new JSONObject();
                entity.put("entity",jo);

                enitities.add(entity);
            }

            c.close();

            JSONObject root = new JSONObject();
            root.put("entities", enitities);

            return root.toString();

        } catch (Exception ex) {
            return "Damn " + (count++) + " " + ex.getLocalizedMessage();
        }

    }

    /*@GET
    public String get(@Context UriInfo uriInfo) {
        String query = uriInfo.getRequestUri().getQuery();
        return "Query is " + query;
    }*/
}
