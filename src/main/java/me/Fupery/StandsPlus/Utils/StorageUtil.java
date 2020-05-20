package me.Fupery.StandsPlus.Utils;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;

public class StorageUtil {
    private static final Map<Player, StandState> STORAGE = new HashMap<>();

    private StorageUtil() {}

    public static void store(Player player, ArmorStand stand) {
        StandState state = new StandState();
        state.arms = stand.hasArms();
        state.basePlate = stand.hasBasePlate();
        state.bodyPose = stand.getBodyPose();
        state.headPose = stand.getHeadPose();
        state.leftArmPose = stand.getLeftArmPose();
        state.leftLegPose = stand.getLeftLegPose();
        state.rightArmPose = stand.getRightArmPose();
        state.rightLegPose = stand.getRightLegPose();
        state.small = stand.isSmall();
        state.visible = stand.isVisible();
        state.gravity = stand.hasGravity();
        state.yaw = stand.getLocation().getYaw();
        STORAGE.put(player, state);
    }

    public static boolean apply(Player player, ArmorStand target) {
        StandState source = STORAGE.get(player);
        if (source == null) {
            return false;
        } else {
            target.setArms(source.arms);
            target.setBasePlate(source.basePlate);
            target.setBodyPose(source.bodyPose);
            target.setHeadPose(source.headPose);
            target.setLeftArmPose(source.leftArmPose);
            target.setLeftLegPose(source.leftLegPose);
            target.setRightArmPose(source.rightArmPose);
            target.setRightLegPose(source.rightLegPose);
            target.setSmall(source.small);
            target.setVisible(source.visible);
            target.setGravity(source.gravity);

            Location targetLoc = target.getLocation();
            targetLoc.setYaw(source.yaw);
            target.teleport(targetLoc, PlayerTeleportEvent.TeleportCause.PLUGIN);
            return true;
        }
    }
}
