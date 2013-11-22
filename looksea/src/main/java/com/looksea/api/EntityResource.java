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

            String tag = null;
            if (queryParams.containsKey("tag")) {
                tag = queryParams.getFirst("tag");
            }

            String query = "SELECT * FROM entity_view_normalized";

            // need to match on tag id
            if (tag != null) {
                query = String.format("SELECT entity.entity_id,entity.user_id AS username,entity.user_comment,entity.url,entity.tagstring FROM entity \n"
                        + "LEFT JOIN entity_tags\n"
                        + "ON entity.entity_id = entity_tags.`entity_id`\n"
                        + "WHERE entity_tags.`tag_id` IN (SELECT tag.`tag_id` FROM tag WHERE tag.name = '%s') GROUP BY entity.entity_id", tag);
            }

            if (doLimit && doStart) {
                query = String.format("%s LIMIT %d,%d", query, start, limit);
            }

            Connection c = DBConnection.getConnection();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);

            JSONArray enitities = new JSONArray();
            while (rs.next()) {
                int entity_id = rs.getInt("entity_id");
                String url = rs.getString("url");
                String username = rs.getString("username");
                String comment = rs.getString("user_comment");
                String tagstring = rs.getString("tagstring");
                if (rs.wasNull()) {
                    tagstring = "";
                }

                JSONObject jo = new JSONObject();
                jo.put("entity_id", entity_id);
                jo.put("username", username);
                jo.put("comment", comment);
                jo.put("url", url);
                jo.put("tagstring", tagstring);

                JSONObject entity = new JSONObject();
                entity.put("entity", jo);

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
}
