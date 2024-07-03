package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.Schedule;
import roomescape.ScheduleRequest;
import roomescape.dao.ScheduleDao;
import roomescape.exception.ScheduleException;

import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleDao scheduleDao;

    public ScheduleService(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    public List<Schedule> getSchedules() {
        return scheduleDao.findAll();
    }

    public Schedule create(ScheduleRequest request) {
        Schedule schedule = new Schedule(request.getTime());
        long createdId = scheduleDao.save(schedule);
        return scheduleDao.findById(createdId)
                .orElseThrow(() -> ScheduleException.notFound(createdId));
    }

    public void delete(Long id) {
        if (scheduleDao.findById(id).isEmpty()) {
            throw ScheduleException.notFound(id);
        }

        scheduleDao.delete(id);
    }
}
