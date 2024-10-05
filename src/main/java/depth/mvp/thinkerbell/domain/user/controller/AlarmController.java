package depth.mvp.thinkerbell.domain.user.controller;

import depth.mvp.thinkerbell.domain.user.dto.AlarmDto;
import depth.mvp.thinkerbell.domain.user.service.AlarmService;
import depth.mvp.thinkerbell.global.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @Operation(summary = "알림 기능 테스트", description = "사용자 SSAID와 키워드로 알림 기능을 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/alarm-test")
    public void testAlarm(@RequestParam String SSAID, @RequestParam String keyword) {
        alarmService.updateNoticeAndMatchKeywordTest(SSAID, keyword);
    }

    @Operation(summary = "키워드 별 미확인 알림 여부 확인", description = "사용자 SSAID와 키워드로 미확인 알림이 있는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/check")
    public ApiResult<Boolean> checkUnviewedAlarm(@RequestParam String SSAID, @RequestParam String keyword) {
            boolean hasUnviewed = alarmService.hasUnviewedAlarm(SSAID, keyword);
            return ApiResult.ok(hasUnviewed);
    }

    @Operation(summary = "전체 공지 중 미확인 알림 여부 확인", description = "사용자 SSAID로 미확인 알림이 있는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/check-all")
    public ApiResult<Boolean> checkUnviewedAllAlarm(@RequestParam String SSAID) {
            boolean hasUnviewed = alarmService.hasUnviewedAllAlarm(SSAID);
            return ApiResult.ok(hasUnviewed);
    }

    @Operation(summary = "알람 읽음 처리", description = "특정 알람을 읽음 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/mark-viewed")
    public ApiResult<String> markAsViewed(@RequestParam Long alarmId) {
            alarmService.markAsViewed(alarmId);
            return ApiResult.ok("알림이 성공적으로 읽음 처리되었습니다.");
    }

    @Operation(summary = "알람 조회", description = "키워드와 사용자 SSAID로 알람을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("")
    public ApiResult<List<AlarmDto>> getAlarm(@RequestParam String SSAID, @RequestParam String keyword) {
            List<AlarmDto> alarms = alarmService.getAlarms(SSAID, keyword);
            return ApiResult.ok(alarms);
    }

    @Operation(summary = "알람 여부 조회", description = "사용자 SSAID로 알람 여부를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 확인됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("/alarm-status")
    public ApiResult<Boolean> getAlarmEnabled(@RequestParam String SSAID) {
        return ApiResult.ok(alarmService.getUserAlarm(SSAID));
    }

    @Operation(summary = "알람 여부 변경", description = "사용자 SSAID로 알람 여부를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 변경됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PatchMapping("/alarm-toggle")
    public ApiResult<String> toggleUserAlarm(@RequestParam String SSAID) {
        alarmService.toggleUserAlarm(SSAID);
        return ApiResult.ok("성공적으로 변경되었습니다.");
    }
}
