package project.challengers.serviceImpl;

import com.querydsl.core.Tuple;
import org.apache.commons.io.IOUtils;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.challengers.DTO.notice.*;
import project.challengers.component.FileComponent;
import project.challengers.entity.Notice;
import project.challengers.entity.NoticeFile;
import project.challengers.exception.ChallengersException;
import project.challengers.repository.MemberRepository;
import project.challengers.repository.NoticeFileRepository;
import project.challengers.repository.NoticeRepository;
import project.challengers.service.NoticeService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MessageSource messageSource;

    @Autowired
    FileComponent fileComp;

    @Autowired
    NoticeFileRepository noticeFileRepository;

    /**
     * 게시글 전체 목록 조회
     * 최신글 또는 최신 업데이트된 게시글이 위로 나오게 설정
     *
     * @return
     */
    @Override
    public NoticeListDto noticeList() {
        List<Notice> noticeList = noticeRepository.findAll();
        List<NoticeDto> noticeDto = new ArrayList<>();

        Collections.sort(noticeList, (notice1, notice2) -> {
            LocalDateTime time1 = notice1.getUpdateTime(),
                    time2 = notice2.getUpdateTime();
            if (time1 != null && time2 != null) {
                if (time1.isBefore(time2))
                    return -1;
                else if (time1.isAfter(time2))
                    return 0;
            }
            return (int) (notice2.getNoticeSeq() - notice1.getNoticeSeq());
        });

        for (Notice notice : noticeList) {
            NoticeDto noticeDTO = null;
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
    public List<byte[]> getNotice(long noticeSeq, ServerHttpResponse res) throws IOException {
        List<Tuple> noticeTuple = noticeRepository.getNotice(noticeSeq);
        Notice notice = noticeTuple.get(0).get(0, Notice.class);

        List<NoticeFile> noticeFile = new ArrayList<>();
        noticeTuple.stream().forEach(tuple -> {
            noticeFile.add(tuple.get(1, NoticeFile.class));
        });
        List<byte[]> list = new ArrayList<>();
        for (NoticeFile file : noticeFile) {
//            HttpHeaders headers = res.getHeaders();
//            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFileName());
//            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//            headers.setContentDispositionFormData(file.getFileName(), file.getFileName());
//            Mono<Void> result = res.writeWith(fileComp.download(file.getFilePath()));
            System.out.println(file.getFilePath());
            InputStream in = new FileInputStream(file.getFilePath());
//                    getClass().getResourceAsStream(file.getFilePath());
            if (in == null)
                System.out.println("!!!!!!!!!!!!");
            list.add(IOUtils.toByteArray(in));
//            System.out.println(fileComp.download(file.getFilePath()));
        }

        return list;
    }

}
