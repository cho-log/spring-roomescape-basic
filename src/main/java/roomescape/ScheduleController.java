package roomescape;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private JdbcTemplate jdbcTemplate;

    private RowMapper<Schedule> rowMapper = (rs, rowNum) -> new Schedule(
            rs.getLong("id"),
            rs.getString("time")
    );

    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity<List<Schedule>> findAll() {
        String sql = "SELECT * FROM schedule";
        List<Schedule> schedules = jdbcTemplate.query(sql, rowMapper);
        return ResponseEntity.ok(schedules);
    }

    @PostMapping
    public ResponseEntity<Schedule> create(@RequestBody ScheduleRequest request) {
        String sql = "INSERT INTO schedule (time) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, request.getTime());
            return preparedStatement;
        }, keyHolder);

        Schedule schedule = new Schedule(keyHolder.getKey().longValue(), request.getTime());
        return ResponseEntity.created(java.net.URI.create("/schedules/" + schedule.getId())).body(schedule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        String sql = "DELETE FROM schedule WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return ResponseEntity.noContent().build();
    }
}
