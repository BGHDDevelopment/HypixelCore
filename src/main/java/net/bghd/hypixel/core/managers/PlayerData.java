package net.bghd.hypixel.core.managers;

import lombok.Getter;
import lombok.Setter;
import net.bghd.hypixel.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
public class PlayerData implements Listener {

    private Core plugin = Core.getInstance();
    private java.util.UUID UUID;
    private String playerName;
    private Rank rank = Rank.DEFAULT;
    private Int networkLevel = new Int();

    public PlayerData() {

    }
    public PlayerData(UUID uuid, String name) {
        this.UUID = uuid;
        this.playerName = name;
    }

    public void load(Player player) {
        Core.getInstance().getMySQLManager().select("SELECT * FROM player WHERE uuid=?;", resultSet -> {
            try {
                if (resultSet.next()) {
                    networkLevel.setAmount(resultSet.getInt("network_level"));
                    Core.getInstance().getMySQLManager().execute("UPDATE player SET name=? WHERE UUID=?",
                            player.getName(), player.getUniqueId().toString());
                } else {
                    Core.getInstance().getMySQLManager().execute("INSERT INTO player(uuid, name, network_level, gold, rank) VALUES (?,?,?,?,?)", player.getUniqueId().toString(), player.getName(), 1, 0, Rank.DEFAULT.toString());
                }
            } catch (SQLException exception) {
                //Throw
            }
        }, player.getUniqueId().toString());
    }

    public void save(Player player) {
        Core.getInstance().getMySQLManager().execute("UPDATE player SET network_level=? WHERE UUID=?", networkLevel.getAmount(), player.getUniqueId().toString());
    }

    @EventHandler
    void handleLogin(AsyncPlayerPreLoginEvent event) {
        try {
            plugin.getProfileManager().handleProfileCreation(event.getUniqueId(), event.getName());
        } catch (NullPointerException e) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "&cERROR: Could not create profile!");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player);
        try {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> profile.getData().load(player));
        } catch (NullPointerException e) {
            player.kickPlayer(ChatColor.RED + "ERROR: Profile returned null.");
            return;
        }
        if (profile == null) {
            player.kickPlayer(ChatColor.RED + "ERROR: Profile returned null.");
            return;
        }
    }

    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player);
        if (profile.getData() == null) return;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> profile.getData().save(player));
    }

}
