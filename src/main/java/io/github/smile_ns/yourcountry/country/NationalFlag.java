package io.github.smile_ns.yourcountry.country;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.Map;

public class NationalFlag extends ItemStack {

    public NationalFlag(DyeColor baseColor, Map<DyeColor, PatternType> map) {
        super(Material.WHITE_BANNER);
        BannerMeta meta = (BannerMeta) super.getItemMeta();
        assert meta != null : "メタがnullです";

        meta.addPattern(new Pattern(baseColor, PatternType.BASE));
        for (Map.Entry<DyeColor, PatternType> entry : map.entrySet()) {
            Pattern pattern = new Pattern(entry.getKey(), entry.getValue());
            meta.addPattern(pattern);
        }

        super.setItemMeta(meta);
    }
}
