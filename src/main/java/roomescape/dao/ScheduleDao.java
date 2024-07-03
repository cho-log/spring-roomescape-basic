package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.Schedule;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class ScheduleDao {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Schedule> rowMapper = (rs, rowNum) -> new Schedule(
            rs.getLong("id"),
            rs.getString("time")
    );

    public ScheduleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Schedule> findAll() {
        String sql = "SELECT * FROM schedule";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Schedule> findById(Long id) {
        String sql = "SELECT * FROM schedule WHERE id = ?";
        List<Schedule> schedules = jdbcTemplate.query(sql, rowMapper, id);
        return schedules.stream().findFirst();
    }

    public long save(Schedule schedule) {
        String sql = "INSERT INTO schedule (time) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, schedule.getTime());
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void delete(Long id) {
        String sql = "DELETE FROM schedule WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
