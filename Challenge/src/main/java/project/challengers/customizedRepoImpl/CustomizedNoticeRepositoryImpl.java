package project.challengers.customizedRepoImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import project.challengers.DTO.notice.NoticeDTO;
import project.challengers.customizedRepo.CustomizedNoticeRepository;

import java.util.List;

import static project.challengers.entity.QNotice.notice;

public class CustomizedNoticeRepositoryImpl implements CustomizedNoticeRepository {

    final private JPAQueryFactory jpaQueryFactory;

    public CustomizedNoticeRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }
}
