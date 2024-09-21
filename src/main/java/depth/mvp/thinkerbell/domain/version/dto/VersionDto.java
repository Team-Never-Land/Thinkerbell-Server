package depth.mvp.thinkerbell.domain.version.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class VersionDto {

    private String versionCode;
    private String versionName;

}
