package project.challengers.serviceImpl;

import com.querydsl.core.Tuple;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.challengers.DTO.notice.*;
import project.challengers.base.ChallengeStatus;
import project.challengers.base.ParticipantType;
import project.challengers.base.PointHistoryStatus;
import project.challengers.component.FileComponent;
import project.challengers.entity.*;
import project.challengers.exception.ChallengersException;
import project.challengers.repository.*;
import project.challengers.service.NoticeService;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class NoticeServiceImpl implements NoticeService {
    Logger logger = LoggerFactory.getLogger(NoticeServiceImpl.class);

    @Autowired
    NoticeRepository noticeRepository;

    @Value("${challengers.path.prefix}${challengers.path.files}")
    private String UPLOAD_FILE_PATH;

    @Autowired
    MessageSource messageSource;

    @Autowired
    FileComponent fileComp;

    @Autowired
    NoticeFileRepository noticeFileRepository;

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    PointRepository pointRepository;

    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Autowired
    ParticipantRepository participantRepository;

    /**
     * ????????? ?????? ?????? ??????
     * ????????? ?????? ?????? ??????????????? ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @Override
    public NoticeListDto noticeList() {
        List<Notice> noticeList = noticeRepository.findNoticeAll();
        List<NoticeDto> noticeDto = new ArrayList<>();

        // ???????????? ?????? ??????
        if (noticeList.size() == 0) {
            throw new ChallengersException(HttpStatus.NOT_FOUND,
                    messageSource.getMessage("error.notice.notfound.noticeList.E0006", null, Locale.KOREA));
        }

        for (Notice notice : noticeList) {
            NoticeDto noticeDTO = new NoticeDto();
            BeanUtils.copyProperties(notice, noticeDTO);
            noticeDto.add(noticeDTO);
        }

        return NoticeListDto.builder()
                .noticeList(noticeDto)
                .searchPaging(SearchPagingDto.builder()
                        .offset(0L)
                        .size((long) noticeDto.size())
                        .totalCount(noticeDto.size())
                        .build())
                .build();
    }

    /**
     * ????????? ?????? (????????? ????????? ????????? ????????? ??????)
     *
     * @param notice
     * @param filePartFlux
     * @param authentication
     * @return
     */
    @Transactional
    @Override
    public int createNotice(NoticeCreateDto notice, Flux<FilePart> filePartFlux, Authentication authentication) {
        // ??????
        if (StringUtils.isBlank(notice.getTitle())) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003",
                            new String[]{"??????"}, Locale.KOREA));
        }

        // ????????? ??????
        if (StringUtils.isBlank(notice.getType())) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003",
                            new String[]{"????????? ??????"}, Locale.KOREA));
        }

        // ?????? ?????? ??????
        if (notice.getMaxPeople() == null) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003",
                            new String[]{"?????? ?????? ??????"}, Locale.KOREA));
        }

        // ?????????
        if (notice.getType() == null) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003",
                            new String[]{"?????????"}, Locale.KOREA));
        }

        // ????????? ??????
        if (StringUtils.isBlank(notice.getContent())) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003",
                            new String[]{"????????? ??????"}, Locale.KOREA));
        }

        // ?????? ?????? ??????
        if (notice.getStartTime() == null) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003",
                            new String[]{"?????? ????????????"}, Locale.KOREA));
        }

        // ?????? ????????? ??????
        if (notice.getEndTime() == null) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003",
                            new String[]{"?????? ????????? ??????"}, Locale.KOREA));
        }

        Notice result = noticeRepository.save(Notice.builder()
                .title(notice.getTitle())
                .member(Member.builder()
                        .id((String) authentication.getPrincipal())
                        .build())
                .type(notice.getType())
                .maxPeople(notice.getMaxPeople())
                .price(notice.getPrice())
                .content(notice.getContent())
                .startTime(notice.getStartTime())
                .endTime(notice.getEndTime())
                .build());

        // ????????? ????????? ????????? ????????? ??????
        Point pointEntity = pointRepository.findByMember(Member.builder()
                .id((String) authentication.getPrincipal()).build());

        // ?????? ????????? ??? ?????? ??????
        if (pointEntity == null || notice.getPrice() > pointEntity.getPoint()) {
            throw new ChallengersException(HttpStatus.CONFLICT,
                    messageSource.getMessage("error.point.withdraw.big.E0013", null, Locale.KOREA));
        }

        pointRepository.updatePoint(pointEntity.getPoint() - notice.getPrice(),
                (String) authentication.getPrincipal());

        // ?????? ??????
        pointHistoryRepository.save(PointHistory.builder()
                .member(Member.builder()
                        .id((String) authentication.getPrincipal())
                        .build())
                .point(notice.getPrice())
                .status(PointHistoryStatus.withdraw.ordinal())
                .insertTime(LocalDateTime.now())
                .build());

        participantRepository.save(Participant.builder()
                .participantId((String) authentication.getPrincipal())
                .masterId((String) authentication.getPrincipal())
                .notice(Notice.builder()
                        .noticeSeq(result.getNoticeSeq())
                        .build())
                .participantType(ParticipantType.normal.ordinal())
                .credit(0)
                .warning(0)
                .build());

        challengeRepository.save(Challenge.builder()
                .notice(Notice.builder()
                        .noticeSeq(result.getNoticeSeq())
                        .build())
                .money(notice.getPrice())
                .status(ChallengeStatus.startBefore.ordinal())
                .build());

        if (filePartFlux != null) {
            // ???????????? ??????, ?????? ??????
            List<NoticeFile> noticeFiles = new ArrayList<>();
            fileComp.save(filePartFlux)
                    .subscribe(file -> {
                        noticeFiles.add(NoticeFile.builder()
                                .notice(Notice.builder()
                                        .noticeSeq(result.getNoticeSeq())
                                        .build())
                                .fileName(file.getLeft())
                                .filePath(file.getRight())
                                .build());
                    });
            // ???????????? ??????
            List<NoticeFile> noticeFileResult = noticeFileRepository.saveAll(noticeFiles);
            return result != null && noticeFileResult != null ? 1 : 0;
        }

        return result != null ? 1 : 0;
    }

    /**
     * ????????? ????????? ??????
     * size, offset ?????? ?????? default ????????? size : 10, offset : 0
     *
     * @param paging
     * @return
     */
    @Override
    public NoticeListDto noticePagingList(SearchPagingDto paging) {
        // ????????? ?????? ?????? ??????
        if (paging.getSize() == null) {
            paging.setSize(10L);
        }

        // offset ?????? ?????? ??????
        if (paging.getOffset() == null) {
            paging.setOffset(0L);
        }

        List<Notice> noticeList = noticeRepository.noticePagingList(paging);
        List<NoticeDto> noticeDto = new ArrayList<>();

        // ???????????? ?????? ??????
        if (noticeList.size() == 0) {
            throw new ChallengersException(HttpStatus.NOT_FOUND,
                    messageSource.getMessage("error.notice.notfound.noticeList.E0006", null, Locale.KOREA));
        }

        noticeList.forEach(notice -> {
            noticeDto.add(NoticeDto.builder()
                    .noticeSeq(notice.getNoticeSeq())
                    .title(notice.getTitle())
                    .id(notice.getMember().getId())
                    .type(notice.getType())
                    .maxPeople(notice.getMaxPeople())
                    .price(notice.getPrice())
                    .content(notice.getContent())
                    .startTime(notice.getStartTime())
                    .endTime(notice.getEndTime())
                    .updateTime(notice.getUpdateTime())
                    .build());
        });

        return NoticeListDto.builder()
                .searchPaging(SearchPagingDto.builder()
                        .offset(paging.getOffset())
                        .size(paging.getSize())
                        .totalCount(noticeDto.size())
                        .build())
                .noticeList(noticeDto)
                .build();
    }


    /**
     * ????????? ?????? ??????
     *
     * @param noticeSeq
     * @return
     */
    @Override
    public NoticeInfoDto getNotice(long noticeSeq, ServerHttpRequest req) {
        List<Tuple> noticeTuple = noticeRepository.getNotice(noticeSeq);

        // ????????? ?????? ??????
        if (noticeTuple.size() == 0) {
            throw new ChallengersException(HttpStatus.NOT_FOUND,
                    messageSource.getMessage("error.notice.notfound.noticeList.E0006", null, Locale.KOREA));
        }

        Notice notice = noticeTuple.get(0).get(0, Notice.class);

        List<NoticeFile> noticeFile = new ArrayList<>();
        noticeTuple.stream().forEach(tuple -> {
            noticeFile.add(tuple.get(1, NoticeFile.class));
        });

        List<String> fileDownloadUri = new ArrayList<>();

        if (noticeFile.size() != 0 && noticeFile.get(0) != null) {
            noticeFile.forEach(file -> {
                fileDownloadUri.add(req.getURI().toString()
                        .substring(0, req.getURI().toString().length() - Long.toString(noticeSeq).length())
                        + "downloadFile"
                        + File.separator
                        + file.getFilePath().substring(UPLOAD_FILE_PATH.length() + 1));
            });
        }

        return NoticeInfoDto.builder()
                .title(notice.getTitle())
                .type(notice.getType())
                .maxPeople(notice.getMaxPeople())
                .price(notice.getPrice())
                .content(notice.getContent())
                .id(notice.getMember().getId())
                .startTime(notice.getStartTime())
                .endTime(notice.getEndTime())
                .fileUrl(fileDownloadUri)
                .build();
    }

    /**
     * ????????? ?????? ??????
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
     * ????????? ?????? Like ??????
     * size, offset ?????? ?????? default ????????? size : 10, offset : 0
     *
     * @param title
     * @return
     */
    @Override
    public NoticeListDto noticeSearchTitle(String title, SearchPagingDto paging) {

        // ????????? ?????? ??????
        if (StringUtils.isBlank(title) || StringUtils.isEmpty(title)) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.notice.notfound.noticeTitle.E0007", null, Locale.KOREA));
        }

        // ????????? ?????? ?????? ??????
        if (paging.getSize() == null) {
            paging.setSize(10L);
        }

        // offset ?????? ?????? ??????
        if (paging.getOffset() == null) {
            paging.setOffset(0L);
        }

        List<Notice> noticeList = noticeRepository.noticeSearchTitle(title, paging);
        List<NoticeDto> noticeDto = new ArrayList<>();

        // ???????????? ?????? ??????
        if (noticeList.size() == 0) {
            throw new ChallengersException(HttpStatus.NOT_FOUND,
                    messageSource.getMessage("error.notice.notfound.noticeList.E0006", null, Locale.KOREA));

        }

        noticeList.forEach(notice -> {
            noticeDto.add(NoticeDto.builder()
                    .noticeSeq(notice.getNoticeSeq())
                    .title(notice.getTitle())
                    .id(notice.getMember().getId())
                    .type(notice.getType())
                    .maxPeople(notice.getMaxPeople())
                    .price(notice.getPrice())
                    .content(notice.getContent())
                    .startTime(notice.getStartTime())
                    .endTime(notice.getEndTime())
                    .updateTime(notice.getUpdateTime())
                    .build());
        });

        return NoticeListDto.builder()
                .searchPaging(SearchPagingDto.builder()
                        .offset(paging.getOffset())
                        .size(paging.getSize())
                        .totalCount(noticeList.size())
                        .build())
                .noticeList(noticeDto)
                .build();
    }

    /**
     * ????????? ?????? Like ??????
     * size, offset ?????? ?????? default ????????? size : 10, offset : 0
     *
     * @param content
     * @return
     */
    @Override
    public NoticeListDto noticeSearchContent(String content, SearchPagingDto paging) {
        // ????????? ?????? ??????
        if (StringUtils.isBlank(content) || StringUtils.isEmpty(content)) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.notice.notfound.noticeContent.E0008", null, Locale.KOREA));
        }

        // ????????? ?????? ?????? ??????
        if (paging.getSize() == null) {
            paging.setSize(10L);
        }

        // offset ?????? ?????? ??????
        if (paging.getOffset() == null) {
            paging.setOffset(0L);
        }

        List<Notice> noticeList = noticeRepository.noticeSearchContent(content, paging);
        List<NoticeDto> noticeDto = new ArrayList<>();

        // ???????????? ?????? ??????
        if (noticeList.size() == 0) {
            throw new ChallengersException(HttpStatus.NOT_FOUND,
                    messageSource.getMessage("error.notice.notfound.noticeList.E0006", null, Locale.KOREA));

        }

        noticeList.forEach(notice -> {
            noticeDto.add(NoticeDto.builder()
                    .noticeSeq(notice.getNoticeSeq())
                    .title(notice.getTitle())
                    .id(notice.getMember().getId())
                    .type(notice.getType())
                    .maxPeople(notice.getMaxPeople())
                    .price(notice.getPrice())
                    .content(notice.getContent())
                    .startTime(notice.getStartTime())
                    .endTime(notice.getEndTime())
                    .updateTime(notice.getUpdateTime())
                    .build());
        });

        return NoticeListDto.builder()
                .searchPaging(SearchPagingDto.builder()
                        .offset(paging.getOffset())
                        .size(paging.getSize())
                        .totalCount(noticeList.size())
                        .build())
                .noticeList(noticeDto)
                .build();
    }

    /**
     * ????????? ??????
     *
     * @param noticeSeq
     * @param authentication
     * @return
     */
    @Transactional
    @Override
    public int deleteNotice(long noticeSeq, Authentication authentication) {
        Challenge challenge = challengeRepository.findById(noticeSeq).get();

        // ???????????? ???????????? ???????????? ??????
        if (challenge.getMoney() > 0) {
            throw new ChallengersException(HttpStatus.CONFLICT,
                    messageSource.getMessage("error.notice.delete.money.conflict.E0018", null, Locale.KOREA));
        }
        checkNotice(noticeSeq, authentication);
        List<NoticeFile> noticeFileList = noticeFileRepository.findByNotice(Notice.builder().noticeSeq(noticeSeq).build());

        List<String> filePath = new ArrayList<>();
        noticeFileList.forEach(noticeFile -> filePath.add(noticeFile.getFilePath()));
        fileComp.delete(Flux.fromIterable(filePath)).subscribe();

        List<Participant> participantList = participantRepository.findByNotice(Notice.builder()
                .noticeSeq(noticeSeq).build());
        List<String> idList = new ArrayList<>();
        participantList.forEach(participant -> {
            idList.add(participant.getParticipantId());
        });
        participantRepository.updateType(idList, noticeSeq);

        return noticeRepository.deleteNotice(noticeSeq);
    }

    /**
     * ????????? ??????
     *
     * @param notice         ????????? ?????? ??????
     * @param fileSeq        ????????? ?????? ??????
     * @param filePartFlux   ?????? ????????? ??????
     * @param authentication
     * @return
     */
    @Transactional
    @Override
    public int updateNotice(NoticeUpdateDto notice, Flux<String> fileSeq, Flux<FilePart> filePartFlux, Authentication authentication) {
        // ????????? ????????? ?????? ??????
        if (notice.getNoticeSeq() == 0) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.notice.notfound.seq.E0011"
                            , null, Locale.KOREA));
        }
        checkNotice(notice.getNoticeSeq(), authentication);

        // ????????? ????????? ?????? ?????? ?????? ??????
        if (fileSeq != null) {
            List<Long> fileSeqList = new ArrayList<>();
            fileSeq.subscribe(seq -> {
                fileSeqList.add(Long.parseLong(seq));
            });
            List<String> filePathList = noticeRepository.findByFilePath(fileSeqList);

            fileComp.delete(Flux.fromIterable(filePathList)).subscribe(); // ?????? ??????
            noticeRepository.deleteFile(fileSeqList); // DB delete
        }

        // ????????? ????????? ?????? ?????? ?????? ??????
        if (filePartFlux != null) {
            // ???????????? ??????, ?????? ??????
            List<NoticeFile> noticeFiles = new ArrayList<>();
            fileComp.save(filePartFlux)
                    .subscribe(file -> {
                        noticeFiles.add(NoticeFile.builder()
                                .notice(Notice.builder()
                                        .noticeSeq(notice.getNoticeSeq())
                                        .build())
                                .fileName(file.getLeft())
                                .filePath(file.getRight())
                                .build());
                    });
            // ???????????? ??????
            noticeFileRepository.saveAll(noticeFiles);
        }
        return noticeRepository.updateNotice(notice);
    }

    /**
     * ????????? ??????????????? ????????? ????????? ??????????????? ??????
     *
     * @param noticeSeq
     * @param authentication
     */
    private Notice checkNotice(long noticeSeq, Authentication authentication) {
        Notice notice = noticeRepository.findById(noticeSeq).orElseThrow(() -> {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.notice.notfound.E0010"
                            , new String[]{Long.toString(noticeSeq)}, Locale.KOREA));
        });

        // ????????? ??????????????? ??????
        if (!notice.getMember().getId().equals(authentication.getPrincipal())) {
            throw new ChallengersException(HttpStatus.FORBIDDEN,
                    messageSource.getMessage("error.notice.identification.fail.E0009", null,
                            Locale.KOREA));
        }
        return notice;
    }
}
