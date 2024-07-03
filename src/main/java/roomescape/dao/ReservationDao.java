package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.Reservation;
import roomescape.Schedule;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> rowMapper = (rs, rowNum) -> new Reservation(
            rs.getLong("reservation_id"),
            rs.getString("reservation_name"),
            rs.getString("reservation_date"),
            new Schedule(rs.getLong("schedule_id"), rs.getString("schedule_time"))
    );

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        String sql = "SELECT \n" +
                "    r.id as reservation_id, \n" +
                "    r.name as reservation_name, \n" +
                "    r.date as reservation_date, \n" +
                "    s.id as schedule_id, \n" +
                "    s.time as schedule_time \n" +
                "FROM reservation as r \n" +
                "INNER JOIN schedule as s ON r.schedule_id = s.id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT \n" +
                "    r.id as reservation_id, \n" +
                "    r.name as reservation_name, \n" +
                "    r.date as reservation_date, \n" +
                "    s.id as schedule_id, \n" +
                "    s.time as schedule_time \n" +
                "FROM reservation as r \n" +
                "INNER JOIN schedule as s ON r.schedule_id = s.id \n" +
                "WHERE r.id = ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, rowMapper, id);
        return reservations.stream().findFirst();
    }

    public long save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO reservation (name, date, schedule_id) VALUES (?, ?, ?)", new String[]{"id"});
            preparedStatement.setString(1, reservation.getName());
            preparedStatement.setString(2, reservation.getDate());
            preparedStatement.setLong(3, reservation.getSchedule().getId());
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void delete(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
