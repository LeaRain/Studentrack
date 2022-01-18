package sw.laux.Studentrack.application.DTO;

import java.util.Date;

public class StudentrackAppointmentDTO {
    private boolean singleAppointment;
    private Date start;
    private long duration;

    public StudentrackAppointmentDTO() {

    }

    public StudentrackAppointmentDTO(boolean singleAppointment, Date start, long duration) {
        this.singleAppointment = singleAppointment;
        this.start = start;
        this.duration = duration;
    }

    public boolean isSingleAppointment() {
        return singleAppointment;
    }

    public void setSingleAppointment(boolean singleAppointment) {
        this.singleAppointment = singleAppointment;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
