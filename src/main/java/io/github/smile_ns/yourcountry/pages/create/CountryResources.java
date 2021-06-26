package io.github.smile_ns.yourcountry.pages.create;

import io.github.smile_ns.yourcountry.country.AccessPerms;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CountryResources {

    public static final Map<Player, CountryResources> resourceMap = new HashMap<>();

    private String name;

    private ChatColor color;

    private AccessPerms perm = AccessPerms.PUBLIC;

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public void setPerm(AccessPerms perm) {
        this.perm = perm;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public AccessPerms getPerm() {
        return perm;
    }
}
