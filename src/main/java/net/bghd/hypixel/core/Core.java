package net.bghd.hypixel.core;

import lombok.Getter;
import net.bghd.hypixel.core.managers.MySQLManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Core extends JavaPlugin {

    @Getter
    public static Core instance;
    private MySQLManager mySQLManager;


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        loadManagers();
        getMySQLManager().connect();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getMySQLManager().shutdown();
    }

    public void loadManagers() {
        mySQLManager = new MySQLManager(this);

    }

}
