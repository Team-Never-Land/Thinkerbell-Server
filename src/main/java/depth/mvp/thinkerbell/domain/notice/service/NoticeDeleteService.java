package depth.mvp.thinkerbell.domain.notice.service;

import depth.mvp.thinkerbell.domain.notice.entity.*;
import depth.mvp.thinkerbell.domain.notice.repository.*;
import depth.mvp.thinkerbell.domain.user.entity.Alarm;
import depth.mvp.thinkerbell.domain.user.entity.Bookmark;
import depth.mvp.thinkerbell.domain.user.repository.AlarmRepository;
import depth.mvp.thinkerbell.domain.user.repository.BookmarkRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeDeleteService {

    private final AlarmRepository alarmRepository;
    private final BookmarkRepository bookmarkRepository;
    private final AcademicScheduleRepository academicScheduleRepository;
    private final AcademicNoticeRepository academicNoticeRepository;
    private final BiddingNoticeRepository biddingNoticeRepository;
    private final CareerNoticeRepository careerNoticeRepository;
    private final DormitoryEntryNoticeRepository entryNoticeRepository;
    private final DormitoryNoticeRepository dormitoryNoticeRepository;
    private final EventNoticeRepository eventNoticeRepository;
    private final LibraryNoticeRepository libraryNoticeRepository;
    private final NormalNoticeRepository normalNoticeRepository;
    private final RevisionNoticeRepository revisionNoticeRepository;
    private final SafetyNoticeRepository safetyNoticeRepository;
    private final ScholarshipNoticeRepository scholarshipNoticeRepository;
    private final StudentActsNoticeRepository studentActsNoticeRepository;
    private final TeachingNoticeRepository teachingNoticeRepository;

    // 저장된 학사 일정에서 저장 기한이 지난 데이터 제거
    private void DeleteSchedule(){
        List<AcademicSchedule> schedules = academicScheduleRepository.findAll();

        int currentYear = LocalDate.now().getYear();

        for (AcademicSchedule schedule : schedules) {
            LocalDate[] localDate = ScheduleParser.parseDate(schedule.getSchedule());
            int year = localDate[0].getYear();

            // 현재 날짜를 기준으로 2년전 자료보다 이전 자료인지 확인
            if (year <= currentYear - 2) {
                List<Bookmark> bookmarks = bookmarkRepository.findALLByCategoryAndNoticeID("AcademicSchedule", schedule.getId());
                bookmarkRepository.deleteAll(bookmarks);

                academicScheduleRepository.delete(schedule);
            }
        }
    }

    // 저장된 학사 공지에서 6개월 이상이 된 데이터 제거
    // 중요 공지는 삭제하지 않고 일반 공지 중 저장 일자가 지난 공지만 삭제한다.
    private void DeleteAcademicNotice(){
        List<AcademicNotice> academicNotices = academicNoticeRepository.findAllByImportantFalse();

        LocalDate currentDate = LocalDate.now();

        for (AcademicNotice notice : academicNotices) {

            LocalDate noticeDate = notice.getPubDate();

            Period period = Period.between(noticeDate, currentDate);

            // 기간을 확인한 후 즐겨찾기와 알림에서 해당 공지를 삭제한다.
            if (period.getYears() >= 3){
                List <Bookmark> bookmarks = bookmarkRepository.findALLByCategoryAndNoticeID("AcademicNotice", notice.getId());
                bookmarkRepository.deleteAll(bookmarks);

                List <Alarm> alarms = alarmRepository.findALLByNoticeTypeAndNoticeID("academic_notice", notice.getId());
                alarmRepository.deleteAll(alarms);

                academicNoticeRepository.delete(notice);
            }
        }
    }

    // 저장된 입찰 공지에서 6개월 이상이 된 데이터 제거
    private void DeleteBiddingNotice(){
        List<BiddingNotice> biddingNotices = biddingNoticeRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for (BiddingNotice notice : biddingNotices) {
            LocalDate noticeDate = notice.getPubDate();
            Period period = Period.between(noticeDate, currentDate);

            // 기간을 확인한 후 즐겨찾기와 알림에서 해당 공지를 삭제한다.
            if (period.getYears() >= 3){
                List<Bookmark> bookmarks = bookmarkRepository.findALLByCategoryAndNoticeID("BiddingNotice", notice.getId());
                bookmarkRepository.deleteAll(bookmarks);

                List<Alarm> alarms = alarmRepository.findALLByNoticeTypeAndNoticeID("bidding_notice", notice.getId());
                alarmRepository.deleteAll(alarms);

                biddingNoticeRepository.delete(notice);
            }
        }
    }

    // 저장된 진로/취업/창업 공지에서 6개월 이상이 된 데이터 제거
    private void DeleteCareerNotice(){
        List<CareerNotice> careerNotices = careerNoticeRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for (CareerNotice notice : careerNotices) {
            LocalDate noticeDate = notice.getPubDate();
            Period period = Period.between(noticeDate, currentDate);

            // 기간을 확인한 후 즐겨찾기와 알림에서 해당 공지를 삭제한다.
            if (period.getYears() >= 3){
                List<Bookmark> bookmarks = bookmarkRepository.findALLByCategoryAndNoticeID("CareerNotice", notice.getId());
                bookmarkRepository.deleteAll(bookmarks);

                List<Alarm> alarms = alarmRepository.findALLByNoticeTypeAndNoticeID("career_notice", notice.getId());
                alarmRepository.deleteAll(alarms);

                careerNoticeRepository.delete(notice);
            }
        }
    }

    // 저장된 기숙사 입/퇴사 공지중 저장 기한이 다된 데이터 제거
    // 중요 공지는 삭제하지 않고 일반 공지 중 저장 일자가 지난 공지만 삭제한다.
    private void DeleteDormitoryEntryNotice(){
        List<DormitoryEntryNotice> dormitoryEntryNotices = entryNoticeRepository.findAllByImportantFalse();
        LocalDate currentDate = LocalDate.now();

        for (DormitoryEntryNotice notice : dormitoryEntryNotices) {
            LocalDate noticeDate = notice.getPubDate();
            Period period = Period.between(noticeDate, currentDate);

            // 기간을 확인한 후 즐겨찾기와 알림에서 해당 공지를 삭제한다.
            if (period.getYears() >= 3){
                List<Bookmark> bookmarks = bookmarkRepository.findALLByCategoryAndNoticeID("DormitoryEntryNotice", notice.getId());
                bookmarkRepository.deleteAll(bookmarks);

                List<Alarm> alarms = alarmRepository.findALLByNoticeTypeAndNoticeID("dormitory_entry_notice", notice.getId());
                alarmRepository.deleteAll(alarms);

                entryNoticeRepository.delete(notice);
            }
        }
    }

    // 저장된 기숙사 공지중 저장 기한이 다된 데이터 제거
    // 중요 공지는 삭제하지 않고 일반 공지 중 저장 일자가 지난 공지만 삭제한다.
    private void DeleteDormitoryNotice(){
        List<DormitoryNotice> dormitoryNotice = dormitoryNoticeRepository.findAllByImportantFalse();
        LocalDate currentDate = LocalDate.now();

        for (DormitoryNotice notice : dormitoryNotice) {
            LocalDate noticeDate = notice.getPubDate();
            Period period = Period.between(noticeDate, currentDate);

            // 기간을 확인한 후 즐겨찾기와 알림에서 해당 공지를 삭제한다.
            if (period.getYears() >= 3){
                List<Bookmark> bookmarks = bookmarkRepository.findALLByCategoryAndNoticeID("DormitoryNotice", notice.getId());
                bookmarkRepository.deleteAll(bookmarks);

                List<Alarm> alarms = alarmRepository.findALLByNoticeTypeAndNoticeID("dormitory_notice", notice.getId());
                alarmRepository.deleteAll(alarms);

                dormitoryNoticeRepository.delete(notice);
            }
        }
    }

    // 저장된 행사 공지에서 6개월 이상이 된 데이터 제거
    private void DeleteEventNotice(){
        List<EventNotice> eventNotices = eventNoticeRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for (EventNotice notice : eventNotices) {
            LocalDate noticeDate = notice.getPubDate();
            Period period = Period.between(noticeDate, currentDate);

            // 기간을 확인한 후 즐겨찾기와 알림에서 해당 공지를 삭제한다.
            if (period.getYears() >= 3){
                List<Bookmark> bookmarks = bookmarkRepository.findALLByCategoryAndNoticeID("EventNotice", notice.getId());
                bookmarkRepository.deleteAll(bookmarks);

                List<Alarm> alarms = alarmRepository.findALLByNoticeTypeAndNoticeID("event_notice", notice.getId());
                alarmRepository.deleteAll(alarms);

                eventNoticeRepository.delete(notice);
            }
        }
    }

    // 저장된 도서관 공지중 저장 기한이 다된 데이터 제거
    // 중요 공지는 삭제하지 않고 일반 공지 중 저장 일자가 지난 공지만 삭제한다.
    private void DeleteLibraryNotice(){
        List<LibraryNotice> libraryNotices = libraryNoticeRepository.findAllByImportantFalse();
        LocalDate currentDate = LocalDate.now();

        for (LibraryNotice notice : libraryNotices) {
            LocalDate noticeDate = notice.getPubDate();
            Period period = Period.between(noticeDate, currentDate);

            // 기간을 확인한 후 즐겨찾기와 알림에서 해당 공지를 삭제한다.
            if (period.getYears() >= 3){
                List<Bookmark> bookmarks = bookmarkRepository.findALLByCategoryAndNoticeID("LibraryNotice", notice.getId());
                bookmarkRepository.deleteAll(bookmarks);

                List<Alarm> alarms = alarmRepository.findALLByNoticeTypeAndNoticeID("library_notice", notice.getId());
                alarmRepository.deleteAll(alarms);

                libraryNoticeRepository.delete(notice);
            }
        }
    }

    // 저장된 일반 공지중 저장 기한이 다된 데이터 제거
    // 중요 공지는 삭제하지 않고 일반 공지 중 저장 일자가 지난 공지만 삭제한다.
    private void DeleteNormalNotice(){
        List<NormalNotice> normalNotices = normalNoticeRepository.findAllByImportantFalse();
        LocalDate currentDate = LocalDate.now();

        for (NormalNotice notice : normalNotices) {
            LocalDate noticeDate = notice.getPubDate();
            Period period = Period.between(noticeDate, currentDate);

            // 기간을 확인한 후 즐겨찾기와 알림에서 해당 공지를 삭제한다.
            if (period.getYears() >= 3){
                List<Bookmark> bookmarks = bookmarkRepository.findALLByCategoryAndNoticeID("NormalNotice", notice.getId());
                bookmarkRepository.deleteAll(bookmarks);

                List<Alarm> alarms = alarmRepository.findALLByNoticeTypeAndNoticeID("normal_notice", notice.getId());
                alarmRepository.deleteAll(alarms);

                normalNoticeRepository.delete(notice);
            }
        }
    }

    // 저장된 학칙개정 공지 중 저장 기한이 다된 데이터 제거
    private void DeleteRevisionNotice(){
        List<RevisionNotice> revisionNotices = revisionNoticeRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for (RevisionNotice notice : revisionNotices) {
            LocalDate noticeDate = notice.getPubDate();
            Period period = Period.between(noticeDate, currentDate);

            if (period.getYears() >= 3){
                List<Bookmark> bookmarks = bookmarkRepository.findALLByCategoryAndNoticeID("RevisionNotice", notice.getId());
                bookmarkRepository.deleteAll(bookmarks);

                List<Alarm> alarms = alarmRepository.findALLByNoticeTypeAndNoticeID("revision_notice", notice.getId());
                alarmRepository.deleteAll(alarms);

                revisionNoticeRepository.delete(notice);
            }
        }
    }

    // 저장된 안전 공지 중 저장 기한이 다된 데이터 제거
    private void DeleteSafetyNotice(){
        List<SafetyNotice> safetyNotices = safetyNoticeRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for (SafetyNotice notice : safetyNotices) {
            LocalDate noticeDate = notice.getPubDate();
            Period period = Period.between(noticeDate, currentDate);

            if (period.getYears() >= 3){
                List<Bookmark> bookmarks = bookmarkRepository.findALLByCategoryAndNoticeID("SafetyNotice", notice.getId());
                bookmarkRepository.deleteAll(bookmarks);

                List<Alarm> alarms = alarmRepository.findALLByNoticeTypeAndNoticeID("safety_notice", notice.getId());
                alarmRepository.deleteAll(alarms);

                safetyNoticeRepository.delete(notice);
            }
        }
    }

    // 저장된 장학/학자금 공지 중 기한이 다된 데이터 제거
    private void DeleteScholarshipNotice(){
        List<ScholarshipNotice> scholarshipNotices = scholarshipNoticeRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for (ScholarshipNotice notice : scholarshipNotices) {
            LocalDate noticeDate = notice.getPubDate();
            Period period = Period.between(noticeDate, currentDate);

            if (period.getYears() >= 3){
                List<Bookmark> bookmarks = bookmarkRepository.findALLByCategoryAndNoticeID("ScholarshipNotice", notice.getId());
                bookmarkRepository.deleteAll(bookmarks);

                List<Alarm> alarms = alarmRepository.findALLByNoticeTypeAndNoticeID("Scholarship_notice", notice.getId());
                alarmRepository.deleteAll(alarms);

                scholarshipNoticeRepository.delete(notice);
            }
        }
    }

    private void DeleteStudentActsNotice(){
        List<StudentActsNotice> studentActsNotices = studentActsNoticeRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for (StudentActsNotice notice : studentActsNotices) {
            LocalDate noticeDate = notice.getPubDate();
            Period period = Period.between(noticeDate, currentDate);

            if (period.getYears() >= 3){
                List<Bookmark> bookmarks = bookmarkRepository.findALLByCategoryAndNoticeID("StudentActsNotice", notice.getId());
                bookmarkRepository.deleteAll(bookmarks);

                List<Alarm> alarms = alarmRepository.findALLByNoticeTypeAndNoticeID("Student_acts_notice", notice.getId());
                alarmRepository.deleteAll(alarms);

                studentActsNoticeRepository.delete(notice);
            }
        }
    }

    public void DeleteTeachingNotice(){
        List<TeachingNotice> teachingNotices = teachingNoticeRepository.findAllByImportantFalse();
        LocalDate currentDate = LocalDate.now();

        for (TeachingNotice notice : teachingNotices) {
            LocalDate noticeDate = notice.getPubDate();
            Period period = Period.between(noticeDate, currentDate);

            if (period.getYears() >= 3){
                List<Bookmark> bookmarks = bookmarkRepository.findALLByCategoryAndNoticeID("TeachingNotice", notice.getId());
                bookmarkRepository.deleteAll(bookmarks);

                List<Alarm> alarms = alarmRepository.findALLByNoticeTypeAndNoticeID("Teaching_notice", notice.getId());
                alarmRepository.deleteAll(alarms);

                teachingNoticeRepository.delete(notice);
            }
        }
    }



    @Scheduled(cron = "0 0 3 * * ?", zone = "Asia/Seoul")
    public void DeleteAllNotices(){
        DeleteSchedule();
        DeleteAcademicNotice();
        DeleteBiddingNotice();
        DeleteCareerNotice();
        DeleteDormitoryNotice();
        DeleteDormitoryEntryNotice();
        DeleteEventNotice();
        DeleteLibraryNotice();
        DeleteNormalNotice();
        DeleteRevisionNotice();
        DeleteScholarshipNotice();
        DeleteStudentActsNotice();
        DeleteTeachingNotice();
        DeleteSafetyNotice();
    }
}
