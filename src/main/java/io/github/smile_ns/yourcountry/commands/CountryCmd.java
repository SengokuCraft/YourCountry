package io.github.smile_ns.yourcountry.commands;

import io.github.smile_ns.yourcountry.country.*;
import io.github.smile_ns.yourcountry.exceptions.IllegalArgsException;
import io.github.smile_ns.yourcountry.exceptions.PlayerIsAlreadyLeaderException;
import io.github.smile_ns.yourcountry.exceptions.PlayerNotFoundException;
import io.github.smile_ns.yourcountry.pages.Menu;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.SQLException;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.*;

public class CountryCmd {
    private final Player sender;
    private final String[] args;

    public CountryCmd(Player sender, String[] args) {
        this.sender = sender;
        this.args = args;

        CountryManager.open();
        try {
            select();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            CountryManager.close();
        }
    }

    private void select() throws Exception {
        if (args.length == 0) {
            new Menu().open(sender);
            return;
        }

        switch (args[0]) {
            case "create":
                create();
                break;
            case "delete":
                delete();
                break;
            case "join":
                join();
                break;
            case "leave":
                leave();
                break;
            case "ban":
                ban();
                break;
            case "pardon":
                pardon();
                break;
            default:
                throw new IllegalArgsException(sender);
        }
    }

    private void create() throws Exception {
        if (args.length != 3 && args.length != 4) throw new IllegalArgsException(sender);
        if (!isChatColor(args[2])) throw new IllegalArgsException(sender);
        if (args.length == 4 && !isPerm(args[3])) throw new IllegalArgsException(sender);
        if (MemberManager.isLeader(sender)) throw new PlayerIsAlreadyLeaderException(sender);
        if (CountryManager.existCountry(args[1])) {
            sender.sendMessage(RED + "その国名は既に存在します");
            return;
        }

        ChatColor color = ChatColor.valueOf(args[2].toUpperCase());
        AccessPerms perm = args.length == 4 ? AccessPerms.valueOf(args[3].toUpperCase()) : AccessPerms.PUBLIC;
        try {
            Country country = CountryManager.create(args[1], sender, color, perm);
            String msg = GRAY + "新しく " + country.getColor() + country.getName() + "(" + country.getId() + ")" +  GRAY + " が建国されました";

            if (perm == AccessPerms.PUBLIC)
                getServer().broadcastMessage(msg);
            else if (perm == AccessPerms.PRIVATE)
                sender.sendMessage(msg + "\n※このメッセージはあなただけに表示されています");
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(RED + "建国に失敗しました");
        }
    }

    private void delete() throws Exception {
        if (args.length != 1) throw new IllegalArgsException(sender);
        if (!MemberManager.isLeader(sender)) {
            sender.sendMessage(RED + "あなたは国王ではありません");
            return;
        }

        try {
            CountryManager.delete(sender);
            sender.sendMessage(GRAY + "国を削除しました");
        } catch (SQLException e) {
            e.printStackTrace();
            sender.sendMessage(RED + "削除に失敗しました");
        }
    }

    private void join() throws Exception {
        if (args.length != 2) throw new IllegalArgsException(sender);
        if (MemberManager.isLeader(sender)) throw new PlayerIsAlreadyLeaderException(sender);

        int id = Integer.parseInt(args[1]);
        if (!CountryManager.existCountry(id)) {
            sender.sendMessage(RED + "指定された国が見つかりませんでした");
            return;
        }

        Country country = new Country(id);
        MemberManager member = country.getMemberManager();
        if (member.isBannedPlayer(sender)) {
            sender.sendMessage(RED + "あなたはこの国から追放されています");
            return;
        }
        member.add(sender, MemberRank.ORDINARY);
        sender.sendMessage(country.getColor() + country.getName() + GRAY + " に所属しました");
        country.broadcast(YELLOW + sender.getName() + " が入国しました");
    }

    private void leave() throws Exception {
        if (args.length != 1) throw new IllegalArgsException(sender);
        if (MemberManager.isLeader(sender)) throw new PlayerIsAlreadyLeaderException(sender);

        Country country = new Country(sender);
        MemberManager member = country.getMemberManager();
        member.remove(sender);
        sender.sendMessage(country.getColor() + country.getName() + GRAY + " から脱退しました");
        country.broadcast(RED + sender.getName() + " が脱退しました");
    }

    private void ban() throws Exception {
        if (args.length != 2 && args.length != 3) throw new IllegalArgsException(sender);

        OfflinePlayer player = getServer().getPlayerExact(args[1]);
        if (player == null) throw new PlayerNotFoundException(sender);

        if (MemberManager.isLeader(player)) {
            sender.sendMessage(RED + "国王を追放することはできません");
            return;
        }

        MemberManager member = new Country(sender).getMemberManager();
        if (member.isBannedPlayer(player)) {
            sender.sendMessage(RED + "既に追放されています");
            return;
        }
        member.ban(player, args.length == 3 ? args[2] : null);
        sender.sendMessage(GRAY + player.getName() + " を国外追放しました");
    }

    private void pardon() throws Exception {
        if (args.length != 2) throw new IllegalArgsException(sender);

        OfflinePlayer player = getServer().getPlayerExact(args[1]);
        if (player == null) throw new PlayerNotFoundException(sender);

        MemberManager member = new Country(sender).getMemberManager();
        if (!member.isBannedPlayer(player)) {
            sender.sendMessage(RED + "追放されていません");
            return;
        }
        member.pardon(sender);
        sender.sendMessage(GRAY + player.getName() + " の追放を解除しました");
    }

    private boolean isChatColor(String name) {
        for (ChatColor color : ChatColor.values()) {
            if (color.name().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    private boolean isPerm(String name) {
        for (AccessPerms perm : AccessPerms.values()) {
            if (perm.name().equalsIgnoreCase(name)) return true;
        }
        return false;
    }
}
