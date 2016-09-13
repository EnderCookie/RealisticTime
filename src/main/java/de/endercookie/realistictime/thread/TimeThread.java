package de.endercookie.realistictime.thread;

import de.endercookie.realistictime.RealisticTime;
import de.endercookie.realistictime.math.SunMath;
import java.time.Duration;
import java.time.OffsetDateTime;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author EnderCookie
 */
public class TimeThread extends BukkitRunnable {

    private OffsetDateTime last;

    private OffsetDateTime next;

    private boolean night;

    private double factor;

    private short ticks;

    private static final int NIGHT_LENGTH = 10935;
    private static final int DAY_LENGTH = 13065;

    public TimeThread() {
        //Is night?
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime set = SunMath.getSunset(RealisticTime.getInstance().getLatitude(), RealisticTime.getInstance().getLongitude(), now.getDayOfYear()).atDate(now.toLocalDate());
        if (now.isAfter(set)) {
            this.last = set;
            this.next = SunMath.getSunrise(RealisticTime.getInstance().getLatitude(), RealisticTime.getInstance().getLongitude(), now.getDayOfYear() + 1).atDate(now.toLocalDate());
            this.night = true;
        } else {
            OffsetDateTime rise = SunMath.getSunrise(RealisticTime.getInstance().getLatitude(), RealisticTime.getInstance().getLongitude(), now.getDayOfYear()).atDate(now.toLocalDate());
            if (now.isBefore(rise)) {
                this.last = SunMath.getSunrise(RealisticTime.getInstance().getLatitude(), RealisticTime.getInstance().getLongitude(), now.getDayOfYear() - 1).atDate(now.toLocalDate());
                this.next = rise;
                this.night = true;
            } else {
                this.last = rise;
                this.next = set;
                this.night = false;
            }
        }

        calcFactor();
    }

    @Override
    public void run() {
        OffsetDateTime now = OffsetDateTime.now();
        if (now.isAfter(next)) {
            this.last = this.next;
            this.night = !this.night;
            //        = night ? SunMath.getSunrise(.., .., now.getDayOfYear + 1) : SunMath.getSunset(.., .., now.getDayOfYear);
            this.next = night ? SunMath.getSunrise(RealisticTime.getInstance().getLatitude(), RealisticTime.getInstance().getLongitude(), now.getDayOfYear() + 1).atDate(now.toLocalDate()) : SunMath.getSunset(RealisticTime.getInstance().getLatitude(), RealisticTime.getInstance().getLongitude(), now.getDayOfYear()).atDate(now.toLocalDate());
        }

        ticks = (short) (Duration.between(last, now).toMinutes() * factor);
        ticks = (short) (night ? ticks + 11615 : ticks - 1450);
        if (ticks < 0) {
            ticks += 24000; // ticks = -1450 -> ticks = -1450 + 24000
        } else if (ticks > 23999) {
            ticks -= 24000; //ticks = 24500 -> ticks = 24500 - 24000
        }
        Bukkit.getWorlds().forEach(w -> w.setTime(ticks));
    }

    private void calcFactor() {
        factor = Duration.between(last, next).toMinutes() / (this.night ? TimeThread.NIGHT_LENGTH : TimeThread.DAY_LENGTH);
    }
}
