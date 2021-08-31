package project.challengers.customizedRepoImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import project.challengers.customizedRepo.CustomizedChallengeRepository;

import static project.challengers.entity.QChallenge.challenge;

public class CustomizedChallengeRepositoryImpl implements CustomizedChallengeRepository {
    final private JPAQueryFactory jpaQueryFactory;

    public CustomizedChallengeRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public int updateMoney(long noticeSeq, int money) {
        return (int) jpaQueryFactory.update(challenge)
                .set(challenge.money, money)
                .where(challenge.notice.noticeSeq.eq(noticeSeq))
                .execute();
    }
}
