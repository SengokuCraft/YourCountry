package io.github.smile_ns.yourcountry.pages.create;

import io.github.smile_ns.yourcountry.pages.Page;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.*;
import static org.bukkit.Material.PAPER;

public class NamingPage extends Page {

    @Override
    protected Inventory createInv() {
        return null;
    }

    @Override
    public void open(Player viewer) {
        int maxNameLength = 16;
        String defaultMsg = "ここに国名を書いて下さい";
        ItemStack icon = createIcon(PAPER, defaultMsg, GRAY + "※空白は無視されます");
        AtomicBoolean nextPage = new AtomicBoolean(false);

        new AnvilGUI.Builder()
                .onClose(player -> {
                    if (!nextPage.get())
                        CountryResources.resourceMap.remove(viewer);
                })
                .onComplete((player, text) -> {
                    if(text.length() <= maxNameLength){
                        nextPage.set(true);
                        onName(text, viewer);
                        return AnvilGUI.Response.close();
                    } else return AnvilGUI.Response.text("国名は16文字以下でないといけません");
                })
                .text(defaultMsg)
                .itemLeft(icon)
                .title("NAME")
                .plugin(getServer().getPluginManager().getPlugin("YourCountry"))
                .open(viewer);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }

    private void onName(String name, Player viewer) {
        CountryResources resources = new CountryResources();
        resources.setName(name);
        CountryResources.resourceMap.put(viewer, resources);
        new ChoiceColorPage().open(viewer);
    }
}
