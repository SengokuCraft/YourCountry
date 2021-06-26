package io.github.smile_ns.yourcountry.country;

import io.github.smile_ns.yourcountry.sqlite.SQLiteValues;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.sql.*;

public class CountryManager {

    public static final String URL =
            "jdbc:sqlite:plugins" + File.separator + "YourCountry" + File.separator + "countries.db";

    static Connection connection;

    static Statement statement;

    static {
        try {
            openDirect();
            String cmd = "CREATE TABLE IF NOT EXISTS countries(id INTEGER, name TEXT, leader TEXT, color TEXT, perm TEXT)";
            statement.executeUpdate(cmd);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void createMemberTable(int id) throws SQLException {
        String cmd = "CREATE TABLE member" + id + "(uuid TEXT, rank TEXT)";
        statement.executeUpdate(cmd);
    }

    private static void createBanTable(int id) throws SQLException {
        String cmd = "CREATE TABLE ban" + id + "(uuid TEXT, ban_reason TEXT)";
        statement.executeUpdate(cmd);
    }

    private static void deleteMemberTable(int id) throws SQLException {
        String cmd = "DROP TABLE member" + id;
        statement.executeUpdate(cmd);
        statement.executeUpdate("VACUUM");
    }

    private static void deleteBanTable(int id) throws SQLException {
        String cmd = "DROP TABLE ban" + id;
        statement.executeUpdate(cmd);
        statement.executeUpdate("VACUUM");
    }

    public static Country create(String name, OfflinePlayer leader, ChatColor color, AccessPerms perm) throws SQLException {
        int id;
        do id = (int) ((Math.random() * 9000) + 1000);
        while (existCountry(id));

        SQLiteValues val = new SQLiteValues();
        val.set(id).set(name).set(leader.getUniqueId().toString()).set(color.name()).set(perm.name());
        String insert = "INSERT INTO countries VALUES(" + val.toString() + ")";
        statement.executeUpdate(insert);

        createMemberTable(id);
        createBanTable(id);
        Country country = new Country(id);
        MemberManager member = country.getMemberManager();
        member.add(leader, MemberRank.LEADER);

        country.consoleLog("created new country: [ id = " + id + " ]");
        return country;
    }

    public static void delete(int id) throws SQLException {
        String cmd = "DELETE FROM countries WHERE id == " + id;
        statement.executeUpdate(cmd);
        deleteMemberTable(id);
        deleteBanTable(id);

        System.out.println("deleted country: [ id = " + id + " ]");
    }

    public static void delete(OfflinePlayer leader) throws SQLException{
        String uuid = leader.getUniqueId().toString();
        ResultSet rs = statement.executeQuery(
                "SELECT id FROM countries WHERE leader == '" + uuid + "'");

        int id = rs.getInt(1);
        delete(id);
    }

    public static boolean existCountry(int id) throws SQLException {
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM countries WHERE id == " + id);
        return rs.next();
    }

    public static boolean existCountry(String name) throws SQLException {
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM countries WHERE name == '" + name + "'");
        return rs.next();
    }

    public static void open() {
        try {
            if (!connection.isClosed()) return;
            openDirect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void openDirect() throws SQLException {
        connection = DriverManager.getConnection(URL);
        statement = connection.createStatement();
        statement.setQueryTimeout(30);
    }

    public static void close() {
        try {
            if(connection != null) connection.close();
            if(statement != null) statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
