package me.Fupery.StandsPlus.Recipe;

import me.Fupery.StandsPlus.StandsPlus;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class StandCrate extends ItemStack {

    private static final NamespacedKey key = new NamespacedKey(JavaPlugin.getPlugin(StandsPlus.class), "standsplus.standcrate");
    private static String STAND_CRATE = "§b§oStandCrate";

    private StandCrate() {
        super(Material.CHEST);
        ItemMeta meta = getItemMeta();
        meta.setDisplayName("§e§l•§6§lArmor Stand Crate§e§l•");
        meta.setLore(Arrays.asList(
                STAND_CRATE,
                ChatColor.GRAY + "Right-Click an §aArmor Stand",
                ChatColor.GRAY + "To store it. Right-Click to place."));
        addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 1);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        setItemMeta(meta);
    }

    public static boolean isValidMaterial(ItemStack itemStack) {
        return itemStack.getType() == Material.CHEST && itemStack.hasItemMeta()
                && itemStack.getItemMeta().hasLore() && itemStack.getItemMeta().getLore().get(0).equals(STAND_CRATE);
    }

    public static Recipe getRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(key, new StandCrate());
        recipe.shape(".w.", "sxs", ".w.");
        recipe.setIngredient('w', Material.COBWEB);
        recipe.setIngredient('s', Material.SLIME_BALL);
        recipe.setIngredient('x', Material.TRIPWIRE_HOOK);
        return recipe;
    }
}
