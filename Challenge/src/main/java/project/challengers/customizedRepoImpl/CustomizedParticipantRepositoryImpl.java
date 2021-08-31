package project.challengers.customizedRepoImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import project.challengers.customizedRepo.CustomizedParticipantRepository;

import java.util.List;

import static project.challengers.entity.QParticipant.participant;

public class CustomizedParticipantRepositoryImpl implements CustomizedParticipantRepository {
    final private JPAQueryFactory jpaQueryFactory;

    public CustomizedParticipantRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public int updateType(List<String> idList, long noticeSeq, int type) {
        return (int) jpaQueryFactory.update(participant)
                .set(participant.participantType, type)
                .where(participant.participantId.in(idList)
                        .and(participant.notice.noticeSeq.eq(noticeSeq)))
                .execute();
    }

    @Override
    public int setCertification(List<String> idList, long noticeSeq) {
        return (int) jpaQueryFactory.update(participant)
                .set(participant.warning, participant.warning.add(1))
                .where(participant.participantId.in(idList)
                        .and(participant.notice.noticeSeq.eq(noticeSeq)))
                .execute();
    }

    @Override
    public int setCredit(List<String> idList, long noticeSeq, int credit) {
        return (int) jpaQueryFactory.update(participant)
                .set(participant.credit, participant.credit.add(credit))
                .where(participant.participantId.in(idList)
                        .and(participant.notice.noticeSeq.eq(noticeSeq)))
                .execute();
    }
}
