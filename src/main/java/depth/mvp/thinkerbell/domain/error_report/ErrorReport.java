package depth.mvp.thinkerbell.domain.error_report;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "ErrorReport")
public class ErrorReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String errorMessage;

    @Column(updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public ErrorReport(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
