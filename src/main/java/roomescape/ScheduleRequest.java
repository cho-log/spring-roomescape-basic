package roomescape;

public class ScheduleRequest {
    private String time;

    public String getTime() {
        return time;
    }

    public Schedule toSchedule() {
        return new Schedule(time);
    }
}
