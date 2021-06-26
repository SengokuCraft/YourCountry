package io.github.smile_ns.yourcountry.pages.create;

import io.github.smile_ns.yourcountry.country.AccessPerms;
import io.github.smile_ns.yourcountry.pages.Page;
import org.bukkit.ChatColor;
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

public class CreateConfirmPage extends Page {

    private static final List<Inventory> invList = new ArrayList<>();

    private static final ItemStack toPrivate = createIcon(Material.TRIPWIRE_HOOK, GOLD + "公開設定",
            GRAY + "クリックでprivateに切り替えます",
            GRAY + "privateにすることで国リストに表示されず、",
            GRAY + "IDを教えたプレイヤーのみ入国可能になります",
            GRAY + "建国時のログも自身にのみ表示されます");

    private static final ItemStack toPublic = createIcon(Material.TRIPWIRE_HOOK, GOLD + "公開設定",
            GRAY + "クリックでpublicに切り替えます",
            GRAY + "publicにすることで国リストに表示されるようになり、",
            GRAY + "建国時のログがサーバー内のプレイヤー全員に表示されます",
            GRAY + "デフォルトはpublicです");

    private Inventory createInv(Player player) {
        Inventory inv = getServer().createInventory(null, 27,"CONFIRM");
        CountryResources resources = CountryResources.resourceMap.get(player);
        ChatColor color = resources.getColor();

        ItemStack line = createIcon(Material.BLACK_STAINED_GLASS_PANE, " ");
        ItemStack info = createIcon(Material.PAPER, AQUA + "INFO",
                GRAY + "国名： " + resources.getName(), GRAY + "表示色： " + color + color.name()
        );
        ItemStack confirm = createIcon(Material.LIME_WOOL, GREEN + "作成する");

        ItemStack[] items = {
                null, line, info, confirm, BACK, toPrivate
        };

        int[] placements = {
                1, 1, 1, 0, 0, 0, 0, 0, 0,
                1, 2, 1, 0, 0, 3, 4, 0, 0,
                1, 1, 1, 5, 0, 0, 0, 0, 0
        };

        setGuiItems(items, placements, inv);
        return inv;
    }

    @Override
    protected Inventory createInv() {
        return null;
    }

    @Override
    public void open(Player viewer) {
        Inventory inv = createInv(viewer);
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

        CountryResources resources = CountryResources.resourceMap.get(viewer);
        switch (slot) {
            case 14:
                String countryName = resources.getName().replace(" ", "");
                String cmd = "/country create "
                        + countryName + " " + resources.getColor().name() + " " + resources.getPerm().name();

                CountryResources.resourceMap.remove(viewer);
                viewer.closeInventory();
                viewer.chat(cmd);
                break;
            case 15:
                new ChoiceColorPage().open(viewer);
                break;
            case 21:
                AccessPerms perm = resources.getPerm();
                resources.setPerm(perm == AccessPerms.PUBLIC ? AccessPerms.PRIVATE :
                        perm == AccessPerms.PRIVATE ? AccessPerms.PUBLIC : null);

                inv.setItem(21,
                        perm == AccessPerms.PUBLIC ? toPublic :
                                perm == AccessPerms.PRIVATE ? toPrivate : null);
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
