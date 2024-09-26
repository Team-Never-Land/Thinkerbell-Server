package depth.mvp.thinkerbell.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ssaid;
    private String fcmToken;
    private Boolean alarmEnabled = true;

    @Builder
    public User(String ssaid, String fcmToken, Boolean alarmEnabled) {
        this.ssaid = ssaid;
        this.fcmToken = fcmToken;
        this.alarmEnabled = true;
    }

    public void toggleAlarmEnabled() {
        this.alarmEnabled = !this.alarmEnabled;
    }
}
