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
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("date"),
            rs.getString("time")
    );

    public ReservationController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> reservations() {
        List<Reservation> reservations = jdbcTemplate.query("SELECT * FROM reservation", (rs, rowNum) -> new Reservation(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("date"),
                rs.getString("time")
        ));
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody ReservationRequest request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO reservation (name, date, time) VALUES (?, ?, ?)", new String[]{"id"});
            preparedStatement.setString(1, request.getName());
            preparedStatement.setString(2, request.getDate());
            preparedStatement.setString(3, request.getTime());
            return preparedStatement;
        }, keyHolder);

        Reservation reservation = new Reservation(keyHolder.getKey().longValue(), request.getName(), request.getDate(), request.getTime());

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        String selectSql = "SELECT * FROM reservation WHERE id = ?";
        List<Reservation> reservations = jdbcTemplate.query(selectSql, rowMapper, id);
        Reservation reservation = reservations.stream().findFirst().orElseThrow(() -> ReservationException.notFound(id));

        String deleteSql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(deleteSql, id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<String> handle(ReservationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}