package project.challengers.customizedRepoImpl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import project.challengers.DTO.member.UpdateMemberDTO;
import project.challengers.customizedRepo.CustomizedMemberRepository;

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
    public int saveResetPw(String id, String pw) {
        return (int) jpaQueryFactory.update(member)
                .set(member.pw, pw)
                .where(member.id.eq(id))
                .execute();
    }

    @Override
    public int saveMember(UpdateMemberDTO updateMember, String id) {
        return (int) jpaQueryFactory.update(member)
                .set(member.name, updateMember.getName())
                .set(member.email, updateMember.getEmail())
                .set(member.phone, updateMember.getPhone())
                .set(member.nickname, updateMember.getNickname())
                .where(member.id.eq(id))
                .execute();
    }

    @Override
    public int deleteMember(String id) {
        return (int) jpaQueryFactory.delete(member)
                .where(member.id.eq(id))
                .execute();
    }
}
