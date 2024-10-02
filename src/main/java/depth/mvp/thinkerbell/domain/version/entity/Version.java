package depth.mvp.thinkerbell.domain.version.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@Table(name = "version")
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String versionCode;
    private String versionName;

    @Builder
    public Version(String versionCode, String versionName) {
        this.versionCode = versionCode;
        this.versionName = versionName;
    }
}
