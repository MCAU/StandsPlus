package me.Fupery.StandsPlus;

import me.Fupery.StandsPlus.Event.PlayerInteractListener;
import me.Fupery.StandsPlus.GUI.API.GenericMenuListener;
import me.Fupery.StandsPlus.GUI.API.InventoryMenu;
import me.Fupery.StandsPlus.Recipe.StandKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Reader;
import java.util.Map;
import java.util.UUID;

public class StandsPlus extends JavaPlugin {

    private GenericMenuListener menuListener;

    @Override
    public void onEnable() {
        addRecipes();
        menuListener = new GenericMenuListener(this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
    }

    @Override
    public void onDisable() {
        Map<UUID, InventoryMenu> menus = getOpenMenus();
        for (UUID uuid : menus.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            menus.get(uuid).close(player);
        }
    }

    public Map<UUID, InventoryMenu> getOpenMenus() {
        return menuListener.getOpenMenus();
    }

    public Reader getTextResourceFile(String fileName) {
        return getTextResource(fileName);
    }

    private void addRecipes() {
        try {
            Bukkit.addRecipe(StandKey.getRecipe());
        } catch (IllegalStateException e) {
            if (!e.getMessage().startsWith("Duplicate recipe ignored")) {
                e.printStackTrace();
            }
        }
    }
}
