package depth.mvp.thinkerbell.domain.error_report;

import depth.mvp.thinkerbell.global.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/error-report")
public class ErrorReportController {
    private final ErrorReportService errorReportService;

    @Operation(summary = "오류 신고 메세지 저장", description = "오류 신고 메세지을 저장합니다. 내용은 10자 이상이여야 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 저장됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PostMapping
    public ResponseEntity<ApiResult<String>> reportError(@Valid @RequestBody ErrorReportDTO errorReportDTO) {
        errorReportService.saveErrorReport(errorReportDTO);

        ApiResult<String> apiResult = ApiResult.ok("오류 신고 메시지가 성공적으로 저장되었습니다.");
        return ResponseEntity.ok(apiResult);
    }

}
