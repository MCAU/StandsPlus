package me.Fupery.StandsPlus.GUI;

import me.Fupery.StandsPlus.Utils.Lang;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;

enum StandProperty {
    VISIBLE(Lang.VISIBLE.message(), Lang.VISIBLE_DESCRIPTION.message(), Material.POTION),
    GRAVITY(Lang.GRAVITY.message(), Lang.GRAVITY_DESCRIPTION.message(), Material.FEATHER),
    BASEPLATE(Lang.BASEPLATE.message(), Lang.BASEPLATE_DESCRIPTION.message(), Material.STONE_PRESSURE_PLATE),
    ARMS(Lang.ARMS.message(), Lang.ARMS_DESCRIPTION.message(), Material.STICK),
    SMALL(Lang.SMALL.message(), Lang.SMALL_DESCRIPTION.message(), Material.EGG);

    private final String propertyName;
    private final String description;
    private final Material icon;

    StandProperty(String name, String description, Material icon) {
        this.propertyName = name;
        this.description = description;
        this.icon = icon;
    }

    String getButtonTitle(boolean value) {
        return String.format("%s: %s", ChatColor.GOLD + propertyName,
                value ? ChatColor.GREEN + Lang.TOGGLE_ON.message() : ChatColor.RED + Lang.TOGGLE_OFF.message());
    }

    void apply(ArmorStand stand, boolean value) {
        switch (this) {
            case VISIBLE:
                stand.setVisible(value);
                break;
            case GRAVITY:
                stand.setGravity(value);
                break;
            case BASEPLATE:
                stand.setBasePlate(value);
                break;
            case ARMS:
                stand.setArms(value);
                break;
            case SMALL:
                stand.setSmall(value);
                break;
        }
    }

    boolean getValue(ArmorStand stand) {
        switch (this) {
            case VISIBLE:
                return stand.isVisible();
            case GRAVITY:
                return stand.hasGravity();
            case BASEPLATE:
                return stand.hasBasePlate();
            case ARMS:
                return stand.hasArms();
            case SMALL:
                return stand.isSmall();
            default:
                return false;
        }
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getDescription() {
        return description;
    }

    public Material getIcon() {
        return icon;
    }
}
