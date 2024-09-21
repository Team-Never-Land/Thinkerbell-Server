package depth.mvp.thinkerbell.domain.version.service;

import depth.mvp.thinkerbell.domain.version.dto.VersionDto;
import depth.mvp.thinkerbell.domain.version.entity.Version;
import depth.mvp.thinkerbell.domain.version.repository.VersionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class VersionService {

    private final VersionRepository versionRepository;

    public VersionDto getNecessaryVersion() {
        Version version = versionRepository.findOne();
        try {
            return VersionDto.builder()
                    .versionCode(version.getVersionCode())
                    .versionName(version.getVersionName())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
