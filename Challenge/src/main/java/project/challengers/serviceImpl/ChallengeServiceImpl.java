package project.challengers.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.challengers.base.PointHistoryStatus;
import project.challengers.entity.*;
import project.challengers.exception.ChallengersException;
import project.challengers.repository.ChallengeRepository;
import project.challengers.repository.ParticipantRepository;
import project.challengers.repository.PointHistoryRepository;
import project.challengers.repository.PointRepository;
import project.challengers.service.ChallengeService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ChallengeServiceImpl implements ChallengeService {

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    PointRepository pointRepository;

    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    MessageSource messageSource;

    /**
     * 도전방 시작시간 이후에 방 수동 시작
     *
     * @param noticeSeq
     * @param id
     * @return
     */

    @Transactional
    @Override
    public int startChallenge(long noticeSeq, String id) {

        Challenge challenge = challengeRepository.findById(noticeSeq)
                .orElseThrow(() -> new ChallengersException(HttpStatus.BAD_REQUEST,
                        messageSource.getMessage("error.notice.notfound.E0010", new String[]{Long.toString(noticeSeq)}, Locale.KOREA)));

        checkAuth(challenge.getNotice(), id);

        // 도전방 시작시간 이전에 시작하는 경우
        if (!challenge.getNotice().getStartTime().isBefore(LocalDateTime.now())) {
            throw new ChallengersException(HttpStatus.CONFLICT,
                    messageSource.getMessage("error.challenge.startDate.before.E0026", null, Locale.KOREA));
        }

        return challengeRepository.startChallenge(noticeSeq);
    }

    /**
     * 도전방 종료시간 이후 and 포인트 분배 완료했을 경우 도전 방 수동 종료
     *
     * @param noticeSeq
     * @param id
     * @return
     */

    @Transactional
    @Override
    public int stopChallenge(long noticeSeq, String id) {
        Challenge challenge = challengeRepository.findById(noticeSeq)
                .orElseThrow(() -> new ChallengersException(HttpStatus.BAD_REQUEST,
                        messageSource.getMessage("error.notice.notfound.E0010", new String[]{Long.toString(noticeSeq)}, Locale.KOREA)));

        checkAuth(challenge.getNotice(), id);

        // 도전방 종료시간 이전에 종료하는 경우
        if (challenge.getNotice().getEndTime().isBefore(LocalDateTime.now())) {
            throw new ChallengersException(HttpStatus.CONFLICT,
                    messageSource.getMessage("error.challenge.endDate.before.E0027", null, Locale.KOREA));
        }

        // 남아 있는 포인트 체크
        if (challengeRepository.findById(noticeSeq).get().getMoney() != 0) {
            throw new ChallengersException(HttpStatus.CONFLICT,
                    messageSource.getMessage("error.notice.delete.money.conflict.E0018", null, Locale.KOREA));
        }

        return challengeRepository.stopChallenge(noticeSeq);
    }

    /**
     * 포인트 분배
     *
     * @param noticeSeq
     * @param id
     * @return
     */

    @Transactional
    @Override
    public int pointDistribution(long noticeSeq, String id) {
        Challenge challenge = challengeRepository.findById(noticeSeq)
                .orElseThrow(() -> new ChallengersException(HttpStatus.BAD_REQUEST,
                        messageSource.getMessage("error.notice.notfound.E0010", new String[]{Long.toString(noticeSeq)}, Locale.KOREA)));

        checkAuth(challenge.getNotice(), id);

        List<Participant> participantList = participantRepository.findByNotice(Notice.builder()
                .noticeSeq(noticeSeq)
                .build());

        int participantMoney = challenge.getMoney() / participantList.size(),
                masterMoney = (challenge.getMoney() - (participantMoney * participantList.size())) + participantMoney;

        List<PointHistory> participantHistory = new ArrayList<>();
        final PointHistory[] masterHistory = new PointHistory[1];

        List<Point> participantPoint = new ArrayList<>();
        List<String> idList = new ArrayList<>();
        final Point[] masterPoint = new Point[1];

        participantList.forEach(participant -> {
            // 방장일 경우
            if (participant.getParticipantId().equals(id)) {
                masterHistory[0] = PointHistory.builder()
                        .member(Member.builder()
                                .id(participant.getParticipantId())
                                .build())
                        .point(masterMoney)
                        .status(PointHistoryStatus.deposit.ordinal())
                        .insertTime(LocalDateTime.now())
                        .build();

                masterPoint[0] = Point.builder()
                        .member(Member.builder()
                                .id(participant.getParticipantId())
                                .build())
                        .point(masterMoney)
                        .build();
            } else { // 참가자 일 경우
                idList.add(participant.getParticipantId());

                participantHistory.add(PointHistory.builder()
                        .member(Member.builder()
                                .id(participant.getParticipantId())
                                .build())
                        .point(participantMoney)
                        .status(PointHistoryStatus.deposit.ordinal())
                        .insertTime(LocalDateTime.now())
                        .build());

                participantPoint.add(Point.builder()
                        .member(Member.builder()
                                .id(participant.getParticipantId())
                                .build())
                        .point(participantMoney)
                        .build());
            }
        });

        // 참가자 포인트 상금 입금 이력 추가
        pointHistoryRepository.saveAll(participantHistory);

        // 방장 포인트 상금 입금이력 추가
        pointHistoryRepository.save(masterHistory[0]);

        // 참가자 포인트 상금 입금
        pointRepository.updateUserPoint(participantMoney, idList);

        // 방장 포인트 상금 입금
        pointRepository.updateMasterPoint(masterMoney, id);

        return challengeRepository.pointDistribution(noticeSeq);
    }

    /**
     * 게시글 존재유무와 방장 권한 확인
     *
     * @param notice
     * @param id
     */
    private void checkAuth(Notice notice, String id) {
        // 방장 권한이 없을 경우
        if (!notice.getMember().getId().equals(id)) {
            throw new ChallengersException(HttpStatus.CONFLICT,
                    messageSource.getMessage("error.apply.master.conflict.E0017", null, Locale.KOREA));
        }
    }
}
