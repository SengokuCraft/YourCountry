package io.github.smile_ns.yourcountry.pages.create;

import io.github.smile_ns.yourcountry.pages.Page;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.*;

public class ChoiceColorPage extends Page {

    private static final List<Inventory> invList = new ArrayList<>();

    @Override
    public Inventory createInv() {
        Inventory inv = getServer().createInventory(null, 27,"COLOR");

        List<ItemStack> items = new ArrayList<>(Arrays.asList(null, BACK));
        items.addAll(getColorIcons());

        int[] placements = {
                0, 2, 3, 4, 5, 6, 7, 8, 0,
                0, 9, 10, 11, 12, 13, 14, 15, 0,
                0, 0, 0, 0, 1, 0 ,0 ,0, 0
        };

        setGuiItems(items.toArray(new ItemStack[values().length + 2]), placements, inv);
        return inv;
    }

    @Override
    public void open(Player viewer) {
        Inventory inv = createInv();
        viewer.openInventory(inv);
        invList.add(inv);
    }

    private List<ItemStack> getColorIcons() {
        ItemStack white = createIcon(Material.WHITE_WOOL, WHITE + "WHITE");
        ItemStack gold = createIcon(Material.ORANGE_WOOL, GOLD + "GOLD");
        ItemStack darkPurple = createIcon(Material.MAGENTA_WOOL, DARK_PURPLE + "DARK PURPLE");
        ItemStack aqua = createIcon(Material.LIGHT_BLUE_WOOL, AQUA + "AQUA");
        ItemStack yellow = createIcon(Material.YELLOW_WOOL, YELLOW + "YELLOW");
        ItemStack green = createIcon(Material.LIME_WOOL, GREEN + "GREEN");
        ItemStack lightPurple = createIcon(Material.PINK_WOOL, LIGHT_PURPLE + "LIGHT PURPLE");
        ItemStack darkGray = createIcon(Material.GRAY_WOOL, DARK_GRAY + "DARK GRAY");
        ItemStack gray = createIcon(Material.LIGHT_GRAY_WOOL, GRAY + "GRAY");
        ItemStack darkAqua = createIcon(Material.CYAN_WOOL, DARK_AQUA + "DARK AQUA");
        ItemStack blue = createIcon(Material.BLUE_WOOL, BLUE + "BLUE");
        ItemStack darkGreen = createIcon(Material.GREEN_WOOL, DARK_GREEN + "DARK GREEN");
        ItemStack red = createIcon(Material.RED_WOOL, RED + "RED");
        ItemStack black = createIcon(Material.BLACK_WOOL, BLACK + "BLACK");

        return Arrays.asList(
                white, gold, darkPurple, aqua, yellow,
                green, lightPurple, darkGray, gray, darkAqua,
                blue, darkGreen, red, black
        );
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player viewer = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        Inventory inv = viewer.getOpenInventory().getTopInventory();

        event.setCancelled(true);
        if (inv != event.getClickedInventory()) return;
        if (slot == 22) {
            new NamingPage().open(viewer);
            return;
        }

        ChatColor color =
                slot == 1 ? WHITE : slot == 2 ? GOLD : slot == 3 ? DARK_PURPLE :
                        slot == 4 ? AQUA : slot == 5 ? YELLOW : slot == 6 ? GREEN :
                                slot == 7 ? LIGHT_PURPLE : slot == 10 ? DARK_GRAY : slot == 11 ? GRAY :
                                slot == 12 ? DARK_AQUA : slot == 13 ? BLUE : slot == 14 ? DARK_GREEN :
                                        slot == 15 ? RED : slot == 16 ? BLACK : null;
        if (color == null) return;

        CountryResources resources = CountryResources.resourceMap.get(viewer);
        resources.setColor(color);
        new CreateConfirmPage().open(viewer);
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
