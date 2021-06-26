package io.github.smile_ns.yourcountry.country;

import org.bukkit.OfflinePlayer;

import java.sql.SQLException;
import java.util.Map;

import static io.github.smile_ns.yourcountry.country.CountryManager.statement;

public interface MemberManager {

    void add(OfflinePlayer player, MemberRank rank) throws SQLException;

    void remove(OfflinePlayer player) throws SQLException;

    void ban(OfflinePlayer player, String banReason) throws SQLException;

    Map<OfflinePlayer, MemberRank> getMembers() throws SQLException;


    void pardon(OfflinePlayer player) throws SQLException;

    boolean isBannedPlayer(OfflinePlayer player) throws SQLException;

    static boolean isLeader(OfflinePlayer player) throws SQLException {
        String uuid = player.getUniqueId().toString();
        String cmd = "SELECT leader FROM countries WHERE leader == '" + uuid + "'";
        return statement.executeQuery(cmd).next();
    }
}
