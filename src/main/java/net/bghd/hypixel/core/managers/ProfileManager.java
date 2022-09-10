package net.bghd.hypixel.core.managers;

import net.bghd.hypixel.core.Core;
import net.bghd.hypixel.core.Manager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProfileManager extends Manager {

    private Map<UUID, Profile> profiles = new HashMap<>();

    public ProfileManager(Core plugin) {
        super(plugin);
    }

    public void handleProfileCreation(UUID uuid, String name) {
        if (!this.profiles.containsKey(uuid)) {
            profiles.put(uuid, new Profile(uuid, name));
        }
    }

    public Profile getProfile(Object object) {
        if (object instanceof Player) {
            Player target = (Player) object;
            if (!this.profiles.containsKey(target.getUniqueId())) {
                return null;
            }
            return profiles.get(target.getUniqueId());
        }
        if (object instanceof UUID) {
            UUID uuid = (UUID) object;
            if (!this.profiles.containsKey(uuid)) {
                return null;
            }
            return profiles.get(uuid);
        }
        if (object instanceof String) {
            return this.profiles.values().stream().filter(profile -> profile.getPlayerName().equalsIgnoreCase(object.toString())).findFirst().orElse(null);
        }
        return null;
    }

/*    public List<Player> getPlayersInState(PlayerState playerState) {
        return Utilities.getOnlinePlayers().stream().filter(player ->
                getProfile(player) != null && getProfile(player).getPlayerState().equals(playerState)).collect(Collectors.toList());
    }*/

    public Map<UUID, Profile> getProfiles() {
        return this.profiles;
    }

    public void setProfiles(Map<UUID, Profile> profiles) {
        this.profiles = profiles;
    }

}
