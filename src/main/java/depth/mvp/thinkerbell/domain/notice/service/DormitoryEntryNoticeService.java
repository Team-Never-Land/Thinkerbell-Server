package depth.mvp.thinkerbell.domain.notice.service;

import depth.mvp.thinkerbell.domain.common.pagination.PaginationDTO;
import depth.mvp.thinkerbell.domain.notice.dto.DormitoryEntryNoticeDTO;
import depth.mvp.thinkerbell.domain.notice.entity.DormitoryEntryNotice;
import depth.mvp.thinkerbell.domain.notice.repository.DormitoryEntryNoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DormitoryEntryNoticeService {

    @Autowired
    private DormitoryEntryNoticeRepository dormitoryEntryNoticeRepository;

//    public List<DormitoryEntryNoticeDTO> getAllEntryNotices() {
//        return dormitoryEntryNoticeRepository.findAll().stream().map(notice -> new DormitoryEntryNoticeDTO(
//                notice.getId(), notice.getPubDate(), notice.getTitle(), notice.getUrl(),
//                notice.isImportant(), notice.getCampus()
//        )).collect(Collectors.toList());
//    }

    public PaginationDTO<DormitoryEntryNoticeDTO> getImportantNotices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DormitoryEntryNotice> resultPage = dormitoryEntryNoticeRepository.findAllByOrderByIsImportantDescPubDateDesc(pageable);

        List<DormitoryEntryNoticeDTO> dtoList = resultPage.stream()
                .map(DormitoryEntryNoticeDTO::fromEntity)  // DTO 클래스 내의 메서드 호출
                .collect(Collectors.toList());

        return new PaginationDTO<>(
                dtoList,
                resultPage.getNumber(),
                resultPage.getSize(),
                resultPage.getTotalElements()
        );
    }
}
