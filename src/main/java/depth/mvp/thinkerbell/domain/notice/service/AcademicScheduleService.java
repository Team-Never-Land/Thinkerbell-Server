package depth.mvp.thinkerbell.domain.notice.service;

import depth.mvp.thinkerbell.domain.notice.dto.AcademicScheduleDto;
import depth.mvp.thinkerbell.domain.notice.entity.AcademicSchedule;
import depth.mvp.thinkerbell.domain.notice.repository.AcademicScheduleRepository;
import depth.mvp.thinkerbell.domain.user.service.BookmarkService;
import depth.mvp.thinkerbell.global.exception.ErrorCode;
import depth.mvp.thinkerbell.global.exception.MapperException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AcademicScheduleService {
    private final BookmarkService bookmarkService;
    private final AcademicScheduleRepository academicScheduleRepository;

    public List<AcademicScheduleDto> getMonthlySchedule(int month, int year, String ssaid) {
        // USER가 북마크한 내역(id리스트) 가져오기
        List<Long> bookmarkedNoticeIds = bookmarkService.getBookmark(ssaid,
                this.getClass().getSimpleName().replace("Service", ""));

        LocalDate currentDate = LocalDate.now();

        if (currentDate.getYear() - 1 > year || currentDate.getYear() + 1 < year) {
            throw new IllegalArgumentException("현재 년도의 +-1의 년도 사이만 입력가능합니다.");
        }

        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("1월 부터 12월 사이의 값을 입력해주세요");
        }

        try {
            List<AcademicSchedule> schedules = academicScheduleRepository.findAll();
            if (schedules.isEmpty()) {
                throw new MapperException(ErrorCode.INVALID_INPUT_VALUE);
            }

            return schedules.stream()
                    .map(schedule -> {
                        // get 'startDate' and 'endDate' from 'schedule'
                        String date = schedule.getSchedule();
                        LocalDate[] localDate = ScheduleParser.parseDate(date);

                        boolean isMarked = bookmarkedNoticeIds.contains(schedule.getId());

                        return AcademicScheduleDto.builder()
                                .id(schedule.getId())
                                .title(schedule.getTitle())
                                .startDate(localDate[0])
                                .endDate(localDate[1])
                                .marked(isMarked)
                                .build();
                    })
                    .filter(academicScheduleDto -> {
                        LocalDate localDate = academicScheduleDto.getStartDate();
                        return localDate != null && localDate.getMonthValue() == month && localDate.getYear() == year;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
