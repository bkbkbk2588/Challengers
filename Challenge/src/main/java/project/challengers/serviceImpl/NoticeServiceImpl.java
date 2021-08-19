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

    /**
     * 게시글 전체 목록 조회
     * 최신글 또는 최신 업데이트된 게시글이 위로 나오게 설정
     *
     * @return
     */
    @Override
    public NoticeListDto noticeList() {
        List<Notice> noticeList = noticeRepository.findNoticeAll();
        List<NoticeDto> noticeDto = new ArrayList<>();

        // 게시글이 없을 경우
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
        if (StringUtils.isBlank(notice.getContent())) {
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
     * size, offset 없을 경우 default 값으로 size : 10, offset : 0
     *
     * @param paging
     * @return
     */
    @Override
    public NoticeListDto noticePagingList(SearchPagingDto paging) {
        // 사이즈 입력 없을 경우
        if (paging.getSize() == null) {
            paging.setSize(10L);
        }

        // offset 입력 없을 경우
        if (paging.getOffset() == null) {
            paging.setOffset(0L);
        }

        List<Notice> noticeList = noticeRepository.noticePagingList(paging);
        List<NoticeDto> noticeDto = new ArrayList<>();

        // 게시글이 없을 경우
        if (noticeList.size() == 0) {
            throw new ChallengersException(HttpStatus.NOT_FOUND,
                    messageSource.getMessage("error.notice.notfound.noticeList.E0006", null, Locale.KOREA));
        }

        noticeList.forEach(notice -> {
            noticeDto.add(NoticeDto.builder()
                    .noticeSeq(notice.getNoticeSeq())
                    .title(notice.getTitle())
                    .id(notice.getId())
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

    /**
     * 게시글 제목 Like 검색
     * size, offset 없을 경우 default 값으로 size : 10, offset : 0
     *
     * @param title
     * @return
     */
    @Override
    public NoticeListDto noticeSearchTitle(String title, SearchPagingDto paging) {

        // 제목이 없을 경우
        if (StringUtils.isBlank(title) || StringUtils.isEmpty(title)) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.notice.notfound.noticeTitle.E0007", null, Locale.KOREA));
        }

        // 사이즈 입력 없을 경우
        if (paging.getSize() == null) {
            paging.setSize(10L);
        }

        // offset 입력 없을 경우
        if (paging.getOffset() == null) {
            paging.setOffset(0L);
        }

        List<Notice> noticeList = noticeRepository.noticeSearchTitle(title, paging);
        List<NoticeDto> noticeDto = new ArrayList<>();

        // 게시글이 없을 경우
        if (noticeList.size() == 0) {
            throw new ChallengersException(HttpStatus.NOT_FOUND,
                    messageSource.getMessage("error.notice.notfound.noticeList.E0006", null, Locale.KOREA));

        }

        noticeList.forEach(notice -> {
            noticeDto.add(NoticeDto.builder()
                    .noticeSeq(notice.getNoticeSeq())
                    .title(notice.getTitle())
                    .id(notice.getId())
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
     * 게시글 내용 Like 검색
     * size, offset 없을 경우 default 값으로 size : 10, offset : 0
     *
     * @param content
     * @return
     */
    @Override
    public NoticeListDto noticeSearchContent(String content, SearchPagingDto paging) {
        // 내용이 없을 경우
        if (StringUtils.isBlank(content) || StringUtils.isEmpty(content)) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.notice.notfound.noticeContent.E0008", null, Locale.KOREA));
        }

        // 사이즈 입력 없을 경우
        if (paging.getSize() == null) {
            paging.setSize(10L);
        }

        // offset 입력 없을 경우
        if (paging.getOffset() == null) {
            paging.setOffset(0L);
        }

        List<Notice> noticeList = noticeRepository.noticeSearchContent(content, paging);
        List<NoticeDto> noticeDto = new ArrayList<>();

        // 게시글이 없을 경우
        if (noticeList.size() == 0) {
            throw new ChallengersException(HttpStatus.NOT_FOUND,
                    messageSource.getMessage("error.notice.notfound.noticeList.E0006", null, Locale.KOREA));

        }

        noticeList.forEach(notice -> {
            noticeDto.add(NoticeDto.builder()
                    .noticeSeq(notice.getNoticeSeq())
                    .title(notice.getTitle())
                    .id(notice.getId())
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
     * 게시글 삭제
     *
     * @param noticeSeq
     * @param authentication
     * @return
     */
    @Override
    public int deleteNotice(long noticeSeq, Authentication authentication) {
        Notice notice = noticeRepository.findById(noticeSeq).orElseThrow(() -> {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.notice.notfound.E0010"
                            , new String[]{Long.toString(noticeSeq)}, Locale.KOREA));
        });

        // 본인의 게시글인지 확인
        if (!notice.getId().equals(authentication.getPrincipal())) {
            throw new ChallengersException(HttpStatus.FORBIDDEN,
                    messageSource.getMessage("error.notice.identification.fail.E0009", null,
                            Locale.KOREA));
        }

        List<NoticeFile> noticeFileList = noticeFileRepository.findByNoticeSeq(noticeSeq);

        // 게시글 이미지 파일이 없을 경우
        if (noticeFileList.size() == 0) {
            return noticeRepository.deleteNotice(noticeSeq);
        }

        List<String> filePath = new ArrayList<>();
        noticeFileList.forEach(noticeFile -> filePath.add(noticeFile.getFilePath()));
        fileComp.delete(Flux.fromIterable(filePath)).subscribe();

        // TODO notice, noticeFile delete
        return 0;
    }
}
