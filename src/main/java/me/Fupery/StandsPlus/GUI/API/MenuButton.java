package me.Fupery.StandsPlus.GUI.API;

import me.Fupery.StandsPlus.Utils.SoundCompat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public abstract class MenuButton extends ItemStack {

    public MenuButton(Material type, String... text) {
        super(type);
        ItemMeta meta = getItemMeta();

        if (text != null && text.length > 0) {
            meta.setDisplayName(text[0]);

            if (text.length > 1) {
                String[] lore = new String[text.length - 1];
                System.arraycopy(text, 1, lore, 0, text.length - 1);
                meta.setLore(Arrays.asList(lore));
            }
        }
        setItemMeta(meta);
    }

    public abstract void onClick(JavaPlugin plugin, Player player, ClickType click);

    public static class LinkedButton extends MenuButton {

        final InventoryMenu linkedMenu;

        public LinkedButton(InventoryMenu linkedMenu, Material type, String... text) {
            super(type, text);
            this.linkedMenu = linkedMenu;
        }

        @Override
        public void onClick(JavaPlugin plugin, Player player, ClickType click) {
            new SoundCompat(Sound.UI_BUTTON_CLICK).play(player);
            linkedMenu.open(plugin, player);
        }
    }

    public static class StaticButton extends MenuButton {

        public StaticButton(Material type, String... text) {
            super(type, text);
        }

        @Override
        public void onClick(JavaPlugin plugin, Player player, ClickType click) {
        }
    }

    public static class CloseButton extends MenuButton {

        InventoryMenu menu;

        public CloseButton(InventoryMenu menu) {
            super(Material.BARRIER, "close");
            this.menu = menu;
        }

        @Override
        public void onClick(final JavaPlugin plugin, final Player player, ClickType click) {
            Bukkit.getScheduler().runTask(plugin, new Runnable() {
                @Override
                public void run() {
                    menu.close(player);

                    if (menu.parent != null) {
                        menu.parent.open(plugin, player);
                        new SoundCompat(Sound.UI_BUTTON_CLICK).play(player, 1, 3);
                    }
                }
            });
        }
    }

    public static abstract class ToggleButton extends MenuButton {

        private boolean toggle;

        public ToggleButton(Material type, boolean defaultToggle, String... text) {
            super(type, text);
            this.toggle = defaultToggle;
        }

        @Override
        public void onClick(JavaPlugin plugin, Player player, ClickType click) {
            toggle = !toggle;
        }

        public abstract void onToggle(JavaPlugin plugin, Player player);
    }
}

