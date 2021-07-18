package project.challengers.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.challengers.DTO.member.LoginResponseDTO;
import project.challengers.DTO.member.MemberIdDupCheckDTO;
import project.challengers.DTO.member.MemberLoginDTO;
import project.challengers.DTO.member.MemberSignUpDto;
import project.challengers.component.JWTTokenComp;
import project.challengers.entity.Member;
import project.challengers.exception.ChallengersException;
import project.challengers.repository.MemberRepository;
import project.challengers.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {
    Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTTokenComp jwtComp;

    /**
     * 아이디 중복확인
     *
     * @param id
     * @return
     */
    @Override
    public MemberIdDupCheckDTO memberIdDupCheck(String id) {
        String idDup = memberRepository.findByMemberId(id);

        if (idDup == null) {
            return MemberIdDupCheckDTO.builder()
                    .dupYn("N")
                    .build();
        }
        return MemberIdDupCheckDTO.builder()
                .dupYn("Y")
                .build();
    }

    /**
     * 회원가입
     *
     * @param member
     */
    @Override
    public void signUp(MemberSignUpDto member) {
        memberRepository.save(Member.builder()
                .id(member.getId())
                .pw(passwordEncoder.encode(member.getPw()))
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .phone(member.getPhone())
                .build());
    }

    /**
     * 로그인
     *
     * @param member
     * @return
     */
    @Override
    public ResponseEntity<LoginResponseDTO> login(MemberLoginDTO member) {
        Member memberEntity = memberRepository.findById(member.getId())
                .orElseThrow(() -> new ChallengersException(HttpStatus.BAD_REQUEST,
                        "error.user.notfound.user.valid.E0001"));

        if (!passwordEncoder.matches(member.getPw(), memberEntity.getPw())) {
            throw new ChallengersException(HttpStatus.UNAUTHORIZED,
                    "error.user.login.fail.userpw.E0002");
        }

        return ResponseEntity.ok(LoginResponseDTO.builder()
                .accessToken(jwtComp.createToken(memberEntity.getId(), memberEntity.getRoles()))
                .member(LoginResponseDTO.Member.builder()
                        .id(memberEntity.getId())
                        .name(memberEntity.getName())
                        .nickname(memberEntity.getNickname())
                        .email(memberEntity.getEmail())
                        .phone(memberEntity.getPhone())
                        .build())
                .build());
    }
}
