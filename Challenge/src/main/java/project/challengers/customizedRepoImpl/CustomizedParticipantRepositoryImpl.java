package project.challengers.customizedRepoImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomizedParticipantRepositoryImpl {
    final private JPAQueryFactory jpaQueryFactory;

    public CustomizedParticipantRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }
}
