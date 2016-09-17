package de.endercookie.realistictime;

import de.endercookie.realistictime.thread.TimeThread;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
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

    @Getter
    private Collection<World> worlds;

    @Override
    public void onEnable() {
        super.onEnable();

        this.saveDefaultConfig();

        instance = this;

        this.latitude = this.getConfig().getDouble("latitude", 0D);
        this.longitude = this.getConfig().getDouble("longitude", 0D);

        if (this.getConfig().getBoolean("all_worlds", true)) {
            this.worlds = Bukkit.getWorlds();
        } else {
            Collection<String> c = this.getConfig().getStringList("worlds");
            this.worlds = Bukkit.getWorlds().stream()
                    .filter(w -> c.contains(w.getName()))
                    .collect(Collectors.toSet());
        }

        this.worlds.forEach(w -> {
            w.setGameRuleValue("doDaylightCycle", "false");
        });

        this.thread = new TimeThread();
        this.thread.runTaskTimer(this, 0, this.getConfig().getInt("update_rate", 60) * 20);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
