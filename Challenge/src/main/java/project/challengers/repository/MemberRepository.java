package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.customizedRepo.CustomizedMemberRepository;
import project.challengers.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String>, CustomizedMemberRepository {
}
