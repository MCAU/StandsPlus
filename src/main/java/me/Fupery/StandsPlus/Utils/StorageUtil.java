package me.Fupery.StandsPlus.Utils;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;

public class StorageUtil {
    private static final Map<Player, ArmorStand> STORAGE = new HashMap<>();

    public static void store(Player player, ArmorStand stand) {
        STORAGE.put(player, stand);
    }

    public static boolean apply(Player player, ArmorStand target) {
        ArmorStand source = STORAGE.get(player);
        if (source == null) {
            return false;
        } else {
            target.setArms(source.hasArms());
            target.setBasePlate(source.hasBasePlate());
            target.setBodyPose(source.getBodyPose());
            target.setHeadPose(source.getHeadPose());
            target.setLeftArmPose(source.getLeftArmPose());
            target.setLeftLegPose(source.getLeftLegPose());
            target.setRightArmPose(source.getRightArmPose());
            target.setRightLegPose(source.getRightLegPose());
            target.setSmall(source.isSmall());
            target.setVisible(source.isVisible());
            target.setGravity(source.hasGravity());

            Location targetLoc = target.getLocation();
            targetLoc.setYaw(source.getLocation().getYaw());
            target.teleport(targetLoc, PlayerTeleportEvent.TeleportCause.PLUGIN);
            return true;
        }
    }
}
