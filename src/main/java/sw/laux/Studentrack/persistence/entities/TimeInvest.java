package sw.laux.Studentrack.persistence.entities;

import sw.laux.Studentrack.persistence.entities.Course;
import sw.laux.Studentrack.persistence.entities.SingleIdEntity;
import sw.laux.Studentrack.persistence.entities.TimeOrder;

import javax.persistence.*;

@Entity
public class TimeInvest extends SingleIdEntity<Long> {
    @Id
    @GeneratedValue
    private long timeInvestId;
    @OneToOne(mappedBy="timeInvest")
    private ModuleResults moduleResults;
    @Embedded
    private TimeDuration timeDuration;

    public TimeInvest(ModuleResults moduleResults, TimeDuration timeDuration) {
        this.moduleResults = moduleResults;
        this.timeDuration = timeDuration;
    }

    public ModuleResults getModuleResults() {
        return moduleResults;
    }

    public void setModuleResults(ModuleResults moduleResults) {
        this.moduleResults = moduleResults;
    }

    public TimeInvest() {

    }

    @Override
    public Long getId() {
        return getTimeInvestId();
    }

    public long getTimeInvestId() {
        return timeInvestId;
    }

    public void setTimeInvestId(long timeInvestId) {
        this.timeInvestId = timeInvestId;
    }

    public TimeDuration getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(TimeDuration timeDuration) {
        this.timeDuration = timeDuration;
    }
}
