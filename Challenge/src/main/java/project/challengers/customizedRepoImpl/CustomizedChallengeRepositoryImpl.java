package project.challengers.customizedRepoImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import project.challengers.base.ChallengeStatus;
import project.challengers.customizedRepo.CustomizedChallengeRepository;
import project.challengers.entity.Challenge;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public int startChallenge(long noticeSeq) {
        return (int) jpaQueryFactory.update(challenge)
                .set(challenge.status, ChallengeStatus.progress.ordinal())
                .where(challenge.notice.noticeSeq.eq(noticeSeq))
                .execute();
    }

    @Override
    public int startChallengeList(List<Long> noticeSeq) {
        return (int) jpaQueryFactory.update(challenge)
                .set(challenge.status, ChallengeStatus.progress.ordinal())
                .where(challenge.notice.noticeSeq.in(noticeSeq))
                .execute();
    }

    @Override
    public int stopChallenge(long noticeSeq) {
        return (int) jpaQueryFactory.update(challenge)
                .set(challenge.status, ChallengeStatus.end.ordinal())
                .where(challenge.notice.noticeSeq.eq(noticeSeq))
                .execute();
    }

    @Override
    public int stopChallengeList(List<Long> noticeSeq) {
        return (int) jpaQueryFactory.update(challenge)
                .set(challenge.status, ChallengeStatus.end.ordinal())
                .where(challenge.notice.noticeSeq.in(noticeSeq))
                .execute();
    }

    @Override
    public int pointDistribution(long noticeSeq) {
        return (int) jpaQueryFactory.update(challenge)
                .set(challenge.money, 0)
                .where(challenge.challengeSeq.eq(noticeSeq))
                .execute();
    }

    @Override
    public List<Challenge> findByChallengeBefore(LocalDateTime now) {
        return jpaQueryFactory.selectFrom(challenge)
                .where(challenge.status.eq(ChallengeStatus.startBefore.ordinal())
                .and(challenge.notice.startTime.before(now)))
                .fetch();
    }

    @Override
    public List<Challenge> findByChallengeAfter(LocalDateTime now) {
        return jpaQueryFactory.selectFrom(challenge)
                .where(challenge.status.eq(ChallengeStatus.progress.ordinal())
                        .and(challenge.notice.endTime.after(now)))
                .fetch();
    }
}
