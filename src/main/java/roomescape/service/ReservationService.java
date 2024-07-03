package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.Reservation;
import roomescape.ReservationException;
import roomescape.ReservationRequest;
import roomescape.Schedule;
import roomescape.dao.ReservationDao;
import roomescape.dao.ScheduleDao;
import roomescape.exception.ScheduleException;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ScheduleDao scheduleDao;

    public ReservationService(ReservationDao reservationDao, ScheduleDao scheduleDao) {
        this.reservationDao = reservationDao;
        this.scheduleDao = scheduleDao;
    }

    public List<Reservation> getReservations() {
        return reservationDao.findAll();
    }

    public Reservation reserve(ReservationRequest request) {
        Schedule schedule = scheduleDao.findById(request.getScheduleId())
                .orElseThrow(() -> ScheduleException.notFound(request.getScheduleId()));

        Reservation reservation = new Reservation(
                request.getName(),
                request.getDate(),
                schedule
        );

        long createdId = reservationDao.save(reservation);

        return reservationDao.findById(createdId)
                .orElseThrow(() -> ReservationException.notFound(createdId));
    }

    public void delete(Long id) {
        if (reservationDao.findById(id).isEmpty()) {
            throw ReservationException.notFound(id);
        }
        reservationDao.delete(id);
    }
}
