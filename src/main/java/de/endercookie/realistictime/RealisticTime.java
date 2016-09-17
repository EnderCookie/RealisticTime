package de.endercookie.realistictime;

import de.endercookie.realistictime.thread.TimeThread;
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

    private TimeThread thread;

    @Override
    public void onEnable() {
        super.onEnable();

        this.saveDefaultConfig();

        instance = this;

        Bukkit.getWorlds().forEach(w -> {
            w.setGameRuleValue("doDaylightCycle", "false");
        });

        this.latitude = this.getConfig().getDouble("latitude", 0D);
        this.longitude = this.getConfig().getDouble("longitude", 0D);
        this.thread = new TimeThread();
        this.thread.runTaskTimer(this, 0, 1200);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
