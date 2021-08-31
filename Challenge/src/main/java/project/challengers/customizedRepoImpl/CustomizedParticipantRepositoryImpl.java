package project.challengers.customizedRepoImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import project.challengers.base.ParticipantType;
import project.challengers.customizedRepo.CustomizedParticipantRepository;

import java.util.List;

import static project.challengers.entity.QParticipant.participant;

public class CustomizedParticipantRepositoryImpl implements CustomizedParticipantRepository {
    final private JPAQueryFactory jpaQueryFactory;

    public CustomizedParticipantRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public int updateType(List<String> idList, long noticeSeq) {
        return (int) jpaQueryFactory.update(participant)
                .set(participant.participantType, ParticipantType.end.ordinal())
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

    @Override
    public int setBlind(long noticeSeq, String id) {
        return (int) jpaQueryFactory.update(participant)
                .set(participant.participantType, ParticipantType.blind.ordinal())
                .where(participant.participantId.eq(id)
                        .and(participant.notice.noticeSeq.eq(noticeSeq)))
                .execute();
    }

    @Override
    public int setDelete(long noticeSeq, String id) {
        return (int) jpaQueryFactory.update(participant)
                .set(participant.participantType, ParticipantType.expulsion.ordinal())
                .where(participant.participantId.eq(id)
                        .and(participant.notice.noticeSeq.eq(noticeSeq)))
                .execute();
    }
}
