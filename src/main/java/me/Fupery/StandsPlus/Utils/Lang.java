package me.Fupery.StandsPlus.Utils;

import me.Fupery.StandsPlus.StandsPlus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public enum Lang {
    STAND_MENU_TITLE, POSE_MENU_BUTTON, TOGGLE_BUTTON, VISIBLE, VISIBLE_DESCRIPTION, GRAVITY, GRAVITY_DESCRIPTION,
    BASEPLATE, BASEPLATE_DESCRIPTION, ARMS, ARMS_DESCRIPTION, SMALL, SMALL_DESCRIPTION, STAND_KEY_NAME,
    STAND_KEY_LORE_CLICK, STAND_KEY_LORE_ROTATE, AXIS, EDITING, TOGGLE_ON, TOGGLE_OFF;
    String message;

    Lang() {
        StandsPlus plugin = JavaPlugin.getPlugin(StandsPlus.class);
        String language = plugin.getConfig().getString("language");
        FileConfiguration langFile = YamlConfiguration.loadConfiguration(plugin.getTextResourceFile("lang.yml"));

        if (!langFile.contains(language)) {
            language = "english";
        }
        ConfigurationSection lang = langFile.getConfigurationSection(language);
        if (lang.get(name()) != null) {
            message = lang.getString(name());
        } else {
            Bukkit.getLogger().warning(String.format("%sError loading %s from lang.yml", "[StandsPlus]", name()));
        }
    }

    public String message() {
        return message;
    }

    public enum Array {
        POSE_MENU_HELP, POSE_BUTTON, LEGS_BUTTON, STAND_BUTTON;

        String[] messages;

        Array() {
            StandsPlus plugin = JavaPlugin.getPlugin(StandsPlus.class);
            String language = plugin.getConfig().getString("language");
            FileConfiguration langFile =
                    YamlConfiguration.loadConfiguration(plugin.getTextResourceFile("lang.yml"));

            if (!langFile.contains(language)) {
                language = "english";
            }
            ConfigurationSection lang = langFile.getConfigurationSection(language);

            if (lang.get(name()) != null) {
                List<String> strings = lang.getStringList(name());
                messages = strings.toArray(new String[strings.size()]);

            } else {
                Bukkit.getLogger().warning(String.format("Error loading %s from lang.yml", name()));
            }
        }

        public String[] messages() {
            return messages;
        }
    }
}
