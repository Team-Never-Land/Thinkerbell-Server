package depth.mvp.thinkerbell.domain.error_report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorReportDTO {

    @NotBlank(message = "오류 메시지는 필수 항목입니다.")
    @Size(min = 10, max = 200, message = "오류 메시지는 10자 이상, 200자 이하여야 합니다.")
    private String errorMessage;
}
