package roomescape;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ReservationController {
    private List<Reservation> reservations = new ArrayList<>();

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> reservations() {

        reservations.add(new Reservation(1L, "김브리", "2021-07-01", "10:00"));
        reservations.add(new Reservation(2L, "조브리", "2021-07-01", "11:00"));
        reservations.add(new Reservation(3L, "제갈브리", "2021-07-01", "12:00"));

        return ResponseEntity.ok(reservations);
    }
}
