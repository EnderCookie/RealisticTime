package de.endercookie.realistictime;

import java.time.OffsetTime;
import java.time.ZoneOffset;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author EnderCookie
 */
public class RealisticTime extends JavaPlugin {

    @Getter
    private static RealisticTime instance;

    @Getter
    private double latitude;

    @Getter
    private double longitude;

    @Getter
    private ZoneOffset localOffset;

    @Override
    public void onEnable() {
        super.onEnable();

        this.saveDefaultConfig();

        instance = this;

        Bukkit.getWorlds().forEach(w -> {
            w.setGameRuleValue("doDaylightCircle", "false");
        });

        this.latitude = this.getConfig().getDouble("latitude", 0D);
        this.longitude = this.getConfig().getDouble("longitude", 0D);
        this.localOffset = OffsetTime.now().getOffset();

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
