package project.challengers.customizedRepoImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import project.challengers.customizedRepo.CustomizedMemberRepository;

public class CustomizedMemberRepositoryImpl implements CustomizedMemberRepository {
    final private JPAQueryFactory jpaQueryFactory;

    public CustomizedMemberRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public String findByMemberId(String id) {
//      jpaQueryFactory.select(Projections.fields(Member.class))
        return null;
    }
}
