package io.github.smile_ns.yourcountry.pages;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.*;

public class DeleteConfirmPage extends Page {

    private static final List<Inventory> invList = new ArrayList<>();

    @Override
    protected Inventory createInv() {
        Inventory inv = getServer().createInventory(null, 9,"DELETE");

        ItemStack accept = createIcon(Material.RED_WOOL, RED + "削除する");

        ItemStack[] items = {
                null, BACK, accept
        } ;

        int[] placements = {
                1, 0, 0, 0, 2, 0, 0, 0, 0
        };

        setGuiItems(items, placements, inv);
        return inv;
    }

    @Override
    public void open(Player viewer) {
        Inventory inv = createInv();
        viewer.openInventory(inv);
        invList.add(inv);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player viewer = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        Inventory inv = viewer.getOpenInventory().getTopInventory();

        event.setCancelled(true);
        if (inv != event.getClickedInventory()) return;

        if (slot == 0)
            new Menu().open(viewer);
        else if (slot == 4) {
            viewer.closeInventory();
            viewer.chat("/country delete");
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        Inventory inv = event.getPlayer().getOpenInventory().getTopInventory();
        invList.remove(inv);
    }

    public static boolean isPage(Inventory inv) {
        return invList.contains(inv);
    }
}
