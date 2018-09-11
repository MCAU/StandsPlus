package me.Fupery.StandsPlus.Utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundCompat {

    private final Sound sound;

    public SoundCompat(Sound sound) {
        this.sound = sound;
    }

    public void play(Player player) {
        play(player, 1, 1);
    }

    public void play(Location location) {
        play(location, 1, 1);
    }

    public void play(Player player, int volume, int pitch) {
        if (sound != null) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public void play(Location location, int volume, int pitch) {
        if (sound != null) {
            location.getWorld().playSound(location, sound, volume, pitch);
        }
    }
}
