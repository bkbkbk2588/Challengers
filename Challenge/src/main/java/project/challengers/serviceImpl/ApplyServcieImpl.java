package project.challengers.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.challengers.DTO.apply.ApplyDto;
import project.challengers.DTO.apply.ApplyListDto;
import project.challengers.base.ChallengeStatus;
import project.challengers.base.ParticipantType;
import project.challengers.base.PointHistoryStatus;
import project.challengers.entity.*;
import project.challengers.exception.ChallengersException;
import project.challengers.repository.*;
import project.challengers.service.ApplyService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ApplyServcieImpl implements ApplyService {

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    ApplyRepository applyRepository;

    @Autowired
    PointRepository pointRepository;

    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    MessageSource messageSource;

    /**
     * 도전 신청
     *
     * @param apply
     * @param id
     * @return
     */
    @Transactional
    @Override
    public int insertApply(ApplyDto apply, String id) {
        Notice notice = noticeRepository.findById(apply.getNoticeSeq()).get();

        // 게시글이 없을 경우
        if (notice == null) {
            throw new ChallengersException(HttpStatus.NOT_FOUND,
                    messageSource.getMessage("error.notice.notfound.noticeList.E0006", null, Locale.KOREA));
        }

        // 보증금 부족할 경우
        if (notice.getPrice() > apply.getDeposit()) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.apply.deposit.lack.E0014", null, Locale.KOREA));
        }

        // 방장 자신은 신청 못함
        if (notice.getId().equals(id)) {
            throw new ChallengersException(HttpStatus.CONFLICT,
                    messageSource.getMessage("error.apply.already.master.E0016", null, Locale.KOREA));
        }

        // 시작 날짜가 지났을 경우
        if (notice.getStartTime().isBefore(LocalDateTime.now())) {
            throw new ChallengersException(HttpStatus.CONFLICT,
                    messageSource.getMessage("error.apply.startTime.after.E0021", null, Locale.KOREA));
        }

        List<Participant> participant = participantRepository.findByNoticeSeqAndParticipantType(apply.getNoticeSeq(), ParticipantType.normal.ordinal());

        // 참여인원을 초과할 경우
        if (notice.getMaxPeople() - 1 <= participant.size()) {
            throw new ChallengersException(HttpStatus.FORBIDDEN,
                    messageSource.getMessage("error.notice.max.people.E0020", null, Locale.KOREA));
        }
        Challenge challenge = challengeRepository.findById(apply.getNoticeSeq()).get();
        // 방이 끝났을 경우
        if (notice.getEndTime().isBefore(LocalDateTime.now())
                || challenge.getStatus() != ChallengeStatus.startBefore.ordinal()) {
            throw new ChallengersException(HttpStatus.CONFLICT,
                    messageSource.getMessage("error.notice.finish.E0019", null, Locale.KOREA));
        }

        // 이미 참여중인 사용자 일 경우
        participant.forEach(p -> {
            if (p.getParticipantId().equals(id)) {
                throw new ChallengersException(HttpStatus.CONFLICT,
                        messageSource.getMessage("error.apply.already.participant.E0015", null, Locale.KOREA));
            }
        });

        Point pointEntity = pointRepository.findById(id);

        // 출금 금액이 더 많을 경우
        if (pointEntity == null || notice.getPrice() > pointEntity.getPoint()) {
            throw new ChallengersException(HttpStatus.CONFLICT,
                    messageSource.getMessage("error.point.withdraw.big.E0013", null, Locale.KOREA));
        }

        pointRepository.updatePoint(pointEntity.getPoint() - notice.getPrice(), id);

        // 이력 추가
        pointHistoryRepository.save(PointHistory.builder()
                .id(id)
                .point(apply.getDeposit())
                .status(PointHistoryStatus.withdraw.ordinal())
                .insertTime(LocalDateTime.now())
                .build());

        Apply applyEntity = applyRepository.save(Apply.builder()
                .id(id)
                .deposit(apply.getDeposit())
                .noticeSeq(apply.getNoticeSeq())
                .build());

        return applyEntity != null ? 1 : 0;
    }

    /**
     * 도전 신청 수락(방장만 가능)
     * 수락하면 Apply 에서는 delete 후 Participant, challenge 에 insert
     *
     * @param noticeSeq
     * @param id
     * @return
     */
    @Transactional
    @Override
    public int acceptApply(long noticeSeq, List<String> idList, String id) {
        checkAuth(noticeSeq, id);
        List<Apply> applyList = applyRepository.findByNoticeSeq(noticeSeq);

        // 보증금 합산한것 구하기
        int depositSum = applyList.stream()
                .filter(apply ->
                        idList.contains(apply.getId()))
                .mapToInt(Apply::getDeposit)
                .sum();

        List<Participant> participantList = new ArrayList<>();

        idList.forEach(userId -> {
            participantList.add(Participant.builder()
                    .masterId(id)
                    .noticeSeq(noticeSeq)
                    .participantType(ParticipantType.normal.ordinal())
                    .participantId(userId)
                    .build());
        });
        Challenge challenge = challengeRepository.findById(noticeSeq).get();

        // 참가자로 전환
        participantRepository.saveAll(participantList);

        // challenge 에 update
        challengeRepository.updateMoney(noticeSeq, depositSum + challenge.getMoney());

        return applyRepository.acceptApply(idList, noticeSeq);
    }

    /**
     * 도전 신청자 리스트 조회
     *
     * @param noticeSeq
     * @param id
     * @return
     */
    @Override
    public List<ApplyListDto> getApplyList(long noticeSeq, String id) {
        checkAuth(noticeSeq, id);
        List<Apply> applyList = applyRepository.findByNoticeSeq(noticeSeq);
        List<ApplyListDto> resultApply = new ArrayList<>();

        applyList.forEach(apply -> {
            resultApply.add(ApplyListDto.builder()
                    .deposit(apply.getDeposit())
                    .id(apply.getId())
                    .noticeSeq(noticeSeq)
                    .build());
        });

        return resultApply;
    }


    /**
     * 게시글 존재유무와 방장 권한 확인
     *
     * @param noticeSeq
     * @param id
     */
    private void checkAuth(long noticeSeq, String id) {
        Notice notice = noticeRepository.findById(noticeSeq).get();

        // 게시글이 없을 경우
        if (notice == null) {
            throw new ChallengersException(HttpStatus.NOT_FOUND,
                    messageSource.getMessage("error.notice.notfound.noticeList.E0006", null, Locale.KOREA));
        }

        // 방장 권한이 없을 경우
        if (!notice.getId().equals(id)) {
            throw new ChallengersException(HttpStatus.CONFLICT,
                    messageSource.getMessage("error.apply.master.conflict.E0017", null, Locale.KOREA));
        }
    }
}
