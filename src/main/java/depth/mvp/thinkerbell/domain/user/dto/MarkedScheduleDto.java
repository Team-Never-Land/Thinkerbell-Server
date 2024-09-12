package depth.mvp.thinkerbell.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class MarkedScheduleDto {

    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
}
