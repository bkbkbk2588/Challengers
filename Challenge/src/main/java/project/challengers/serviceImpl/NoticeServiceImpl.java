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
import project.challengers.component.FileComponent;
import project.challengers.entity.Notice;
import project.challengers.entity.NoticeFile;
import project.challengers.exception.ChallengersException;
import project.challengers.repository.NoticeFileRepository;
import project.challengers.repository.NoticeRepository;
import project.challengers.service.NoticeService;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

//    /**
//     *
//     * @param noticeList
//     * @return
//     */
//    private List<Notice> sortList(List<Notice> noticeList) {
//        Collections.sort(noticeList, (notice1, notice2) -> {
//            LocalDateTime time1 = notice1.getUpdateTime(),
//                    time2 = notice2.getUpdateTime();
//
//            if (time1 != null && time2 != null) {
//                if (time1.isBefore(time2))
//                    return -1;
//                else if (time1.isAfter(time2))
//                    return 0;
//            }
//            return (int) (notice2.getNoticeSeq() - notice1.getNoticeSeq());
//        });
//
//        return noticeList;
//    }

    /**
     * 게시글 전체 목록 조회
     * 최신글 또는 최신 업데이트된 게시글이 위로 나오게 설정
     *
     * @return
     */
    @Override
    public NoticeListDto noticeList() {
        //TODO order by 설정

        List<Notice> noticeList = noticeRepository.findAll();
        List<NoticeDto> noticeDto = new ArrayList<>();

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
     * 게시글 등록
     *
     * @param notice
     * @param filePartFlux
     * @param authentication
     * @return
     */
    @Transactional
    @Override
    public int createNotice(NoticeCreateDto notice, Flux<FilePart> filePartFlux, Authentication authentication) {
        // 제목
        if (StringUtils.isBlank(notice.getTitle())) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003",
                            new String[]{"제목"}, Locale.KOREA));
        }

        // 게시글 타입
        if (StringUtils.isBlank(notice.getType())) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003",
                            new String[]{"게시글 타입"}, Locale.KOREA));
        }

        // 최대 참여 인원
        if (notice.getMaxPeople() == null) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003",
                            new String[]{"최대 참여 인원"}, Locale.KOREA));
        }

        // 보증금
        if (notice.getType() == null) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003",
                            new String[]{"보증금"}, Locale.KOREA));
        }

        // 게시글 내용
        if (StringUtils.isBlank(notice.getType())) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003",
                            new String[]{"게시글 내용"}, Locale.KOREA));
        }

        // 도전 시작 날짜
        if (notice.getStartTime() == null) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003",
                            new String[]{"도전 시작날짜"}, Locale.KOREA));
        }

        // 도전 끝나는 날짜
        if (notice.getEndTime() == null) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003",
                            new String[]{"도전 끝나는 날짜"}, Locale.KOREA));
        }

        Notice result = noticeRepository.save(Notice.builder()
                .title(notice.getTitle())
                .id((String) authentication.getPrincipal())
                .type(notice.getType())
                .maxPeople(notice.getMaxPeople())
                .price(notice.getPrice())
                .content(notice.getContent())
                .startTime(notice.getStartTime())
                .endTime(notice.getEndTime())
                .build());

        if (filePartFlux != null) {
            // 첨부파일 경로, 이름 설정
            List<NoticeFile> noticeFiles = new ArrayList<>();
            fileComp.save(filePartFlux)
                    .subscribe(file -> {
                        noticeFiles.add(NoticeFile.builder()
                                .noticeSeq(result.getNoticeSeq())
                                .fileName(file.getLeft())
                                .filePath(file.getRight())
                                .build());
                    });
            // 첨부파일 입력
            List<NoticeFile> noticeFileResult = noticeFileRepository.saveAll(noticeFiles);
            return result != null && noticeFileResult != null ? 1 : 0;
        }

        return result != null ? 1 : 0;
    }

    /**
     * 게시글 페이지 조회
     *
     * @param paging
     * @return
     */
    @Override
    public NoticeListDto noticePagingList(SearchPagingDto paging) {

        return null;
    }


    /**
     * 게시글 상세 조회
     *
     * @param noticeSeq
     * @return
     */
    @Override
    public NoticeInfoDto getNotice(long noticeSeq, ServerHttpRequest req) {
        List<Tuple> noticeTuple = noticeRepository.getNotice(noticeSeq);
        Notice notice = noticeTuple.get(0).get(0, Notice.class);

        List<NoticeFile> noticeFile = new ArrayList<>();
        noticeTuple.stream().forEach(tuple -> {
            noticeFile.add(tuple.get(1, NoticeFile.class));
        });

        List<String> fileDownloadUri = new ArrayList<>();

        noticeFile.forEach(file -> {
            fileDownloadUri.add(req.getURI().toString()
                    .substring(0, req.getURI().toString().length() - Long.toString(noticeSeq).length())
                    + "downloadFile"
                    + File.separator
                    + file.getFilePath().substring(UPLOAD_FILE_PATH.length() + 1));
        });

        return NoticeInfoDto.builder()
                .title(notice.getTitle())
                .type(notice.getType())
                .maxPeople(notice.getMaxPeople())
                .price(notice.getPrice())
                .content(notice.getContent())
                .id(notice.getId())
                .startTime(notice.getStartTime())
                .endTime(notice.getEndTime())
                .fileUrl(fileDownloadUri)
                .build();
    }

    /**
     * 이미지 파일 조회
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
}
