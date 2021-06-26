package io.github.smile_ns.yourcountry;

import io.github.smile_ns.yourcountry.commands.CountryCmd;
import io.github.smile_ns.yourcountry.country.NationalFlag;
import io.github.smile_ns.yourcountry.pages.Page;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;
import java.util.Map;

public final class YourCountry extends JavaPlugin implements Listener {

    /*
     * java.sql Javadoc
     * https://docs.oracle.com/javase/jp/8/docs/api/java/sql/package-summary.html
     */

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        saveConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event) {
        Page.clickProcess(event);
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent event) {
        Page.closeProcess(event);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (!(sender instanceof Player)) {
            System.out.println("このコマンドはプレイヤー用です");
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("country")) new CountryCmd(player, args);
        else if (command.getName().equalsIgnoreCase("flag")) {
            Map<DyeColor, PatternType> map = new LinkedHashMap<>();

            int max = 20;
            for (int i = 0;i < (int) (Math.random() * max);i++) {
                DyeColor color = DyeColor.values()[(int) (Math.random() * DyeColor.values().length)];
                PatternType pattern = PatternType.values()[(int) (Math.random() * PatternType.values().length)];

                map.put(color, pattern);
            }

            DyeColor baseColor = DyeColor.values()[(int) (Math.random() * DyeColor.values().length)];
            ItemStack flag = new NationalFlag(baseColor, map);
            player.getInventory().addItem(flag);
        }

        return true;
    }
}
