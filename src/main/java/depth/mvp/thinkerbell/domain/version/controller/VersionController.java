package depth.mvp.thinkerbell.domain.version.controller;

import depth.mvp.thinkerbell.domain.version.service.VersionService;
import depth.mvp.thinkerbell.global.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/version")
@RequiredArgsConstructor
public class VersionController {

    private final VersionService versionService;

    @Operation(summary = "최소 업데이트 필요 버전 조회", description = "최소 업데이트 필요 버전을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 조회됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @GetMapping("")
    public ApiResult<?> getNecessaryVersion() {
        return ApiResult.ok("성공적으로 조회됨", versionService.getNecessaryVersion());
    }

    @Operation(summary = "최소 업데이트 필요 버전 수정", description = "최소 업데이트 필요 버전을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 수정됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값"),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생")
    })
    @PatchMapping("")
    public ApiResult<?> modifyNecessaryVersion(@RequestParam(required = false) String versionCode,
                                               @RequestParam(required = false) String versionName) {
        versionService.modifyNecessaryVersion(versionCode, versionName);
        return ApiResult.ok("성공적으로 수정됨");
    }

}
