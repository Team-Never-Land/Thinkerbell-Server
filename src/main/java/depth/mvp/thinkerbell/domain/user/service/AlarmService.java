package depth.mvp.thinkerbell.domain.user.service;

import depth.mvp.thinkerbell.domain.common.service.CategoryService;
import depth.mvp.thinkerbell.domain.notice.entity.AcademicSchedule;
import depth.mvp.thinkerbell.domain.notice.entity.AllNoticesView;
import depth.mvp.thinkerbell.domain.notice.entity.CrawlingNum;
import depth.mvp.thinkerbell.domain.notice.repository.AcademicScheduleRepository;
import depth.mvp.thinkerbell.domain.notice.repository.AllNoticeViewRepository;
import depth.mvp.thinkerbell.domain.notice.repository.CrawlingNumRepository;
import depth.mvp.thinkerbell.domain.notice.service.ScheduleParser;
import depth.mvp.thinkerbell.domain.user.dto.AlarmDto;
import depth.mvp.thinkerbell.domain.user.entity.Alarm;
import depth.mvp.thinkerbell.domain.user.entity.Bookmark;
import depth.mvp.thinkerbell.domain.user.entity.Keyword;
import depth.mvp.thinkerbell.domain.user.entity.User;
import depth.mvp.thinkerbell.domain.user.repository.AlarmRepository;
import depth.mvp.thinkerbell.domain.user.repository.BookmarkRepository;
import depth.mvp.thinkerbell.domain.user.repository.KeywordRepository;
import depth.mvp.thinkerbell.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {

    @PersistenceContext
    private EntityManager entityManager;

    private final AllNoticeViewRepository allNoticeViewRepository;
    private final AcademicScheduleRepository academicScheduleRepository;
    private final KeywordRepository keywordRepository;
    private final CrawlingNumRepository crawlingNumRepository;
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    private final FCMService fcmService;
    private final CategoryService categoryService;
    private final BookmarkRepository bookmarkRepository;

    //전체 공지사항이 있는 뷰에서 키워드에 일치하는 공지를 찾아서 알람 테이블에 저장하는 기능
    //이때 최신으로 업데이트된 공지사항만 탐색한다.
    //알람 테이블에 저장되는 것들은 바로 fcm 알림까지 전송된다.
    @Scheduled(cron = "0 0 19 * * ?", zone = "Asia/Seoul")
    @Async
    public void updateNoticeAndMatchKeyword(){
        List<CrawlingNum> crawlingNums;

        try {
            crawlingNums = crawlingNumRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("크롤링 번호 레코드를 가져오는 동안 오류가 발생했습니다.", e);
        }

        for (CrawlingNum crawlingNum : crawlingNums) {
            List<AllNoticesView> allNoticesViews;

            try {
                allNoticesViews = allNoticeViewRepository.findNewNoticesByCategory(crawlingNum.getNoticeType(), crawlingNum.getNoticeID());

            } catch (Exception e) {
                throw new RuntimeException(crawlingNum.getNoticeType() + "의 새로운 공지사항을 가져오는 중 오류가 발생했습니다.", e);
            }

            for (Keyword keyword : keywordRepository.findAll()) {
                for (AllNoticesView notice : allNoticesViews) {
                    String titleWithoutSpace = notice.getTitle().replace(" ", "");

                    if (titleWithoutSpace.contains(keyword.getKeyword())) {
                        try{
                            String noticeType = categoryService.convertSnakeToPascal(notice.getTableName());

                            Alarm alarm = new Alarm(notice.getId(), noticeType, keyword.getUser(), notice.getTitle(), keyword.getKeyword());

                            alarmRepository.save(alarm);

                            if (alarm.getUser().getAlarmEnabled()) {
                                fcmService.sendFCMMessage(alarm, keyword.getKeyword());
                            }

                        } catch (Exception e) {
                            throw new RuntimeException("유저 알림을 저장하거나, fcm 알림을 보내는 도중 오류가 발생했습니다.", e);
                        }
                    }
                }
            }

            Long newMaxID = allNoticeViewRepository.findMaxIdByCategory(crawlingNum.getNoticeType());

            updateCrawlingNum(crawlingNum, newMaxID);
        }
    }

    //매일 오전 8시에 학사일정 알림 전송
    @Scheduled(cron = "0 0 8 * * ?", zone = "Asia/Seoul")
    public void sendAlarmForBookmarkSchedule(){
        List<User> userList = userRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for (User user : userList) {
            List<Bookmark> bookmarkList = bookmarkRepository.findByUserAndCategoryOrderByCreatedAtDesc(user, "AcademicSchedule");;

            if (bookmarkList != null) {
                for (Bookmark bookmark : bookmarkList) {
                    AcademicSchedule academicSchedule = academicScheduleRepository.findOneById(bookmark.getNoticeID());

                    if (academicSchedule != null) {
                        if (currentDate.isEqual(ScheduleParser.parseDate(academicSchedule.getSchedule())[0])){
                            fcmService.sendScheduleMessage(user, academicSchedule.getTitle());
                        }
                    }
                }
            }
        }
    }



    public synchronized void updateCrawlingNum(CrawlingNum crawlingNum, Long newMaxID) {
        Optional<CrawlingNum> existingCrawlingNumOpt = crawlingNumRepository.findByNoticeType(crawlingNum.getNoticeType());
        if (existingCrawlingNumOpt.isPresent()) {
            CrawlingNum existingCrawlingNum = existingCrawlingNumOpt.get();
            existingCrawlingNum.setNoticeID(newMaxID);
            crawlingNumRepository.save(existingCrawlingNum);
        } else {
            CrawlingNum newCrawlingNum = CrawlingNum.builder()
                    .noticeID(newMaxID)
                    .noticeType(crawlingNum.getNoticeType())
                    .build();
            crawlingNumRepository.save(newCrawlingNum);
        }
    }

    public void updateNoticeAndMatchKeywordTest(String SSAID, String keyword){
        String keywordWithoutSpace = keyword.replace(" ", "");

        List<AllNoticesView> allNoticesViews = allNoticeViewRepository.findByTitleContainingKeyword(keywordWithoutSpace);

        User user = userRepository.findBySsaid(SSAID)
                .orElseThrow(() -> new RuntimeException("User not found"));

        for (AllNoticesView notice : allNoticesViews) {
            Alarm alarm = new Alarm(notice.getId(), notice.getTableName(), user, notice.getTitle(), keyword);

            if (alarm.getUser().getAlarmEnabled()) {
                fcmService.sendFCMMessage(alarm, keyword);
            }
        }
    }

    //보지 않은 알림이 있으면 true, 없으면 false, 키워드 별로
    public boolean hasUnviewedAlarm(String SSAID, String keyword){
        Optional<User> userOpt = userRepository.findBySsaid(SSAID);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            return alarmRepository.existsByUserIdAndKeywordAndIsViewedFalse(user.getId(), keyword);
        }
        return false;
    }

    public boolean hasUnviewedAllAlarm(String SSAID){
        Optional<User> userOpt = userRepository.findBySsaid(SSAID);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            return alarmRepository.existsByUserIdAndIsViewedFalse(user.getId());
        }
        return false;
    }

    //알림 여부 변경
    public void toggleUserAlarm(String SSAID){
        User user = userRepository.findBySsaid(SSAID)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.toggleAlarmEnabled();

        userRepository.save(user);
    }

    //알림 여부 조회
    public boolean getUserAlarm(String SSAID){
        User user = userRepository.findBySsaid(SSAID)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getAlarmEnabled();
    }

    //안본거 본걸로 바꾸기
    public void markAsViewed(Long alarmID){
        Alarm alarm = alarmRepository.findById(alarmID)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 알림이 없습니다."));
        alarm.setIsViewed(true);
    }

        //알림 키워드, 사용자 기반 조회
    public List<AlarmDto> getAlarms(String SSAID, String keyword){
        Optional<User> userOpt = userRepository.findBySsaid(SSAID);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            List<Alarm> alarms = alarmRepository.findALLByUserIdAndKeywordOrderById(user.getId(), keyword);

            List<AlarmDto> alarmDtos = new ArrayList<>();

            for (Alarm alarm : alarms) {
                String noticeType = alarm.getNoticeType();

                List<Bookmark> bookmark = bookmarkRepository.findByCategoryAndUserAndNoticeID(noticeType, user, alarm.getNoticeID());

                boolean isMarked = !bookmark.isEmpty();

                Map<String, Object> noticeDetails = getNoticeDetails(categoryService.convertPascalToSnake(noticeType), alarm.getNoticeID());
                String url = (String) noticeDetails.get("url");
                String pubDate = (String) noticeDetails.get("pubDate");

                AlarmDto alarmDto = AlarmDto.builder()
                        .id(alarm.getId())
                        .title(alarm.getTitle())
                        .noticeTypeKorean(categoryService.convertEnglishToKorea(alarm.getNoticeType()))
                        .noticeTypeEnglish(categoryService.convertSnakeToPascal(alarm.getNoticeType()))  // PascalCase로 변환
                        .isViewed(alarm.getIsViewed())
                        .isMarked(isMarked)
                        .Url(url)
                        .pubDate(pubDate)
                        .build();

                alarmDtos.add(alarmDto);
            }

            return alarmDtos;
        } else {
            return Collections.emptyList();
        }
    }

    public void deleteAlarm(String keyword, String SSAID){
        User user = userRepository.findBySsaid(SSAID)
                .orElseThrow(() -> new IllegalArgumentException("주어진 ID로 사용자를 찾을 수 없습니다."));

        List<Alarm> alarms = alarmRepository.findALLByUserIdAndKeyword(user.getId(), keyword);

        for (Alarm alarm : alarms) {
            if (alarm == null){
                new NotFoundException("알림을 찾을 수 없습니다.");
            }

            alarmRepository.deleteById(alarm.getId());
        }
    }

    private Map<String, Object> getNoticeDetails(String tableName, Long noticeID) {
        String sql = "SELECT url, DATE_FORMAT(pub_date, '%Y-%m-%d') as pubDate FROM " + tableName + " WHERE id = :noticeID";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("noticeID", noticeID);

        List<Object[]> results = query.getResultList();

        if (!results.isEmpty()) {
            Object[] row = results.get(0);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("url", row[0]);
            resultMap.put("pubDate", row[1]);
            return resultMap;
        } else {
            return Collections.emptyMap();
        }
    }
}
