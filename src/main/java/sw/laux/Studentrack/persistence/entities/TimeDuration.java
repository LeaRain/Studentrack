package sw.laux.Studentrack.persistence.entities;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.concurrent.TimeUnit;

@Embeddable
public class TimeDuration {
    @Transient
    private long duration;
    @Transient
    private long hours;
    @Transient
    private long minutes;
    @Transient
    private long seconds;

    public TimeDuration() {

    }

    public long getDuration() {
        return duration;
    }

    public long getHours() {
        return hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setDuration(long duration) {
        this.duration = duration;
        calculateHoursSecondsMinutes();
    }

    public void calculateHoursSecondsMinutes() {
        this.hours = TimeUnit.MILLISECONDS.toHours(duration);
        this.minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
        this.seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;
    }

    @Override
    public String toString() {
        return hours + ":" + minutes + ":" + seconds;
    }
}
