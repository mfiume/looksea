package com.looksea.db.util;

import com.looksea.db.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author mfiume
 */
public class SampleDataFactory {


    public static void main(String[] s) throws Exception {
        generateEntityTagRelationships();
    }

    private static void generateEntityTagRelationships() throws Exception {
        clearTable("entity_tags");

        Connection c = DBConnection.getConnection();

        // get a list of tag ids
        Statement s0 = c.createStatement();
        ResultSet rs0 = s0.executeQuery("SELECT DISTINCT(tag_id) FROM tag");
        List<Integer> tags = new ArrayList<Integer>();
        while (rs0.next()) {
            System.out.println("tag " + rs0.getInt(1));
            tags.add(rs0.getInt(1));
        }

        // get a list of entity ids
        Statement s1 = c.createStatement();
        ResultSet rs1 = s1.executeQuery("SELECT DISTINCT(entity_id) FROM entity");
        List<Integer> entities = new ArrayList<Integer>();
        while (rs1.next()) {
            System.out.println("entity " + rs1.getInt(1));
            entities.add(rs1.getInt(1));
        }

        Random r = new Random();
        for (Integer entity : entities) {

            int numTags = (Math.abs(r.nextInt()) % 7);

            System.out.println("Adding " + numTags + " tags to entity " + entity);

            for (int i = 0; i < numTags; i++) {

                int tag_index = (Math.abs(r.nextInt()) % tags.size());

                int tag_id = tags.get(tag_index);

                Statement s2 = c.createStatement();
                s2.executeUpdate("INSERT INTO entity_tags VALUES (" + entity + "," + tag_id + ")");

                DBUpdater.updateTagStringForEntity(entity);
            }
        }
        try { c.close(); } catch (Exception e) {}
    }

    private static void clearTable(String tableName) throws Exception {
        Connection c = DBConnection.getConnection();
        Statement s = c.createStatement();
        s.executeUpdate("DELETE FROM " + tableName + " WHERE 1=1");
        c.close();
    }
}
