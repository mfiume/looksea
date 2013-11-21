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
@Path("tag")
public class TagResource {

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

            String searchString = null;
            if (queryParams.containsKey("search")) {
                searchString = queryParams.getFirst("search");
            }

            boolean exactMatch = true;
            if (queryParams.containsKey("match")) {

                if (searchString == null) {
                    throw new Exception("Match specified without search term");
                }

                String matchParam = queryParams.getFirst("match");
                if (matchParam.equals("fuzzy")) {
                    exactMatch = false;
                } else {
                    throw new Exception("Unknown match param " + matchParam);
                }
            }

            String query = "SELECT * FROM tag";

            if (searchString != null) {
                if (exactMatch) {
                    query = String.format("%s WHERE name = '%s'", query, searchString);
                } else {
                    query = String.format("%s WHERE name LIKE '%%%s%%'", query, searchString);
                }
            }

            Connection c = DBConnection.getConnection();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);

            JSONArray tags = new JSONArray();
            while (rs.next()) {
                int tag_id = rs.getInt(1);
                String name = rs.getString(2);

                JSONObject jo = new JSONObject();
                jo.put("tag_id", tag_id);
                jo.put("name", name);

                JSONObject tag = new JSONObject();
                tag.put("tag", jo);

                tags.add(tag);
            }

            c.close();

            JSONObject root = new JSONObject();
            root.put("tags", tags);

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
