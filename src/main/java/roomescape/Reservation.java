package roomescape;

public class Reservation {
    private Long id;
    private String name;
    private String date;
    private Schedule schedule;

    public Reservation(Long id, String name, String date, Schedule schedule) {
        if (name.isEmpty() || date.isEmpty()) {
            throw ReservationException.illegalRequest(name, date);
        }
        this.id = id;
        this.name = name;
        this.date = date;
        this.schedule = schedule;
    }

    public Reservation(String name, String date, Schedule schedule) {
        this.name = name;
        this.date = date;
        this.schedule = schedule;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
