package io.github.smile_ns.yourcountry.pages;

import io.github.smile_ns.yourcountry.pages.create.NamingPage;
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

public class Menu extends Page {

    private static final List<Inventory> invList = new ArrayList<>();

    @Override
    protected Inventory createInv() {
        Inventory inv = getServer().createInventory(null, 54,"COUNTRY");

        ItemStack line = createIcon(Material.BLACK_STAINED_GLASS_PANE, " ");
        ItemStack status = createIcon(Material.BEACON, AQUA + "---",
                GRAY + "国に所属すると、ここにその国のステータス", GRAY + "が表示されるようになります"
                );

        ItemStack create = createIcon(Material.GOLDEN_PICKAXE, GOLD + "CREATE", GRAY + "国を作成します");
        ItemStack delete = createIcon(Material.TNT, GOLD + "DELETE", GRAY + "国を削除します");
        ItemStack join = createIcon(Material.OAK_DOOR, GOLD + "JOIN",
                GRAY + "国に参加します", GRAY + "IDから探すことも可能です"
                );
        ItemStack leave = createIcon(Material.IRON_DOOR, GOLD + "LEAVE", GRAY + "現在所属している国から脱退します");

        ItemStack viewers = createIcon(Material.PLAYER_HEAD, GOLD + "PLAYERS",
                GRAY + "国民の一覧を表示します", GRAY + "国の管理者は、ここからBAN・BAN解除", GRAY + "の操作を行うこともできます"
        );
        ItemStack modify = createIcon(Material.ANVIL, GOLD + "MODIFY",
                GRAY + "国名や、国の表示色などのプロパティを", GRAY + "変更します"
        );
        ItemStack help = createIcon(Material.KNOWLEDGE_BOOK, GOLD + "HELP", GRAY + "国関連のコマンド一覧を表示します");

        ItemStack[] items = {
                null, line, status,
                create, delete, join,
                leave, viewers, modify, help
        };

        int[] placements = {
                1, 1, 1, 1, 1, 1, 1, 1, 1,
                0, 0, 0, 0, 2, 0, 0, 0, 0,
                0, 3, 0, 4, 0, 5, 0, 6, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 7, 8, 9, 0, 0, 0,
                1, 1, 1, 1, 1, 1, 1, 1, 1
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

        switch (slot) {
            case 19:
                new NamingPage().open(viewer);
                break;
            case 21:
                new DeleteConfirmPage().open(viewer);
                break;
            case 23:
                break;
            case 25:
                break;
            case 39:
                break;
            case 40:
                break;
            case 41:
                break;
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
