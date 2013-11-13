package com.looksea.api;

import com.looksea.db.DBConnection;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 * Root resource (exposed at "clothing" path)
 */
@Path("clothing")
public class ClothingItem {

    private static int count = 0;

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "text/plain" media type.
     *
     * @param uriInfo
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        try {
            Connection c = DBConnection.getConnection();
            DatabaseMetaData md = c.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            String s = "";
            while (rs.next()) {
                s += rs.getString(3) + "\n";
            }
            return s;
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
