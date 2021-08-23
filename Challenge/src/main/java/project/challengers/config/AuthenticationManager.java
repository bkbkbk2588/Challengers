package project.challengers.config;

import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import project.challengers.component.JWTTokenComponent;
import project.challengers.entity.Member;
import project.challengers.repository.MemberRepository;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {
    Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);

    @Autowired
    private JWTTokenComponent jwtComp;

    @Autowired
    MemberRepository memberRepository;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getPrincipal().toString();
        String memberId = null;
        try {
            memberId = jwtComp.getMemberIdFromToken(authToken);
        } catch (Exception e) {
            logger.warn("AuthToke Error", e);
            return Mono.empty();
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> null);

        if (member == null) {
            return Mono.empty();
        }

        try {
            if (!jwtComp.validateToken(authToken, member)) {
                return Mono.empty();
            }

            Claims claims = jwtComp.getAllClaimsFromToken(authToken);

            List<String> rolesMap = claims.get("role", List.class);
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String role : rolesMap) {
                authorities.add(new SimpleGrantedAuthority(role));
            }

            // TODO credentials에 나중에 설정할 값 넣기
            return Mono.just(
                    new UsernamePasswordAuthenticationToken(memberId,
                            null, authorities));
        } catch (Exception e) {
            e.printStackTrace();
            return Mono.empty();
        }
    }
}
