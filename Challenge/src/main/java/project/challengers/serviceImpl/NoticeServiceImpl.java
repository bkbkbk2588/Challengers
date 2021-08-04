package project.challengers.serviceImpl;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.challengers.DTO.notice.*;
import project.challengers.entity.Member;
import project.challengers.entity.Notice;
import project.challengers.exception.ChallengersException;
import project.challengers.repository.MemberRepository;
import project.challengers.repository.NoticeRepository;
import project.challengers.service.NoticeService;
import reactor.core.publisher.Flux;

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
     * 첨부파일 없는 게시글 등록
     *
     * @param notice
     * @param authentication
     * @return
     */
    @Transactional
    @Override
    public int createNotice(NoticeCreateDto notice, Authentication authentication) {
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

        Member member = memberRepository.findById((String) authentication.getPrincipal()).get();
        Notice result = noticeRepository.save(Notice.builder()
                .title(notice.getTitle())
                .id(member.getId())
                .type(notice.getType())
                .maxPeople(notice.getMaxPeople())
                .price(notice.getPrice())
                .content(notice.getContent())
                .startTime(notice.getStartTime())
                .endTime(notice.getEndTime())
                .build());

        return result != null ? 1 : 0;
    }

    // TODO 첨부파일 기능 구현 필요
    /**
     * 첨부파일 있는 게시글 등록
     *
     * @param notice
     * @param authentication
     * @return
     */
    @Override
    public int createFileNotice(FileNoticeCreateDto notice, Flux<FilePart> filePartFlux, Authentication authentication) {
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

        // 이미지 경로
        if (StringUtils.isBlank(notice.getImagePath())) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003",
                            new String[]{"이미지 경로"}, Locale.KOREA));
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

        Member member = memberRepository.findById((String) authentication.getPrincipal()).get();
        Notice result = noticeRepository.save(Notice.builder()
                .title(notice.getTitle())
                .id(member.getId())
                .type(notice.getType())
                .maxPeople(notice.getMaxPeople())
                .price(notice.getPrice())
                .content(notice.getContent())
                .startTime(notice.getStartTime())
                .endTime(notice.getEndTime())
                .build());

        return result != null ? 1 : 0;
    }

    @Override
    public NoticeListDto noticePagingList(SearchPagingDto paging) {

        return null;
    }

}
