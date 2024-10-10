package depth.mvp.thinkerbell.domain.notice.repository;

import depth.mvp.thinkerbell.domain.notice.entity.AcademicNotice;
import depth.mvp.thinkerbell.domain.notice.entity.ScholarshipNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScholarshipNoticeRepository extends JpaRepository<ScholarshipNotice, Long> {
    @Query("SELECT n FROM ScholarshipNotice n WHERE n.title LIKE CONCAT('%', :keyword, '%')")
    List<ScholarshipNotice> searchByTitle(@Param("keyword") String keyword);
    ScholarshipNotice findOneById(Long noticeID);
    Page<ScholarshipNotice> findAllByOrderByPubDateDesc(Pageable pageable);
    List<ScholarshipNotice> findTop3ByOrderByPubDateDesc();

}
