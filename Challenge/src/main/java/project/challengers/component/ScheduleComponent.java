package project.challengers.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.challengers.repository.AuthRepository;
import project.challengers.repository.ParticipantRepository;

@Component
public class ScheduleComponent {
    Logger logger = LoggerFactory.getLogger(ScheduleComponent.class);

    /*
        TODO
            1. 경고 횟수 3회 이상 6회 이하일 경우 블라인드 처리
            2. 경고 횟수 7회 이상이면 강퇴 처리
            3. entity comment 처리
     */

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    AuthRepository authRepository;

    // 위의 2개는 삭제 고려

    /**
     * 도전방 시작 시간일 때 시작으로 상태값 변경 (단, 방장 포함 참가자 2명 이상일 경우)
     */
    @Scheduled(cron = "${challengers.schedule}")
    public void challengeStart() {

    }

    /**
     * 도전방 종료 시간일 때 종료로 상태값 변경 (단, 포인트는 n분의 1로 배분)
     */
    @Scheduled(cron = "${challengers.schedule}")
    public void challengeEnd() {

    }

    /**
     * 인증파일 7일 후 삭제
     */
    @Scheduled(cron = "${challengers.schedule}")
    public void deleteAuth() {

    }

    /**
     * 블라인드 된지 3일째 되면 블라인드 해제
     */
    @Scheduled(cron = "${challengers.schedule}")
    public void setBlind() {

    }
}
