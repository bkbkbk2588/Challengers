package project.challengers.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.challengers.DTO.participant.ParticipantCreditDto;
import project.challengers.DTO.participant.ParticipantDto;
import project.challengers.base.ParticipantType;
import project.challengers.entity.Notice;
import project.challengers.entity.Participant;
import project.challengers.exception.ChallengersException;
import project.challengers.repository.NoticeRepository;
import project.challengers.repository.ParticipantRepository;
import project.challengers.service.ParticipantService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    Logger logger = LoggerFactory.getLogger(ParticipantServiceImpl.class);

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    MessageSource messageSource;

    /**
     * 참가자 상태변경(방장만 가능)
     *
     * @param noticeSeq
     * @param idList
     * @param masterId
     * @return
     */
    @Override
    public int setParticipantType(long noticeSeq, List<String> idList, String masterId) {
        checkAuth(noticeSeq, masterId);

        return participantRepository.updateType(idList, noticeSeq);
    }

    /**
     * 참여자 전체 목록 조회
     *
     * @param noticeSeq
     * @param id
     * @return
     */
    @Override
    public List<ParticipantDto> getAllParticipant(long noticeSeq, String id) {
        checkAuth(noticeSeq, id);
        List<Participant> participantList = participantRepository.findByNotice(Notice.builder()
                .noticeSeq(noticeSeq).build());
        ;
        List<ParticipantDto> result = new ArrayList<>();

        participantList.forEach(participant -> {
            result.add(ParticipantDto.builder()
                    .challengeSeq(noticeSeq)
                    .id(participant.getParticipantId())
                    .status(String.valueOf(ParticipantType.values()[participant.getParticipantType()]))
                    .build());
        });
        return result;
    }

    /**
     * 사용자 상태 별 조회
     *
     * @param noticeSeq
     * @param type
     * @param id
     * @return
     */
    @Override
    public List<ParticipantDto> getParticipantStatus(long noticeSeq, int type, String id) {
        checkAuth(noticeSeq, id);
        List<Participant> participantList = participantRepository.findByNoticeAndParticipantType(Notice.builder()
                .noticeSeq(noticeSeq).build(), type);
        List<ParticipantDto> result = new ArrayList<>();

        participantList.forEach(participant -> {
            result.add(ParticipantDto.builder()
                    .challengeSeq(noticeSeq)
                    .id(participant.getParticipantId())
                    .status(String.valueOf(ParticipantType.values()[type]))
                    .build());
        });
        return result;
    }

    /**
     * 블라인드 처리(방장만 가능)
     *
     * @param noticeSeq
     * @param id
     * @param masterId
     * @return
     */
    @Override
    @Transactional
    public int setBlind(long noticeSeq, String id, String masterId) {
        checkAuth(noticeSeq, masterId);

        Participant participant = participantRepository.findByNoticeAndParticipantId(Notice.builder()
                .noticeSeq(noticeSeq)
                .build(), id);

        // 참가중이지 않은 사용자일 경우
        if (participant == null) {
            throw new ChallengersException(HttpStatus.NOT_FOUND,
                    messageSource.getMessage("error.apply.notfound.participant.E0024",
                            new String[]{id, Long.toString(noticeSeq)}, Locale.KOREA));
        }

        return participantRepository.setBlind(noticeSeq, id);
    }

    /**
     * 강퇴 처리(방장만 가능)
     *
     * @param noticeSeq
     * @param id
     * @param masterId
     * @return
     */
    @Override
    @Transactional
    public int setDelete(long noticeSeq, String id, String masterId) {
        checkAuth(noticeSeq, masterId);

        Participant participant = participantRepository.findByNoticeAndParticipantId(Notice.builder()
                .noticeSeq(noticeSeq)
                .build(), id);

        // 참가중이지 않은 사용자일 경우
        if (participant == null) {
            throw new ChallengersException(HttpStatus.NOT_FOUND,
                    messageSource.getMessage("error.apply.notfound.participant.E0024",
                            new String[]{id, Long.toString(noticeSeq)}, Locale.KOREA));
        }

        return participantRepository.setDelete(noticeSeq, id);
    }

    /**
     * 참가자 벌금 조회(방장만 가능)
     *
     * @param noticeSeq
     * @param id
     * @return
     */
    @Override
    public List<ParticipantCreditDto> getUserFine(long noticeSeq, String id) {
        checkAuth(noticeSeq, id);

        List<Participant> participants = participantRepository.findByNotice(Notice.builder()
                .noticeSeq(noticeSeq).build());
        List<ParticipantCreditDto> participantCredits = new ArrayList<>();

        participants.forEach(participant -> {
            participantCredits.add(ParticipantCreditDto.builder()
                    .id(participant.getParticipantId())
                    .credit(participant.getCredit())
                    .build());
        });
        return participantCredits;
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
        if (!notice.getMember().getId().equals(id)) {
            throw new ChallengersException(HttpStatus.CONFLICT,
                    messageSource.getMessage("error.apply.master.conflict.E0017", null, Locale.KOREA));
        }
    }
}
