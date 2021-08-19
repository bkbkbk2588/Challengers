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
    public List<Notice> findNoticeAll() {
        return jpaQueryFactory.selectFrom(notice)
                .orderBy(notice.updateTime.desc())
                .orderBy(notice.noticeSeq.desc())
                .fetch();
    }

    @Override
    public List<Notice> noticePagingList(SearchPagingDto paging) {
        return jpaQueryFactory.selectFrom(notice)
                .orderBy(notice.updateTime.desc())
                .orderBy(notice.noticeSeq.desc())
                .limit(paging.getSize())
                .offset(paging.getOffset())
                .fetch();
    }

    @Override
    public List<Tuple> getNotice(long noticeSeq) {
        return jpaQueryFactory.select(notice, noticeFile)
                .from(notice).rightJoin(noticeFile)
                .on(notice.noticeSeq.eq(noticeFile.noticeSeq))
                .where(notice.noticeSeq.eq(noticeSeq)).fetch();
    }

    @Override
    public List<Notice> noticeSearchTitle(String title, SearchPagingDto paging) {
        return jpaQueryFactory.selectFrom(notice)
                .where(notice.title.contains(title))
                .orderBy(notice.updateTime.desc())
                .orderBy(notice.noticeSeq.desc())
                .offset(paging.getOffset())
                .limit(paging.getSize())
                .fetch();
    }

    @Override
    public List<Notice> noticeSearchContent(String content, SearchPagingDto paging) {
        return jpaQueryFactory.selectFrom(notice)
                .where(notice.content.contains(content))
                .orderBy(notice.updateTime.desc())
                .orderBy(notice.noticeSeq.desc())
                .offset(paging.getOffset())
                .limit(paging.getSize())
                .fetch();
    }

    @Override
    public int deleteNotice(long noticeSeq) {
        return (int) jpaQueryFactory.delete(notice)
                .where(notice.noticeSeq.eq(noticeSeq))
                .execute();
    }

    @Override
    public int deleteNoticeFile(long noticeSeq) {
        return 1;
    }
}
