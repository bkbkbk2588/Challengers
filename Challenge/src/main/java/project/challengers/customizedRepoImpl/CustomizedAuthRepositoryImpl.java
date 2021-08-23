package project.challengers.customizedRepoImpl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import project.challengers.customizedRepo.CustomizedAuthRepository;
import project.challengers.entity.Auth;

import static project.challengers.entity.QAuth.auth;

import java.util.List;

public class CustomizedAuthRepositoryImpl implements CustomizedAuthRepository {
    final private JPAQueryFactory jpaQueryFactory;

    public CustomizedAuthRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Auth> getAuthFile(List<String> idList, long noticeSeq) {
        return jpaQueryFactory.selectFrom(auth)
                .where(auth.id.in(idList)
                        .and(auth.noticeSeq.eq(noticeSeq)))
                .fetch();
    }
}
