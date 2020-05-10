package me.Fupery.StandsPlus.GUI;

import me.Fupery.StandsPlus.GUI.API.InventoryMenu;
import me.Fupery.StandsPlus.GUI.API.MenuButton;
import me.Fupery.StandsPlus.Utils.Lang;
import me.Fupery.StandsPlus.Utils.SoundCompat;
import me.Fupery.StandsPlus.Utils.StandPart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;

import java.math.BigDecimal;
import java.math.RoundingMode;

class PartMenu extends InventoryMenu {
    private final StandPart part;

    PartMenu(StandMenu parent, StandPart part) {
        super(parent, ChatColor.BOLD + part.fancyName(false), InventoryType.HOPPER);
        this.part = part;
        MenuButton[] buttons = new MenuButton[]{
                new MenuButton.StaticButton(Material.OAK_SIGN, Lang.Array.POSE_MENU_HELP.messages()),
                new RotationButton(Axis.X), new RotationButton(Axis.Y), new RotationButton(Axis.Z),
                new MenuButton.CloseButton(this)
        };
        addButtons(buttons);
    }

    @Override
    public void open(JavaPlugin plugin, Player player) {
        super.open(plugin, player);
    }

    @Override
    public void close(Player player) {
        super.close(player);
        ((StandMenu) parent).stopEditing();
    }

    private void updateAngle(Axis axis, double axisAngle) {
        EulerAngle angle = part.getPose(getStand());
        EulerAngle newAngle = null;
        double shift = Math.toRadians(axisAngle);
        switch (axis) {
            case X:
                newAngle = angle.add(shift, 0, 0);
                break;
            case Y:
                newAngle = angle.add(0, shift, 0);
                break;
            case Z:
                newAngle = angle.add(0, 0, shift);
                break;
        }
        part.pose(getStand(), newAngle);
    }

    private void resetAngle(Axis axis) {
        EulerAngle angle = part.getPose(getStand());
        EulerAngle newAngle = null;
        switch (axis) {
            case X:
                newAngle = angle.setX(0);
                break;
            case Y:
                newAngle = angle.setY(0);
                break;
            case Z:
                newAngle = angle.setZ(0);
                break;
        }
        part.pose(getStand(), newAngle);
    }

    private ArmorStand getStand() {
        return ((StandMenu) parent).getStand();
    }

    private enum Axis {
        X, Y, Z;

        String getButtonText(EulerAngle angle) {
            double axisAngle;
            switch (this) {
                case X:
                    axisAngle = angle.getX();
                    break;
                case Y:
                    axisAngle = angle.getY();
                    break;
                case Z:
                    axisAngle = angle.getZ();
                    break;
                default:
                    axisAngle = 0;
            }
            BigDecimal dec = BigDecimal.valueOf(axisAngle);
            dec = dec.setScale(2, RoundingMode.HALF_UP);
            return ChatColor.GREEN + name() + Lang.AXIS.message() + dec.doubleValue();
        }
    }

    private class RotationButton extends MenuButton {
        private Axis axis;

        private RotationButton(Axis axis) {
            super(Material.COMPASS, Lang.Array.POSE_BUTTON.messages());
            this.axis = axis;
            updateButton();
        }

        @Override
        public void onClick(JavaPlugin plugin, Player player, ClickType click) {
            if (click == ClickType.MIDDLE) {
                handleClick(plugin, player, () ->  resetAngle(axis));
                return;
            }

            double shift;
            if (click == ClickType.LEFT) {
                shift = 10;
            } else if (click == ClickType.SHIFT_LEFT) {
                shift = 1;
            } else if (click == ClickType.RIGHT) {
                shift = -10;
            } else if (click == ClickType.SHIFT_RIGHT) {
                shift = -1;
            } else {
                shift = 0;
            }
            if (shift != 0) {
                handleClick(plugin, player, () -> updateAngle(axis, shift));
            }
        }

        private void updateButton() {
            ItemMeta meta = getItemMeta();
            EulerAngle angle = part.getPose(((StandMenu) parent).getStand());
            meta.setDisplayName(axis.getButtonText(angle));
            setItemMeta(meta);
        }

        private void handleClick(JavaPlugin plugin, Player player, ClickLambda lambda) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                lambda.handle();
                updateButton();
                updateInventory(plugin, player);
                new SoundCompat(Sound.UI_BUTTON_CLICK).play(player);
            });
        }
    }

    private interface ClickLambda {
        void handle();
    }
}

