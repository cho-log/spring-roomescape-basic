package roomescape;

public class ReservationRequest {
    private final String name;
    private final String date;
    private final Long scheduleId;

    public ReservationRequest(String name, String date, Long scheduleId) {
        this.name = name;
        this.date = date;
        this.scheduleId = scheduleId;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}

