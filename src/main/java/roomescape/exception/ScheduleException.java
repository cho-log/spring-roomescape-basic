package roomescape.exception;

public class ScheduleException extends RuntimeException {
    private ScheduleException(String message) {
        super(message);
    }

    public static ScheduleException illegalRequest(String time) {
        return new ScheduleException("Schedule time can not be empty: " + time);
    }

    public static ScheduleException notFound(Long id) {
        return new ScheduleException.NotFound(id);
    }

    public static class NotFound extends ScheduleException {
        private NotFound(Long id) {
            super("Schedule not found: " + id);
        }
    }
}
