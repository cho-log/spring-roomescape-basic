package roomescape;

public class Schedule {
    private Long id;
    private String time;

    public Schedule(String time) {
        this(null, time);
    }

    public Schedule(Long id, String time) {
        this.id = id;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }
}
