package roomescape;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.sql.PreparedStatement;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> rowMapper = (rs, rowNum) -> new Reservation(
            rs.getLong("reservation_id"),
            rs.getString("reservation_name"),
            rs.getString("reservation_date"),
            new Schedule(rs.getLong("schedule_id"), rs.getString("schedule_time"))
    );

    public ReservationController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> reservations() {
        String sql = "SELECT " +
                "r.id AS reservation_id, " +
                "r.name AS reservation_name, " +
                "r.date AS reservation_date, " +
                "s.id AS schedule_id, " +
                "s.time AS schedule_time " +
                "FROM reservation r JOIN schedule s ON r.schedule_id = s.id";
        List<Reservation> reservations = jdbcTemplate.query(sql, rowMapper);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody ReservationRequest request) {
        if (request.getName().isEmpty() || request.getDate().isEmpty() || request.getScheduleId() == null) {
            throw ReservationException.illegalRequest(request.getName(), request.getDate());
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO reservation (name, date, schedule_id) VALUES (?, ?, ?)", new String[]{"id"});
            preparedStatement.setString(1, request.getName());
            preparedStatement.setString(2, request.getDate());
            preparedStatement.setLong(3, request.getScheduleId());
            return preparedStatement;
        }, keyHolder);

        Schedule schedule = jdbcTemplate.query("SELECT * FROM schedule WHERE id = ?", (rs, rowNum) -> new Schedule(
                rs.getLong("id"),
                rs.getString("time")
        ), request.getScheduleId()).stream().findFirst().orElseThrow(() -> ReservationException.notFound(request.getScheduleId()));

        Reservation reservation = new Reservation(keyHolder.getKey().longValue(), request.getName(), request.getDate(), schedule);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        String selectSql = "SELECT * FROM reservation WHERE id = ?";
        List<Reservation> reservations = jdbcTemplate.query(selectSql, rowMapper, id);
        reservations.stream().findFirst().orElseThrow(() -> ReservationException.notFound(id));

        String deleteSql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(deleteSql, id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<String> handle(ReservationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}