package project.challengers.serviceImpl;

import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.challengers.DTO.member.*;
import project.challengers.component.JWTTokenComp;
import project.challengers.entity.Member;
import project.challengers.exception.ChallengersException;
import project.challengers.repository.MemberRepository;
import project.challengers.service.MemberService;

import java.util.Locale;

@Service
public class MemberServiceImpl implements MemberService {
    Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTTokenComp jwtComp;

    @Autowired
    MessageSource messageSource;

    /**
     * 아이디 중복확인
     *
     * @param id
     * @return
     */
    @Override
    public DupCheckDTO memberIdDupCheck(String id) {
        String idDup = memberRepository.findByMemberId(id);

        if (idDup == null) {
            return DupCheckDTO.builder()
                    .dupYn("N")
                    .build();
        }
        return DupCheckDTO.builder()
                .dupYn("Y")
                .build();
    }

    /**
     * 핸드폰번호 중복확인
     * 계정 1개씩만 생성 가능하게 하기위해 핸드폰 중복확인
     *
     * @param phone
     * @return
     */
    @Override
    public DupCheckDTO memberPhoneDupCheck(String phone) {
        String idDup = memberRepository.findByMemberPhone(phone);

        if (idDup == null) {
            return DupCheckDTO.builder()
                    .dupYn("N")
                    .build();
        }
        return DupCheckDTO.builder()
                .dupYn("Y")
                .build();
    }

    /**
     * 회원가입
     *
     * @param member
     */
    @Override
    public void signUp(MemberDto member) {
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
    public ResponseEntity<LoginResponseDTO> login(LoginDTO member) {
        Member memberEntity = memberRepository.findById(member.getId())
                .orElseThrow(() -> new ChallengersException(HttpStatus.BAD_REQUEST,
                        messageSource.getMessage("error.user.notfound.user.valid.E0001", null, Locale.KOREA)));

        if (!passwordEncoder.matches(member.getPw(), memberEntity.getPw())) {
            throw new ChallengersException(HttpStatus.UNAUTHORIZED,
                    messageSource.getMessage("error.user.login.fail.userpw.E0002", null, Locale.KOREA));
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

    /**
     * 아이디 찾기
     *
     * @param name
     * @param phone
     * @return
     */
    @Override
    public FindIdDTO findId(String name, String phone) {
        //사용자이름
        if (StringUtils.isBlank(name)) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003", new String[]{"사용자이름"}, Locale.KOREA));
        }
        //사용자연락처
        if (StringUtils.isBlank(phone)) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003", new String[]{"연락처"}, Locale.KOREA));
        }

        return FindIdDTO.builder()
                .id(memberRepository.findByNameAndPhone(name, phone))
                .build();
    }

    @Override
    public MemberDto findMember(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Member member = memberRepository.findById((String) userDetails.getUsername()).get();

        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .phone(member.getPhone())
                .build();
    }

    /**
     * 비밀번호 찾기(초기화)
     * 초기화 규칙 : 아이디 앞3자리 + 휴대전화 뒷 4자리 + !
     *
     * @param id
     * @param name
     * @param phone
     * @return
     */
    @Transactional
    @Override
    public void resetPassword(String id, String name, String phone) {
        //사용자ID
        if (StringUtils.isBlank(id)) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003", new String[]{"사용자ID"}, Locale.KOREA));
        }
        //사용자이름
        if (StringUtils.isBlank(name)) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003", new String[]{"사용자이름"}, Locale.KOREA));
        }
        //사용자연락처
        if (StringUtils.isBlank(phone)) {
            throw new ChallengersException(HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("error.user.notfound.user.valid.E0003", new String[]{"연락처"}, Locale.KOREA));
        }

        Member member = memberRepository.findByIdAndNameAndPhone(id, name, phone)
                .orElseThrow(() -> new ChallengersException(HttpStatus.NOT_FOUND,
                        messageSource.getMessage("error.user.notfound.user.valid.E0001", null, Locale.KOREA)));

        memberRepository.save(Member.builder()
                .id(member.getId())
                .pw(passwordEncoder.encode(id.substring(0, 3) + phone.substring(phone.length() - 4) + "!"))
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .phone(member.getPhone())
                .build());
    }

    // TODO
    //  1. 회원정보 수정
    //  2. 회원 탈퇴
}
