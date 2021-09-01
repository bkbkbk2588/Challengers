package project.challengers.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.challengers.entity.Auth;
import project.challengers.entity.Challenge;
import project.challengers.entity.Notice;
import project.challengers.repository.AuthRepository;
import project.challengers.repository.ChallengeRepository;
import project.challengers.repository.NoticeRepository;
import project.challengers.repository.ParticipantRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleComponent {
    Logger logger = LoggerFactory.getLogger(ScheduleComponent.class);

    @Autowired
    AuthRepository authRepository;

    @Autowired
    ChallengeRepository challengeRepository;


    /**
     * 도전방 시작 시간일 때 시작으로 상태값 변경 (단, 방장 포함 참가자 2명 이상일 경우)
     */
    @Scheduled(cron = "${challengers.schedule}")
    public void challengeStart() {
        List<Challenge> challengeList = challengeRepository.findByChallengeBefore(LocalDateTime.now());
        List<Long> challengeSeqList = new ArrayList<>();

        challengeList.forEach(challenge -> {
            challengeSeqList.add(challenge.getNotice().getNoticeSeq());
        });
        challengeRepository.startChallengeList(challengeSeqList);
    }

    /**
     * 도전방 종료 시간일 때 종료로 상태값 변경 (단, 포인트는 n분의 1로 배분)
     */
    @Scheduled(cron = "${challengers.schedule}")
    public void challengeEnd() {
        List<Challenge> challengeList = challengeRepository.findByChallengeAfter(LocalDateTime.now());
        List<Long> challengeSeqList = new ArrayList<>();

        challengeList.forEach(challenge -> {
            challengeSeqList.add(challenge.getNotice().getNoticeSeq());
        });
        challengeRepository.stopChallengeList(challengeSeqList);
    }

    /**
     * 인증파일 7일 후 삭제
     */
    @Scheduled(cron = "${challengers.schedule}")
    public void deleteAuth() {
        List<Auth> authList = authRepository.findByAuthDateBefore(LocalDate.now().minusDays(6));
        List<Long> authSeqList = new ArrayList<>();

        authList.forEach(auth -> {
            authSeqList.add(auth.getAuthSeq());
        });
        authRepository.deleteAuthFile(authSeqList);
    }
}
