package io.github.smile_ns.yourcountry.pages;

import io.github.smile_ns.yourcountry.pages.create.ChoiceColorPage;
import io.github.smile_ns.yourcountry.pages.create.CreateConfirmPage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.*;

public abstract class Page {

    public static final Plugin PLUGIN = getServer().getPluginManager().getPlugin("YourCountry");
    public static final NamespacedKey KEY;
    public static final ItemStack BACK;

    static {
        assert PLUGIN != null;
        KEY = new NamespacedKey(PLUGIN, "gui-item");
        BACK = createIcon(Material.ARROW, GREEN + "戻る");
    }

    abstract protected Inventory createInv();
    abstract public void open(Player viewer);
    abstract public void onClick(InventoryClickEvent event);
    abstract public void onClose(InventoryCloseEvent event);

    protected static ItemStack createIcon(Material type, String title, String...desc) {
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        if (desc != null)
            meta.setLore(Arrays.asList(desc));
        meta.setDisplayName(title);
        meta.addItemFlags(ItemFlag.values());

        PersistentDataContainer data = meta.getPersistentDataContainer();
        assert KEY != null : "キーがnullです";
        data.set(KEY, PersistentDataType.BYTE, (byte) 1);

        item.setItemMeta(meta);

        return item;
    }

    protected void setGuiItems(ItemStack[] itemArray, int[] placements, Inventory inv) {
        for (int i = 0;i < placements.length;i++) {
            ItemStack item = itemArray[placements[i]];
            if (item == null) continue;
            inv.setItem(i, item);
        }
    }

    public static void clickProcess(InventoryClickEvent event) {
        Inventory inv = event.getWhoClicked().getOpenInventory().getTopInventory();

        if (Menu.isPage(inv))
            new Menu().onClick(event);
        else if (ChoiceColorPage.isPage(inv))
            new ChoiceColorPage().onClick(event);
        else if (CreateConfirmPage.isPage(inv))
            new CreateConfirmPage().onClick(event);
        else if (DeleteConfirmPage.isPage(inv))
            new DeleteConfirmPage().onClick(event);
    }

    public static void closeProcess(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();

        if (Menu.isPage(inv))
            new Menu().onClose(event);
        else if (ChoiceColorPage.isPage(inv))
            new ChoiceColorPage().onClose(event);
        else if (CreateConfirmPage.isPage(inv))
            new CreateConfirmPage().onClose(event);
        else if (DeleteConfirmPage.isPage(inv))
            new DeleteConfirmPage().onClose(event);
    }
}
