package project.challengers.service;

import org.springframework.stereotype.Service;
import project.challengers.vo.MemberIdDupCheckVo;

@Service
public interface MemberService {
    public MemberIdDupCheckVo memberIdDupCheck(String id);
}
