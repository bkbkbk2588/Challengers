package project.challengers.customizedRepoImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import project.challengers.customizedRepo.CustomizedAuthRepository;
import project.challengers.entity.Auth;

import java.util.Date;
import java.util.List;

import static project.challengers.entity.QAuth.auth;

public class CustomizedAuthRepositoryImpl implements CustomizedAuthRepository {
    final private JPAQueryFactory jpaQueryFactory;

    public CustomizedAuthRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Auth> getAuthFile(List<String> idList, long noticeSeq, Date date) {
        return jpaQueryFactory.selectFrom(auth)
                .where(auth.member.id.in(idList)
                        .and(auth.notice.noticeSeq.eq(noticeSeq))
                        .and(auth.authDate.eq(date)))
                .fetch();
    }
}
