package net.bghd.hypixel.core.managers;

import lombok.Getter;
import net.bghd.hypixel.core.Core;

@Getter
public class Profile {


    private Core plugin = Core.getInstance();
    private java.util.UUID uuid;
    private String playerName;
    private PlayerData data;

    public Profile(java.util.UUID uuid, String name) {
        this.uuid = uuid;
        this.playerName = name;
        this.data = new PlayerData(uuid, name);
    }

}
