package net.bghd.hypixel.core.managers;

import com.google.common.collect.Lists;
import net.bghd.hypixel.core.Core;
import net.bghd.hypixel.core.Manager;
import net.bghd.hypixel.core.util.Color;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class ChatManager extends Manager implements Listener {

    public ChatManager(Core plugin) {
        super(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Color.log("ChatManager has been loaded.");
    }

    private final List<String> BLOCKED_COMMANDS = Lists.newArrayList("pl", "plugins", "ver", "version", "icanhasbukkit", "about", "op");


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String commandName = event.getMessage().substring(1);
        String argString = event.getMessage().substring(event.getMessage().indexOf(' ') + 1);
        String[] args = new String[]{};
        Profile profile = plugin.getProfileManager().getProfile(event.getPlayer());
        String lowerCase = event.getMessage().toLowerCase();
        if (lowerCase.startsWith("/me") || lowerCase.startsWith("/bukkit") || lowerCase.startsWith("/minecraft")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Color.main("Chat", "&cYou cannot use that command!"));
        }
        if (BLOCKED_COMMANDS.contains(commandName.toLowerCase()) && !profile.getData().getRank().isHigherOrEqualsTo(event.getPlayer(), Rank.MOD, false)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Color.main("Chat", "&cYou cannot use that command!"));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void handleChat(AsyncPlayerChatEvent event) {
        Profile profile = plugin.getProfileManager().getProfile(event.getPlayer());
        Player sender = event.getPlayer();
        String message = event.getMessage();

        if (event.isCancelled() || message.isEmpty()) {
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        message = ChatColor.translateAlternateColorCodes('&', message);
        final TextComponent formatted = new TextComponent("");
        String rank = profile.getData().getRank().getPrefix();
        TextComponent rankComponent = new TextComponent(rank + " ");
        TextComponent playerNameText = new TextComponent(Color.translate(profile.getData().getRank().getColor() + event.getPlayer().getDisplayName()));
        TextComponent messageContent = new TextComponent(" " + message);
        formatted.addExtra(rankComponent);
        formatted.addExtra(playerNameText);
        formatted.addExtra(messageContent);
        event.getRecipients().forEach(player -> player.spigot().sendMessage(formatted));
    }
}
