package net.bghd.hypixel.core.managers;

import lombok.Getter;
import net.bghd.hypixel.core.util.Color;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Getter
public enum Rank {

    OWNER("Owner", ChatColor.RED, true, false, 4),
    ADMIN("Admin", ChatColor.RED, true, false, 3),
    MOD("Mod", ChatColor.GREEN, true, false, 2),
    DEFAULT("", ChatColor.GRAY, false, false, 1);

    private String name;
    private ChatColor color;
    private boolean bold, ital;
    private int level;

    Rank(String name, ChatColor color, boolean bold, boolean ital, int level) {
        this.name = name;
        this.color = color;
        this.bold = bold;
        this.ital = ital;
        this.level = level;
    }

    public static boolean contains(String rank) {
        for (Rank ranks : Rank.values()) {
            if(ranks.name().equals(rank)) {
                return true;
            }
        }
        return false;
    }

    public boolean isHigher(Rank compare) {
        return this.level > compare.level;
    }
    public boolean isLower(Rank compare) {
        return this.level < compare.level;
    }
    public boolean isHigherOrEqualsTo(Player player, Rank rank, boolean callback) {
        if(callback && this.level != rank.level) {
            player.sendMessage(Color.main("Permissions", "&cYou need to be rank " + rank.getName() + "to use this command!"));
            return false;
        }
        if(this.level == rank.level) {
            return true;
        }
        return false;
    }
    public boolean isLowerOrEqualsTo(Rank compare) {
        return this.level <= compare.level;
    }
    public String getPrefix() {
        String name = this.name.toUpperCase();

        if (bold && ital) return Color.translate(this.color + "&o&l" + name);
        if (bold) return Color.translate(this.color + "&l" + name);
        if (ital) return Color.translate(this.color + "&o" + name);

        return Color.translate(this.color + name);
    }
}
