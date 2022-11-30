package net.bghd.hypixel.core;

import lombok.Getter;
import net.bghd.hypixel.core.managers.MySQLManager;
import net.bghd.hypixel.core.managers.PlayerData;
import net.bghd.hypixel.core.managers.ProfileManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Core extends JavaPlugin {

    @Getter
    public static Core instance;
    private MySQLManager mySQLManager;
    private ProfileManager profileManager;
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        loadManagers();
        getMySQLManager().connect();
        getServer().getPluginManager().registerEvents(new PlayerData(), this);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getMySQLManager().shutdown();
    }

    public void loadManagers() {
        mySQLManager = new MySQLManager(this);
        profileManager = new ProfileManager(this);

    }


}
