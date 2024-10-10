package depth.mvp.thinkerbell.domain.notice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import depth.mvp.thinkerbell.domain.notice.entity.StudentActsNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentActsNoticeRepository extends JpaRepository<StudentActsNotice, Long> {
    @Query("SELECT n FROM StudentActsNotice n WHERE n.title LIKE CONCAT('%', :keyword, '%')")
    List<StudentActsNotice> searchByTitle(@Param("keyword") String keyword);
    Page<StudentActsNotice> findAllByOrderByPubDateDesc(Pageable pageable);
    StudentActsNotice findOneById(Long noticeID);
}
