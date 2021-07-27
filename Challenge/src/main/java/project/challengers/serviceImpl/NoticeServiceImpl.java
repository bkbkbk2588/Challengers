package project.challengers.serviceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.challengers.DTO.notice.NoticeCreateDto;
import project.challengers.DTO.notice.NoticeDto;
import project.challengers.DTO.notice.NoticeListDto;
import project.challengers.DTO.notice.SearchPagingDto;
import project.challengers.entity.Member;
import project.challengers.entity.Notice;
import project.challengers.repository.NoticeRepository;
import project.challengers.service.NoticeService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    NoticeRepository noticeRepository;

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
                        .size(noticeDto.size())
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
        Member userDetails = (Member) authentication.getPrincipal();

        return noticeRepository.createNotice(notice, userDetails.getId());
    }
}
