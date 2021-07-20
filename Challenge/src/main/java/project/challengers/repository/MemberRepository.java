package project.challengers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.challengers.customizedRepo.CustomizedMemberRepository;
import project.challengers.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String>, CustomizedMemberRepository {
        Optional<Member> findByIdAndNameAndPhone(String id, String name, String phone);
}
