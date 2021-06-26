package io.github.smile_ns.yourcountry.exceptions;

import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.RED;

public class PlayerIsAlreadyLeaderException extends Exception {

    private static final long serialVersionUID = 1L;
    private static final String msg = "国王はこの操作を完了できません";

    public PlayerIsAlreadyLeaderException(Player sender){
        super(msg);
        sender.sendMessage(RED + msg);
    }

    public PlayerIsAlreadyLeaderException(){
        super(msg);
    }

}
