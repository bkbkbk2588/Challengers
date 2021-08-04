package project.challengers.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import project.challengers.component.JWTTokenComp;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {
    Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    private final JWTTokenComp jwtTokenComp;

    @Value("${challengers.server.white.ips}")
    List<String> whiteListIp;

    public WebSecurityConfig(JWTTokenComp jwtTokenComp) {
        this.jwtTokenComp = jwtTokenComp;
    }

    // 암호화에 필요한 PasswordEncoder 를 Bean 등록
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @PostConstruct
    public void initWhiteList() {
        if (whiteListIp == null) {
            whiteListIp = new ArrayList<>();
        }
        whiteListIp.add("0:0:0:0:0:0:0:1");
        whiteListIp.add("127.0.0.1");
        logger.debug("Server White List Ip : {}", whiteListIp);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
                    swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                })).accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
                    swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                })).and().csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers("/swagger*/**").access(this::whiteListIp)
                .pathMatchers("/swagger-resources/**").access(this::whiteListIp)
                .pathMatchers("/v2/**").access(this::whiteListIp)
                .pathMatchers("/member/dupcheck-id/**").permitAll()
                .pathMatchers("/member/dupcheck-phone/**").permitAll()
                .pathMatchers("/member/signUp").permitAll()
                .pathMatchers("/member/login").permitAll()
                .pathMatchers("/member/findId/**").permitAll()
                .pathMatchers("/member/findPw").permitAll()
                .anyExchange().authenticated()
                .and().build();
    }

    private Mono<AuthorizationDecision> whiteListIp(Mono<Authentication> authentication, AuthorizationContext context) {
        String ip = context.getExchange().getRequest().getRemoteAddress().getAddress().toString().replace("/", "");
        return authentication.map((a) -> new AuthorizationDecision(a.isAuthenticated()))
                .defaultIfEmpty(new AuthorizationDecision(
                        (whiteListIp.contains(ip)) ? true : false
                ));
    }
}
