package me.Fupery.StandsPlus.Event;

import me.Fupery.StandsPlus.GUI.StandMenu;
import me.Fupery.StandsPlus.Recipe.StandKey;
import me.Fupery.StandsPlus.StandsPlus;
import me.Fupery.StandsPlus.Utils.SoundCompat;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 * Created by aidenhatcher on 10/07/2016.
 */
public class PlayerInteractListener implements Listener {

    private StandsPlus plugin;

    public PlayerInteractListener(StandsPlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && StandKey.handIsValidMaterial(event.getPlayer())) {
            event.setCancelled(true);
            for (Entity armour : event.getPlayer().getNearbyEntities(10, 10, 10)) {
                if (armour instanceof ArmorStand) {
                    ((ArmorStand) armour).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 40, 1));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && checkValidStand(event.getEntity())) {
            Player player = ((Player) event.getDamager());
            if (StandKey.handIsValidMaterial(player)) {
                event.setCancelled(true);
                ArmorStand stand = ((ArmorStand) event.getEntity());
                Vector vecDiff = stand.getLocation().toVector().subtract(player.getLocation().toVector());
                Vector vector = vecDiff.normalize().multiply(.1);
                if (player.isSneaking()) {
                    vector = vector.multiply(.1);
                }

                if (stand.hasGravity()) {
                    stand.setVelocity(vector);
                } else {
                    stand.teleport(stand.getLocation().add(vector));
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        handleClick(event, event.getPlayer(), event.getRightClicked());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        handleClick(event, event.getPlayer(), event.getRightClicked());
    }

    private boolean checkValidStand(Entity entity) {
        return entity.getType() == EntityType.ARMOR_STAND;
    }

    private void handleClick(Cancellable event, Player player, Entity clicked) {
        if (checkValidStand(clicked) && StandKey.handIsValidMaterial(player)) {
            event.setCancelled(true);
            new SoundCompat(Sound.BLOCK_WOODEN_BUTTON_CLICK_ON).play(player);
            Bukkit.getScheduler().runTaskLater(
                    plugin,
                    () -> new StandMenu(plugin, ((ArmorStand) clicked)).open(plugin, player),
                    10);
        }
    }
}
