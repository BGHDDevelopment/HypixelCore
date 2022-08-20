package net.bghd.hypixel.core.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class Color {

    public static String translate(String source) {
        return ChatColor.translateAlternateColorCodes('&', source);
    }

    public static List<String> translate(List<String> source) {
        return source.stream().map(Color::translate).collect(Collectors.toList());
    }

    public static String main(String module, String body) {
        return ChatColor.RED + "[" + module + "]" + " " + ChatColor.GRAY + Color.translate(body);
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(main("Debug", message));
    }


}
