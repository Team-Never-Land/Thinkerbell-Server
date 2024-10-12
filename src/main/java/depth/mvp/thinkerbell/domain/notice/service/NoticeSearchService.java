package depth.mvp.thinkerbell.domain.notice.service;

import depth.mvp.thinkerbell.domain.notice.dto.*;
import depth.mvp.thinkerbell.domain.notice.repository.*;
import depth.mvp.thinkerbell.domain.user.entity.User;
import depth.mvp.thinkerbell.domain.user.repository.BookmarkRepository;
import depth.mvp.thinkerbell.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeSearchService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final DormitoryEntryNoticeRepository dormitoryEntryNoticeRepository;
    private final DormitoryNoticeRepository dormitoryNoticeRepository;
    private final LibraryNoticeRepository libraryNoticeRepository;
    private final TeachingNoticeRepository teachingNoticeRepository;
    private final JobTrainingNoticeRepository jobTrainingNoticeRepository;
    private final NormalNoticeRepository normalNoticeRepository;
    private final AcademicNoticeRepository academicNoticeRepository;
    private final EventNoticeRepository eventNoticeRepository;
    private final CareerNoticeRepository careerNoticeRepository;
    private final ScholarshipNoticeRepository scholarshipNoticeRepository;
    private final StudentActsNoticeRepository studentActsNoticeRepository;
    private final BiddingNoticeRepository biddingNoticeRepository;
    private final SafetyNoticeRepository safetyNoticeRepository;
    private final RevisionNoticeRepository revisionNoticeRepository;


    public Map<String, List<?>> searchNotices(String keyword, String ssaid, String noticeType) {
        // 키워드 길이 예외 처리
        if (keyword.length() < 2 || keyword.length() > 12) {
            throw new IllegalArgumentException("키워드는 2글자 이상, 12글자 이하만 가능합니다.");
        }

        // 특수기호/이모지 필터링 로직 추가
        if (!keyword.matches("^[가-힣a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("키워드에는 특수기호나 이모지를 사용할 수 없습니다.");
        }

        Map<String, List<?>> result = new HashMap<>();
        // USER가 북마크한 내역(id리스트) 가져오기
        User user = userRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다."));

        // noticeType에 따른 공지 검색
        if (noticeType.equals("AllNotice")) {
            // 전체 공지 검색
            result.putAll(searchAllNotices(keyword, user));
        } else {
            // 카테고리별 공지 검색
            switch (noticeType) {
                case "DormitoryEntryNotice":
                    result.put("DormitoryEntryNotice", searchDormitoryEntryNotices(keyword, user));
                    break;
                case "DormitoryNotice":
                    result.put("DormitoryNotice", searchDormitoryNotices(keyword, user));
                    break;
                case "LibraryNotice":
                    result.put("LibraryNotice", searchLibraryNotices(keyword, user));
                    break;
                case "AcademicNotice":
                    result.put("AcademicNotice", searchAcademicNotices(keyword, user));
                    break;
                case "JobTrainingNotice":
                    result.put("JobTrainingNotice", searchJobTrainingNotices(keyword, user));
                    break;
                case "NormalNotice":
                    result.put("NormalNotice", searchNormalNotices(keyword, user));
                    break;
                case "EventNotice":
                    result.put("EventNotice", searchEventNotices(keyword, user));
                    break;
                case "CareerNotice":
                    result.put("CareerNotice", searchCareerNotices(keyword, user));
                    break;
                case "ScholarshipNotice":
                    result.put("ScholarshipNotice", searchScholarshipNotices(keyword, user));
                    break;
                case "StudentActsNotice":
                    result.put("StudentActsNotice", searchStudentActsNotices(keyword, user));
                    break;
                case "BiddingNotice":
                    result.put("BiddingNotice", searchBiddingNotices(keyword, user));
                    break;
                case "SafetyNotice":
                    result.put("SafetyNotice", searchSafetyNotices(keyword, user));
                    break;
                case "RevisionNotice":
                    result.put("RevisionNotice", searchRevisionNotices(keyword, user));
                    break;
                case "TeachingNotice":
                    result.put("TeachingNotice", searchTeachingNotices(keyword, user));
                    break;
                default:
                    throw new IllegalArgumentException("잘못된 noticeType 값입니다.");
            }
        }
        return result;
    }


    private Map<String, List<?>> searchAllNotices(String keyword, User user) {
        Map<String, List<?>> result = new HashMap<>();

        // 각 카테고리별 검색 결과를 모두 병합
        result.put("DormitoryEntryNotice", searchDormitoryEntryNotices(keyword, user));
        result.put("DormitoryNoticeRepository", searchDormitoryNotices(keyword, user));
        result.put("LibraryNotice", searchLibraryNotices(keyword, user));
        result.put("TeachingNotice", searchTeachingNotices(keyword, user));
        result.put("AcademicNotice", searchAcademicNotices(keyword, user));
        result.put("JobTrainingNotice", searchJobTrainingNotices(keyword, user));
        result.put("NormalNotice", searchNormalNotices(keyword, user));
        result.put("EventNotice", searchEventNotices(keyword, user));
        result.put("CareerNotice", searchCareerNotices(keyword, user));
        result.put("ScholarshipNotice", searchScholarshipNotices(keyword, user));
        result.put("StudentActsNotice", searchStudentActsNotices(keyword, user));
        result.put("BiddingNotice", searchBiddingNotices(keyword, user));
        result.put("SafetyNotice", searchSafetyNotices(keyword, user));
        result.put("RevisionNotice", searchRevisionNotices(keyword, user));

        return result;
    }

    private List<DormitoryEntryNoticeDTO> searchDormitoryEntryNotices(String keyword, User user) {
        return dormitoryEntryNoticeRepository.searchByTitle(keyword)
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "DormitoryEntryNotice", notice.getId(), user);
                    return DormitoryEntryNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .important(notice.isImportant())
                            .campus(notice.getCampus())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<DormitoryNoticeDTO> searchDormitoryNotices(String keyword, User user) {
        return dormitoryNoticeRepository.searchByTitle(keyword)
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "DormitoryNotice", notice.getId(), user);
                    return DormitoryNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .important(notice.isImportant())
                            .campus(notice.getCampus())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<LibraryNoticeDTO> searchLibraryNotices(String keyword, User user) {
        return libraryNoticeRepository.searchByTitle(keyword)
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "LibraryNotice", notice.getId(), user);
                    return LibraryNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .important(notice.isImportant())
                            .campus(notice.getCampus())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<TeachingNoticeDTO> searchTeachingNotices(String keyword, User user) {
        return teachingNoticeRepository.searchByTitle(keyword)
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "TeachingNotice", notice.getId(), user);
                    return TeachingNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .important(notice.isImportant())
                            .build();
                })
                .collect(Collectors.toList());
    }


    private List<AcademicNoticeDTO> searchAcademicNotices(String keyword, User user) {
        return academicNoticeRepository.searchByTitle(keyword)
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "AcademicNotice", notice.getId(), user);
                    return AcademicNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .important(notice.isImportant())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<JobTrainingNoticeDTO> searchJobTrainingNotices(String keyword, User user) {
        return jobTrainingNoticeRepository.searchByTitleOrMajor(keyword)
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "JobTrainingNotice", notice.getId(), user);
                    return JobTrainingNoticeDTO.builder()
                            .id(notice.getId())
                            .company(notice.getCompany())
                            .year(notice.getYear())
                            .semester(notice.getSemester())
                            .period(notice.getPeriod())
                            .major(notice.getMajor())
                            .recrutingNum(notice.getRecrutingNum())
                            .deadline(notice.getDeadline())
                            .jobName(notice.getJobName())
                            .marked(isMarked)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<NormalNoticeDTO> searchNormalNotices(String keyword, User user) {
        return normalNoticeRepository.searchByTitle(keyword)
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "NormalNotice", notice.getId(), user);
                    return NormalNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .important(notice.isImportant())
                            .build();
                }).collect(Collectors.toList());
    }

    private List<EventNoticeDTO> searchEventNotices(String keyword, User user) {
        return eventNoticeRepository.searchByTitle(keyword)
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "EventNotice", notice.getId(), user);
                    return EventNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .build();
                }).collect(Collectors.toList());
    }

    private List<CareerNoticeDTO> searchCareerNotices(String keyword, User user) {
        return careerNoticeRepository.searchByTitle(keyword)
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "CareerNotice", notice.getId(), user);
                    return CareerNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .build();
                }).collect(Collectors.toList());
    }

    private List<ScholarshipNoticeDTO> searchScholarshipNotices(String keyword, User user) {
        return scholarshipNoticeRepository.searchByTitle(keyword)
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "ScholarshipNotices", notice.getId(), user);
                    return ScholarshipNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .build();
                }).collect(Collectors.toList());
    }

    private List<StudentActsNoticeDTO> searchStudentActsNotices(String keyword, User user) {
        return studentActsNoticeRepository.searchByTitle(keyword)
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "StudentActsNotice", notice.getId(), user);
                    return StudentActsNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .build();
                }).collect(Collectors.toList());
    }

    private List<BiddingNoticeDTO> searchBiddingNotices(String keyword, User user) {
        return biddingNoticeRepository.searchByTitle(keyword)
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "BiddingNotice", notice.getId(), user);
                    return BiddingNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .build();
                }).collect(Collectors.toList());
    }

    private List<SafetyNoticeDTO> searchSafetyNotices(String keyword, User user) {
        return safetyNoticeRepository.searchByTitle(keyword)
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "SafetyNotice", notice.getId(), user);
                    return SafetyNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .build();
                }).collect(Collectors.toList());
    }

    private List<RevisionNoticeDTO> searchRevisionNotices(String keyword, User user) {
        return revisionNoticeRepository.searchByTitle(keyword)
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "RevisionNotice", notice.getId(), user);
                    return RevisionNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .build();
                }).collect(Collectors.toList());
    }



    public Map<String, List<?>> getRecentNotices(String ssaid) {
        Map<String, List<?>> result = new HashMap<>();
        // USER가 북마크한 내역(id리스트) 가져오기
        User user = userRepository.findBySsaid(ssaid)
                .orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다."));


        // NormalNotice 검색 및 DTO 변환
        List<NormalNoticeDTO> normalNotices = normalNoticeRepository.findTop3ByOrderByPubDateDesc()
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "NormalNotice", notice.getId(), user);
                    return NormalNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .important(notice.isImportant())
                            .build();
                }).collect(Collectors.toList());
        if (!normalNotices.isEmpty()) {
            result.put("NormalNotice", normalNotices);
        }

        // AcademicNotice 검색 및 DTO 변환
        List<AcademicNoticeDTO> academicNotices = academicNoticeRepository.findTop3ByOrderByPubDateDesc()
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "AcademicNotice", notice.getId(), user);
                    return AcademicNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .important(notice.isImportant())
                            .build();
                }).collect(Collectors.toList());

        if (!academicNotices.isEmpty()) {
            result.put("AcademicNotice", academicNotices);
        }

        // EventNotice 검색 및 DTO 변환
        List<EventNoticeDTO> eventNotices = eventNoticeRepository.findTop3ByOrderByPubDateDesc()
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "EventNotice", notice.getId(), user);
                    return EventNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .build();
                }).collect(Collectors.toList());

        if (!eventNotices.isEmpty()) {
            result.put("EventNotice", eventNotices);
        }

        // CareerNotice 검색 및 DTO 변환
        List<CareerNoticeDTO> careerNotices = careerNoticeRepository.findTop3ByOrderByPubDateDesc()
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "CareerNotice", notice.getId(), user);
                    return CareerNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .build();
                }).collect(Collectors.toList());

        if (!careerNotices.isEmpty()) {
            result.put("CareerNotice", careerNotices);
        }

        // ScholarshipNotice 검색 및 DTO 변환
        List<ScholarshipNoticeDTO> scholarshipNotices = scholarshipNoticeRepository.findTop3ByOrderByPubDateDesc()
                .stream()
                .map(notice -> {
                    boolean isMarked = bookmarkRepository.existsByCategoryAndNoticeIDAndUser(
                            "ScholarshipNotices", notice.getId(), user);
                    return ScholarshipNoticeDTO.builder()
                            .id(notice.getId())
                            .pubDate(notice.getPubDate())
                            .title(notice.getTitle())
                            .url(notice.getUrl())
                            .marked(isMarked)
                            .build();
                }).collect(Collectors.toList());

        if (!scholarshipNotices.isEmpty()) {
            result.put("ScholarshipNotices", scholarshipNotices);
        }
        return result;
    }
}