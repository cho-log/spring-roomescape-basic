package roomescape;

public class ReservationException extends RuntimeException {
    private ReservationException(String message) {
        super(message);
    }

    public static ReservationException illegalRequest(String name, String date, String time) {
        return new ReservationException("Reservation name, date, time can not be empty: " + name + date + time);
    }

    public static ReservationException notFound(Long id) {
        return new ReservationException.NotFound(id);
    }

    public static class NotFound extends ReservationException {
        private NotFound(Long id) {
            super("Reservation not found: " + id);
        }
    }
}
