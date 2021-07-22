package project.challengers.customizedRepoImpl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.core.instrument.util.StringUtils;
import project.challengers.DTO.member.UpdateMemberDTO;
import project.challengers.customizedRepo.CustomizedMemberRepository;
import project.challengers.entity.QMember;

import static project.challengers.entity.QMember.member;

public class CustomizedMemberRepositoryImpl implements CustomizedMemberRepository {
    final private JPAQueryFactory jpaQueryFactory;

    public CustomizedMemberRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public String findByMemberId(String id) {
        return jpaQueryFactory.select(member.id)
                .from(member)
                .where(member.id.eq(id))
                .fetchFirst();
    }

    @Override
    public String findByMemberPhone(String phone) {
        return jpaQueryFactory.select(member.phone)
                .from(member)
                .where(member.phone.eq(phone))
                .fetchFirst();
    }

    @Override
    public String findByNameAndPhone(String name, String phone) {
        return jpaQueryFactory.select(member.id)
                .from(member)
                .where(member.name.eq(name)
                        .and(member.phone.eq(phone)))
                .fetchFirst();
    }

    @Override
    public int saveMember(UpdateMemberDTO updateMember, String id) {
        //TODO 구현해야함
//        BooleanBuilder builder = new BooleanBuilder();
//
//        if (StringUtils.isBlank(updateMember.getName())) {
//
//        }
        jpaQueryFactory.update(member)
                .set(member.email, updateMember.getEmail())
        ;
        return 0;
    }
}
