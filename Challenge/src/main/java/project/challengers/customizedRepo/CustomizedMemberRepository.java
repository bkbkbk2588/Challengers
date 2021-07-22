package project.challengers.customizedRepo;

import project.challengers.DTO.member.UpdateMemberDTO;

public interface CustomizedMemberRepository {
    String findByMemberId(String id);
    String findByMemberPhone(String phone);
    String findByNameAndPhone(String name, String phone);
    int saveMember(UpdateMemberDTO updateMember, String id);
}
