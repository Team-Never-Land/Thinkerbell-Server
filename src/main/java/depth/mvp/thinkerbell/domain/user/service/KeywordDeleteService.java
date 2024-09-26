package depth.mvp.thinkerbell.domain.user.service;

import depth.mvp.thinkerbell.domain.user.entity.User;
import depth.mvp.thinkerbell.domain.user.repository.KeywordRepository;
import depth.mvp.thinkerbell.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class KeywordDeleteService {

    private final KeywordRepository keywordRepository;
    private final AlarmService alarmService;
    private final UserRepository userRepository;

    public Boolean isDeleted(String keyword, String userSSAID) {

        Optional<User> user = userRepository.findBySsaid(userSSAID);
        User userEntity = user.get();

        keywordRepository.deleteByKeywordAndUserId(keyword, userEntity.getId());
        alarmService.deleteAlarm(keyword, userSSAID);

        if (keywordRepository.existsByKeywordAndUserId(keyword, userEntity.getId())) {
            return false;
        } else {
            return true;
        }
    }
}
