package project.challengers.customizedRepoImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import project.challengers.customizedRepo.CustomizedApplyRepository;

import java.util.List;

import static project.challengers.entity.QApply.apply;

public class CustomizedApplyRepositoryImpl implements CustomizedApplyRepository {
    final private JPAQueryFactory jpaQueryFactory;

    public CustomizedApplyRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public int acceptApply(List<String> id, long noticeSeq) {
        return (int) jpaQueryFactory.delete(apply)
                .where(apply.member.id.in(id)
                        .and(apply.notice.noticeSeq.eq(noticeSeq)))
                .execute();
    }
}
