package io.github.smile_ns.yourcountry.country;

import io.github.smile_ns.yourcountry.sqlite.SQLiteValues;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static io.github.smile_ns.yourcountry.country.CountryManager.statement;
import static org.bukkit.Bukkit.getServer;

public class Country implements CountrySystem {

    int id;

    String name;

    OfflinePlayer leader;

    ChatColor color;

    AccessPerms perm;

    MemberManager memberManager = new MemberManager() {
        @Override
        public void add(OfflinePlayer player, MemberRank rank) throws SQLException {
            String uuid = player.getUniqueId().toString();
            SQLiteValues val = new SQLiteValues();
            val.set(uuid).set(rank.name());
            String cmd = "INSERT INTO member" + id + " VALUES(" + val.toString() + ")";
            statement.executeUpdate(cmd);
        }

        @Override
        public void remove(OfflinePlayer player) throws SQLException {
            String uuid = player.getUniqueId().toString();
            String cmd = "DELETE FROM member" + id + " WHERE uuid == '" + uuid + "'";
            statement.executeUpdate(cmd);
        }

        @Override
        public void ban(OfflinePlayer player, String banReason) throws SQLException {
            String uuid = player.getUniqueId().toString();
            SQLiteValues val = new SQLiteValues();
            val.set(uuid).set(banReason);

            String cmd = "INSERT INTO ban" + id + " VALUES(" + val.toString() + ")";
            statement.executeUpdate(cmd);
            remove(player);
        }

        @Override
        public Map<OfflinePlayer, MemberRank> getMembers() throws SQLException {
            String cmd = "SELECT * FROM member" + id;
            ResultSet rs = statement.executeQuery(cmd);

            Map<OfflinePlayer, MemberRank> map = new LinkedHashMap<>();
            while (rs.next()) {
                String uuid = rs.getString(1);
                OfflinePlayer player = getServer().getOfflinePlayer(UUID.fromString(uuid));
                MemberRank rank = MemberRank.valueOf(rs.getString(2));

                map.put(player, rank);
            }
            return map;
        }

        @Override
        public void pardon(OfflinePlayer player) throws SQLException {
            String uuid = player.getUniqueId().toString();
            String cmd = "DELETE FROM ban" + id + " WHERE uuid == '" + uuid + "'";
            statement.executeUpdate(cmd);
        }

        @Override
        public boolean isBannedPlayer(OfflinePlayer player) throws SQLException {
            String uuid = player.getUniqueId().toString();
            String cmd = "SELECT uuid FROM ban" + id + " WHERE uuid == '" + uuid + "'";
            return statement.executeQuery(cmd).next();
        }
    };

    public Country(int id) throws SQLException {
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM countries WHERE id == " + id);
        init(rs);
    }

    public Country(String name) throws SQLException {
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM countries WHERE name == '" + name + "'");
        init(rs);
    }

    public Country(OfflinePlayer leader) throws SQLException {
        String uuid = leader.getUniqueId().toString();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM countries WHERE leader == '" + uuid + "'");
        init(rs);
    }

    private void init(ResultSet rs) throws SQLException {
        this.id = rs.getInt(1);
        this.name = rs.getString(2);
        this.leader = getServer().getOfflinePlayer(UUID.fromString(rs.getString(3)));
        this.color = ChatColor.valueOf(rs.getString(4));
        this.perm = AccessPerms.valueOf(rs.getString(5));
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OfflinePlayer getLeader() {
        return leader;
    }

    public ChatColor getColor() {
        return color;
    }

    public MemberManager getMemberManager() {
        return memberManager;
    }

    @Override
    public void broadcast(String msg) throws SQLException {
        for (OfflinePlayer player : memberManager.getMembers().keySet()) {
            if (player.isOnline())
                ((Player) player).sendMessage(msg);
        }
        consoleLog(msg);
    }

    @Override
    public void consoleLog(String msg) {
        System.out.println("[" + name + "] " + msg);
    }

    @Override
    public String toString() {
        return "id: " + id + ", name: " + name + ", leader: " + leader.getName() +
                ", color: " + color.name() + ", perm: " + perm.name();
    }
}
