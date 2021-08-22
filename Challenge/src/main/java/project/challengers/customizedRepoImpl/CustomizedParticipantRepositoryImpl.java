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
                        .and(participant.noticeSeq.eq(noticeSeq)))
                .execute();
    }
}
