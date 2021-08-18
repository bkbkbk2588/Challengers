package project.challengers.customizedRepoImpl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import project.challengers.DTO.notice.NoticeInfoDto;
import project.challengers.DTO.notice.NoticeListDto;
import project.challengers.DTO.notice.SearchPagingDto;
import project.challengers.customizedRepo.CustomizedNoticeRepository;
import project.challengers.entity.Notice;

import java.util.List;

import static project.challengers.entity.QNotice.notice;
import static project.challengers.entity.QNoticeFile.noticeFile;

public class CustomizedNoticeRepositoryImpl implements CustomizedNoticeRepository {

    final private JPAQueryFactory jpaQueryFactory;

    public CustomizedNoticeRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public NoticeListDto noticePagingList(SearchPagingDto paging) {
        List<Notice> noticeList = jpaQueryFactory.selectFrom(notice)
                .limit(paging.getSize())
                .offset(paging.getOffset())
                .fetch();
        return null;
    }

    @Override
    public List<Tuple> getNotice(long noticeSeq) {
        return jpaQueryFactory.select(notice, noticeFile)
                .from(notice).rightJoin(noticeFile)
                .on(notice.noticeSeq.eq(noticeFile.noticeSeq))
                .where(notice.noticeSeq.eq(noticeSeq)).fetch();
    }
}
