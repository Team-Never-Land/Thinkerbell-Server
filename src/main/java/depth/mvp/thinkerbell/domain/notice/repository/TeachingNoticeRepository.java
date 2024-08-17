package depth.mvp.thinkerbell.domain.notice.repository;

import depth.mvp.thinkerbell.domain.notice.entity.TeachingNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeachingNoticeRepository extends JpaRepository<TeachingNotice, Long> {
    Page<TeachingNotice> findAllByOrderByImportantDescPubDateDesc(Pageable pageable);
}
