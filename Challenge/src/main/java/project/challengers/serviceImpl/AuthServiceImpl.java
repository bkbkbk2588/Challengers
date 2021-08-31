package project.challengers.serviceImpl;

import io.micrometer.core.instrument.util.StringUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.challengers.DTO.auth.AuthDto;
import project.challengers.DTO.auth.AuthInfoDto;
import project.challengers.base.PointHistoryStatus;
import project.challengers.component.FileComponent;
import project.challengers.entity.*;
import project.challengers.exception.ChallengersException;
import project.challengers.repository.*;
import project.challengers.service.AuthService;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {
    @Value("${challengers.path.prefix}${challengers.path.files}")
    private String UPLOAD_FILE_PATH;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    PointRepository pointRepository;

    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Autowired
    FileComponent fileComponent;

    @Autowired
    MessageSource messageSource;

    /**
     * 받은 인증 파일 저장
     *
     * @param filePartFlux
     * @param noticeSeq
     * @param id
     * @return
     */
    @Transactional
    @Override
    public int attendanceAuth(Flux<FilePart> filePartFlux, long noticeSeq, String id) {
        Participant participant = participantRepository.findByNoticeAndParticipantId(Notice.builder()
                .noticeSeq(noticeSeq)
                .build(), id);

        // 정상참여 계정이 아닐 경우
        if (participant.getParticipantType() != 0) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.auth.not.normal.user.E0025", null, Locale.KOREA));
        }

        // 인증 파일이 없을 경우
        if (filePartFlux == null) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.auth.notfound.file.E0022", null, Locale.KOREA));
        }

        List<Auth> auths = new ArrayList<>();
        fileComponent.save(filePartFlux)
                .subscribe(file -> {
                    auths.add(Auth.builder()
                            .member(Member.builder()
                                    .id(id)
                                    .build())
                            .notice(Notice.builder()
                                    .noticeSeq(noticeSeq)
                                    .build())
                            .fileName(file.getLeft())
                            .filePath(file.getRight())
                            .authDate(LocalDate.now())
                            .build());
                });
        List<Auth> insertResult = authRepository.saveAll(auths);

        return insertResult != null ? 1 : 0;
    }

    /**
     * 인증파일 url 확인
     *
     * @param noticeSeq
     * @param masterId
     * @return
     */
    @Override
    public AuthDto getAuthFile(long noticeSeq, String masterId, ServerHttpRequest req) {
        List<Participant> participantList = participantRepository.findByNotice(Notice.builder()
                .noticeSeq(noticeSeq)
                .build());

        // 방장권한 확인
        if (participantList == null || !participantList.get(0).getMasterId().equals(masterId)) {
            throw new ChallengersException(HttpStatus.UNAUTHORIZED,
                    messageSource.getMessage("error.apply.master.conflict.E0017", null, Locale.KOREA));
        }
        List<String> idList = new ArrayList<>();

        participantList.forEach(participant -> {
            idList.add(participant.getParticipantId());
        });

        // 인증 파일 select
        List<Auth> authList = authRepository.getAuthFile(idList, noticeSeq, LocalDate.now());
        Map<String, List<String>> authInfoMap = new HashMap<>();

        // url, id 설정
        authList.forEach(auth -> {
            List<String> list;
            if (authInfoMap.containsKey(auth.getMember().getId())) {
                list = authInfoMap.get(auth.getMember().getId());
            } else {
                list = new ArrayList<>();
            }
            list.add(req.getURI().toString()
                    .substring(0, req.getURI().toString().length() - Long.toString(noticeSeq).length())
                    + "downloadFile"
                    + File.separator
                    + auth.getFilePath().substring(UPLOAD_FILE_PATH.length() + 1));
            authInfoMap.put(auth.getMember().getId(), list);
        });
        List<AuthInfoDto> authInfoList = new ArrayList<>();

        authInfoMap.forEach((key, value) -> {
            authInfoList.add(AuthInfoDto.builder()
                    .id(key)
                    .fileUrl(value)
                    .build());
        });

        return AuthDto.builder()
                .authInfo(authInfoList)
                .noticeSeq(noticeSeq)
                .build();
    }

    /**
     * 인증 파일 조회
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    @Override
    public byte[] downloadFile(String fileName) throws IOException {
        if (StringUtils.isBlank(fileName) || StringUtils.isEmpty(fileName)) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.notice.notfound.file.E0005",
                            null, Locale.KOREA));
        }

        InputStream in = new FileInputStream(UPLOAD_FILE_PATH + File.separator + fileName);
        return IOUtils.toByteArray(in);
    }

    /**
     * 인증 처리 (방장 권한)
     *
     * @param noticeSeq
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int setCertification(long noticeSeq, List<String> idList, String id) {
        List<Participant> participantList = participantRepository.findByNotice(Notice.builder()
                .noticeSeq(noticeSeq)
                .build());

        // 방장권한 확인
        if (participantList == null || !participantList.get(0).getMasterId().equals(id)) {
            throw new ChallengersException(HttpStatus.UNAUTHORIZED,
                    messageSource.getMessage("error.apply.master.conflict.E0017", null, Locale.KOREA));
        }

        int price = participantList.get(0).getNotice().getPrice();
        List<String> uncertifiedId = new ArrayList<>();
        List<String> fineList = new ArrayList<>();
        List<String> credits = new ArrayList<>();
        List<PointHistory> pointHistories = new ArrayList<>();

        // 미인증 사용자 id 세팅
        participantList.forEach(participant -> {
            if (participant.getParticipantType() == 0 && !idList.contains(participant.getParticipantId())) {
                uncertifiedId.add(participant.getParticipantId());

                Point point = pointRepository.findByMember(Member.builder()
                        .id(participant.getParticipantId())
                        .build());

                // 벌금 자동 이체 값 세팅
                if (point.getPoint() >= price) {
                    fineList.add(participant.getParticipantId());
                    pointHistories.add(PointHistory.builder()
                            .member(Member.builder()
                                    .id(participant.getParticipantId())
                                    .build())
                            .point(price)
                            .status(PointHistoryStatus.withdraw.ordinal())
                            .insertTime(LocalDateTime.now())
                            .build());
                } else { // 외상 id 세팅
                    credits.add(participant.getParticipantId());
                }
            }
        });
        // 벌금 자동이체
        if (pointHistories.size() > 0) {
            pointRepository.updateUserPoint(price, fineList);
            pointHistoryRepository.saveAll(pointHistories);
        }

        // 외상 추가
        if (credits.size() > 0)
            participantRepository.setCredit(credits, noticeSeq, price);

        return participantRepository.setCertification(uncertifiedId, noticeSeq);
    }
}
