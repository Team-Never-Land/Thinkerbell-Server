package depth.mvp.thinkerbell.domain.user.repository;

import depth.mvp.thinkerbell.domain.user.entity.Alarm;
import depth.mvp.thinkerbell.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    boolean existsByUserIdAndKeywordAndIsViewedFalse(Long userId, String keyword);

    boolean existsByUserIdAndIsViewedFalse(Long userId);

    List<Alarm> findALLByUserIdAndKeyword (Long userId, String keyword);

    Alarm findByKeywordAndUserAndNoticeID(String keyword, User user, Long noticeID);

}
