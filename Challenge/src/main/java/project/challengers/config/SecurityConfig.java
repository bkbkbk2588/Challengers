package project.challengers.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.challengers.component.JWTTokenComp;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final JWTTokenComp jwtTokenComp;

    @Value("#{'${challengers.server.white.ips}'.split(',')}")
    List<String> whiteListIp;

    public SecurityConfig(JWTTokenComp jwtTokenComp) {
        this.jwtTokenComp = jwtTokenComp;
    }

    // 암호화에 필요한 PasswordEncoder 를 Bean 등록
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // authenticationManager를 Bean 등록
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @PostConstruct
    public void initWhiteList() {
        if(whiteListIp == null) {
            whiteListIp = new ArrayList<>();
        }
        whiteListIp.add("0:0:0:0:0:0:0:1");
        whiteListIp.add("127.0.0.1");
        logger.debug("Server White List Ip : {}", whiteListIp);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //TODO 임시용 나중에 다시 설정
        http
                .httpBasic().disable()
                .cors().and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //토큰 기반 인증이어서 세션 빼기
                .and()
                .authorizeRequests()
                .antMatchers("/v2/**",
                        "/configuration/**",
                        "/swagger*/**",
                        "/webjars/**",
                        "/swagger-resources/**").permitAll()
//                .antMatchers("/swagger-ui/**").hasIpAddress(whiteListIp)
//                      .pathMatchers("/swagger-resources/**").access(this::whiteListIp)
//                      .pathMatchers("/v2/**").access(this::whiteListIp)
                .antMatchers("/member/dupcheck-id/**").permitAll()
                .antMatchers("/member/dupcheck-phone/**").permitAll()
                .antMatchers("/member/signUp").permitAll()
                .antMatchers("/member/login").permitAll()
                .antMatchers("/member/findId/**").permitAll()
                .antMatchers("/member/findPw").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenComp),
                        UsernamePasswordAuthenticationFilter.class);
                // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
    }
}
