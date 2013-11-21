package com.looksea.db.util;

import com.looksea.db.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mfiume
 */
public class DBUpdater {

    public static void updateTagStringForEntity(int entity_id) throws Exception {

        System.out.println("Updating tagstring for entity " + entity_id);
        Connection c = DBConnection.getConnection();
        Statement s2 = c.createStatement();
        ResultSet r2 = s2.executeQuery("SELECT tagname FROM entity_view WHERE entity_id = " + entity_id);

        String tagsForEntity = "";
        while (r2.next()) {
            String tagname = r2.getString(1);
            if (r2.wasNull()) { continue; }
            tagsForEntity += tagname + " ";
        }
        tagsForEntity = tagsForEntity.trim();

        Statement s3 = c.createStatement();
        if (tagsForEntity.isEmpty()) {
            s3.executeUpdate("UPDATE entity SET tagstring = NULL WHERE entity_id = " + entity_id);
        } else {
            s3.executeUpdate("UPDATE entity SET tagstring = '" + tagsForEntity + "' WHERE entity_id = " + entity_id);
        }
        try { c.close(); } catch (Exception e) {}
    }

    private static void updateTagStringsForAllEntities() throws Exception {
        Connection c = DBConnection.getConnection();

        Statement s1 = c.createStatement();
        ResultSet r1 = s1.executeQuery("SELECT entity_id FROM entity");

        //c.setAutoCommit(false);
        while (r1.next()) {
            int entity_id = r1.getInt(1);
            updateTagStringForEntity(entity_id);
        }
        //c.commit();
        //c.setAutoCommit(true);

        c.close();
    }

    public static void main(String[] argv) throws Exception {
        DBUpdater.updateTagStringsForAllEntities();
    }
}
