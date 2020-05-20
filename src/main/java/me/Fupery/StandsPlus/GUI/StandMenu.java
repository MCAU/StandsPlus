package me.Fupery.StandsPlus.GUI;

import me.Fupery.StandsPlus.GUI.API.InventoryMenu;
import me.Fupery.StandsPlus.GUI.API.MenuButton;
import me.Fupery.StandsPlus.StandsPlus;
import me.Fupery.StandsPlus.Utils.Lang;
import me.Fupery.StandsPlus.Utils.SoundCompat;
import me.Fupery.StandsPlus.Utils.StandPart;
import me.Fupery.StandsPlus.Utils.StorageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;

public class StandMenu extends InventoryMenu {
    private final ArmorStand stand;
    private final StandsPlus plugin;
    private boolean switchingMenus = false;

    public StandMenu(StandsPlus plugin, ArmorStand stand) {
        super(null, ChatColor.BOLD + Lang.STAND_MENU_TITLE.message(), InventoryType.CHEST);
        this.stand = stand;
        this.plugin = plugin;

        addButton(0, new RotateStandButton());
        addButton(9, new PropertyButton(StandProperty.ARMS));
        addButton(18, new PropertyButton(StandProperty.BASEPLATE));

        addButton(4, new PartButton(Material.CHAINMAIL_HELMET, StandPart.HEAD));
        addButton(12, new PartButton(Material.REDSTONE_TORCH, StandPart.LEFT_ARM));
        addButton(13, new PartButton(Material.CHAINMAIL_CHESTPLATE, StandPart.BODY));
        addButton(14, new PartButton(Material.REDSTONE_TORCH, StandPart.RIGHT_ARM));
        addButton(22, new LegButton());

        addButton(24, new CopyButton());
        addButton(25, new PasteButton());

        addButton(8, new PropertyButton(StandProperty.VISIBLE));
        addButton(17, new PropertyButton(StandProperty.SMALL));
        addButton(26, new PropertyButton(StandProperty.GRAVITY));

        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == null) {
                buttons[i] = new MenuButton.StaticButton(Material.GRAY_STAINED_GLASS_PANE, " ");
            }
        }
    }

    @Override
    protected Map<UUID, InventoryMenu> getOpenMenus() {
        return plugin.getOpenMenus();
    }

    @Override
    public void open(JavaPlugin plugin, Player player) {
        super.open(plugin, player);
        startEditing();
        switchingMenus = false;
    }

    @Override
    public void close(Player player) {
        super.close(player);
        if (!switchingMenus) stopEditing();
    }

    void startEditing() {
        stand.setInvulnerable(true);
        stand.setGlowing(true);
    }

    void stopEditing() {
        stand.setInvulnerable(false);
        stand.setGlowing(false);
    }

    ArmorStand getStand() {
        return stand;
    }

    private StandMenu getMenu() {
        return this;
    }

    private class PartButton extends MenuButton {
        private StandPart part;

        PartButton(Material type, StandPart part) {
            super(type, part.fancyName(true), Lang.POSE_MENU_BUTTON.message());
            this.part = part;
            ItemMeta meta = getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            setItemMeta(meta);
        }

        @Override
        public void onClick(JavaPlugin plugin, Player player, ClickType click) {
            switchingMenus = true;
            new PartMenu(getMenu(), part).open(plugin, player);
        }
    }

    private class LegButton extends MenuButton {
        LegButton() {
            super(Material.CHAINMAIL_LEGGINGS, Lang.Array.LEGS_BUTTON.messages());
            ItemMeta meta = getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            setItemMeta(meta);
        }

        @Override
        public void onClick(JavaPlugin plugin, Player player, ClickType click) {
            switchingMenus = true;
            if (click.isLeftClick()) {
                new PartMenu(getMenu(), StandPart.LEFT_LEG).open(plugin, player);
            } else if (click.isRightClick()) {
                new PartMenu(getMenu(), StandPart.RIGHT_LEG).open(plugin, player);
            }
        }
    }

    private class PropertyButton extends MenuButton {
        boolean value;
        StandProperty property;

        PropertyButton(StandProperty property) {
            super(property.getIcon(), property.getButtonTitle(property.getValue(stand)),
                    property.getDescription(), Lang.TOGGLE_BUTTON.message());
            this.value = property.getValue(stand);
            this.property = property;
            if (value) {
                addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 1);
            }
            ItemMeta meta = getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            setItemMeta(meta);
        }

        @Override
        public void onClick(JavaPlugin plugin, Player player, ClickType click) {
            value = !value;
            if (value) {
                addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 1);
            } else {
                removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
            }
            property.apply(stand, value);
            updateButton();
            updateInventory(plugin, player);
            new SoundCompat(Sound.UI_BUTTON_CLICK).play(player);
        }

        void updateButton() {
            ItemMeta meta = getItemMeta();
            meta.setDisplayName(property.getButtonTitle(value));
            setItemMeta(meta);
        }
    }

    private class RotateStandButton extends MenuButton {
        RotateStandButton() {
            super(Material.ARMOR_STAND, Lang.Array.STAND_BUTTON.messages());
        }

        @Override
        public void onClick(JavaPlugin plugin, Player player, ClickType click) {
            Location loc = stand.getLocation();
            float yaw = loc.getYaw();

            if (click == ClickType.MIDDLE) {
                yaw = 0;
            } else {
                int increment = click.isShiftClick() ? 1 : 15;
                if (click.isLeftClick()) {
                    yaw = (yaw + increment) % 360;
                } else if (click.isRightClick()) {
                    yaw = (yaw + 360 - increment) % 360;
                }
            }

            loc.setYaw(yaw);
            stand.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

    private class CopyButton extends MenuButton {
        CopyButton() {
            super(Material.LAVA_BUCKET, Lang.Array.COPY_BUTTON.messages());
        }

        @Override
        public void onClick(JavaPlugin plugin, Player player, ClickType click) {
            StorageUtil.store(player, stand);
            Bukkit.getScheduler().runTask(plugin, () -> getMenu().close(player));
        }
    }

    private class PasteButton extends MenuButton {
        PasteButton() {
            super(Material.BUCKET, Lang.Array.PASTE_BUTTON.messages());
        }

        @Override
        public void onClick(JavaPlugin plugin, Player player, ClickType click) {
            StorageUtil.apply(player, stand);
        }
    }
}
