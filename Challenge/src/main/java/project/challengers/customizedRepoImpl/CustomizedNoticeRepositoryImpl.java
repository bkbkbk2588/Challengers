package project.challengers.customizedRepoImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import project.challengers.DTO.notice.NoticeCreateDto;
import project.challengers.customizedRepo.CustomizedNoticeRepository;

import static project.challengers.entity.QNotice.notice;

public class CustomizedNoticeRepositoryImpl implements CustomizedNoticeRepository {

    final private JPAQueryFactory jpaQueryFactory;

    public CustomizedNoticeRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public int createNotice(NoticeCreateDto noticeCreate, String id) {
        //TODO execute에서 나오는 널포인터 수정필요
//        System.out.println(noticeCreate.getContent() + " " +
//                noticeCreate.getTitle() + " " + noticeCreate.getType() + " " + noticeCreate.getEndTime() + " " + noticeCreate.getStartTime() +
//                " " + noticeCreate.getPrice() + " " + noticeCreate.getMaxPeople() + " " + id);
        jpaQueryFactory.insert(notice)
                .columns(notice.content,notice.id,notice.endTime,notice.maxPeople
                ,notice.startTime, notice.title, notice.price, notice.type)
                .values(noticeCreate.getContent(),id,noticeCreate.getEndTime(),noticeCreate.getMaxPeople()
                ,noticeCreate.getStartTime(), noticeCreate.getTitle(), noticeCreate.getPrice(), noticeCreate.getType())
                .execute();
        return 1;
    }
}
