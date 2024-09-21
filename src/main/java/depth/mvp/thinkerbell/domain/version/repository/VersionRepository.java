
package depth.mvp.thinkerbell.domain.version.repository;

import depth.mvp.thinkerbell.domain.version.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VersionRepository extends JpaRepository<Version, Long> {

    @Query("select v from Version v")
    Version findOne();

}
