package project.challengers.component;

import com.google.common.collect.Lists;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import project.challengers.entity.Member;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@PropertySource("classpath:config/challengers-dev.properties")
public class JWTTokenComp {
    @Value("${jwt.secret}")
    private String secretKey;

    // 토큰 유효시간 60분
    private long tokenValidTime = 1 * 60 * 60 * 1000L;

    // 객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 토큰에서 회원 정보 추출
    public String getMemberIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // 토큰에서 claim 추출
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // 토큰 유효기간 판단
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // 토큰에서 유효기간 추출
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // 토큰에서 claim 전부 추출
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT 토큰 생성
    public String generateToken(Member userDetails) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("role", Lists.newArrayList("ROLE_" + userDetails.getRoles()));
        return doGenerateToken(claims, userDetails.getId());
    }

    // JWT 토큰값 설정
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        long nowMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(nowMillis + tokenValidTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Boolean validateToken(String token, Member userDetails) {
        final String userId = getMemberIdFromToken(token);
        return (userId.equals(userDetails.getId()) && !isTokenExpired(token));
    }
}
