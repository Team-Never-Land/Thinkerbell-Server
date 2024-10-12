package depth.mvp.thinkerbell.domain.user.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class AlarmDto {

    private Long id;
    private Long categoryId;
    private String title;
    private String noticeTypeKorean;
    private String noticeTypeEnglish;
    private boolean isViewed;
    private boolean isMarked;
    private String Url;
    private String pubDate;
}
