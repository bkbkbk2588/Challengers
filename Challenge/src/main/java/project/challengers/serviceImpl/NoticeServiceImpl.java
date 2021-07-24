package project.challengers.serviceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import project.challengers.DTO.notice.NoticeDTO;
import project.challengers.DTO.notice.NoticeListDTO;
import project.challengers.DTO.notice.SearchPagingDTO;
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
    public NoticeListDTO noticeList() {
        List<Notice> noticeList = noticeRepository.findAll();
        List<NoticeDTO> noticeDto = new ArrayList<>();

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
            NoticeDTO noticeDTO = null;
            BeanUtils.copyProperties(notice, noticeDTO);
            noticeDto.add(noticeDTO);
        }

        return NoticeListDTO.builder()
                .noticeList(noticeDto)
                .searchPaging(SearchPagingDTO.builder()
                        .offset(0L)
                        .size(noticeDto.size())
                        .totalCount(noticeDto.size())
                        .build())
                .build();
    }
}
